package com.workpoint.icpak.tests.dao;

import java.io.IOException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.ApplicationFormDaoHelper;
import com.icpak.rest.dao.helper.UsersDaoHelper;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationType;
import com.workpoint.icpak.shared.model.UserDto;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestApplicationForm extends AbstractDaoTest {

	@Inject
	ApplicationFormDaoHelper helper;
	@Inject
	UsersDaoHelper usersDaoHelper;

	@Ignore
	public void create() {
		ApplicationFormHeaderDto header = new ApplicationFormHeaderDto();
		header.setSurname("Macharia");
		header.setOtherNames("Duggan");
		header.setEmail("mdkimani@gmail.com");
		header.setApplicationType(ApplicationType.ASSOCIATE);
		header.setAddress1("p.o Box 37425 Nrb");
		header.setPostCode("00100");
		header.setCity1("Nairobi");
		header.setEmployer("Workpoint Limited");
		helper.createApplication(header);
		System.err.println("Invoice >> " + header.getInvoiceRef());
	}

	@Test
	public void Import() {
		List<ApplicationFormHeaderDto> members = helper.importMembers(0, 30000);
		for (ApplicationFormHeaderDto dto : members) {
			// System.err.println("Member No>>>" + dto.getMemberNo());
			helper.createApplicationFromImport(dto);
		}
	}

	@Ignore
	public void getApplications() {
		List<ApplicationFormHeaderDto> members = helper.getAllApplications(0,
				100, "");
		for (ApplicationFormHeaderDto dto : members) {
			System.out.println("Application:" + dto.getUserRefId());
		}
	}

	@Ignore
	public void getUser() {
		List<UserDto> users = usersDaoHelper.getAllUsers(0, 20, "");
		for (UserDto user : users) {
			System.err.println("Names:" + user.getMemberNo());
		}
	}

	@Ignore
	public void getCount() throws IOException {
		
	}
}
