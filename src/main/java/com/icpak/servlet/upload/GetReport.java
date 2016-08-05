package com.icpak.servlet.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.AttachmentsDao;
import com.icpak.rest.dao.CPDDao;
import com.icpak.rest.dao.EventsDao;
import com.icpak.rest.dao.InvoiceDaoHelper;
import com.icpak.rest.dao.MemberDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.dao.helper.ApplicationFormDaoHelper;
import com.icpak.rest.dao.helper.BookingsDaoHelper;
import com.icpak.rest.dao.helper.CPDDaoHelper;
import com.icpak.rest.dao.helper.EventsDaoHelper;
import com.icpak.rest.dao.helper.StatementDaoHelper;
import com.icpak.rest.dao.helper.UsersDaoHelper;
import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.cpd.CPD;
import com.icpak.rest.models.event.Event;
import com.icpak.rest.models.membership.ApplicationFormHeader;
import com.icpak.rest.models.membership.GoodStandingCertificate;
import com.icpak.rest.models.membership.Member;
import com.icpak.rest.models.util.Attachment;
import com.icpak.rest.util.ApplicationSettings;
import com.icpak.rest.utils.Doc;
import com.icpak.rest.utils.DocumentLine;
import com.icpak.rest.utils.HTMLToPDFConvertor;
import com.itextpdf.text.DocumentException;
import com.workpoint.icpak.server.util.ServerDateUtils;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.shared.model.CPDStatus;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.MemberStanding;
import com.workpoint.icpak.shared.model.TransactionDto;
import com.workpoint.icpak.shared.model.UserDto;
import com.workpoint.icpak.shared.model.events.BookingDto;
import com.workpoint.icpak.shared.model.events.DelegateDto;
import com.workpoint.icpak.shared.model.events.EventDto;
import com.workpoint.icpak.shared.model.statement.StatementDto;

