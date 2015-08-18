package com.workpoint.icpak.tests.dao;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.ApplicationFormDaoHelper;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationType;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestApplicationForm extends AbstractDaoTest {

	@Inject
	ApplicationFormDaoHelper helper;

	@Ignore
	public void create() {
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

		System.err.println("Invoice >> " + header.getInvoiceRef());

	}

	@Test
	public void Import() {
		List<ApplicationFormHeaderDto> members = helper.importMembers(0, 20000);

		int counter = 0;
		for (ApplicationFormHeaderDto dto : members) {
			if (counter < 10) {
				helper.createApplication(dto);
				counter++;
			}
		}

		// System.err.println("Size of Members:" + members.size());

	}

}
