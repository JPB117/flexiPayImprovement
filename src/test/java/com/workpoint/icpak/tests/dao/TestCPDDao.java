package com.workpoint.icpak.tests.dao;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.CPDDaoHelper;
import com.icpak.servlet.upload.GetReport;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestCPDDao extends AbstractDaoTest {
	Logger logger = Logger.getLogger(TestCPDDao.class);

	@Inject
	CPDDaoHelper helper;
	@Inject
	GetReport reporter;

	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	@Ignore
	public void getCPD2() throws ParseException {
	}

	@Test
	public void generateGoodStandingCert() {

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
				formatter.parse("01/01/2000").getTime(), new Date().getTime());

		for (CPDDto dto : list) {
			System.err.println(dto.getTitle());
			// System.err.println("Start Date:" + dto.getStartDate()
			// + " \nEnd Date:" + dto.getEndDate());
		}

	}

	@Ignore
	public void testCount() throws ParseException {
		// System.err.println(helper.getCount("ALL",
		// formatter.parse("01/01/2015")
		// .getTime(), new Date().getTime()));

		System.err.println(helper.getCount("ALL", formatter.parse("01/01/2000")
				.getTime(), new Date().getTime()));

	}
	
	@Ignore
	public void testSearchCount(){
		int count = helper.cpdSearchCount("kimani");
		
		logger.error("========= CPP search count=== " + count);
		
	}
	
	@Test
	public void searchCount(){
		List<CPDDto> cpdDtos = helper.searchCPD("kimani", null, null);
		
		logger.error("========= CPP search count=== " + helper.cpdSearchCount("kimani"));
		logger.error("========= List length=== " + cpdDtos.size());
		
	}

}
