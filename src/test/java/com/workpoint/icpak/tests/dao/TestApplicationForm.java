package com.workpoint.icpak.tests.dao;

import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.ApplicationFormDaoHelper;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationType;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestApplicationForm extends AbstractDaoTest{

	@Inject ApplicationFormDaoHelper helper;
	
	@Test
	public void create(){
		ApplicationFormHeaderDto header = new ApplicationFormHeaderDto();
		header.setSurname("Macharia");
		header.setOtherNames("Duggan");
		header.setEmail("mdkimani@gmail.com");
		header.setApplicationType(ApplicationType.ASSOCIATE);
		header.setAddress1("P.o Box 37425 Nrb");
		header.setPostCode("00100");
		header.setCity1("Nairobi");
		header.setEmployer("Workpoint Limited");
		
		helper.createApplication(header);
		
		System.err.println("Invoice >> "+header.getInvoiceRef());
		
	}
}
