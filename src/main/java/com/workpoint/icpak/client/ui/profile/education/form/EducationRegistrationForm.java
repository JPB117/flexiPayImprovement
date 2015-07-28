package com.workpoint.icpak.client.ui.profile.education.form;

import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.DateField;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.shared.model.ApplicationFormEducationalDto;

public class EducationRegistrationForm extends Composite {

	private static EducationRegistrationFormUiBinder uiBinder = GWT
			.create(EducationRegistrationFormUiBinder.class);

	@UiField
	TextField txtInstitution;
	@UiField
	TextField txtExaminingBody;
	@UiField
	DateField dtStartDate;
	@UiField
	DateField dtDateCompleted;
	@UiField
	TextField txtSectionsPassed;
	@UiField
	TextField txtRegistrationNo;
	@UiField
	TextField txtClassOrDivision;
	@UiField
	TextField txtAward;
	@UiField
	TextField txtEduType;

	@UiField
	IssuesPanel issues;

	private ApplicationFormEducationalDto educationDto;

	interface EducationRegistrationFormUiBinder extends
			UiBinder<Widget, EducationRegistrationForm> {
	}

	public EducationRegistrationForm() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public EducationRegistrationForm(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public boolean isValid() {
		boolean isValid = true;
		issues.clear();

		if (isNullOrEmpty(txtInstitution.getValue())) {
			isValid = false;
			issues.addError("Institution is mandatory");
		}

		if (isNullOrEmpty(txtExaminingBody.getValue())) {
			isValid = false;
			issues.addError("Examining Body is mandatory");
		}

		if (dtStartDate.getValueDate() == null) {
			isValid = false;
			issues.addError("Start date is mandatory");
		}
		if (dtDateCompleted.getValueDate() == null) {
			isValid = false;
			issues.addError("Date Completed is mandatory");
		}

		if (isNullOrEmpty(txtSectionsPassed.getValue())) {
			isValid = false;
			issues.addError("Sections Passed is mandatory");
		}

		if (isNullOrEmpty(txtRegistrationNo.getValue())) {
			isValid = false;
			issues.addError("Registration No is mandatory");
		}
		if (isNullOrEmpty(txtClassOrDivision.getValue())) {
			isValid = false;
			issues.addError("Class/Division is mandatory");
		}
		if (isNullOrEmpty(txtAward.getValue())) {
			isValid = false;
			issues.addError("Award is mandatory");
		}

		return isValid;
	}

	public void clear() {
		txtInstitution.setValue("");
		txtExaminingBody.setValue("");
		dtStartDate.setValue("");
		dtDateCompleted.setValue("");
		txtSectionsPassed.setValue("");
		txtRegistrationNo.setValue("");
		txtClassOrDivision.setValue("");
	}

	public ApplicationFormEducationalDto getEducationDto() {

		ApplicationFormEducationalDto dto = new ApplicationFormEducationalDto();
		if (educationDto != null) {
			dto = educationDto;
		}
		dto.setWhereObtained(txtInstitution.getValue());
		dto.setExaminingBody(txtExaminingBody.getValue());
		dto.setFromDate(dtStartDate.getValueDate());
		dto.setToDate(dtDateCompleted.getValueDate());
		dto.setSections(txtSectionsPassed.getValue());
		dto.setRegNo(txtRegistrationNo.getValue());
		dto.setClassDivisionAttained(txtClassOrDivision.getValue());
		// dto.setCertificateAwarded(txtAward.getValue());
		// dto.setType(txtEduType.getValue());

		return dto;
	}

	public void bindDetail(ApplicationFormEducationalDto educationDto) {
		this.educationDto = educationDto;

		txtInstitution.setValue(educationDto.getWhereObtained());
		txtExaminingBody.setValue(educationDto.getExaminingBody());
		dtStartDate.setValue(educationDto.getFromDate());
		dtDateCompleted.setValue(educationDto.getToDate());
		txtSectionsPassed.setValue(educationDto.getSections());
		txtRegistrationNo.setValue(educationDto.getRegNo());
		txtClassOrDivision.setValue(educationDto.getClassDivisionAttained());
		// txtAward.setValue(educationDto.getCertificateAwarded());
		// txtEduType.setValue(educationDto.getType().name());
	}

}
