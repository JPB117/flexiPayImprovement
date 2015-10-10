package com.workpoint.icpak.tests.dao;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.InvoiceDaoHelper;
import com.icpak.rest.dao.helper.StatementDaoHelper;
import com.icpak.servlet.upload.GetReport;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestStatementsDao extends AbstractDaoTest{

	@Inject InvoiceDaoHelper helper; 
	@Inject StatementDaoHelper statementHelper;
	@Inject GetReport reportServlet;
	
	@Test 
	public void generateReport(){
		String memberId= "MRnWxqBFVfwdnMQ2";
		reportServlet.processStatementsRequest(memberRefId, startDate, endDate)
	}
	
	@Ignore
	public void getStatementCount(){
		String memberId= "MRnWxqBFVfwdnMQ2";
		Integer count = statementHelper.getCount(memberId, null, null);
		System.out.println(count);
		Assert.assertEquals(new Integer(9), count);
	}
	/**
	 * This was used to assign refIds to
	 * imported cpds
	 */
	@Ignore
	public void insertIds(){
		helper.insertIds();
	}
}
