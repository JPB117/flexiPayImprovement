package com.workpoint.icpak.tests.dao;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import com.amazonaws.util.json.JSONException;
import com.google.inject.Inject;
import com.icpak.rest.dao.MemberDao;
import com.icpak.rest.dao.helper.CPDDaoHelper;
import com.icpak.rest.dao.helper.MemberDaoHelper;
import com.icpak.rest.dao.helper.UsersDaoHelper;
import com.icpak.rest.models.membership.Member;
import com.workpoint.icpak.shared.model.MemberDto;
import com.workpoint.icpak.shared.model.MemberStanding;
import com.workpoint.icpak.shared.model.UserDto;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestMemberDao extends AbstractDaoTest {

	Logger logger = Logger.getLogger(TestMemberDao.class);

	@Inject
	MemberDaoHelper helper;
	@Inject
	UsersDaoHelper userHelper;
	@Inject
	CPDDaoHelper cpdDaoHelper;

	@Inject
	MemberDao memberDao;

	@Ignore
	public void resetPassword() {
		userHelper.resetAccount("gNtLJ03iEfS3LCac");
	}

	@Ignore
	public void testSearchMemberFromOldTable() {
		List<MemberDto> list = helper.getMembersFromOldTable("FCPA", "nairobi", "all", 0, 0);

		Integer count = helper.getMembersCount("FCPA", "nairobi", "all");

		logger.error("===== <>>>>>> Search Count === " + count);
		logger.error("===== <>>>>>> List Size === " + list.size());

	}

	// @Test
	public void search() {
		List<MemberDto> list = helper.getAllMembers(0, 100, "", "mar");
		for (MemberDto dto : list) {
			System.err.println(dto.getRefId() + " " + dto.getMemberNo() + " " + dto.getFullName());
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

	@Ignore
	public void updateMemberStatements() throws JSONException {
		Integer count = memberDao.getMembersCount();

		int offset = 0;
		int limit = 10;

		int trips = (count / limit) + 1;

		logger.info(" TOTAL TRIP = " + trips);

		// for (double trips = (countInt / limit); trips > 0; trips++) {
		// logger.info(" TRIP" + trips);
		// }
		while (trips > 0) {
			logger.info(" TRIP = " + trips);
			logger.info(" Offset = " + offset);

			List<String> memberNos = memberDao.getAllMemberNumbers(offset, limit);

			logger.info(" LENGTH " + memberNos.size());
			if (!memberNos.isEmpty()) {
				logger.info(" NOT EMPTY ");
				for (String memberNo : memberNos) {
					logger.info(" i = ");
					try {
						helper.updateMemberRecord(memberNo, true);
					} catch (IllegalStateException | IOException | ParseException e) {
						e.printStackTrace();
					}
				}
			}

			offset = offset + limit + 1;
			trips--;
		}
	}

	// @Test
	public void findDuplicateMemberNo() {
		helper.findDuplicateMemberNo();
	}

	@Test
	public void getAllMemberStandingStatus() {
		List<MemberDto> allMembers = helper.getAllMembers(0, 30000, "", "");

		for (MemberDto m : allMembers) {
			MemberStanding standing = cpdDaoHelper.getMemberStanding(m.getRefId());
			Member member = memberDao.findByRefId(m.getRefId(), Member.class);
			member.setInGoodStanding((standing.getStanding() == 1 ? true : false));
			member.setMemberBalance(standing.getMemberBalance());
			memberDao.save(member);
			System.out.println("Completed for memberNo>>" + m.getMemberNo());
		}

	}
}
