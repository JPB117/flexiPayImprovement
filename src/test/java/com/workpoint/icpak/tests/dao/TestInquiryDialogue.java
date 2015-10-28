package com.workpoint.icpak.tests.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.EnquiriesDaoHelper;
import com.icpak.rest.models.util.EnquiriesDialogue;
import com.workpoint.icpak.shared.model.EnquiriesCategory;
import com.workpoint.icpak.shared.model.EnquiriesDto;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestInquiryDialogue extends AbstractDaoTest {
	Logger logger = Logger.getLogger(TestInquiryDialogue.class);

	@Inject
	EnquiriesDaoHelper enquiriesDaoHelper;

	@Ignore
	public void testDialogue() {
		String result = null;

		String enquiryRefId = "GPqar5cNe9lAHJsD";
		String memberRefId = "LLU0eoZpPuA4lfSU";
		String responseMessage = "Another Dialog Item";
		result = enquiriesDaoHelper.respondToInquiry(enquiryRefId, memberRefId,
				responseMessage);
		logger.debug(" ====== >><<<<<<<<======= dialogue message " + result);
	}

	@Ignore
	public void testGetEnquiries() {
		String enquiryRefId = "GPqar5cNe9lAHJsD";

		List<EnquiriesDialogue> dialogues = enquiriesDaoHelper
				.getEnquiryDialogue(enquiryRefId);

		for (EnquiriesDialogue dialogue : dialogues) {
			logger.debug(" ====== >><<<<<<<<======= dialogue message "
					+ dialogue.getText());
		}
	}
	
	@Test
	public void testCreateEnquiry(){
		String memberRefId = "LLU0eoZpPuA4lfSU";
		
		EnquiriesDto enquiriesDto = new EnquiriesDto();
		enquiriesDto.setDate(new Date());
		enquiriesDto.setEmailAddress("wladek.airo@gmail.com");
		enquiriesDto.setMessage("Test Mail");
		enquiriesDto.setFullNames("Wladek");
		enquiriesDto.setPhone("0705712841");
		enquiriesDto.setCategory(EnquiriesCategory.COMMENT);
		
		enquiriesDaoHelper.create(memberRefId, enquiriesDto);
	}

}
