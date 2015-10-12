package com.workpoint.icpak.tests.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.google.inject.Inject;
import com.icpak.rest.dao.CPDDao;
import com.icpak.rest.dao.InvoiceDaoHelper;
import com.icpak.rest.dao.helper.StatementDaoHelper;
import com.icpak.rest.models.cpd.CPD;
import com.icpak.servlet.upload.GetReport;
import com.itextpdf.text.DocumentException;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestStatementsDao extends AbstractDaoTest {

	@Inject
	InvoiceDaoHelper helper;
	@Inject
	StatementDaoHelper statementHelper;
	@Inject
	GetReport reportServlet;
	
	@Inject CPDDao cpdDao;

	@Ignore
	public void generateReport() throws FileNotFoundException, IOException,
			SAXException, ParserConfigurationException,
			FactoryConfigurationError, DocumentException {
		String memberRefId = "LLU0eoZpPuA4lfSU";
		
		byte[] bites = reportServlet.processStatementsRequest(memberRefId,
				null, null);
		IOUtils.write(bites, new FileOutputStream(new File("statements.pdf")));

	}
	
	@Test
	public void generateMemebrCPDReport() throws FileNotFoundException, IOException,
			SAXException, ParserConfigurationException,
			FactoryConfigurationError, DocumentException {
		String memberRefId = "69WQZqVMM54kunKf";
		
		List<CPD> cpds = cpdDao.getAllCPDS(memberRefId, null, null, 0, 1000);
		Assert.assertEquals(12, cpds.size());
		System.err.println("No of entries = "+cpds.size());
		
		
		byte[] bites = reportServlet.processMemberCPDStatementRequest(memberRefId, null, null);
		IOUtils.write(bites, new FileOutputStream(new File("memberStatement.pdf")));

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
}
