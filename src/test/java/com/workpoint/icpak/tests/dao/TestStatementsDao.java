package com.workpoint.icpak.tests.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.google.inject.Inject;
import com.icpak.rest.dao.InvoiceDaoHelper;
import com.icpak.rest.dao.helper.StatementDaoHelper;
import com.icpak.servlet.upload.GetReport;
import com.itextpdf.text.DocumentException;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestStatementsDao extends AbstractDaoTest{

	@Inject InvoiceDaoHelper helper; 
	@Inject StatementDaoHelper statementHelper;
	@Inject GetReport reportServlet;
	
	@Test 
	public void generateReport() throws FileNotFoundException, IOException, SAXException, ParserConfigurationException, FactoryConfigurationError, DocumentException{
		String memberRefId= "LLU0eoZpPuA4lfSU";
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
		
		IOUtils.write(bites, new FileOutputStream(new File("statements.pdf")));

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
