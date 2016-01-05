package com.icpak.rest.dao.helper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.IDUtils;
import com.icpak.rest.dao.CPDDao;
import com.icpak.rest.dao.MemberDao;
import com.icpak.rest.dao.StatementDao;
import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.cpd.CPD;
import com.icpak.rest.models.membership.Member;
import com.icpak.rest.models.trx.Statement;
import com.icpak.rest.models.util.Attachment;
import com.icpak.rest.utils.Doc;
import com.icpak.rest.utils.DocumentLine;
import com.icpak.rest.utils.EmailServiceHelper;
import com.icpak.rest.utils.HTMLToPDFConvertor;
import com.icpak.servlet.upload.GetReport;
import com.itextpdf.text.DocumentException;
import com.workpoint.icpak.shared.model.CPDStatus;
import com.workpoint.icpak.shared.model.MemberDto;
import com.workpoint.icpak.shared.model.UserDto;
import com.workpoint.icpak.shared.model.statement.StatementDto;
import com.workpoint.icpak.shared.model.statement.StatementSummaryDto;

/**
 * Created by wladek on 9/18/15. Database transactions are carried out here
 */
@Transactional
public class StatementDaoHelper {
	Logger logger = Logger.getLogger(StatementDaoHelper.class);

	@Inject
	StatementDao statementDao;
	@Inject
	MemberDao memberDao;
	@Inject
	CPDDao CPDDao;

	public List<StatementDto> getAllStatements(String memberId, Date startDate, Date endDate, Integer offset,
			Integer limit) {

		List<Statement> statementList = new ArrayList<>();
		Member member = null;
		if (memberId != null && memberId.equals("ALL")) {
			statementList = statementDao.getAllStatements(startDate, endDate, offset, limit);

		} else {
			member = statementDao.findByRefId(memberId, Member.class);
			if (member.getMemberNo() == null) {
				return new ArrayList<>();
			}
			statementList = statementDao.getAllStatements(member.getMemberNo(), startDate, endDate, offset, limit);
		}

		List<StatementDto> statementDtos = new ArrayList<>();
		StatementDto openingBal = null;
		if (startDate != null && member != null) {
			openingBal = statementDao.getOpeningBalance(member.getMemberNo(), startDate);
			openingBal.setDescription("Opening Balance");
			openingBal.setDocumentType("Opening Balance");
			statementDtos.add(openingBal);
		}

		for (Statement statement : statementList) {
			StatementDto statementDto = new StatementDto();
			statementDto.setDueDate(statement.getDueDate());
			statementDto.setCustomerNo(statement.getCustomerNo());
			statementDto.setDescription(statement.getDescription());
			statementDto.setAmount(statement.getAmount());
			statementDto.setDocumentNo(statement.getDocumentNo());
			statementDto.setDocumentType(statement.getDocumentType());
			statementDto.setPostingDate(statement.getPostingDate());
			statementDto.setEntryType(statement.getEntryType());
			statementDto.setCustLedgerEntryNo(statement.getCustLedgerEntryNo());
			statementDto.setEntryNo(statement.getEntryNo());
			statementDto.setRefId(statement.getRefId());

			statementDtos.add(statementDto);
		}

		// if (member != null) {
		// StatementDto balanceCF =
		// statementDao.getBalanceCD(member.getMemberNo(),
		// endDate == null ? new Date() : endDate);
		// balanceCF.setDescription("Balance C/F");
		// balanceCF.setDocumentType("Balance C/F");
		// statementDtos.add(balanceCF);
		// }

		return statementDtos;
	}

	public Integer getCount(String memberId, Date startDate, Date endDate) {
		if (memberId != null && memberId.equals("ALL")) {

			return statementDao.getStatementCount(null, startDate, endDate);
		} else {
			Member member = statementDao.findByRefId(memberId, Member.class);
			if (member.getMemberNo() == null) {
				return 0;
			}
			return statementDao.getStatementCount(member.getMemberNo(), startDate, endDate);
		}

	}

	public StatementDto createStatement(StatementDto statementDto) {

		assert statementDto.getRefId() == null;
		Statement statement = new Statement();
		statement.setRefId(IDUtils.generateId());
		// statementDto.setRefId(statement.getRefId());
		statement.copyFrom(statementDto);
		statementDao.save(statement);
		assert statement.getId() != null;

		return statement.toStatementDto();
	}

	public StatementDto getByStatementId(String statementId) {
		Statement statementInDb = statementDao.getByStatementId(statementId);
		return statementInDb.toStatementDto();
	}

	public void updateStatement(String statementId, StatementDto statementDto) {
		assert statementDto.getRefId() != null;
		Statement poStatementInDb = statementDao.getByStatementId(statementId);
		poStatementInDb.copyFrom(statementDto);
		statementDao.save(poStatementInDb);
	}

