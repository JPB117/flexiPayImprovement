package com.workpoint.icpak.tests.dao;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.junit.Ignore;
import org.junit.Test;

import com.amazonaws.util.json.JSONObject;
import com.google.inject.Inject;
import com.icpak.rest.dao.ApplicationFormDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.dao.helper.AccountancyDaoHelper;
import com.icpak.rest.dao.helper.ApplicationFormDaoHelper;
import com.icpak.rest.dao.helper.EducationDaoHelper;
import com.icpak.rest.dao.helper.SpecializationDaoHelper;
import com.icpak.rest.dao.helper.TrainingDaoHelper;
import com.icpak.rest.dao.helper.UsersDaoHelper;
import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.membership.ApplicationFormHeader;
import com.icpak.rest.models.membership.MemberImport;
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

	Logger logger = Logger.getLogger(TestApplicationForm.class);

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
	@Inject
	UsersDao usersDao;

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

	// @Test
	public void ImportMissingMembers() {
		List<ApplicationFormHeaderDto> members = helper.importMissingMembers(applicationDao.importMissingMembers());
	}

	// @Test
	public void ImportApprovedMembers() {
		helper.importApprovedMembers();
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

	}

	// @Test
	public void testUpdatingOfCPDDto() {
		ApplicationFormHeaderDto dto = applicationDao.findByApplicationId("nAvpeTnX9OmHxRPV", true).toDto();
		dto.setApplicationStatus(ApplicationStatus.APPROVED);
		// dto.setManagementComment("Please attach your profile photo");
		// System.err.println("Submission Date::" + dto.getDateSubmitted());

		helper.updateApplication("nAvpeTnX9OmHxRPV", dto);
	}

	// @Test
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
	public void checkStubornMemberNo() {
		List<MemberImport> memberImports = applicationDao.importMissingMembers();

		for (MemberImport m : memberImports) {
			List<User> users = usersDao.findUsersByMemberNo(m.getMemberNo());

			if (!users.isEmpty() && users.size() > 1) {
				logger.warn(" DIRTY MEMBER NO " + m.getMemberNo());
			}
		}
	}

	// @Test
	public void testArrayEncode() {
		String[] applicationNos = { "C/101", "C/102", "C/103" };
		JSONArray mJSONArray = new JSONArray(Arrays.asList(applicationNos));
		System.err.println(mJSONArray);
	}

	@Test
	public void testSyncToErp() {
		helper.syncApprovedApplicantsFromErp();
	}
}
