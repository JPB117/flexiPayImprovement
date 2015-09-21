package com.workpoint.icpak.tests.dao;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.gwtplatform.dispatch.rest.rebind.utils.Arrays;
import com.icpak.rest.dao.RolesDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.dao.helper.UsersDaoHelper;
import com.icpak.rest.models.auth.Role;
import com.workpoint.icpak.shared.model.RoleDto;
import com.workpoint.icpak.shared.model.UserDto;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestUserDao extends AbstractDaoTest{

	@Inject UsersDaoHelper helper;
	@Inject RolesDao roleDao;//Groups
	@Inject UsersDao usersDao;
	
	@Test
	public void hashPassword(){
		System.out.println(usersDao.encrypt("pass"));
	}
	
	@Ignore
	public void testCreateUser(){
		UserDto user = new UserDto();
		user.setEmail("kimani@at.co.ke");
		user.setName("Dennis");
		user.setSurname("Milgo");
		user.setPhoneNumber("0721002323");
		RoleDto dto = ((Role)roleDao.findByRefId("ngfLt4ERZm0hQtXp", Role.class)).toDto();
		user.setGroups(Arrays.asList(dto));
		
		//This was previously failing due to a not null contraint on the password column
		//TODO: run this query across the current dbs
		//alter table user modify column password varchar(64) null;
		UserDto saved = helper.create(user);
		Assert.assertNotNull(saved.getRefId());
	}
	
}
