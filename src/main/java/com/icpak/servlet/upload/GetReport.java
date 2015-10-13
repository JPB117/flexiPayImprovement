package com.icpak.servlet.upload;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
import com.icpak.rest.dao.AttachmentsDao;
import com.icpak.rest.dao.CPDDao;
import com.icpak.rest.dao.MemberDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.dao.helper.CPDDaoHelper;
import com.icpak.rest.dao.helper.StatementDaoHelper;
import com.icpak.rest.dao.helper.UsersDaoHelper;
import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.cpd.CPD;
import com.icpak.rest.models.membership.Member;
import com.icpak.rest.models.util.Attachment;
import com.icpak.rest.utils.Doc;
import com.icpak.rest.utils.DocumentLine;
import com.icpak.rest.utils.HTMLToPDFConvertor;
import com.itextpdf.text.DocumentException;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.shared.model.UserDto;
import com.workpoint.icpak.shared.model.statement.StatementDto;

@Singleton
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
			throw new RuntimeException(e);
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

		if (action.equalsIgnoreCase("GETSTATEMENT")) {
			procesStatementsRequest(req, resp);
		}

		if (action.equals("GETCPDSTATEMENT")) {
			processMemberCPDStatementRequest(req, resp);
		}

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

		Date finalStartDate = new Date(startDate);
		Date finalEndDate = new Date(endDate);

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

		if (req.getParameter("startdate") != null) {
			startDate = new Long(req.getParameter("startdate"));
		}

		if (req.getParameter("enddate") != null) {
			endDate = new Long(req.getParameter("enddate"));
		}

		if (req.getParameter("memberRefId") != null) {
			memberRefId = req.getParameter("memberRefId");
		}

		Date finalStartDate = new Date(startDate);
		Date finalEndDate = new Date(endDate);

		byte[] data = processMemberCPDStatementRequest(memberRefId,
				finalStartDate, finalEndDate);

		processAttachmentRequest(resp, data, "memberCPDStatement.pdf");
	}

	public byte[] processStatementsRequest(String memberRefId, Date startDate,
			Date endDate) throws FileNotFoundException, IOException,
			SAXException, ParserConfigurationException,
			FactoryConfigurationError, DocumentException {

		Member member = null;

		try {
			member = userDao.findByRefId(memberRefId, Member.class);
		} catch (Exception e) {
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

		for (StatementDto statementDto : statements) {
			totalAmount = statementDto.getAmount() + totalAmount;
		}

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("date", formatter.format(new Date()));
		values.put("memberAddress", user.getAddress());
		values.put("memberLocation", user.getCity());
		values.put("memberNo", user.getMemberNo());
		values.put("memberNames", user.getUserData().getFullNames());
		values.put("totalAmount", totalAmount);

		Doc doc = new Doc(values);

		for (StatementDto dto : statements) {

			values = new HashMap<String, Object>();
			values.put("postingDate", formatter.format(dto.getPostingDate()));
			values.put("docNo", dto.getDocumentNo());
			values.put("description", dto.getDescription());
			values.put("originalAmount", dto.getAmount());
			values.put("credit", dto.getAmount());
			values.put("balance", dto.getAmount());
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

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("eventName", cpd.getTitle());
		values.put("eventDates", formatter.format(cpd.getStartDate()) + " to "
				+ formatter.format(cpd.getEndDate()));
		values.put("memberName", cpd.getFullNames());
		values.put("dateIssued", formatter.format(new Date()));
		values.put("cpdHours", cpd.getCpdHours());
		values.put("eventVenue", cpd.getEventLocation());
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
		// log.debug("Attachment found for setting: ["+settingName+"], FileName = "+attachment.getName());
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

	private void processAttachmentRequest(HttpServletRequest req,
			HttpServletResponse resp) {
		String id = req.getParameter("attachmentId");
		if (id == null)
			return;

		// LocalAttachment attachment = DB.getAttachmentDao().getAttachmentById(
		// Long.parseLong(id));

		// processAttachmentRequest(resp, attachment);
	}

	private void processAttachmentRequest(HttpServletResponse resp,
			Attachment attachment) {

		resp.setContentType(attachment.getContentType());
		processAttachmentRequest(resp, attachment.getAttachment(),
				attachment.getName());
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
	public byte[] processMemberCPDStatementRequest(String memberRefId,
			Date startDate, Date endDate) throws FileNotFoundException,
			IOException, SAXException, ParserConfigurationException,
			FactoryConfigurationError, DocumentException {

		Member member = memberDao.findByRefId(memberRefId, Member.class);
		User user = member.getUser();

		UserDto userDto = user.toDto();

		// List<CPDDto> cpds = cpdHelper.getAllMemberCpd(memberRefId, startDate,
		// endDate, 0, 1000);
		List<CPD> cpds = CPDDao.getAllCPDS(memberRefId, null, null, 0, 1000);
		log.info("CPD Records Count = " + cpds.size());

		SimpleDateFormat formatter = new SimpleDateFormat("YYYY");
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("memberNames", userDto.getFullName());
		values.put("memberNo", user.getMemberNo());
		values.put("startYear", formatter.format(startDate));
		values.put("endYear", formatter.format(endDate));

		Doc doc = new Doc(values);

		for (CPD cpd : cpds) {
			values = new HashMap<String, Object>();
			values.put("number", cpd.getMemberId());
			values.put("courseName", cpd.getTitle());
			values.put("date", cpd.getEndDate());
			values.put("category", cpd.getCategory());
			values.put("cpd", cpd.getCpdHours());
			// create row to loop through
			DocumentLine line = new DocumentLine("invoiceDetails", values);

			doc.addDetail(line);
		}

		for (CPD cpd : cpds) {
			values = new HashMap<String, Object>();
			values.put("year", cpd.getCreated());
			values.put("totalStructured", cpd.getTitle());
			values.put("totalUnstructured", cpd.getEndDate());
			values.put("category", cpd.getCategory());
			values.put("total", cpd.getCpdHours());

			// create row to loop through
			DocumentLine line = new DocumentLine("cpdDetails", values);

			doc.addDetail(line);
		}

		HTMLToPDFConvertor convertor = new HTMLToPDFConvertor();
		InputStream is = GetReport.class.getClassLoader().getResourceAsStream(
				"cpd-statement.html");
		String html = IOUtils.toString(is);
		byte[] data = convertor.convert(doc, html);

		String name = userDto.getFullName() + " "
				+ formatter.format(new Date()) + ".pdf";

		return data;
	}
}
