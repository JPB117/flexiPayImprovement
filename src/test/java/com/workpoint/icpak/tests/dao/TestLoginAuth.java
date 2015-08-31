package com.workpoint.icpak.tests.dao;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.UserSessionDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.dao.helper.UsersDaoHelper;
import com.icpak.rest.models.auth.User;
import com.workpoint.icpak.shared.model.UserDto;
import com.workpoint.icpak.shared.model.auth.LogInAction;
import com.workpoint.icpak.shared.model.auth.LogInResult;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestLoginAuth extends AbstractDaoTest{
	
	@Inject UsersDaoHelper helper;
	@Inject UserSessionDao sessionDao;
	
	@Inject UsersDao usersDao;

	@Ignore
	public void getUser(){
		User user= usersDao.findUserByMemberNo("ASSOC/867");
		Assert.assertNotNull(user);
		System.err.println(user.getRefId());
	}
	
	@Test
	public void auth2(){
		
		LogInResult result = helper.execLogin(new LogInAction("ASSOC/867", "pass1"));
		Assert.assertTrue(result.getCurrentUserDto().getUser() != null);
		
	}
	
	@Ignore
	public void authenticateCookie(){
		LogInResult result = helper.execLogin(new LogInAction("3a59edd4-b473-471a-ba67-f2424f5022bf"));
		Assert.assertTrue(result.getCurrentUserDto().getUser() != null);
		Assert.assertTrue(result.getCurrentUserDto().isLoggedIn());
	}
	
	@Ignore
	public void logout(){
		sessionDao.removeLoggedInCookie(new UserDto("NPw8aeaZlJtCS6I2"));
	}
}
