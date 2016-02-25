package com.workpoint.icpak.tests.dao;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.gwtplatform.dispatch.rest.rebind.utils.Arrays;
import com.icpak.rest.dao.RolesDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.dao.helper.UsersDaoHelper;
import com.icpak.rest.models.auth.Role;
import com.icpak.rest.models.auth.User;
import com.workpoint.icpak.shared.model.RoleDto;
import com.workpoint.icpak.shared.model.UserDto;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestUserDao extends AbstractDaoTest {
	Logger logger = Logger.getLogger(TestUserDao.class);

	@Inject
	UsersDaoHelper helper;
	@Inject
	RolesDao roleDao;// Groups
	@Inject
	UsersDao usersDao;

	@Ignore
	public void hashPassword() {
		System.out.println(usersDao.encrypt("pass"));
	}

	@Ignore
	public void testCreateUser() {
		UserDto user = new UserDto();
		user.setEmail("kimani@at.co.ke");
		user.setName("Dennis");
		user.setSurname("Milgo");
		user.setPhoneNumber("0721002323");
		RoleDto dto = ((Role) roleDao.findByRefId("ngfLt4ERZm0hQtXp",
				Role.class)).toDto();
		user.setGroups(Arrays.asList(dto));

		// This was previously failing due to a not null contraint on the
		// password column
		// TODO: run this query across the current dbs
		// alter table user modify column password varchar(64) null;
		UserDto saved = helper.create(user);
		Assert.assertNotNull(saved.getRefId());
	}

	@Ignore
	public void getUser() {
		User user = usersDao.findByUserId("cJJooj0U1kizZ9bY");
		UserDto userDto = new UserDto();
		userDto = user.toDto();
		System.err.println(userDto.getLastDateUpdateFromErp());
	}

	@Ignore
	public void getAllUsers() {
		List<UserDto> users = usersDao.getAllUsersDtos(0, 10, null, "");

		for (UserDto user : users) {
			System.err.println(">>>" + user.getFullName());
		}
	}

	@Ignore
	public void testEmails() {
		helper.resetAccount("30ZDpATQxFXx9WR7");
	}

	@Ignore
	public void testLMS() throws IOException {
		helper.postUserToLMS("bf4j7WfaMDBph6nP", "pass");
	}

	@Ignore
	public void updatePwd() {
		List<User> users = usersDao.getAllUsers();
		logger.error("== Users found = " + users.size());
		int count = 1;
		for (User u : users) {
			logger.error("== Updating password user = " + count);
			helper.updatePassword(u.getRefId(), "pass");
			count++;
		}
	}

	@Ignore
	public void testLMSIntergration() throws IOException {
		// helper.postUserToLMS("AOUcFrD0WgflCc1O", "pass");
		helper.getUserByActivationEmail("sammurez@gmail.com");
	}

	@Ignore
	public void testActivationMail() {
		// User userInDb = usersDao.findByUserId("AoencYogwvoPxa7T");
		// helper.sendActivationEmail2(userInDb);
		helper.getUserByActivationEmail("tomkim@wira.io");
	}
	
	@Test
	public void getGender(){
		String gender = usersDao.getGender("7087");
		
		logger.warn(" GENDER === "+gender);
	}
}
