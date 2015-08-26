package com.icpak.rest.dao.helper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.IDUtils;
import com.icpak.rest.dao.ApplicationFormDao;
import com.icpak.rest.dao.InvoiceDaoHelper;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.auth.BioData;
import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.membership.ApplicationCategory;
import com.icpak.rest.models.membership.ApplicationFormHeader;
import com.icpak.rest.models.membership.MemberImport;
import com.icpak.rest.models.util.Attachment;
import com.icpak.rest.utils.Doc;
import com.icpak.rest.utils.DocumentHTMLMapper;
import com.icpak.rest.utils.DocumentLine;
import com.icpak.rest.utils.EmailServiceHelper;
import com.icpak.rest.utils.HTMLToPDFConvertor;
import com.workpoint.icpak.shared.model.ApplicationCategoryDto;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationType;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.InvoiceLineDto;

@Transactional
public class ApplicationFormDaoHelper {

	@Inject
	ApplicationFormDao applicationDao;
	@Inject
	UsersDao userDao;
	@Inject
	UsersDaoHelper usersDaoHelper;
	@Inject
	InvoiceDaoHelper invoiceHelper;
	@Inject
	TransactionDaoHelper trxHelper;

	public void createApplication(ApplicationFormHeaderDto application) {

		if (application.getRefId() != null) {
			updateApplication(application.getRefId(), application);
			return;
		}

		// Copy into PO
		ApplicationFormHeader po = new ApplicationFormHeader();
		po.copyFrom(application);

		// Create Temp User
		User user = createTempUser(po);
		po.setUserRefId(user.getRefId());

		// Generate Invoice
		InvoiceDto invoice = generateInvoice(po);
		po.setInvoiceRef(invoice.getRefId());

		// Save Data
		applicationDao.createApplication(po);

		// setCategory(po);

		// Send Email
		sendEmail(po, invoice, user);

		// Copy into DTO
		po.copyInto(application);
	}

	private User createTempUser(ApplicationFormHeader application) {
		User po = new User();
		po.setEmail(application.getEmail());
		// po.setUsername(user.getUsername());
		po.setRefId(application.getRefId());
		po.setAddress(application.getAddress1());
		po.setCity(application.getCity1());
		po.setNationality(application.getNationality());
		po.setMemberNo(application.getMemberNo());

		BioData bioData = new BioData();
		bioData.setFirstName(application.getOtherNames());
		bioData.setLastName(application.getSurname());
		po.setUserData(bioData);

		String password = IDUtils.generateTempPassword();
		po.setPassword(password);

		usersDaoHelper.create(po);

		// userDao.createUser(po);
		User u = po.clone();
		u.setPassword(password);

		return u;
	}