@Singleton
@Transactional
public class GetReport extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(GetReport.class);

	@Inject
	UsersDaoHelper helper;
	@Inject
	MemberDao memberDao;
	@Inject
	UsersDao userDao;
	@Inject
	AttachmentsDao attachmentDao;
	@Inject
	CPDDaoHelper cpdHelper;
	@Inject
	StatementDaoHelper statementDaoHelper;
	@Inject
	CPDDao CPDDao;
	@Inject
	BookingsDaoHelper bookingsDaoHelper;
	@Inject
	ApplicationFormDaoHelper applicationDaoHelper;
	@Inject
	InvoiceDaoHelper invoiceDaoHelper;
	@Inject
	EventsDaoHelper eventDaoHelper;
	@Inject
	EventsDao eventDao;
	@Inject
	ApplicationSettings settings;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		log.info("LOADED SERVLET " + getClass() + ": ContextPath= "
				+ config.getServletContext().getContextPath()
				+ ", ContextName= "
				+ config.getServletContext().getServletContextName()
				+ ", ServletName= " + config.getServletName());
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			executeGet(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
			writeError(resp, e.getMessage());
		}
	}

	protected void executeGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, SAXException,
			ParserConfigurationException, FactoryConfigurationError,
			DocumentException {

		String action = req.getParameter("action");
		if (action == null) {
			action = req.getParameter("ACTION");
		}
		log.debug("GetReport Action = " + action);
		if (action == null) {
			return;
		}
		if (action.equalsIgnoreCase("GETATTACHMENT")) {
			processAttachmentRequest(req, resp);
		}
		if (action.equalsIgnoreCase("GetUserImage")) {
			processUserImage(req, resp);
		}

		if (action.equalsIgnoreCase("GetLogo")) {
			processSettingsImage(req, resp);
		}

		if (action.equalsIgnoreCase("GENERATEOUTPUT")) {
			processOutputDoc(req, resp);
		}

		if (action.equalsIgnoreCase("DownloadCPDCert")) {
			processCPDCertRequest(req, resp);
		}

		if (action.equalsIgnoreCase("DownloadCertGoodStanding")) {
			processCertGoodStanding(req, resp);
		}

		if (action.equalsIgnoreCase("GETSTATEMENT")) {
			procesStatementsRequest(req, resp);
		}

		if (action.equals("GETCPDSTATEMENT")) {
			processMemberCPDStatementRequest(req, resp);
		}

		if (action.equals("GETDELEGATESREPORT")) {
			generateDelegateReport(req, resp);
		}

		if (action.equals("GETTRANSACTIONSREPORT")) {
			generateTransactionReport(req, resp);
		}

		if (action.equals("GETPROFORMA")) {
			processProformaInvoice(req, resp);
		}

		if (action.equals("GETOFFLINESQL")) {
			processOfflineSql(req, resp);
		}
		if (action.equals("SYNCTOSERVER")) {
			processSyncToServer(req, resp);
		}
	}

	private void processSyncToServer(HttpServletRequest req,
			HttpServletResponse resp) {
		if (req.getParameter("eventRefId") != null) {
			List<DelegateDto> allDelegates = bookingsDaoHelper.getAllDelegates(
					null, req.getParameter("eventRefId"), 0, 1000000, "", "",
					"");
			Integer successCount = bookingsDaoHelper.syncWithServer(req
					.getParameter("eventRefId"));
			writeError(resp, "Successful synced " + successCount + " out of "
					+ allDelegates.size() + " delegates..");
		}
	}

	private void processOfflineSql(HttpServletRequest req,
			HttpServletResponse resp) {
		byte[] data = bookingsDaoHelper.Backupdbtosql();
		processAttachmentRequest(resp, data, "backup_"
				+ ServerDateUtils.DATEFORMAT_SYS.format(new Date()) + ".sql");
	}

	private void processProformaInvoice(HttpServletRequest req,
			HttpServletResponse resp) throws FileNotFoundException,
			IOException, SAXException, ParserConfigurationException,
			FactoryConfigurationError, DocumentException {
		String bookingRefId = null;
		String applicationRefId = null;
		String invoiceRefId = null;
		if (req.getParameter("bookingRefId") != null) {

			bookingRefId = req.getParameter("bookingRefId");
			BookingDto booking = bookingsDaoHelper.getBookingById(null,
					bookingRefId);
			byte[] invoicePdf = bookingsDaoHelper
					.generateInvoicePdf(bookingRefId);

			processAttachmentRequest(resp, invoicePdf, booking.getContact()
					.getContactName() + ".pdf");
		}

		if (req.getParameter("applicationRefId") != null) {
			applicationRefId = req.getParameter("applicationRefId");
			ApplicationFormHeader application = applicationDaoHelper
					.getApplicationById(applicationRefId);

			InvoiceDto invoice = invoiceDaoHelper.getInvoice(application
					.getInvoiceRef());
			byte[] invoicePdf = applicationDaoHelper.generateInvoicePDF(
					application, invoice);

			processAttachmentRequest(resp, invoicePdf, application.getSurname()
					+ " " + application.getOtherNames() + ".pdf");
		}

		if (req.getParameter("invoiceRefId") != null) {
			invoiceRefId = req.getParameter("invoiceRefId");
			InvoiceDto invoice = invoiceDaoHelper.getInvoice(invoiceRefId);
			byte[] invoicePdf = invoiceDaoHelper.generatePDFDocument(invoice);
			processAttachmentRequest(resp, invoicePdf, invoice.getContactName()
					+ ".pdf");
		}

	}

	private void generateDelegateReport(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		String eventRefId = null;
		String docType = null;
		boolean updatePhones = false;
		if (req.getParameter("eventRefId") != null) {
			eventRefId = req.getParameter("eventRefId");
		}
		if (req.getParameter("docType") != null) {
			docType = req.getParameter("docType");
		}

		if (req.getParameter("updatePhones") != null) {
			if (req.getParameter("updatePhones").equals("true")) {
				updatePhones = true;
			}
		}
		List<DelegateDto> delegateDtos = bookingsDaoHelper.getAllDelegates("",
				eventRefId, null, 10000, "", "", "1", updatePhones);

		EventDto event = eventDaoHelper.getEventById(eventRefId);
		GetDelegatesReport report = new GetDelegatesReport(delegateDtos,
				docType, event.getName());
		processAttachmentRequest(resp, report.getBytes(), report.getName());

	}

	private void generateTransactionReport(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		String paymentType = null;
		String paymentMode = null;
		String searchTerm = null;
		String fromDate = null;
		String endDate = null;

		if (req.getParameter("paymentType") != null) {
			paymentType = req.getParameter("paymentType");
		}

		if (req.getParameter("paymentMode") != null) {
			paymentMode = req.getParameter("paymentMode");
		}

		if (req.getParameter("searchTerm") != null) {
			searchTerm = req.getParameter("searchTerm");
		}

		if (req.getParameter("fromDate") != null) {
			fromDate = req.getParameter("fromDate");
		}

		if (req.getParameter("endDate") != null) {
			endDate = req.getParameter("endDate");
		}

		List<TransactionDto> trxDtos = invoiceDaoHelper.getAllTransactions(
				"ALL", searchTerm, fromDate, endDate, paymentType, paymentMode,
				0, 100000);

		String reportName = "Transaction_report_";

		if (trxDtos.size() > 0) {
			reportName = reportName + "_" + trxDtos.get(0).getId() + "_"
					+ trxDtos.get(trxDtos.size() - 1).getId();
		}
		GetTransactionsReport report = new GetTransactionsReport(trxDtos,
				"xls", reportName);
		processAttachmentRequest(resp, report.getBytes(), report.getName());
	}

	private void procesStatementsRequest(HttpServletRequest req,
			HttpServletResponse resp) throws IOException, SAXException,
			ParserConfigurationException, FactoryConfigurationError,
			DocumentException {
		Long startDate = null;
		Long endDate = null;
		String memberRefId = null;

		if (req.getParameter("startdate") != null) {
			startDate = new Long(req.getParameter("startdate"));
		}

		if (req.getParameter("enddate") != null) {
			endDate = new Long(req.getParameter("enddate"));
		}

		if (req.getParameter("memberRefId") != null) {
			memberRefId = req.getParameter("memberRefId");
		}
		Date finalStartDate = startDate == null ? null : new Date(startDate);
		Date finalEndDate = endDate == null ? null : new Date(endDate);
		byte[] data = processStatementsRequest(memberRefId, finalStartDate,
				finalEndDate);
		processAttachmentRequest(resp, data, "statement.pdf");
	}

	// cdp statement Request
	private void processMemberCPDStatementRequest(HttpServletRequest req,
			HttpServletResponse resp) throws IOException, SAXException,
			ParserConfigurationException, FactoryConfigurationError,
			DocumentException {
		Long startDate = null;
		Long endDate = null;
		String memberRefId = null;
		String memberNo = null;

		if (req.getParameter("startdate") != null) {
			startDate = new Long(req.getParameter("startdate"));
		}

		if (req.getParameter("enddate") != null) {
			endDate = new Long(req.getParameter("enddate"));
		}

		if (req.getParameter("memberRefId") != null) {
			memberRefId = req.getParameter("memberRefId");
		}

		if (req.getParameter("memberNo") != null) {
			memberNo = req.getParameter("memberNo");
		}

		Date finalStartDate = new Date(startDate);
		Date finalEndDate = new Date(endDate);

		if (memberRefId.equals("ALLMEMBERS")) {
			// Create a new folder
			String folderName = getAllMembersCPDFilePath() + "_"
					+ ServerDateUtils.SHORTTIMESTAMP.format(new Date());
			File d = new File(folderName);
			// Create directory now.
			d.mkdir();

			// Get All members
			List<User> allUsers = userDao.getAllUsers();
			int counter = 0;
			for (User u : allUsers) {
				if (u.getMember() != null) {
					counter = counter + 1;
					byte[] data = processMemberCPDStatementRequest(null, u
							.getMember().getRefId(), finalStartDate,
							finalEndDate, resp);
					File file = new File(folderName + "/memberCPDStatement_"
							+ u.getMemberNo() + ".pdf");
					OutputStream os = new FileOutputStream(file);
					os.write(data);
					os.close();
				}
			}
			writeError(resp,
					"Completed generating memberCPD statement. Total Generated:"
							+ counter);
		} else {
			byte[] data = processMemberCPDStatementRequest(memberNo,
					memberRefId, finalStartDate, finalEndDate, resp);
			processAttachmentRequest(resp, data, "memberCPDStatement.pdf");
		}

	}

	private String getAllMembersCPDFilePath() {
		return settings.getProperty("allmembers_cpd_path");
	}

	public static String priceWithDecimal(Double price) {
		DecimalFormat formatter = new DecimalFormat("###,###,###.00");
		return formatter.format(price);
	}

	public byte[] processStatementsRequest(String memberRefId, Date startDate,
			Date endDate) throws FileNotFoundException, IOException,
			SAXException, ParserConfigurationException,
			FactoryConfigurationError, DocumentException {

		Member member = null;
		NumberFormat df = NumberFormat.getCurrencyInstance();
		try {
			member = userDao.findByRefId(memberRefId, Member.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		User user = null;
		if (member != null) {
			user = member.getUser();
		} else {
			// Probably an admin
			throw new IllegalArgumentException(
					"No Member statements found. Kindly confirm you are logged in");
		}

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		List<StatementDto> statements = statementDaoHelper.getAllStatements(
				memberRefId, startDate, endDate, 0, 1000);

		Double totalAmount = 0.00;
		Double total = 0.00;

		for (StatementDto statementDto : statements) {
			totalAmount = statementDto.getAmount() + totalAmount;
		}

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("date", formatter.format(new Date()));
		values.put("startingDate", formatter.format(startDate));
		StatementDto lastStatement = statements.get(statements.size() - 1);

		values.put("endingDate",
				formatter.format(lastStatement.getPostingDate()));
		values.put("memberAddress",
				(user.getAddress() == null ? "" : user.getAddress()) + " "
						+ user.getPostalCode());
		values.put("memberLocation", user.getCity());
		values.put("memberNo", user.getMemberNo());
		values.put("memberNames", user.getFullName());
		values.put("totalAmount", priceWithDecimal(totalAmount));

		Doc doc = new Doc(values);

		for (StatementDto dto : statements) {
			total = dto.getAmount() + total;
			values = new HashMap<String, Object>();
			values.put("postingDate", formatter.format(dto.getPostingDate()));
			values.put("type", (dto.getDocumentType().equals("null") || dto
					.getDocumentType() == null) ? "" : dto.getDocumentType());
			values.put("docNo",
					(dto.getDocumentNo() == null ? "" : dto.getDocumentNo()));
			values.put("description", dto.getDescription());
			values.put("debit",
					dto.getAmount() < 0 ? priceWithDecimal(dto.getAmount())
							: 0.00);
			values.put("credit",
					dto.getAmount() > 0 ? priceWithDecimal(dto.getAmount())
							: 0.00);
			values.put("balance", priceWithDecimal(total));
			DocumentLine line = new DocumentLine("statementDetail", values);
			doc.addDetail(line);
		}

		HTMLToPDFConvertor convertor = new HTMLToPDFConvertor();
		InputStream is = GetReport.class.getClassLoader().getResourceAsStream(
				"member-statement.html");
		String html = IOUtils.toString(is);
		byte[] data = convertor.convert(doc, html);

		String name = user.getMemberNo() + " " + formatter.format(new Date())
				+ ".pdf";

		return data;
	}

	private void processCPDCertRequest(HttpServletRequest req,
			HttpServletResponse resp) throws IOException, SAXException,
			ParserConfigurationException, FactoryConfigurationError,
			DocumentException {
		String cpdRefId = req.getParameter("cpdRefId");
		assert cpdRefId != null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		CPDDto cpd = cpdHelper.getCPD(cpdRefId);
		assert cpd != null;

		// Event e =eventDao.getByEvenFromCPDId(id, true)

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventName", cpd.getTitle());
		values.put("eventDates", formatter.format(cpd.getStartDate()) + " to "
				+ formatter.format(cpd.getEndDate()));
		values.put("memberName", cpd.getFullNames());
		values.put("dateIssued", formatter.format(cpd.getEndDate()));
		values.put("cpdHours", cpd.getCpdHours());

		if ((cpd.getEventLocation() == null || cpd.getEventLocation().isEmpty())) {
			Event e = eventDao
					.findByRefId(cpd.getEventId(), Event.class, false);
			if (e != null) {
				values.put("eventVenue", e.getVenue());
			}
		} else {
			values.put("eventVenue", cpd.getEventLocation());
		}
		Doc doc = new Doc(values);
		HTMLToPDFConvertor convertor = new HTMLToPDFConvertor();
		InputStream is = GetReport.class.getClassLoader().getResourceAsStream(
				"cpdcertificate.html");
		String html = IOUtils.toString(is);
		byte[] data = convertor.convert(doc, html);

		String name = cpd.getFullNames() + " " + cpd.getTitle()
				+ formatter.format(new Date()) + ".pdf";

		processAttachmentRequest(resp, data, name);
	}

	private void processCertGoodStanding(HttpServletRequest req,
			HttpServletResponse resp) throws IOException, SAXException,
			ParserConfigurationException, FactoryConfigurationError,
			DocumentException {

		String memberId = req.getParameter("memberRefId");
		if (memberId == null) {
			writeError(resp,
					"Member Id must be provied to generate this certificate");
			return;
		}

		Member member = userDao.findByRefId(memberId, Member.class);
		User user = member.getUser();

		MemberStanding standing = cpdHelper.getMemberStanding(memberId);
		if (standing.getStanding() == 0) {
			for (String reason : standing.getReasons()) {
				writeError(resp, reason);
			}
			return;
		}

		GoodStandingCertificate cert = new GoodStandingCertificate();
		cert.setMember(member);
		userDao.save(cert);
		userDao.flush();
		cert = userDao.findByRefId(cert.getRefId(),
				GoodStandingCertificate.class); // reload?

		// userDao.merge(cert);

		if (cert.getId() == null) {
			writeError(resp,
					"Your Cert reference number was not generated, kindly contact ICPAK for help");
			return;
		}

		Map<String, Object> values = new HashMap<String, Object>();
		String refNo = cert.getId() + "";// memberDao.getGoodStandingCertDocNumber(cert.getId());

		values.put("refNo", cert.getId());
		values.put("letterDate",
				ServerDateUtils.HALFDATEFORMAT.format(new Date()));
		values.put("memberName", user.toDto().getFullName().toUpperCase());
		values.put("smallName", user.toDto().getFullName());
		values.put("memberNo", member.getMemberNo());
		values.put("cpdHours", cpdHelper.getCPDHours(memberId));
		values.put("firstName", user.getUserData().getFirstName());
		Doc doc = new Doc(values);

		HTMLToPDFConvertor convertor = new HTMLToPDFConvertor();
		InputStream is = GetReport.class.getClassLoader().getResourceAsStream(
				"good_standing.html");
		String html = IOUtils.toString(is);
		byte[] data = convertor.convert(doc, html);

		Attachment attachment = new Attachment();
		attachment.setGoodStandingCert(cert);
		attachment.setAttachment(data);
		attachment.setContentType("application/pdf");
		attachment.setSize(data.length);
		userDao.save(attachment);

		String name = "CertificateOfGoodStanding_" + refNo + ".pdf";

		processAttachmentRequest(resp, data, name);
	}

	private void writeError(HttpServletResponse resp, String message) {
		resp.setContentType("text/html");
		try {
			PrintWriter out = resp.getWriter();
			out.print(message);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void processOutputDoc(HttpServletRequest req,
			HttpServletResponse resp) {
		String outdoc = req.getParameter("template");
		String name = req.getParameter("name");
		String doc = req.getParameter("doc");
		String path = req.getParameter("path");

		// assert outdoc!=null && doc!=null;
		//
		// byte[] pdf;
		//
		// try {
		//
		// String template = OutputDocumentDaoHelper.getHTMLTemplate(outdoc);
		// Long documentId = new Long(doc);
		// Doc document = DocumentDaoHelper.getDocument(documentId);
		// pdf = new HTMLToPDFConvertor().convert(document, template);
		//
		// LocalAttachment attachment = new LocalAttachment();
		//
		// AttachmentDaoImpl dao = DB.getAttachmentDao();
		// List<LocalAttachment> attachments =
		// dao.getAttachmentsForDocument(documentId, name);
		// if(!attachments.isEmpty()){
		// attachment = attachments.get(0);
		// }
		//
		// attachment.setArchived(false);
		// attachment.setContentType("application/pdf");
		// attachment.setDocument(DB.getDocumentDao().getById(documentId));
		// attachment.setName(name+(name.endsWith(".pdf")? "": name));
		// attachment.setPath(path);
		// attachment.setSize(pdf.length);
		// attachment.setAttachment(pdf);
		// dao.save(attachment);
		//
		// } catch (IOException | SAXException | ParserConfigurationException
		// | FactoryConfigurationError | DocumentException e) {
		//
		// e.printStackTrace();
		// return;
		// }
		//
		// processAttachmentRequest(resp, pdf, name);
	}

	private void processSettingsImage(HttpServletRequest req,
			HttpServletResponse resp) {

		// String settingName = req.getParameter("settingName");
		// log.debug("Logging- SettingName "+settingName);
		// assert settingName!=null;
		//
		// String widthPx = req.getParameter("width");
		// String heightPx = req.getParameter("height");
		//
		// if(widthPx!=null && heightPx==null){
		// heightPx=widthPx;
		// }
		//
		// Double width=null;
		// Double height=null;
		//
		// if(widthPx!=null && widthPx.matches("[0-9]+(\\.[0-9][0-9]?)?")){
		// width = new Double(widthPx);
		// height = new Double(heightPx);
		// }
		//
		// LocalAttachment attachment =
		// DB.getAttachmentDao().getSettingImage(SETTINGNAME.valueOf(settingName));
		//
		// if(attachment==null){
		// log.debug("No Attachment Found for Setting: ["+settingName+"]");
		// return;
		// }
		//
		// log.debug("Attachment found for setting: ["+settingName+"], FileName
		// = "+attachment.getName());
		//
		// byte[] bites = attachment.getAttachment();
		//
		// if(width!=null){
		// ImageUtils.resizeImage(resp, bites, width.intValue(),
		// height.intValue());
		// }else{
		// ImageUtils.resizeImage(resp, bites);
		// }
	}

	private void processUserImage(HttpServletRequest req,
			HttpServletResponse resp) {
		String userId = req.getParameter("userRefId");
		assert userId != null;

		String widthPx = req.getParameter("width");
		String heightPx = req.getParameter("height");

		if (widthPx != null && heightPx == null) {
			heightPx = widthPx;
		}

		Double width = null;
		Double height = null;

		if (widthPx != null && widthPx.matches("[0-9]+(\\.[0-9][0-9]?)?")) {
			width = new Double(widthPx);
			height = new Double(heightPx);
		}

		Attachment attachment = attachmentDao.getUserProfileImage(userId);

		if (attachment == null)
			return;

		processAttachmentRequest(resp, attachment);

		// if(width!=null){
		// ImageUtils.resizeImage(resp, bites, width.intValue(),
		// height.intValue());
		// }else{
		// ImageUtils.resizeImage(resp, bites);
		// }

	}

	/**
	 * http://domain:port/icpakportal/
	 * 
	 * @param req
	 * @param resp
	 */
	private void processAttachmentRequest(HttpServletRequest req,
			HttpServletResponse resp) {
		String refId = req.getParameter("refId");
		if (refId == null) {
			writeError(resp, "RefId is required to download attachment");
			return;
		}

		Attachment a = attachmentDao.findByRefId(refId, Attachment.class);
		if (a == null) {
			writeError(resp, "Attachment with id '" + refId
					+ "' could not be found");
		}

		// LocalAttachment attachment = DB.getAttachmentDao().getAttachmentById(
		// Long.parseLong(id));

		processAttachmentRequest(resp, a);
	}

	private void processAttachmentRequest(HttpServletResponse resp,
			Attachment attachment) {
		resp.setContentType(attachment.getContentType());

		if (attachment.getAttachment() != null) {
			processAttachmentRequest(resp, attachment.getAttachment(),
					attachment.getName());
		} else {
			try {
				processAttachmentRequest(resp,
						IOUtils.toByteArray(new FileInputStream(new File(
								attachment.getFileName()))),
						attachment.getName());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void processAttachmentRequest(HttpServletResponse resp,
			byte[] data, String name) {
		if (name.endsWith("png") || name.endsWith("jpg")
				|| name.endsWith("html") || name.endsWith("htm")
				|| name.endsWith("svg") || name.endsWith("pdf")) {
			// displayed automatically
			resp.setHeader("Content-disposition", "inline;filename=\"" + name);
		} else {
			resp.setHeader("Content-disposition", "attachment;filename=\""
					+ name);
		}
		resp.setContentLength(data.length);
		writeOut(resp, data);
	}

	private void writeOut(HttpServletResponse resp, byte[] data) {
		ServletOutputStream out = null;
		try {
			out = resp.getOutputStream();
			out.write(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		try {
			out.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// generate member cpd statement pdf
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	public byte[] processMemberCPDStatementRequest(String memberNo,
			String memberRefId, Date startDate, Date endDate,
			HttpServletResponse resp) throws FileNotFoundException,
			IOException, SAXException, ParserConfigurationException,
			FactoryConfigurationError, DocumentException {
		Member member = null;
		if (memberRefId != null) {
			member = memberDao.findByRefId(memberRefId, Member.class);
		} else {
			member = memberDao.getByMemberNo(memberNo);
		}

		if (member == null) {
			writeError(resp,
					"No member found, check if you passed correct memberNo or memberRefId..");
			return null;
		}

		User user = member.getUser();
		UserDto userDto = user.toDto();

		// Get All CPD Records
		List<CPD> cpds = CPDDao.getAllCPDS(member.getRefId(),
				startDate == null ? null : startDate, endDate == null ? null
						: endDate, 0, 10000);
		List<CPD> sortedCpd = new ArrayList<>();
		for (CPD cpd : cpds) {
			if (cpd.getStatus() != null) {
				if (cpd.getStatus().equals(CPDStatus.Approved)) {
					sortedCpd.add(cpd);
				}
			}
		}

		// Write out member details
		log.info("CPD Records Count = " + sortedCpd.size());
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("memberNames", userDto.getFullName());
		values.put("memberNo", user.getMemberNo());
		values.put("startYear", formatter.format(startDate));
		values.put("endYear", formatter.format(endDate));

		// Document information
		Doc doc = new Doc(values);
		for (CPD cpd : sortedCpd) {
			String cpdCategory = null;
			values = new HashMap<String, Object>();
			values.put("number", "CPD-" + cpd.getId());
			values.put("courseName", cpd.getTitle());
			values.put("date", formatter.format(cpd.getEndDate()));

			if (cpd.getCategory() == null) {
				cpdCategory = "No Category";
				values.put("category", cpdCategory);
			} else if (cpd.getCategory().toString().equals("CATEGORY_D")) {
				cpdCategory = "Un-structured";
				values.put("category", cpdCategory);
			} else {
				cpdCategory = "Structured";
				values.put("category", cpdCategory);
			}
			values.put("cpd", cpd.getCpdHours());

			// create row to loop through
			DocumentLine line = new DocumentLine("invoiceDetails", values);
			doc.addDetail(line);
		}
		SimpleDateFormat formatter_ = new SimpleDateFormat("yyyy");

		Hashtable<String, List<CPD>> cpdStatementSummary = new Hashtable<>();
		Hashtable<String, List<CPD>> cpdStatementSummary2 = new Hashtable<>();

		int cpdCount = sortedCpd.size();

		for (int i = 0; i < cpdCount; i++) {
			List<CPD> cdpTableValues = new ArrayList<>();
			// current cpd
			CPD currentCpd = sortedCpd.get(i);

			for (CPD cpd : sortedCpd) {
				String currentCpdYear = formatter_.format(currentCpd
						.getEndDate());
				String comparisonYear = formatter_.format(cpd.getEndDate());

				if (currentCpdYear.equals(comparisonYear)) {
					cdpTableValues.add(cpd);
				}

				// check if our hash tree is empty
				if (cpdStatementSummary.isEmpty()) {
					cpdStatementSummary2.put(currentCpdYear, cdpTableValues);
				} else {
					for (Map.Entry m : cpdStatementSummary.entrySet()) {
						if (!m.getKey().equals(currentCpdYear)) {
							cpdStatementSummary2.put(currentCpdYear,
									cdpTableValues);
						}
					}
				}

			}
		}

		// lets print the final hash table
		for (Map.Entry m : cpdStatementSummary2.entrySet()) {
			// a list to hold the key values that are cpds
			List<CPD> hashTreeValues = (List<CPD>) m.getValue();

			Double totalStructured = new Double(0);
			Double totalUnstructured = new Double(0);
			values = new HashMap<String, Object>();
			values.put("year", m.getKey());
			for (CPD cpdValue : hashTreeValues) {
				if (cpdValue.getCategory() == null) {
				} else if (cpdValue.getCategory().toString()
						.equals("CATEGORY_D")) {
					totalUnstructured = totalUnstructured
							+ cpdValue.getCpdHours();
				} else {
					totalStructured = totalStructured + cpdValue.getCpdHours();
				}
			}

			Double total = totalStructured + totalUnstructured;
			values.put("totalStructured", totalStructured);
			values.put("totalUnstructured", totalUnstructured);
			values.put("total", total);
			// create row to loop through
			DocumentLine line = new DocumentLine("cpdDetails", values);
			doc.addDetail(line);
			System.out.println("==>total Structured> " + totalStructured);
			System.out.println("==>total totalUnstructured> "
					+ totalUnstructured);

		}

		HTMLToPDFConvertor convertor = new HTMLToPDFConvertor();
		InputStream is = GetReport.class.getClassLoader().getResourceAsStream(
				"cpd-statement.html");
		String html = IOUtils.toString(is);
		byte[] data = convertor.convert(doc, html);
		String name = userDto.getFullName() + " "
				+ formatter_.format(new Date()) + ".pdf";

		return data;
	}

	// calculates the cpd totals in a given cpd list
	public Double calculateCPDTotal(List<CPD> cpdList) {
		Double total = new Double(0);

		for (CPD cpd : cpdList) {
			total = total + cpd.getCpdHours();
		}

		return total;
	}

	private String getFilePath() {
		return settings.getProperty("upload_path");
	}
}
