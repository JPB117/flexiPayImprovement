package com.workpoint.icpak.tests.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import com.amazonaws.util.json.JSONObject;
import com.google.inject.Inject;
import com.icpak.rest.dao.MemberDao;
import com.icpak.rest.dao.helper.MemberDaoHelper;
import com.icpak.rest.dao.helper.UsersDaoHelper;
import com.workpoint.icpak.shared.model.MemberDto;
import com.workpoint.icpak.shared.model.UserDto;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestMemberDao extends AbstractDaoTest {

	Logger logger = Logger.getLogger(TestMemberDao.class);

	@Inject
	MemberDaoHelper helper;
	@Inject
	UsersDaoHelper userHelper;
	@Inject
	MemberDao memberDao;

	@Ignore
	public void resetPassword() {
		userHelper.resetAccount("gNtLJ03iEfS3LCac");
	}

	@Ignore
	public void testSearchMemberFromOldTable() {
		List<MemberDto> list = helper.getMembersFromOldTable("FCPA", "nairobi",
				"all", 0, 0);

		Integer count = helper.getMembersCount("FCPA", "nairobi", "all");

		logger.error("===== <>>>>>> Search Count === " + count);
		logger.error("===== <>>>>>> List Size === " + list.size());

	}

	@Test
	public void search() {
		List<MemberDto> list = helper.getAllMembers(0, 100, "", "mar");
		for (MemberDto dto : list) {
			System.err.println(dto.getRefId() + " " + dto.getMemberNo() + " "
					+ dto.getFullName());
		}
		System.err.print("Size = " + list.size());
	}

	@Ignore
	public void searchUser() {
		List<UserDto> list = userHelper.getAllUsers(0, 100, "", "mar");
		for (UserDto dto : list) {
			System.err.println(dto.getRefId() + " " + dto.getName() + " ");
		}
		System.err.print("Size = " + list.size());
	}

	@Ignore
	public void testGetMambers() {
		List<MemberDto> members = memberDao.getMembers(0, 0);

		logger.info(" Size = " + members.size());
	}

	// @Test
	public void updateMemberStatements() {
		Integer count = memberDao.getMembersCount();

		int offset = 0;
		int limit = 10;

		double trips = (count / limit);

		// for (double trips = (countInt / limit); trips > 0; trips++) {
		// logger.info(" TRIP" + trips);
		// }
		int count2 = 3;
		while (count2 > 1) {
			logger.info(" TRIP = " + trips);
			logger.info(" Offset = " + offset);

			List<String> memberNos = memberDao.getAllMemberNumbers(offset,
					limit);

			logger.info(" LENGTH " + memberNos.size());
			if (!memberNos.isEmpty()) {
				JSONObject jo = new JSONObject(memberNos);
				logger.info(" ITEMS = " + memberNos);
			}

			offset = offset + limit + 1;
			trips = trips - 1;

			count2--;
		}
	}
}