	public void delete(String statementId) {
		Statement poStatementInDb = statementDao.getByStatementId(statementId);
		statementDao.delete(poStatementInDb);
	}

	public Double getAccountBalance(String memberRefId) {
		Member member = statementDao.findByRefId(memberRefId, Member.class);
		return statementDao.getOpeningBalance(member.getMemberNo(), new Date()).getAmount();
	}

	/**
	 * 
	 * @param memberRefId
	 * @throws URISyntaxException
	 * @throws ParseException
	 * 
	 *             update registered member statement records
	 * @throws JSONException
	 */
	public void updateStatementsRecord(String memberRefId) throws URISyntaxException, ParseException, JSONException {
		Member memberInDb = statementDao.findByRefId(memberRefId, Member.class);

		List<Statement> memberStatements = getMemberStatementsFromErp(memberInDb);

		for (Statement statement : memberStatements) {
			logger.info("=== >><<<<< === Entry No from erp statements ==" + statement.getEntryNo());

			Statement statementInDb = statementDao.getByEntryNo(statement.getEntryNo(), true);

			logger.info("=== statement in Db refId ==" + statement.getRefId());

			if (statementInDb != null) {
				logger.info("=== >><<<<< === Updating ==");
				statementInDb.setCustLedgerEntryNo(statement.getCustLedgerEntryNo());
				statementInDb.setEntryType(statement.getEntryType());
				statementInDb.setPostingDate(statement.getPostingDate());
				statementInDb.setDocumentType(statement.getDocumentType());
				statementInDb.setDocumentNo(statement.getDocumentNo());
				statementInDb.setAmount(statement.getAmount());
				statementInDb.setDescription(statement.getDescription());
				statementInDb.setCustomerNo(statement.getCustomerNo());
				statementInDb.setDueDate(statement.getDueDate());

				statementDao.updateStatement(statementInDb);
			} else {
				logger.info("=== >><<<<< === Creating ==");

				statementDao.createStatement(statement);

			}
		}
	}

