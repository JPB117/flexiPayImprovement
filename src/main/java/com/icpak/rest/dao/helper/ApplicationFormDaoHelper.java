package com.icpak.rest.dao.helper;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.UriInfo;

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
import com.icpak.rest.models.base.ResourceCollectionModel;
import com.icpak.rest.models.membership.ApplicationFormHeader;
import com.icpak.rest.models.membership.ApplicationType;
import com.icpak.rest.models.membership.Category;
import com.icpak.rest.models.util.Attachment;
import com.icpak.rest.utils.Doc;
import com.icpak.rest.utils.DocumentHTMLMapper;
import com.icpak.rest.utils.DocumentLine;
import com.icpak.rest.utils.EmailServiceHelper;
import com.icpak.rest.utils.HTMLToPDFConvertor;

@Transactional
public class ApplicationFormDaoHelper {

	
	@Inject ApplicationFormDao applicationDao;
	@Inject MemberDao memberDao;
	@Inject UsersDao userDao;
	
	public void createApplication(ApplicationFormHeader application){
		if(application.getRefId()!=null){
			updateApplication(application.getRefId(), application);
			return;
		}
		
//		if(application.getMember()!=null){
//			application.setMember(memberDao.findByMemberId(application.getMember().getRefId()));
//		}
//		applicationDao.createApplication(application);
		
		applicationDao.createApplication(application);
				
		setCategory(application);	
		User user = createTempUser(application);
		sendEmail(application, user);
		assert application.getId()!=null;
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

	public void updateApplication(String applicationId, ApplicationFormHeader application){
		ApplicationFormHeader po = applicationDao.findByApplicationId(applicationId,true);
		ApplicationFormHeader header = new ApplicationFormHeader();
		header.setSurname(application.getSurname());
		header.setOtherNames(application.getOtherNames());
		header.setEmail(application.getEmail());
		header.setApplicationType(application.getApplicationType());
		header.setAddress1(application.getAddress1());
		header.setPostCode(application.getPostCode());
		header.setCity1(application.getCity1());
		header.setEmployer(application.getEmployer());
		setCategory(application);
		applicationDao.updateApplication(po);
	}
	
	public void deleteApplication(String applicationId){
//		ApplicationFormHeader application = applicationDao.findByApplicationId(applicationId);
//		applicationDao.delete(application);
	}
	public ResourceCollectionModel<ApplicationFormHeader> getAllApplications(Integer offset, Integer limit,
			UriInfo uriInfo) {
		int total = applicationDao.getApplicationCount();
		
		ResourceCollectionModel<ApplicationFormHeader> collection = 
				new ResourceCollectionModel<>(offset,limit,total, uriInfo);
		List<ApplicationFormHeader> applications = applicationDao.getAllApplications(offset, limit);
		
		//List<ApplicationFormHeader> rtn = new ArrayList<>();
//		for(ApplicationFormHeader application: applications){
//			application.setUri(uriInfo.getAbsolutePath().toString()+"/"+application.getRefId());
//			rtn.add(application.clone(ExpandTokens.DETAIL.toString()));
//		}
		
		collection.setItems(applications);
		return collection;
	}
	
	public ApplicationFormHeader getApplicationById(String applicationId) {
		ApplicationFormHeader application = applicationDao.findByApplicationId(applicationId);
		if(application==null){
			throw new ServiceException(ErrorCodes.NOTFOUND,"'"+applicationId+"'");
		}
		
		setCategory(application);
		
		return application;
	}


}
