package com.workpoint.icpak.client.ui.profile.specialization.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.shared.model.ApplicationFormEducationalDto;

public class SpecializationRegistrationForm extends Composite {

	private static EducationRegistrationFormUiBinder uiBinder = GWT
			.create(EducationRegistrationFormUiBinder.class);

	@UiField
	IssuesPanel issues;

	private ApplicationFormEducationalDto educationDto;

	interface EducationRegistrationFormUiBinder extends
			UiBinder<Widget, SpecializationRegistrationForm> {
	}

	public SpecializationRegistrationForm() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public SpecializationRegistrationForm(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public boolean isValid() {
		boolean isValid = true;
		issues.clear();

		return isValid;
	}

	public void clear() {
	}

	public ApplicationFormEducationalDto getEducationDto() {

		ApplicationFormEducationalDto dto = new ApplicationFormEducationalDto();
		if (educationDto != null) {
			dto = educationDto;
		}
		return dto;
	}

	public void bindDetail(ApplicationFormEducationalDto educationDto) {
		this.educationDto = educationDto;
	}

}