	/**
	 * 
	 * @param memberInDb
	 * @return
	 * @throws URISyntaxException
	 * @throws ParseException
	 * 
	 *             sends member statement request to icpak erp
	 * @throws JSONException
	 */
	public List<Statement> getMemberStatementsFromErp(Member memberInDb)
			throws URISyntaxException, ParseException, JSONException {
		List<Statement> memberStatements = new ArrayList<>();

		final HttpClient httpClient = new DefaultHttpClient();
		final List<NameValuePair> qparams = new ArrayList<NameValuePair>();

		qparams.add(new BasicNameValuePair("type", "statement"));
		qparams.add(new BasicNameValuePair("reg_no", memberInDb.getMemberNo()));

		final URI uri = URIUtils.createURI("http", "41.139.138.165/", -1, "members/memberdata.php",
				URLEncodedUtils.format(qparams, "UTF-8"), null);
		final HttpGet request = new HttpGet();
		request.setURI(uri);

		String res = "";
		HttpResponse response = null;

		StringBuffer result = null;

		try {
			request.setHeader("accept", "application/json");
			response = httpClient.execute(request);

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			result = new StringBuffer();

			String line = "";

			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		assert result != null;
		res = result.toString();

		/**
		 * Check if the erp server returns null string
		 */
		if (res.equals("null")) {

			logger.error(" ===>>><<<< === NO STATEMENTS FROM ERP ===>><<<>>== ");

		} else {
			JSONObject jo = new JSONObject(res);

			logger.error(" ===>>><<<< === jo " + jo.names().length());

			/**
			 * Loop through the array json object
			 */
			for (int i = 0; i < jo.names().length(); i++) {
				JSONObject jObject = (JSONObject) jo.getJSONObject(jo.names().getString(i));
				Statement erpMemberStatement = new Statement();
				erpMemberStatement.setEntryNo(jObject.getString("EntryNo_"));
				erpMemberStatement.setCustLedgerEntryNo(jObject.getString("Cust_LedgerEntryNo_"));
				erpMemberStatement.setEntryType(jObject.getString("EntryType"));
				erpMemberStatement.setPostingDate(formatter.parse(jObject.getString("PostingDate")));
				erpMemberStatement.setDocumentType(jObject.getString("DocumentType"));
				erpMemberStatement.setDocumentNo(jObject.getString("DocumentNo_"));
				erpMemberStatement.setAmount(new Double(jObject.getString("Amount")));
				erpMemberStatement.setDescription(jObject.getString("description"));
				erpMemberStatement.setCustomerNo(jObject.getString("CustomerNo_"));
				erpMemberStatement.setDueDate(formatter.parse(jObject.getString("due_date")));

				/**
				 * Add this statement to list
				 */
				memberStatements.add(erpMemberStatement);
			}
		}
		logger.error(" ===>>><<<< === result " + res);
		return memberStatements;
	}

	public StatementSummaryDto getSummary(String memberRefId, Date startDate, Date endDate) {

		Member member = statementDao.findByRefId(memberRefId, Member.class);

		StatementSummaryDto statementSummary = new StatementSummaryDto();
		if (member.getMemberNo() != null) {
			Double openingBalance = statementDao.getOpeningBalance(member.getMemberNo(), startDate).getAmount();
			Double totalDebit = openingBalance + (statementDao.getTotalDebit(member.getMemberNo(), startDate) == null
					? 0.0 : statementDao.getTotalDebit(member.getMemberNo(), startDate));
			Double totalCredit = statementDao.getTotalCredit(member.getMemberNo(), startDate) == null ? 0.0
					: statementDao.getTotalCredit(member.getMemberNo(), startDate);
			Double memberBalance = totalDebit - (-totalCredit);
			statementSummary.setTotalBalance(memberBalance);
			statementSummary.setTotalCredit(totalCredit);
			statementSummary.setTotalDebit(totalDebit);
		}
		return statementSummary;
	}

	public String initiTimer() {
		String result = null;

		List<MemberDto> members = memberDao.getMembersForschedular(0, 0);
		
		logger.info(" Size "+members.size());

		Date endDate = new Date();
		long fromDate = (endDate.getTime() - (7776000 * 1000 * 93));
		Date startDate = new Date(fromDate);

		for (MemberDto member : members) {
			logger.info(" refId "+member.getRefId());
			try {
				byte[] cpdStatementPDF = getMemberStatementPDF(member.getRefId(), startDate,
						endDate);

				Attachment attachment = new Attachment();
				attachment.setAttachment(cpdStatementPDF);
				attachment.setName("Quaterly_Statement_as_at_ " + endDate + " for_" + member.getMemberNo() + ".pdf");

				String html = "This is your annual quaterly Statement from " + fromDate + " to " + endDate;
				String subject = "ANNUAL QUATERLY STATEMENT";

				EmailServiceHelper.sendEmail(html, "RE: ICPAK '" + subject, Arrays.asList("wladek.airo@gmail.com"),
						Arrays.asList(member.getMemberNo()), attachment);

			} catch (IOException | SAXException | ParserConfigurationException | FactoryConfigurationError
					| DocumentException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	private byte[] getMemberStatementPDF(String memberRefId, Date startDate, Date endDate)
			throws IOException, SAXException, ParserConfigurationException, FactoryConfigurationError, DocumentException {
		Member member = null;
		if (memberRefId != null) {
			member = memberDao.findByRefId(memberRefId, Member.class);
		}

		User user = member.getUser();
		UserDto userDto = user.toDto();

		// List<CPDDto> cpds = cpdHelper.getAllMemberCpd(memberRefId, startDate,
		// endDate, 0, 1000);
		List<CPD> cpds = CPDDao.getAllCPDS(member.getRefId(), startDate == null ? null : startDate,
				endDate == null ? null : endDate, 0, 1000);
		List<CPD> sortedCpd = new ArrayList<>();
		for (CPD cpd : cpds) {
			if (cpd.getStatus() != null) {
				if (!cpd.getStatus().equals(CPDStatus.Unconfirmed)) {
					sortedCpd.add(cpd);
				}
			}
		}

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		logger.info("CPD Records Count = " + sortedCpd.size());

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("memberNames", userDto.getFullName());
		values.put("memberNo", user.getMemberNo());
		values.put("startYear", formatter.format(startDate));
		values.put("endYear", formatter.format(endDate));

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
			} else if (cpd.getCategory().toString().equals("CATEGORY_A")) {
				cpdCategory = "Structured";
				values.put("category", cpdCategory);
			} else {
				cpdCategory = "Un-structured";
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

				String currentCpdYear = formatter_.format(currentCpd.getEndDate());
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
							cpdStatementSummary2.put(currentCpdYear, cdpTableValues);
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

				} else if (cpdValue.getCategory().toString().equals("CATEGORY_A")) {
					totalStructured = totalStructured + cpdValue.getCpdHours();
				} else {
					totalUnstructured = totalUnstructured + cpdValue.getCpdHours();
				}
			}

			Double total = totalStructured + totalUnstructured;

			System.out.println("==total Structured " + totalStructured);
			System.out.println("==total totalUnstructured " + totalUnstructured);

			values.put("totalStructured", totalStructured);
			values.put("totalUnstructured", totalUnstructured);
			values.put("total", total);

			// create row to loop through
			DocumentLine line = new DocumentLine("cpdDetails", values);

			doc.addDetail(line);
		}

		HTMLToPDFConvertor convertor = new HTMLToPDFConvertor();
		InputStream is = GetReport.class.getClassLoader().getResourceAsStream("cpd-statement.html");
		String html = IOUtils.toString(is);
		byte[] data = convertor.convert(doc, html);

		String name = userDto.getFullName() + " " + formatter_.format(new Date()) + ".pdf";

		return data;
	}

}