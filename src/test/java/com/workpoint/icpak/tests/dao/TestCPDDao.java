package com.workpoint.icpak.tests.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.CPDDao;
import com.icpak.rest.dao.helper.CPDDaoHelper;
import com.icpak.rest.models.cpd.CPD;
import com.icpak.servlet.upload.GetReport;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.shared.model.MemberCPDDto;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestCPDDao extends AbstractDaoTest {
	Logger logger = Logger.getLogger(TestCPDDao.class);

	@Inject
	CPDDaoHelper helper;
	@Inject
	CPDDao cpdDao;
	@Inject
	GetReport reporter;

	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	@Ignore
	public void getCPD2() throws ParseException {
	}

	@Ignore
	public void generateGoodStandingCert() {

	}

	@Ignore
	public void testCreateCPD() {
		CPD cpd = cpdDao.findByCPDId("NgmZcYUU0mu7JEyr");
		CPDDto dto = cpd.toDTO();
		dto.setManagementComment("Please provide the necessary attachments");
		helper.update("3pzAyw110E2i5VTE", "NgmZcYUU0mu7JEyr", dto);
	}

	@Ignore
	public void getCPDHrs() {
		double val = helper.getCPDHours(null);
		System.err.println(val);
	}

	@Ignore
	public void getCPD() throws ParseException {
		String memberId = "pabGC3dh0OOzLzSC";
		List<CPDDto> list = helper.getAllCPD("ALL", 0, 1000,
				formatter.parse("01/01/2000").getTime(), new Date().getTime(),
				"Another");
		for (CPDDto dto : list) {
			System.err.println(dto.getTitle());
		}

	}

	@Ignore
	public void testCount() throws ParseException {
		System.err.println("Total Archive>>>>"
				+ helper.getCPDSummary("ALL", 1420059600000L, 1451494912593L)
						.getTotalArchive()
				+ "Total Returns"
				+ helper.getCPDSummary("ALL", 1420059600000L, 1451494912593L)
						.getTotalReturns());
	}

	@Ignore
	public void testMemberCPD() throws ParseException {
		List<MemberCPDDto> memberCPDDtos = cpdDao.getMemberCPD("1020", 0, 10);
		System.err.println("Count>>" + cpdDao.getMemberCPDCount("1020"));
		for (MemberCPDDto memberCPD : memberCPDDtos) {
			System.err.println("No_" + memberCPD.getMemberNo());
			System.err.println("Category::" + memberCPD.getCustomerType());
			System.err.println("Status::" + memberCPD.getStatus());
			System.err.println("2015::" + memberCPD.getYear2015());
			System.err.println("2011::" + memberCPD.getYear2011());
		}

		// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		// Date another = formatter.parse("2013-1-1");
		//
		// cpdDao.getYearSummaries("iypUNLFxbsEFMJLB", another, new Date());
	}

	@Ignore
	public void testSearchCount() {
		int count = helper.cpdSearchCount("kimani");

		logger.error("========= CPP search count=== " + count);

	}

	@Ignore
	public void testSearch() {
		Long startDate = 1420059600000L;
		Long endDate = 1450645200000L;
		// List<CPDDto> cpdDtos = helper.searchCPD("2020", 0, 10, startDate,
		// endDate);
		// logger.error("========= CPP search count=== "
		// + helper.cpdSearchCount("2020"));
		// logger.error("========= List length=== " + cpdDtos.size());

	}

	@Ignore
	public void testGetAllCPD() throws ParseException {
		Date today = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date another = formatter.parse("2015-1-1");

		List<CPDDto> cpdDtos = helper.getAllCPD("returnArchive", 0, 100,
				another.getTime(), today.getTime(), "");

		// Integer count = helper.getCount("ALLRETURNS", another.getTime(),
		// today.getTime());

		for (CPDDto dto : cpdDtos) {
			System.err.println("Title>>" + dto.getTitle());
		}
		logger.error("======= List length=== " + cpdDtos.size());
	}
	
	@Test
	public void transferCPDBlob(){
		cpdDao.dumpBlobToFile();
	}
	
	@After
	public void commitTrx(){
		//commit();
	}
}
