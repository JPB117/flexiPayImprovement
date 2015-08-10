package com.workpoint.icpak.tests.dao;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.UserSessionDao;
import com.icpak.rest.dao.helper.UsersDaoHelper;
import com.workpoint.icpak.shared.model.UserDto;
import com.workpoint.icpak.shared.model.auth.LogInAction;
import com.workpoint.icpak.shared.model.auth.LogInResult;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestLoginAuth extends AbstractDaoTest{
	
	@Inject UsersDaoHelper helper;
	@Inject UserSessionDao sessionDao;

	@Ignore
	public void auth2(){
		
		LogInResult result = helper.execLogin(new LogInAction("mdkimani@gmail.com", "Th1KsC"));
		Assert.assertTrue(result.getCurrentUserDto().getUser() != null);
		
	}
	
	@Ignore
	public void authenticateCookie(){
		LogInResult result = helper.execLogin(new LogInAction("3a59edd4-b473-471a-ba67-f2424f5022bf"));
		Assert.assertTrue(result.getCurrentUserDto().getUser() != null);
		Assert.assertTrue(result.getCurrentUserDto().isLoggedIn());
	}
	
	@Test
	public void logout(){
		sessionDao.removeLoggedInCookie(new UserDto("NPw8aeaZlJtCS6I2"));
	}
}
