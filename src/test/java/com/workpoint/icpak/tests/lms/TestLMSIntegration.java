package com.workpoint.icpak.tests.lms;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;

import com.workpoint.icpak.server.integration.lms.LMSIntegrationUtil;
import com.workpoint.icpak.shared.lms.LMSMemberDto;
import com.workpoint.icpak.shared.model.Gender;
import com.workpoint.icpak.shared.model.Title;

public class TestLMSIntegration {

	@Test
	public void sendMemberData() throws IOException{
		LMSMemberDto dto = new LMSMemberDto();
		dto.setDob(new Date());
		dto.setFirstName("Duggan");
		dto.setGender(Gender.MALE.getCode());
		dto.setLastName("Kimani");
		dto.setMobileNo("+254721239821");
		dto.setPassword("testpass");
		dto.setTimeZone("EAT");
		dto.setTitle(Title.Dr.getCode());
		dto.setUserName("mdkimani@gmail.com1");
		
		String response = LMSIntegrationUtil.getInstance().executeCall("/Account/Register", dto, String.class);
		System.out.println("SysResponse = "+response);
	}
}
