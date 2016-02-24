package com.workpoint.icpak.tests.dao;

import java.io.IOException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.amazonaws.util.json.JSONObject;
import com.google.inject.Inject;
import com.icpak.rest.dao.ApplicationFormDao;
import com.icpak.rest.dao.helper.AccountancyDaoHelper;
import com.icpak.rest.dao.helper.ApplicationFormDaoHelper;
import com.icpak.rest.dao.helper.EducationDaoHelper;
import com.icpak.rest.dao.helper.SpecializationDaoHelper;
import com.icpak.rest.dao.helper.TrainingDaoHelper;
import com.icpak.rest.dao.helper.UsersDaoHelper;
import com.icpak.rest.models.membership.ApplicationFormHeader;
import com.workpoint.icpak.shared.model.ApplicationERPDto;
import com.workpoint.icpak.shared.model.ApplicationFormAccountancyDto;
import com.workpoint.icpak.shared.model.ApplicationFormEducationalDto;
import com.workpoint.icpak.shared.model.ApplicationFormEmploymentDto;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationFormSpecializationDto;
import com.workpoint.icpak.shared.model.ApplicationFormTrainingDto;
import com.workpoint.icpak.shared.model.ApplicationType;
import com.workpoint.icpak.shared.model.UserDto;
import com.workpoint.icpak.shared.model.auth.ApplicationStatus;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestApplicationForm extends AbstractDaoTest {

	@Inject
	ApplicationFormDaoHelper helper;
	@Inject
	EducationDaoHelper eduHelper;
	@Inject
	TrainingDaoHelper trainingHelper;
	@Inject
	AccountancyDaoHelper accountancyHelper;

	@Inject
	ApplicationFormDao applicationDao;

	@Inject
	SpecializationDaoHelper specializationHelper;
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

	@Ignore
	public void Import() {
		List<ApplicationFormHeaderDto> members = helper.importMembers(0, 30000);
	}
	
	@Test
	public void ImportMissingMembers() {
		List<ApplicationFormHeaderDto> members = helper.importMissingMembers();
	}

	@Ignore
	public void getApplications() {
		List<ApplicationFormHeaderDto> members = helper.getAllApplications(0, 10, "", "");
		for (ApplicationFormHeaderDto dto : members) {
			System.out.println("Previous>>" + dto.getPreviousRefId());
			System.out.println("Next>>" + dto.getNextRefId());
		}
	}

	@Ignore
	public void testERPIntergration() {
		String applicationNo = "C/18881";
		ApplicationFormHeaderDto application = helper.getApplicationById("4MgvVkX0Sgr4uwIZ").toDto();
		List<ApplicationFormEducationalDto> educationDetails = eduHelper.getAllEducationEntrys("", "4MgvVkX0Sgr4uwIZ",
				0, 100);
		List<ApplicationFormTrainingDto> trainings = trainingHelper.getAllTrainingEntrys("", "4MgvVkX0Sgr4uwIZ", 0,
				100);
		List<ApplicationFormAccountancyDto> accountancy = accountancyHelper.getAllAccountancyEntrys("",
				"4MgvVkX0Sgr4uwIZ", 0, 100);
		List<ApplicationFormSpecializationDto> specializations = specializationHelper.getAllSpecializationEntrys("",
				"4MgvVkX0Sgr4uwIZ", 0, 100);
		List<ApplicationFormEmploymentDto> employment = specializationHelper.getAllEmploymentEntrys("",
				"4MgvVkX0Sgr4uwIZ", 0, 100);

		application.setApplicationNo(applicationNo);
		for (ApplicationFormEducationalDto education : educationDetails) {
			education.setApplicationNo(applicationNo);
		}
		for (ApplicationFormTrainingDto training : trainings) {
			training.setApplicationNo(applicationNo);
		}
		for (ApplicationFormAccountancyDto acc : accountancy) {
			acc.setApplicationNo(applicationNo);
		}
		for (ApplicationFormSpecializationDto specialization : specializations) {
			specialization.setApplicationNo(applicationNo);
		}
		for (ApplicationFormEmploymentDto emp : employment) {
			emp.setApplicationNo(applicationNo);
		}

		ApplicationERPDto erpDto = new ApplicationERPDto();
		erpDto.setApplication(application);
		erpDto.setEducationDetails(educationDetails);
		erpDto.setTrainings(trainings);
		erpDto.setAccountancy(accountancy);
		erpDto.setSpecializations(specializations);
		erpDto.setEmployment(employment);

		JSONObject payLoad = new JSONObject(erpDto);
		System.err.println("payload to ERP>>>>" + payLoad);

		// try {
		// //helper.postApplicationToERP("C/18881", erpDto);
		// } catch (URISyntaxException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	@Ignore
	public void testUpdatingOfCPDDto() {
		ApplicationFormHeaderDto dto = applicationDao.findByApplicationId("AQSSHoAkdY6VMNLv", true).toDto();
		dto.setErpCode("C/18878595");
		dto.setApplicationStatus(ApplicationStatus.PROCESSING);
		// dto.setManagementComment("Please attach your profile photo");
		System.err.println("Submission Date::" + dto.getDateSubmitted());

		helper.updateApplication("4MgvVkX0Sgr4uwIZ", dto);
	}

	@Ignore
	public void testBulkSMS() {
		applicationDao.sendMessageToHonourables();
	}

	@Ignore
	public void testGettingAppDtos() {
		List<ApplicationFormHeaderDto> applications = helper.getAllApplicationNativeQuery(0, 10, "", "", "");

		System.err.println("Applications>>>>" + applications.size());
		for (ApplicationFormHeaderDto dto : applications) {
			System.out.println(">>>" + dto.getPaymentStatus());
			System.out.println(">>>" + dto.getApplicationStatus());
		}
	}

	@Ignore
	public void testGetSingleApp() {
		ApplicationFormHeader app = helper.getApplicationById("dAbfgoN4TvBo4hB9");
		System.err.println("Found:::" + app.getSurname());

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
