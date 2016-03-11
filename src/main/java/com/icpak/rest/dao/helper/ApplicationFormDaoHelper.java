package com.icpak.rest.dao.helper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

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
import org.xml.sax.SAXException;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.IDUtils;
import com.icpak.rest.dao.ApplicationFormDao;
import com.icpak.rest.dao.InvoiceDaoHelper;
import com.icpak.rest.dao.RolesDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.auth.BioData;
import com.icpak.rest.models.auth.Role;
import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.membership.ApplicationCategory;
import com.icpak.rest.models.membership.ApplicationFormHeader;
import com.icpak.rest.models.membership.Member;
import com.icpak.rest.models.membership.MemberImport;
import com.icpak.rest.models.util.Attachment;
import com.icpak.rest.util.ApplicationSettings;
import com.icpak.rest.utils.Doc;
import com.icpak.rest.utils.DocumentHTMLMapper;
import com.icpak.rest.utils.DocumentLine;
import com.icpak.rest.utils.EmailServiceHelper;
import com.icpak.rest.utils.HTMLToPDFConvertor;
import com.itextpdf.text.DocumentException;
import com.workpoint.icpak.server.util.ServerDateUtils;
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
import com.workpoint.icpak.shared.model.Branch;
import com.workpoint.icpak.shared.model.Gender;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.InvoiceLineDto;
import com.workpoint.icpak.shared.model.MembershipStatus;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.auth.ApplicationStatus;

@Transactional
public class ApplicationFormDaoHelper {

	@Inject
	ApplicationFormDao applicationDao;

	@Inject
	RolesDao roleDao;
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
	@Inject
	MemberDaoHelper memberDaoHelper;

	SimpleDateFormat formatter = new SimpleDateFormat("MMM d Y");

	Logger logger = Logger.getLogger(ApplicationFormDaoHelper.class);
	Locale locale = new Locale("en", "KE");
	NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

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

		// Update existing User
		// User user = createTempUser(po);

		User user = userDao.findUserByMemberNo(application.getMemberNo());
		// user.getMember().set
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
		Role role = roleDao.getByName("basic_member");
		po.addRole(role);

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
			byte[] invoicePDF = generateInvoicePDF(application, invoice);
			String documentNo = "ProForma Invoice_" + application.getSurname();
			Attachment attachment = new Attachment();
			attachment.setAttachment(invoicePDF);
			attachment.setName(documentNo + ".pdf");

			Doc doc = generateEmailValues(application, invoice);

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