	private void sendEmail(ApplicationFormHeader application,
			InvoiceDto invoice, User user) {
		try {
			Map<String, Object> values = new HashMap<String, Object>();
			values.put("companyName", application.getEmployerCode());
			values.put("companyAddress", application.getAddress1());
			values.put("quoteNo", application.getId());
			values.put("date", application.getDate());
			values.put("firstName", application.getOtherNames());
			values.put("DocumentURL", "http://www2.icpak.com/icpak/");
			values.put("email", application.getEmail());
			values.put("password", user.getHashedPassword());
			Doc doc = new Doc(values);

			Map<String, Object> line = new HashMap<String, Object>();
			InvoiceLineDto lineDto = invoice.getLines().get(0);

			line.put("description", lineDto.getDescription());
			line.put("unitPrice", lineDto.getUnitPrice());
			line.put("amount", lineDto.getTotalAmount());
			values.put("totalAmount", lineDto.getTotalAmount());
			doc.addDetail(new DocumentLine("invoiceDetails", line));

			String documentNo = "ProForma Invoice_" + application.getSurname();
			// PDF Invoice Generation
			InputStream inv = EmailServiceHelper.class.getClassLoader()
					.getResourceAsStream("proforma-invoice.html");
			String invoiceHTML = IOUtils.toString(inv);
			byte[] invoicePDF = new HTMLToPDFConvertor().convert(doc,
					new String(invoiceHTML));
			Attachment attachment = new Attachment();
			attachment.setAttachment(invoicePDF);
			attachment.setName(documentNo + ".pdf");

			String subject = "ICPAK Member Registration";
			// Email Template parse and map variables
			InputStream is = EmailServiceHelper.class.getClassLoader()
					.getResourceAsStream("application-email.html");
			String html = IOUtils.toString(is);
			html = new DocumentHTMLMapper().map(doc, html);
			EmailServiceHelper.sendEmail(
					html,
					"RE: ICPAK Member Registration",
					Arrays.asList(application.getEmail()),
					Arrays.asList(application.getSurname() + " "
							+ application.getOtherNames()), attachment);

			trxHelper.charge(user.getRefId(), new Date(), subject, null,
					invoice.getAmount(), documentNo, invoice.getRefId());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private InvoiceDto generateInvoice(ApplicationFormHeader application) {
		ApplicationType type = application.getApplicationType();
		ApplicationCategory category = applicationDao
				.findApplicationCategory(type);

		if (category == null) {
			// throw new
			// NullPointerException("Application Category "+type+" not found");
			throw new ServiceException(ErrorCodes.NOTFOUND,
					"Application Category '" + type + "'");
		}

		String documentNo = "ProForma Invoice_" + application.getSurname();

		InvoiceDto dto = new InvoiceDto();
		dto.setDescription("Member Registration Invoice");
		dto.setDocumentNo(documentNo);
		dto.setAmount(category.getApplicationAmount());
		dto.setCompanyName(application.getEmployer());
		dto.setCompanyAddress(application.getContactAddress());
		dto.setPhoneNumber(application.getContactTelephone());
		dto.setContactName(application.getSurname() + " "
				+ application.getOtherNames());
		dto.setDate(new Date().getTime());
		
		
		dto.addLine(new InvoiceLineDto(
				dto.getContactName()+", "+
				"'"+category.getType().getDisplayName()+"' member registration fee", category
				.getApplicationAmount(), category.getApplicationAmount()));

		dto = invoiceHelper.save(dto);

		return dto;
	}

	public void updateApplication(String applicationId,
			ApplicationFormHeaderDto dto) {
		ApplicationFormHeader po = applicationDao.findByApplicationId(
				applicationId, true);

		// Fields only generated once
		dto.setUserId(po.getUserRefId());
		dto.setInvoiceRef(po.getInvoiceRef());

		po.copyFrom(dto);
		// setCategory(po);

		applicationDao.updateApplication(po);
	}

	public void deleteApplication(String applicationId) {
		// ApplicationFormHeader application =
		// applicationDao.findByApplicationId(applicationId);
		// applicationDao.delete(application);
	}

	public List<ApplicationFormHeaderDto> getAllApplications(Integer offset,
			Integer limit, String uri) {

		List<ApplicationFormHeader> applications = applicationDao
				.getAllApplications(offset, limit);
		List<ApplicationFormHeaderDto> rtn = new ArrayList<>();

		for (ApplicationFormHeader application : applications) {
			ApplicationFormHeaderDto dto = application.toDto();
			dto.setUri(uri + "/" + application.getRefId());
			rtn.add(dto);
		}
		return rtn;
	}

	public List<ApplicationFormHeaderDto> importMembers(Integer offset,
			Integer limit) {
		List<MemberImport> applications = applicationDao.importMembers(offset,
				limit);
		List<ApplicationFormHeaderDto> rtn = new ArrayList<>();

		for (MemberImport member : applications) {
			ApplicationFormHeaderDto dto = member.toDTO();
			rtn.add(dto);
		}

		return rtn;
	}

	public Integer getApplicationCount() {
		return applicationDao.getApplicationCount();
	}

	public ApplicationFormHeader getApplicationById(String applicationId) {
		ApplicationFormHeader application = applicationDao
				.findByApplicationId(applicationId);
		if (application == null) {
			throw new ServiceException(ErrorCodes.NOTFOUND, "'" + applicationId
					+ "'");
		}

		// setCategory(application);

		return application;
	}

	public List<ApplicationCategoryDto> getAllCategories() {
		List<ApplicationCategory> categories = applicationDao
				.getAllCategories();

		List<ApplicationCategoryDto> dtos = new ArrayList<>();
		for (ApplicationCategory c : categories) {
			dtos.add(c.toDto());
		}
		return dtos;
	}

	public ApplicationCategory getCategoryById(String categoryId) {
		return applicationDao.getCategory(categoryId);
	}

	public void createCategory(ApplicationCategoryDto dto) {
		if (dto.getRefId() != null) {
			updateCategory(dto.getRefId(), dto);
			return;
		}

		ApplicationCategory c = new ApplicationCategory();
		c.copyFrom(dto);
		applicationDao.save(c);
		dto.setRefId(c.getRefId());

	}

	public void updateCategory(String categoryId, ApplicationCategoryDto dto) {
		ApplicationCategory c = getCategoryById(categoryId);
		c.copyFrom(dto);
		applicationDao.save(c);
	}

	public void deleteCategory(String categoryId) {
		applicationDao.delete(getCategoryById(categoryId));
	}

	public InvoiceDto getInvoice(String applicationId) {
		return generateInvoice(getApplicationById(applicationId));
	}

}
