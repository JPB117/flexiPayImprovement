package com.icpak.servlet.upload;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
import com.icpak.rest.dao.MemberDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.dao.helper.CPDDaoHelper;
import com.icpak.rest.dao.helper.StatementDaoHelper;
import com.icpak.rest.dao.helper.UsersDaoHelper;
import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.cpd.CPD;
import com.icpak.rest.models.membership.GoodStandingCertificate;
import com.icpak.rest.models.membership.Member;
import com.icpak.rest.models.util.Attachment;
import com.icpak.rest.utils.Doc;
import com.icpak.rest.utils.DocumentLine;
import com.icpak.rest.utils.HTMLToPDFConvertor;
import com.itextpdf.text.DocumentException;
import com.workpoint.icpak.server.util.DateUtils;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.shared.model.MemberStanding;
import com.workpoint.icpak.shared.model.UserDto;

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
			// throw new RuntimeException(e);
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
		values.put("letterDate", DateUtils.DATEFORMAT.format(new Date()));
		values.put("memberName", user.toDto().getFullName());
		values.put("memberNo", member.getMemberNo());
		values.put("cpdHours", cpdHelper.getCPDHours(memberId));
		values.put("firstName", user.getUserData().getFirstName());
		Doc doc = new Doc(values);

		HTMLToPDFConvertor convertor = new HTMLToPDFConvertor();
		InputStream is = GetReport.class.getClassLoader().getResourceAsStream(
				"goodstanding_certificate.html");
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
		List<CPD> cpds = CPDDao.getAllCPDS(memberRefId,
				startDate == null ? null : startDate, endDate == null ? null
						: endDate, 0, 1000);
		log.info("CPD Records Count = " + cpds.size());

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("memberNames", userDto.getFullName());
		values.put("memberNo", user.getMemberNo());

		Doc doc = new Doc(values);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		for (CPD cpd : cpds) {
			String cpdCategory = null;
			values = new HashMap<String, Object>();
			values.put("number", "CPD-" + cpd.getId());
			values.put("courseName", cpd.getTitle());
			values.put("date", formatter.format(cpd.getEndDate()));

			if (cpd.getCategory() != null) {
				if (cpd.getCategory().toString().equals("CATEGORY_A")) {

					cpdCategory = "Structured";
					values.put("category", cpdCategory);

				} else {

					cpdCategory = "Un-strucured";
					values.put("category", cpdCategory);
				}
			} else {
				System.err.println("Cpd does not have a category");
			}
			values.put("cpd", cpd.getCpdHours());

			// create row to loop through
			DocumentLine line = new DocumentLine("invoiceDetails", values);

			doc.addDetail(line);
		}

		SimpleDateFormat formatter_ = new SimpleDateFormat("yyyy");

		Hashtable<String, List<CPD>> cpdStatementSummary = new Hashtable<>();
		Hashtable<String, List<CPD>> cpdStatementSummary2 = new Hashtable<>();

		int cpdCount = cpds.size();

		for (int i = 0; i < cpdCount; i++) {
			List<CPD> cdpTableValues = new ArrayList<>();
			// current cpd
			CPD currentCpd = cpds.get(i);

			for (CPD cpd : cpds) {

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
				if (cpdValue.getCategory().toString().equals("CATEGORY_A")) {
					totalStructured = totalStructured + cpdValue.getCpdHours();
				} else {
					totalUnstructured = totalUnstructured
							+ cpdValue.getCpdHours();
				}
			}

			Double total = totalStructured + totalUnstructured;

			System.out.println("==total Structured " + totalStructured);
			System.out
					.println("==total totalUnstructured " + totalUnstructured);

			values.put("totalStructured", totalStructured);
			values.put("totalUnstructured", totalUnstructured);
			values.put("total", total);

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
}
