package com.workpoint.icpak.tests.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.CPDDaoHelper;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestCPDDao extends AbstractDaoTest{

	@Inject CPDDaoHelper helper;
	@Test
	public void getCPD(){
		String memberId= "pabGC3dh0OOzLzSC";
		List<CPDDto> list = helper.getAllCPD(memberId, 0, 500);
		
		Assert.assertEquals(180,list.size());
	}
}
