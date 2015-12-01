package com.workpoint.icpak.tests.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.amazonaws.util.json.JSONException;
import com.google.inject.Inject;
import com.icpak.rest.dao.CPDDao;
import com.icpak.rest.dao.InvoiceDaoHelper;
import com.icpak.rest.dao.helper.StatementDaoHelper;
import com.icpak.rest.models.cpd.CPD;
import com.icpak.servlet.upload.GetReport;
import com.itextpdf.text.DocumentException;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestStatementsDao extends AbstractDaoTest{
	Logger logger = Logger.getLogger(TestStatementsDao.class);

	@Inject InvoiceDaoHelper helper; 
	@Inject StatementDaoHelper statementHelper;
	@Inject GetReport reportServlet;
	@Inject CPDDao cpdDao;
	
	@Ignore
	public void generateReport() throws FileNotFoundException, IOException, SAXException, ParserConfigurationException, FactoryConfigurationError, DocumentException{
		String memberRefId= "cb4ZWESs9um1k8BN";
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, 02);
		c.set(Calendar.YEAR, 2009);
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		byte[] bites = reportServlet.processStatementsRequest(memberRefId,
				c.getTime(), null);

		IOUtils.write(bites, new FileOutputStream(new File("/home/wladek/Documents/statements.pdf")));

	}

	@Ignore
	public void generateMemebrCPDReport() throws FileNotFoundException,
			IOException, SAXException, ParserConfigurationException,
			FactoryConfigurationError, DocumentException {
		String memberRefId = "69WQZqVMM54kunKf";

//		List<CPD> cpds = cpdDao.getAllCPDS(memberRefId, null, null, 0, 1000);
//		Assert.assertEquals(12, cpds.size());
//		System.err.println("No of entries = " + cpds.size());

		byte[] bites = reportServlet.processMemberCPDStatementRequest(
				memberRefId, null, null);
		IOUtils.write(bites, new FileOutputStream(new File(
				"memberStatement.pdf")));

	}

	@Ignore
	public void getStatementCount() {
		String memberId = "MRnWxqBFVfwdnMQ2";
		Integer count = statementHelper.getCount(memberId, null, null);
		System.out.println(count);
		Assert.assertEquals(new Integer(9), count);
	}

	/**
	 * This was used to assign refIds to imported cpds
	 */
	@Ignore
	public void insertIds() {
		helper.insertIds();
	}
	
	@Ignore
	public void testFromErp() throws URISyntaxException, ParseException, JSONException{
		statementHelper.updateStatementsRecord("cb4ZWESs9um1k8BN");
	}
	
	@Test
	public void testDateFormatter() throws ParseException{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = (Date) formatter.parse("2012-05-21");
		logger.info(">>>>> Formatted "+date);
	}
}
