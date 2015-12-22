package com.workpoint.icpak.tests.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONObject;
import com.google.inject.Inject;
import com.icpak.rest.dao.CPDDao;
import com.icpak.rest.dao.helper.CPDDaoHelper;
import com.icpak.rest.models.cpd.CPD;
import com.icpak.servlet.upload.GetReport;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.shared.model.CPDStatus;
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
		CPD cpd = cpdDao.findByCPDId("xU8Bf2olyPVxWQom");
		CPDDto dto = cpd.toDTO();
		dto.setStatus(CPDStatus.Rejected);
		dto.setManagementComment("This is Ok, but please provide the necessary attachments");
		helper.update("3pzAyw110E2i5VTE", "xU8Bf2olyPVxWQom", dto);
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
	public void testSearchCount() {
		int count = helper.cpdSearchCount("kimani");

		logger.error("========= CPP search count=== " + count);

	}

	@Test
	public void testSearch() {
		Long startDate = 1420059600000L;
		Long endDate = 1450645200000L;
		List<CPDDto> cpdDtos = helper.searchCPD("2020", 0, 10, startDate,
				endDate);
		logger.error("========= CPP search count=== "
				+ helper.cpdSearchCount("2020"));
		logger.error("========= List length=== " + cpdDtos.size());

	}

	@Ignore
	public void testGetAllCPD() throws ParseException {
		Date today = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date another = formatter.parse("2015-1-1");
		List<CPDDto> cpdDtos = helper.getAllCPD("ALL", 0, 1000,
				another.getTime(), today.getTime());
		JSONArray jArray = new JSONArray();

		for (CPDDto dto : cpdDtos) {
			JSONObject jO = new JSONObject(dto);
			jArray.put(jO);
		}

		logger.error("========= List length=== " + cpdDtos.size());
		logger.error("========= RESULT JARRAY === " + jArray.toString());
	}

}
