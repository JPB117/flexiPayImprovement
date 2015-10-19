package com.workpoint.icpak.tests.dao;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.EnquiriesDaoHelper;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestInquiryDialogue extends AbstractDaoTest {
	Logger logger = Logger.getLogger(TestInquiryDialogue.class);
	
	@Inject EnquiriesDaoHelper enquiriesDaoHelper;
	
	@Test
	public void testDialogue(){
		String result = null;
		
		String enquiryRefId = "GPqar5cNe9lAHJsD";
		String memberRefId = "LLU0eoZpPuA4lfSU";
		String responseMessage = "Test Dialogue";
		
		result = enquiriesDaoHelper.respondToInquiry(enquiryRefId, memberRefId, responseMessage);
		
		logger.debug(" ====== >><<<<<<<<======= dialogue message " + result);
	}

}
