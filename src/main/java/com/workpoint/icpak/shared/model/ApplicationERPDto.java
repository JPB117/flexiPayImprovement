package com.workpoint.icpak.shared.model;

import java.util.List;

public class ApplicationERPDto {

	ApplicationFormHeaderDto application;
	List<ApplicationFormEducationalDto> educationDetails;
	List<ApplicationFormTrainingDto> trainings;
	List<ApplicationFormAccountancyDto> accountancy;
	List<ApplicationFormSpecializationDto> specializations;
	List<ApplicationFormEmploymentDto> employment;

	public ApplicationFormHeaderDto getApplication() {
		return application;
	}

	public void setApplication(ApplicationFormHeaderDto application) {
		this.application = application;
	}

	public List<ApplicationFormEducationalDto> getEducationDetails() {
		return educationDetails;
	}

	public void setEducationDetails(
			List<ApplicationFormEducationalDto> educationDetails) {
		this.educationDetails = educationDetails;
	}

	public List<ApplicationFormTrainingDto> getTrainings() {
		return trainings;
	}

	public void setTrainings(List<ApplicationFormTrainingDto> trainings) {
		this.trainings = trainings;
	}

	public List<ApplicationFormAccountancyDto> getAccountancy() {
		return accountancy;
	}

	public void setAccountancy(List<ApplicationFormAccountancyDto> accountancy) {
		this.accountancy = accountancy;
	}

	public List<ApplicationFormSpecializationDto> getSpecializations() {
		return specializations;
	}

	public void setSpecializations(
			List<ApplicationFormSpecializationDto> specializations) {
		this.specializations = specializations;
	}

	public List<ApplicationFormEmploymentDto> getEmployment() {
		return employment;
	}

	public void setEmployment(List<ApplicationFormEmploymentDto> employment) {
		this.employment = employment;
	}

}
