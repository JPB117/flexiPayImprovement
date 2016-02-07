package com.icpak.rest.dao.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
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
import com.icpak.rest.util.ApplicationSettings;
import com.icpak.rest.utils.Doc;
import com.icpak.rest.utils.DocumentHTMLMapper;
import com.icpak.rest.utils.DocumentLine;
import com.icpak.rest.utils.EmailServiceHelper;
import com.icpak.rest.utils.HTMLToPDFConvertor;
import com.workpoint.icpak.shared.model.ApplicationCategoryDto;
import com.workpoint.icpak.shared.model.ApplicationERPDto;
import com.workpoint.icpak.shared.model.ApplicationFormAccountancyDto;
import com.workpoint.icpak.shared.model.ApplicationFormEducationalDto;
import com.workpoint.icpak.shared.model.ApplicationFormEmploymentDto;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationFormSpecializationDto;
import com.workpoint.icpak.shared.model.ApplicationFormTrainingDto;
import com.workpoint.icpak.shared.model.ApplicationSummaryDto;
import com.workpoint.icpak.shared.model.ApplicationType;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.InvoiceLineDto;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.auth.ApplicationStatus;

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
	@Inject
	ApplicationSettings settings;
	@Inject
	EducationDaoHelper eduHelper;
	@Inject
	TrainingDaoHelper trainingHelper;
	@Inject
	AccountancyDaoHelper accountancyHelper;
	@Inject
	SpecializationDaoHelper specializationHelper;

	Logger logger = Logger.getLogger(ApplicationFormDaoHelper.class);

	public void createApplication(ApplicationFormHeaderDto application) {
		if (application.getRefId() != null) {
			updateApplication(application.getRefId(), application);
			return;
		}

		// Copy into PO
		ApplicationFormHeader po = new ApplicationFormHeader();
		po.copyFrom(application);
		po.setApplicationStatus(ApplicationStatus.PENDING);

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

	public void createApplicationFromImport(ApplicationFormHeaderDto application) {

		if (application.getRefId() != null) {
			updateApplication(application.getRefId(), application);
			return;
		}
		// Copy into PO
		ApplicationFormHeader po = new ApplicationFormHeader();
		po.copyFrom(application);
		po.setApplicationStatus(ApplicationStatus.APPROVED);

		// Create Temp User
		User user = createTempUser(po);
		po.setUserRefId(user.getRefId());

		// Save Data
		applicationDao.createApplication(po);

		// Copy into DTO
		po.copyInto(application);
	}

	private User createTempUser(ApplicationFormHeader application) {
		User po = new User();
		po.setEmail(application.getEmail());
		po.setRefId(application.getRefId());
		po.setAddress(application.getAddress1());
		po.setCity(application.getCity1());
		po.setNationality(application.getNationality());
		po.setMemberNo(application.getMemberNo());
		po.setPhoneNumber(application.getMobileNo());
		po.setFullName(application.getSurname() + " "
				+ application.getOtherNames());
		BioData bioData = new BioData();
		bioData.setFirstName(application.getSurname());
		bioData.setLastName(application.getOtherNames());
		po.setUserData(bioData);

		String password = IDUtils.generateTempPassword();
		po.setPassword(password);

		usersDaoHelper.create(po);

		// userDao.createUser(po);
		User u = po.clone();
		u.setPassword(password);

		return u;
	}

	@SuppressWarnings("unused")
	private void sendEmail(ApplicationFormHeader application,
			InvoiceDto invoice, User user) {
		try {
			Map<String, Object> values = new HashMap<String, Object>();
			values.put("companyName", application.getEmployerCode());
			values.put("companyAddress", application.getAddress1());
			values.put("quoteNo", application.getId());
			values.put("date", application.getDate());
			values.put("firstName", application.getOtherNames());
			// https://www.icpak.com/icpakportal/#eventBooking;eventId=Jx4Ca6HpOutf2ic7;bookingId=ttDzH7OkgAHk5CSk
			values.put("DocumentURL", settings.getApplicationPath());
			values.put("userRefId", user.getRefId());
			values.put("email", application.getEmail());
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

			trxHelper.charge(user.getMember() == null ? null : user.getMember()
					.getRefId(), new Date(), subject, null, invoice
					.getInvoiceAmount(), documentNo, invoice.getRefId());

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

		dto.addLine(new InvoiceLineDto(dto.getContactName() + ", " + "'"
				+ category.getType().getDisplayName()
				+ "' member registration fee", category.getApplicationAmount(),
				category.getApplicationAmount()));

		dto = invoiceHelper.save(dto);

		return dto;
	}

	public void updateApplication(String applicationId,
			ApplicationFormHeaderDto dto) {
		ApplicationFormHeader po = applicationDao.findByApplicationId(
				applicationId, true);
		// Fields only generated once
		dto.setUserId(po.getUserRefId());
		// dto.setInvoiceRef(po.getInvoiceRef());
		if (dto.getErpCode() != null && !dto.getErpCode().isEmpty()) {
			try {
				postApplicationToERP(dto.getErpCode(), prepareErpDto(dto));
			} catch (URISyntaxException | ParseException | JSONException e) {
				e.printStackTrace();
			}
		}

		po.copyFrom(dto);

		applicationDao.updateApplication(po);
	}

	private ApplicationERPDto prepareErpDto(ApplicationFormHeaderDto passedDto) {
		ApplicationFormHeaderDto application = passedDto;
		List<ApplicationFormEducationalDto> educationDetails = eduHelper
				.getAllEducationEntrys("", "j4Eu7OZ7krpuQ9r4", 0, 100);
		List<ApplicationFormTrainingDto> trainings = trainingHelper
				.getAllTrainingEntrys("", "j4Eu7OZ7krpuQ9r4", 0, 100);
		List<ApplicationFormAccountancyDto> accountancy = accountancyHelper
				.getAllAccountancyEntrys("", "j4Eu7OZ7krpuQ9r4", 0, 100);
		List<ApplicationFormSpecializationDto> specializations = specializationHelper
				.getAllSpecializationEntrys("", "j4Eu7OZ7krpuQ9r4", 0, 100);
		List<ApplicationFormEmploymentDto> employment = specializationHelper
				.getAllEmploymentEntrys("", "j4Eu7OZ7krpuQ9r4", 0, 100);

		application.setApplicationNo(passedDto.getErpCode());
		for (ApplicationFormEducationalDto education : educationDetails) {
			education.setApplicationNo(passedDto.getErpCode());
		}
		for (ApplicationFormTrainingDto training : trainings) {
			training.setApplicationNo(passedDto.getErpCode());
		}
		for (ApplicationFormAccountancyDto acc : accountancy) {
			acc.setApplicationNo(passedDto.getErpCode());
		}
		for (ApplicationFormSpecializationDto specialization : specializations) {
			specialization.setApplicationNo(passedDto.getErpCode());
		}
		for (ApplicationFormEmploymentDto emp : employment) {
			emp.setApplicationNo(passedDto.getErpCode());
		}

		ApplicationERPDto erpDto = new ApplicationERPDto();
		erpDto.setApplication(application);
		erpDto.setEducationDetails(educationDetails);
		erpDto.setTrainings(trainings);
		erpDto.setAccountancy(accountancy);
		erpDto.setSpecializations(specializations);
		erpDto.setEmployment(employment);

		return erpDto;
	}

	public void deleteApplication(String applicationId) {
	}

	public List<ApplicationFormHeaderDto> getAllApplications(Integer offset,
			Integer limit, String uri, String searchTerm) {

		List<ApplicationFormHeader> applications = applicationDao
				.getAllApplications(offset, limit, searchTerm);
		List<ApplicationFormHeaderDto> rtn = new ArrayList<>();

		int counter = 0;
		for (ApplicationFormHeader application : applications) {
			ApplicationFormHeaderDto dto = application.toDto();
			dto.setUri(uri + "/" + application.getRefId());
			if (counter < applications.size() - 1) {
				dto.setNextRefId(applications.get(counter + 1).getRefId());
			}
			if (counter > 0) {
				dto.setPreviousRefId(applications.get(counter - 1).getRefId());
			}
			counter++;
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

	public Integer getApplicationCount(String searchTerm) {
		return applicationDao.getApplicationCount(searchTerm);
	}

	public ApplicationFormHeader getApplicationById(String applicationId) {
		ApplicationFormHeader application = applicationDao
				.findByApplicationId(applicationId);
		if (application == null) {
			throw new ServiceException(ErrorCodes.NOTFOUND, "'" + applicationId
					+ "'");
		}

		// Check if there is a corresponding invoice if this is pending;
		if (application.getInvoiceRef() == null
				|| application.getInvoiceRef().isEmpty()) {
			// Generate a New Invoice
			InvoiceDto invoice = generateInvoice(application);
			application.setInvoiceRef(invoice.getRefId());
		}

		if (application.getPaymentStatus() == null) {
			application.setPaymentStatus(PaymentStatus.NOTPAID);
		}

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

	public ApplicationSummaryDto getApplicationSummary() {

		return applicationDao.getApplicationsSummary();
	}

	public void postApplicationToERP(String erpAppId,
			ApplicationERPDto application) throws URISyntaxException,
			ParseException, JSONException {
		final HttpClient httpClient = new DefaultHttpClient();

		JSONObject payLoad = new JSONObject(application);
		logger.info("payload to ERP>>>>" + payLoad);

		final List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("app_no", erpAppId));

		final URI uri = URIUtils.createURI("http", "41.139.138.165/", -1,
				"members/newapp.php", URLEncodedUtils.format(qparams, "UTF-8"),
				null);
		final HttpPost request = new HttpPost();
		request.setURI(uri);

		String res = "";
		HttpResponse response = null;

		StringBuffer result = null;

		try {
			request.setHeader("accept", "application/json");
			@SuppressWarnings("deprecation")
			StringEntity stringEntity = new StringEntity(payLoad.toString(),
					"application/json", "UTF-8");
			request.setEntity(stringEntity);

			response = httpClient.execute(request);
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			result = new StringBuffer();

			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Integer getApplicationCount() {
		return getApplicationCount("");
	}

}
