package com.workpoint.icpak.client.ui.profile.training.form;

import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.DateField;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.shared.model.ApplicationFormEducationalDto;
import com.workpoint.icpak.shared.model.ApplicationFormTrainingDto;

public class TrainingRegistrationForm extends Composite {

	private static EducationRegistrationFormUiBinder uiBinder = GWT
			.create(EducationRegistrationFormUiBinder.class);

	@UiField
	TextField txtOrganization;
	@UiField
	TextField txtPosition;
	@UiField
	TextField txtTaskNature;
	@UiField
	DateField dtStartDate;
	@UiField
	DateField dtDateCompleted;
	@UiField
	TextArea txtResponsibility;
	@UiField
	TextField txtClients;
	@UiField
	DateField dtDatePassed;

	@UiField
	IssuesPanel issues;

	private ApplicationFormTrainingDto trainingDto;

	interface EducationRegistrationFormUiBinder extends
			UiBinder<Widget, TrainingRegistrationForm> {
	}

	public TrainingRegistrationForm() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public TrainingRegistrationForm(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public boolean isValid() {
		boolean isValid = true;
		issues.clear();

		if (isNullOrEmpty(txtOrganization.getValue())) {
			isValid = false;
			issues.addError("Organization is mandatory");
		}

		if (isNullOrEmpty(txtPosition.getValue())) {
			isValid = false;
			issues.addError("Position is mandatory");
		}
		if (dtStartDate.getValueDate() == null) {
			isValid = false;
			issues.addError("Start date is mandatory");
		}
		if (dtDateCompleted.getValueDate() == null) {
			isValid = false;
			issues.addError("Date Completed is mandatory");
		}

		if (isNullOrEmpty(txtTaskNature.getValue())) {
			isValid = false;
			issues.addError("Task Nature is mandatory");
		}

		if (isNullOrEmpty(txtResponsibility.getValue())) {
			isValid = false;
			issues.addError("Responsibility is mandatory");
		}
		if (isNullOrEmpty(txtClients.getValue())) {
			isValid = false;
			issues.addError("Clients is mandatory");
		}
		if (isNullOrEmpty(dtDatePassed.getValue())) {
			isValid = false;
			issues.addError("Date Passed is mandatory");
		}

		return isValid;
	}

	public void clear() {
		txtOrganization.setValue("");
		txtPosition.setValue("");
		dtStartDate.setValue("");
		dtDateCompleted.setValue("");
		txtTaskNature.setValue("");
		txtResponsibility.setValue("");
		txtClients.setValue("");
		dtDatePassed.setValue("");
	}

	public ApplicationFormTrainingDto getTrainingDto() {
		ApplicationFormTrainingDto dto = new ApplicationFormTrainingDto();
		if (trainingDto != null) {
			dto = trainingDto;
		}
		dto.setPosition(txtPosition.getText());
		dto.setOrganisationName(txtOrganization.getText());
		dto.setFromDate(dtStartDate.getValueDate());
		dto.setToDate(dtDateCompleted.getValueDate());
		dto.setTaskNature(txtTaskNature.getText());
		dto.setResponsibility(txtResponsibility.getText());
		dto.setClients(txtClients.getText());
		dto.setDatePassed(dtDatePassed.getValueDate());
		return dto;
	}

	public void bindDetail(ApplicationFormTrainingDto trainingDto) {
		this.trainingDto = trainingDto;
		txtPosition.setValue(trainingDto.getPosition());
		dtStartDate.setValue(trainingDto.getFromDate());
		dtDateCompleted.setValue(trainingDto.getToDate());
		txtTaskNature.setValue(trainingDto.getTaskNature());
		txtResponsibility.setValue(trainingDto.getResponsibility());
		txtClients.setValue(trainingDto.getClients());
		dtDatePassed.setValue(trainingDto.getDatePassed());
	}

}
