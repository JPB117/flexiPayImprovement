package com.workpoint.icpak.tests.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Inject;
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

	@Ignore
	public void resetPassword() {
		userHelper.resetAccount("gNtLJ03iEfS3LCac");
	}

	@Test
	public void testSearchMemberFromOldTable() {
		List<MemberDto> list = helper.getMembersFromOldTable("FCPA",
				"nairobi", "all", 0, 0);

		Integer count = helper.getMembersCount("FCPA", "nairobi",
				"all");
		
		logger.error("===== <>>>>>> Search Count === "+count);
		logger.error("===== <>>>>>> List Size === "+list.size());

	}

	@Ignore
	public void search() {
		List<MemberDto> list = helper.getAllMembers(0, 100, "", "mar");
		for (MemberDto dto : list) {
			System.err.println(dto.getRefId() + " " + dto.getMemberNo() + " "
					+ dto.getLastName() + " " + dto.getFirstName());
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
}
