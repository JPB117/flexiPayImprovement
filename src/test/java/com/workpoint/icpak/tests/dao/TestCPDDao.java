package com.workpoint.icpak.tests.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.CPDDaoHelper;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestCPDDao extends AbstractDaoTest {

	@Inject
	CPDDaoHelper helper;

	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	@Ignore
	public void getCPD() throws ParseException {
		String memberId = "pabGC3dh0OOzLzSC";
		List<CPDDto> list = helper.getAllCPD("ALL", 0, 100,
				formatter.parse("01/01/2000").getTime(), new Date().getTime());

		for (CPDDto dto : list) {
			System.err.println(dto.getTitle());
			// System.err.println("Start Date:" + dto.getStartDate()
			// + " \nEnd Date:" + dto.getEndDate());
		}

	}

	@Test
	public void testCount() throws ParseException {
		// System.err.println(helper.getCount("ALL",
		// formatter.parse("01/01/2015")
		// .getTime(), new Date().getTime()));

		System.err.println(helper.getCount("ALL", formatter.parse("01/01/2000")
				.getTime(), new Date().getTime()));

	}

}
