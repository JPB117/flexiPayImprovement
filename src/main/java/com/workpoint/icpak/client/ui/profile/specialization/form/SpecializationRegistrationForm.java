package com.workpoint.icpak.client.ui.profile.specialization.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.shared.model.ApplicationFormEducationalDto;
import com.workpoint.icpak.shared.model.ApplicationFormSpecializationDto;

public class SpecializationRegistrationForm extends Composite {

	private static EducationRegistrationFormUiBinder uiBinder = GWT
			.create(EducationRegistrationFormUiBinder.class);

	@UiField
	IssuesPanel issues;

	@UiField
	DivElement divHeader;

	// List<ApplicationFormSpecializationDto>

	private ApplicationFormSpecializationDto specializationDto;

	interface EducationRegistrationFormUiBinder extends
			UiBinder<Widget, SpecializationRegistrationForm> {
	}

	public SpecializationRegistrationForm() {
		initWidget(uiBinder.createAndBindUi(this));
		
		setEditMode(false);
	}

	public SpecializationRegistrationForm(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public boolean isValid() {
		boolean isValid = true;
		issues.clear();
		return isValid;
	}

	public void setEditMode(boolean editMode) {
		if (editMode) {
			divHeader.removeClassName("hide");
		} else {
			divHeader.addClassName("hide");
		}
	}

	public void clear() {
	}

	public void bindDetail(ApplicationFormSpecializationDto specializationDto) {
		this.specializationDto = specializationDto;
	}

}