			// trxHelper.charge(user.getMember() == null ? null :
			// user.getMember()
			// .getRefId(), new Date(), subject, null, invoice
			// .getInvoiceAmount(), documentNo, invoice.getRefId());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public byte[] generateInvoicePDF(ApplicationFormHeader application,
			InvoiceDto invoice) throws FileNotFoundException, IOException,
			SAXException, ParserConfigurationException,
			FactoryConfigurationError, DocumentException {
		Doc doc = generateEmailValues(application, invoice);
		// PDF Invoice Generation
		InputStream inv = EmailServiceHelper.class.getClassLoader()
				.getResourceAsStream("registration-invoice.html");
		String invoiceHTML = IOUtils.toString(inv);
		byte[] invoicePDF = new HTMLToPDFConvertor().convert(doc, new String(
				invoiceHTML));
		return invoicePDF;
	}

	private Doc generateEmailValues(ApplicationFormHeader application,
			InvoiceDto invoice) {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("companyName", application.getEmployer());
		values.put("companyAddress", application.getAddress1() + " "
				+ application.getPostCode() + " " + application.getResidence()
				+ "," + application.getCountry());
		values.put("quoteNo", invoice.getDocumentNo());
		values.put("date", formatter.format(invoice.getDate()));
		values.put("firstName", application.getOtherNames());
		values.put("DocumentURL", settings.getApplicationPath());
		values.put("userRefId", application.getRefId());
		values.put("email", application.getEmail());
		Doc doc = new Doc(values);

		Map<String, Object> line = new HashMap<String, Object>();
		InvoiceLineDto lineDto = invoice.getLines().get(0);

		line.put("description", lineDto.getDescription());
		line.put("unitPrice", numberFormat.format(lineDto.getUnitPrice()));
		line.put("amount", numberFormat.format(lineDto.getTotalAmount()));
		values.put("totalAmount",
				numberFormat.format(invoice.getInvoiceAmount()));
		doc.addDetail(new DocumentLine("invoiceDetails", line));

		return doc;

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
		dto.setCompanyAddress(application.getAddress1() + "-"
				+ application.getPostCode() + " " + application.getResidence()
				+ "," + application.getCountry());
		dto.setPhoneNumber(application.getContactTelephone());
		dto.setContactName(application.getSurname() + " "
				+ application.getOtherNames());
		dto.setDate(new Date().getTime());

		InvoiceLineDto invLine = new InvoiceLineDto(dto.getContactName() + ", "
				+ "'" + category.getType().getDisplayName()
				+ "' member registration fee", category.getApplicationAmount(),
				category.getApplicationAmount());
		invLine.setQuantity(1);
		dto.addLine(invLine);

		dto = invoiceHelper.save(dto);

		return dto;
	}

	public void updateApplication(String applicationId,
			ApplicationFormHeaderDto dto) {
		ApplicationFormHeader po = applicationDao.findByApplicationId(
				applicationId, true);
		User user = applicationDao.findByRefId(po.getUserRefId(), User.class);
		updateUserFromApplicationFormChanges(po, user);
		// Fields only generated once
		dto.setUserId(po.getUserRefId());

		boolean isApplicationReadyForErp = false;
		boolean isApplicationReadyForEmail = false;
		if (dto.getApplicationStatus() != null) {
			isApplicationReadyForErp = (po.getApplicationStatus() == ApplicationStatus.SUBMITTED)
					&& (dto.getApplicationStatus() == ApplicationStatus.PROCESSING);
			isApplicationReadyForEmail = ((po.getApplicationStatus() != dto
					.getApplicationStatus()) || (dto.getManagementComment() != null));
			logger.info("Is the Application Ready For Email::"
					+ isApplicationReadyForEmail);
			logger.info("Is the Application ready for ERP::"
					+ isApplicationReadyForErp);

			// Send Review notification
			if (isApplicationReadyForEmail && !isApplicationReadyForErp) {
				sendReviewEmail(dto);
			}
		}
		// Post To ERP
		if (dto.getErpCode() != null && !dto.getErpCode().isEmpty()
				&& isApplicationReadyForErp) {
			try {
				String successMessage = postApplicationToERP(dto.getErpCode(),
						prepareErpDto(dto));
				if (successMessage.equals("success")) {
					sendReviewEmail(dto);
					logger.warn("This application was synced and email has been sent to applicant..");
					dto.setErpMessage(successMessage);
				} else {
					successMessage = "This application was not synced, there was a problem sending data to ERP...\n"
							+ "Error Message:" + successMessage;
					logger.warn(successMessage);
					dto.setErpMessage(successMessage);
					return;
				}
			} catch (URISyntaxException | ParseException | JSONException e) {
				e.printStackTrace();
			}
		}

		po.copyFrom(dto);
		applicationDao.updateApplication(po);
	}

	public User updateUserFromApplicationFormChanges(
			ApplicationFormHeader application, User user) {
		logger.info("Updating User Object" + user.getRefId());
		User userToBeUpdated = user;
		userToBeUpdated.getUserData().setFirstName(application.getSurname());
		userToBeUpdated.getUserData().setLastName(application.getOtherNames());
		userToBeUpdated.setFullName(application.getSurname() + " "
				+ application.getOtherNames());
		userToBeUpdated.setEmail(application.getEmail());
		userToBeUpdated.setPhoneNumber(application.getTelephone1());
		userDao.updateUser(userToBeUpdated);
		return userToBeUpdated;
	}

	SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy");
	private Properties props = new Properties();

	private void sendReviewEmail(ApplicationFormHeaderDto application) {
		logger.info("------Sending review email for "
				+ application.getSurname());
		boolean isSubmitted = application.getApplicationStatus() == ApplicationStatus.SUBMITTED;
		boolean hasPaid = application.getPaymentStatus() == PaymentStatus.PAID;
		boolean hasNotPaid = application.getPaymentStatus() == PaymentStatus.NOTPAID;
		boolean isPending = application.getApplicationStatus() == ApplicationStatus.PENDING;
		boolean isApproved = application.getApplicationStatus() == ApplicationStatus.APPROVED;
		boolean isBeingProcessed = application.getApplicationStatus() == ApplicationStatus.PROCESSING;
		boolean isCancelled = application.getApplicationStatus() == ApplicationStatus.CANCELLED;

		String subject = "Your ICPAK Application for Membership #"
				+ application.getId() + " ";
		String action = "";

		if (isPending) {
			subject = subject + " still Pending - Information Required!";
			action = " is still pending because of the following reason:<br/>";
		} else if (isCancelled) {
			subject = subject + " has Been Cancelled!";
			action = "  has been cancelled because of the following reason:<br/>";
		} else if (isBeingProcessed) {
			try {
				props.load(ApplicationFormDaoHelper.class.getClassLoader()
						.getResourceAsStream("bootstrap.properties"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Object nextRQAString = props.get("next_rqa_meeting");
			Date nextRQA = null;
			try {
				if (nextRQAString != null) {
					nextRQA = formatter2.parse(nextRQAString.toString());
					subject = subject + " is Being Processed!";
					action = " is now being processed. Please note that the next RQA meeting is on "
							+ ServerDateUtils.format(nextRQA, formatter);
				}
				logger.info(action);

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (isApproved) {
			subject = subject + " has been Approved!";
			action = " has been approved, you can proceed to login into your ICPAK account.";
		} else if (isSubmitted) {
			subject = subject + " has been Submitted Successfully!";
			if (hasNotPaid) {
				action = " has been Submitted Successfully, please pay for your application by "
						+ "clicking 'Proceed To Pay Button' on your application";
			} else if (hasPaid) {
				action = " has been Submitted Successfully, you will be notified on the status"
						+ " of this application as it being processed.";
			}
		}

		String link = settings.getApplicationPath();
		String additionLink = "<p><a href=" + link
				+ ">Go to my Application</a>" + "</p>";

		String noReplyMessage = "<p><Strong>Please DO NOT REPLY to this email. "
				+ "In case of any queries, kindly get in touch with us via "
				+ "memberservices@icpak.com" + "</Strong></p>";

		String body = "Dear "
				+ application.getSurname()
				+ " "
				+ application.getOtherNames()
				+ ","
				+ "<br/> "
				+ "<p>Your ICPAK Application for Membership "
				+ action
				+ (application.getManagementComment() == null ? "" : " <br/>'"
						+ application.getManagementComment() + "'")
				+ additionLink + "</p>" + noReplyMessage
				+ "<br/>Thank you,<br/>Member Services";
		try {
			EmailServiceHelper
					.sendEmail(body, subject, Arrays.asList(
							application.getEmail(), "itsupport@icpak.com"),
							Arrays.asList(application.getSurname() + " "
									+ application.getOtherNames(),
									"ICPAK ICT SUPPORT"));

		} catch (Exception e) {
			logger.info("Review Email for " + application.getEmail()
					+ " failed. Cause: " + e.getMessage());
			e.printStackTrace();
			// throw new Run
		}

	}

	private ApplicationERPDto prepareErpDto(ApplicationFormHeaderDto passedDto) {
		ApplicationFormHeaderDto application = passedDto;
		List<ApplicationFormEducationalDto> educationDetails = eduHelper
				.getAllEducationEntrys("", passedDto.getRefId(), 0, 100);
		List<ApplicationFormTrainingDto> trainings = trainingHelper
				.getAllTrainingEntrys("", passedDto.getRefId(), 0, 100);
		List<ApplicationFormAccountancyDto> accountancy = accountancyHelper
				.getAllAccountancyEntrys("", passedDto.getRefId(), 0, 100);
		List<ApplicationFormSpecializationDto> specializations = specializationHelper
				.getAllSpecializationEntrys("", passedDto.getRefId(), 0, 100);
		List<ApplicationFormEmploymentDto> employment = specializationHelper
				.getAllEmploymentEntrys("", passedDto.getRefId(), 0, 100);

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
		for (ApplicationFormHeader application : applications) {
			ApplicationFormHeaderDto dto = application.toDto();
			rtn.add(dto);
		}
		return rtn;
	}

	public List<ApplicationFormHeaderDto> getAllApplicationNativeQuery(
			Integer offset, Integer limit, String searchTerm,
			String applicationStatus, String paymentStatus) {
		return applicationDao.getAllApplicationDtos(offset, limit, searchTerm,
				applicationStatus, paymentStatus);
	}

	public List<ApplicationFormHeaderDto> importMembers(Integer offset,
			Integer limit) {
		List<MemberImport> applications = applicationDao.importMembers(offset,
				limit);
		List<ApplicationFormHeaderDto> rtn = new ArrayList<>();

		for (MemberImport member : applications) {
			System.err.println("Member: " + member.getName());
			// Copy into PO
			ApplicationFormHeader po = new ApplicationFormHeader();
			po.copyFrom(member.toDTO());
			po.setInvoiceRef("jnjndjjkkkkkkkk");
			po.setApplicationStatus(ApplicationStatus.APPROVED);

			User user = userDao.findUserByMemberNo(member.getMemberNo());
			if (user != null && user.getMember() != null) {
				// Update User Information - If Stale
				user.getMember()
						.setRegistrationDate(member.getDateRegistered());
				user.getMember().setPractisingNo(member.getPractisingNo());
				user.getMember().setCustomerType(member.getCustomerType());
				user.getMember().setCustomerPostingGroup(
						member.getCustomerPostingGroup());
				if (member.getStatus() == 1) {
					user.getMember().setMemberShipStatus(
							MembershipStatus.ACTIVE);
				} else {
					user.getMember().setMemberShipStatus(
							MembershipStatus.INACTIVE);
				}
				// Update Member Information
				po.setUserRefId(user.getRefId());

				System.err.println("Date of Birth::" + member.getDateOfBirth());
				System.err.println("Email::" + member.getEmail());
				System.err.println("Id No::" + po.getIdNumber());
				// Create this as an Application
				applicationDao.save(po);
				logger.info("Successfully saved application for member:::"
						+ member.getMemberNo() + " and refId ::"
						+ po.getRefId() + "User RefId::" + user.getRefId());
			}
		}

		return rtn;
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

	public String postApplicationToERP(String erpAppId,
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
		return result.toString();
	}

	public Integer getApplicationCount(String searchTerm, String paymentStatus,
			String applicationStatus) {
		return applicationDao.getApplicationCount(searchTerm, paymentStatus,
				applicationStatus);
	}

	public Integer getApplicationCount() {
		return 1;
	}

	public List<ApplicationFormHeaderDto> importMembers(
			List<MemberImport> memberImports) {
		List<ApplicationFormHeaderDto> rtn = new ArrayList<>();

		for (MemberImport memberIpmort : memberImports) {
			System.err.println("Member: " + memberIpmort.getName());
			// Copy into PO
			ApplicationFormHeader po = new ApplicationFormHeader();
			po.copyFrom(memberIpmort.toDTO());
			po.setInvoiceRef("jnjndjjkkkkkkkk");
			po.setApplicationStatus(ApplicationStatus.APPROVED);

			User user = userDao.findUserByMemberNo(memberIpmort.getMemberNo());
			if (user != null && user.getMember() != null) {
				// Update User Information - If Stale
				user.getMember().setRegistrationDate(
						memberIpmort.getDateRegistered());
				user.getMember()
						.setPractisingNo(memberIpmort.getPractisingNo());
				user.getMember()
						.setCustomerType(memberIpmort.getCustomerType());
				user.getMember().setCustomerPostingGroup(
						memberIpmort.getCustomerPostingGroup());
				if (memberIpmort.getStatus() == 1) {
					user.getMember().setMemberShipStatus(
							MembershipStatus.ACTIVE);
				} else {
					user.getMember().setMemberShipStatus(
							MembershipStatus.INACTIVE);
				}
				// Update Member Information
				po.setUserRefId(user.getRefId());

				System.err.println("Date of Birth::"
						+ memberIpmort.getDateOfBirth());
				System.err.println("Email::" + memberIpmort.getEmail());
				System.err.println("Id No::" + po.getIdNumber());
				// Create this as an Application
				applicationDao.save(po);
				logger.info("Successfully saved application for member:::"
						+ memberIpmort.getMemberNo() + " and refId ::"
						+ po.getRefId() + "User RefId::" + user.getRefId());
			} else {
				logger.warn(" MEMBER NO = = = == " + memberIpmort.getMemberNo());

				BioData bioData = new BioData();

				if (memberIpmort.getGender() == 0) {
					bioData.setGender(Gender.MALE);
				} else if (memberIpmort.getGender() == 1) {
					bioData.setGender(Gender.FEMALE);
				}

				bioData.setDob(memberIpmort.getDateOfBirth());

				user = new User();
				user.setEmail(memberIpmort.getEmail());
				user.setFullName(memberIpmort.getName());
				user.setMemberNo(memberIpmort.getNO());
				user.setAddress(memberIpmort.getAddress());
				user.setCity(memberIpmort.getCity());
				user.setMobileNo(memberIpmort.getPhoneNo_());
				user.setPassword("pass1");
				user.setUsername(memberIpmort.getEmail());
				user.setUserData(bioData);

				userDao.createUser(user);

				User userInDb = userDao.findUserByMemberNo(memberIpmort
						.getMemberNo());

				po.setUserRefId(user.getRefId());

				Member member = new Member();
				member.setMemberNo(memberIpmort.getNO());
				member.setRegistrationDate(memberIpmort.getDateRegistered());
				member.setPractisingNo(memberIpmort.getPractisingNo());
				member.setCustomerType(memberIpmort.getCustomerType());
				member.setCustomerPostingGroup(memberIpmort
						.getCustomerPostingGroup());
				member.setUserRefId(user.getRefId());
				member.setUser(userInDb);
				if (memberIpmort.getStatus() == 1) {
					member.setMemberShipStatus(MembershipStatus.ACTIVE);
				} else {
					member.setMemberShipStatus(MembershipStatus.INACTIVE);
				}

				userInDb.setMember(member);
				userDao.save(member);
				userDao.save(po);
				userDao.save(userInDb);

				logger.warn(" MEMBER ID = = = == " + memberIpmort.getId());
			}
		}

		return rtn;
	}

	public List<ApplicationFormHeaderDto> importMissingMembers(
			List<MemberImport> memberImports) {
		List<ApplicationFormHeaderDto> rtn = new ArrayList<>();

		for (MemberImport memberIpmort : memberImports) {

			Member memberAvailable = memberDaoHelper
					.findByMemberNo(memberIpmort.getMemberNo());
			if (memberAvailable == null) {
				logger.info("Member NAME : " + memberIpmort.getName());
				// Copy into PO
				ApplicationFormHeader po = new ApplicationFormHeader();
				po.copyFrom(memberIpmort.toDTO());
				po.setInvoiceRef("jnjndjjkkkkkkkk");
				po.setApplicationStatus(ApplicationStatus.APPROVED);

				logger.error(" MEMBER NO = = = == "
						+ memberIpmort.getMemberNo());

				BioData bioData = new BioData();

				if (memberIpmort.getGender() == 0) {
					bioData.setGender(Gender.MALE);
				} else if (memberIpmort.getGender() == 1) {
					bioData.setGender(Gender.FEMALE);
				}

				bioData.setDob(memberIpmort.getDateOfBirth());

				User user = new User();
				user.setEmail(memberIpmort.getEmail());
				user.setFullName(memberIpmort.getName());
				user.setMemberNo(memberIpmort.getMemberNo());
				user.setAddress(memberIpmort.getAddress());
				user.setCity(memberIpmort.getCity());
				user.setMobileNo(memberIpmort.getPhoneNo_());
				user.setPassword("pass1");
				user.setUsername(memberIpmort.getEmail());
				user.setUserData(bioData);

				userDao.createUser(user);

				List<User> usersInDb = userDao.findUsersByMemberNo(memberIpmort
						.getMemberNo());

				User userInDb = null;

				if (!usersInDb.isEmpty() && usersInDb != null) {
					userInDb = usersInDb.get(0);
				}

				po.setUserRefId(user.getRefId());

				Member member = new Member();
				member.setMemberNo(memberIpmort.getMemberNo());
				member.setRegistrationDate(memberIpmort.getDateRegistered());
				member.setPractisingNo(memberIpmort.getPractisingNo());
				member.setCustomerType(memberIpmort.getCustomerType());
				member.setCustomerPostingGroup(memberIpmort
						.getCustomerPostingGroup());
				member.setUserRefId(user.getRefId());
				member.setUser(userInDb);

				if (memberIpmort.getStatus() == 1) {
					member.setMemberShipStatus(MembershipStatus.ACTIVE);
				} else {
					member.setMemberShipStatus(MembershipStatus.INACTIVE);
				}

				if (userInDb != null) {
					userInDb.setMember(member);
					userDao.save(member);
					userDao.save(po);
					userDao.save(userInDb);
				}

				logger.error(" MEMBER ID = = = == " + memberIpmort.getId());
			}
		}

		memberDaoHelper.findDuplicateMemberNo();

		return rtn;
	}

	public String subribeToBranch(String applicationRefId, String branchName) {
		Branch subcribedBranch = Branch.valueOf(branchName);
		ApplicationFormHeader application = applicationDao
				.findByApplicationId(applicationRefId);
		if (application != null && subcribedBranch != null) {
			application.setBranch(subcribedBranch.name());
			applicationDao.save(application);
			return "SUCCESS";
		} else {
			return "FAILED";
		}
	}
}
