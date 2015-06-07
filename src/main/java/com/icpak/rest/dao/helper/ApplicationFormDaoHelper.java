package com.icpak.rest.dao.helper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.IDUtils;
import com.icpak.rest.dao.ApplicationFormDao;
import com.icpak.rest.dao.MemberDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.membership.ApplicationFormHeader;
import com.icpak.rest.models.membership.Category;
import com.icpak.rest.models.util.Attachment;
import com.icpak.rest.utils.Doc;
import com.icpak.rest.utils.DocumentHTMLMapper;
import com.icpak.rest.utils.DocumentLine;
import com.icpak.rest.utils.EmailServiceHelper;
import com.icpak.rest.utils.HTMLToPDFConvertor;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationType;
import com.workpoint.icpak.shared.model.CategoryDto;

@Transactional
public class ApplicationFormDaoHelper {

	
	@Inject ApplicationFormDao applicationDao;
	@Inject MemberDao memberDao;
	@Inject UsersDao userDao;
	
	public void createApplication(ApplicationFormHeaderDto application){
		
		if(application.getRefId()!=null){
			updateApplication(application.getRefId(), application);
			return;
		}
		
		//Copy into PO
		ApplicationFormHeader po = new ApplicationFormHeader();
		po.copyFrom(application);
		
		applicationDao.createApplication(po);
		setCategory(po);	
		
		User user = createTempUser(po);
		sendEmail(po, user);
		
		//Copy into DTO
		po.copyInto(application);
	}
	
	private User createTempUser(ApplicationFormHeader application) {
		User po = new User();
		po.setEmail(application.getEmail());
		//po.setUsername(user.getUsername());
		po.setRefId(application.getRefId());
		po.setAddress(application.getAddress1());
		po.setCity(application.getCity1());
		po.setNationality(application.getNationality());
		
		String password = IDUtils.generateTempPassword();
		po.setPassword(password);
		
		userDao.createUser(po);
		
		User u = po.clone();
		u.setPassword(password);
		
		return u;
	}

	private void setCategory(ApplicationFormHeader application) {
		ApplicationType type = application.getApplicationType();
		Category category = applicationDao.findApplicationCategory(type);
		application.setCategory(category);
	}

	private void sendEmail(ApplicationFormHeader application,User user) {
		
		try{
			Map<String,Object> values  = new HashMap<String, Object>();
			values.put("companyName", application.getEmployerCode());
			values.put("companyAddress", application.getAddress1());
			values.put("quoteNo", application.getId());
			values.put("date", application.getDate());
			values.put("firstName", application.getOtherNames());
			values.put("DocumentURL", "http://www.solutech.co.ke/icpak/");
			values.put("email", application.getEmail());
			values.put("password", user.getPassword());
			Doc doc = new Doc(values);
			
			ApplicationType type = application.getApplicationType();
			Category category = applicationDao.findApplicationCategory(type);
			
			if(category==null){
				//throw new NullPointerException("Application Category "+type+" not found");
				throw new ServiceException(ErrorCodes.NOTFOUND,"Application Category '"+type+"'");
			}
			
			Map<String,Object> line  = new HashMap<String, Object>();
			line.put("description", category.getDescription());
			line.put("unitPrice", category.getApplicationAmount());
			line.put("amount", category.getApplicationAmount());
			values.put("totalAmount", category.getApplicationAmount());
			category.getApplicationAmount();
			doc.addDetail(new DocumentLine("invoiceDetails",line));
			
			//PDF Invoice Generation
			InputStream inv = EmailServiceHelper.class.getClassLoader().getResourceAsStream("proforma-invoice.html");
			String invoiceHTML = IOUtils.toString(inv);
			byte[] invoicePDF = new HTMLToPDFConvertor().convert(doc, new String(invoiceHTML));
			Attachment attachment = new Attachment();
			attachment.setAttachment(invoicePDF);
			attachment.setName("ProForma Invoice_"+application.getSurname()+".pdf");
			
			//Email Template parse and map variables
			InputStream is = EmailServiceHelper.class.getClassLoader().getResourceAsStream("application-email.html");
			String html = IOUtils.toString(is);
			html = new DocumentHTMLMapper().map(doc, html);
			EmailServiceHelper.sendEmail(html, "RE: ICPAK Member Registration",
					Arrays.asList(application.getEmail()),
					Arrays.asList(application.getSurname()+" "+application.getOtherNames()), attachment);	
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}

	public void updateApplication(String applicationId, ApplicationFormHeaderDto dto){
		ApplicationFormHeader po = applicationDao.findByApplicationId(applicationId,true);
		po.copyFrom(dto);
		setCategory(po);
		
		applicationDao.updateApplication(po);
	}
	
	public void deleteApplication(String applicationId){
//		ApplicationFormHeader application = applicationDao.findByApplicationId(applicationId);
//		applicationDao.delete(application);
	}
	public List<ApplicationFormHeaderDto> getAllApplications(Integer offset, Integer limit,
			String uri) {

		List<ApplicationFormHeader> applications = applicationDao.getAllApplications(offset, limit);
		List<ApplicationFormHeaderDto> rtn = new ArrayList<>();
		for(ApplicationFormHeader application: applications){
			ApplicationFormHeaderDto dto  = application.toDto();
			dto.setUri(uri+"/"+application.getRefId());
			rtn.add(dto);
		}
		return rtn;
	}
	
	public ApplicationFormHeader getApplicationById(String applicationId) {
		ApplicationFormHeader application = applicationDao.findByApplicationId(applicationId);
		if(application==null){
			throw new ServiceException(ErrorCodes.NOTFOUND,"'"+applicationId+"'");
		}
		
		setCategory(application);
		
		return application;
	}

	public List<CategoryDto> getAllCategories() {
		List<Category> categories = applicationDao.getAllCategories();
		
		List<CategoryDto> dtos = new ArrayList<>();
		for(Category c: categories){
			dtos.add( c.toDto());
		}
		return dtos;
	}

	public Category getCategoryById(String categoryId) {
		return applicationDao.getCategory(categoryId);
	}

	public void createCategory(CategoryDto dto) {
		if(dto.getRefId()!=null){
			updateCategory(dto.getRefId(), dto);
			return;
		}
		
		Category c = new Category();
		c.copyFrom(dto);
		applicationDao.save(c);
		dto.setRefId(c.getRefId());
		
	}

	public void updateCategory(String categoryId, CategoryDto dto) {
		Category c = getCategoryById(categoryId);
		c.copyFrom(dto);
		applicationDao.save(c);
	}

	public void deleteCategory(String categoryId) {
		applicationDao.delete(getCategoryById(categoryId));
	}


}
