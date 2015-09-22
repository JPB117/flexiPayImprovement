package com.workpoint.icpak.tests.dao;

import javax.transaction.Transactional;

import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.InvoiceDaoHelper;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestStatementsDao extends AbstractDaoTest{

	@Inject InvoiceDaoHelper helper; 
	
	@Test
	public void insertIds(){
		helper.insertIds();
	}
}
