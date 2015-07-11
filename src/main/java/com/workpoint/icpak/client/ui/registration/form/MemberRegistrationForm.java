package com.workpoint.icpak.client.ui.registration.form;

import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationType;

public class MemberRegistrationForm extends Composite {

	@UiField
	IssuesPanel issuesPanel;
	@UiField
	TextField txtSurname;
	@UiField
	TextField txtOtherNames;
	@UiField
	TextField txtEmailAddress;
	@UiField
	TextField txtPhone;
	@UiField
	TextField txtEmployer;
	@UiField
	TextField txtCity;
	@UiField
	TextField txtAddress;
	@UiField
	TextField txtPostalCode;

	private boolean isEmailValid = true;

	private static MemberRegistrationFormUiBinder uiBinder = GWT
			.create(MemberRegistrationFormUiBinder.class);
	private int counter;
	private ApplicationType type;

	interface MemberRegistrationFormUiBinder extends
			UiBinder<Widget, MemberRegistrationForm> {
	}

	public MemberRegistrationForm() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public boolean isValid() {
		boolean isValid = true;
		issuesPanel.clear();

		if (counter == 0) {
			if (!isEmailValid) {
				issuesPanel.addError("e-Mail " + txtEmailAddress.getValue()
						+ " is already registered");
			}
			if (isNullOrEmpty(txtSurname.getValue())) {
				isValid = false;
				issuesPanel.addError("Surname is required");
			}
			if (isNullOrEmpty(txtOtherNames.getValue())) {
				isValid = false;
				issuesPanel.addError("Other Names is required");
			}
			if (isNullOrEmpty(txtEmailAddress.getValue())) {
				isValid = false;
				issuesPanel.addError("Email is required");
			}

			if (isNullOrEmpty(txtPhone.getValue())) {
				isValid = false;
				issuesPanel.addError("Phone Number is required");
			}

			if (isNullOrEmpty(txtEmployer.getValue())) {
				isValid = false;
				issuesPanel.addError("Employer is required");
			}

			if (isNullOrEmpty(txtCity.getValue())) {
				isValid = false;
				issuesPanel.addError("City is required");
			}

			if (isNullOrEmpty(txtAddress.getValue())) {
				isValid = false;
				issuesPanel.addError("Address is required");
			}
		} else if (counter == 1) {
			if (type == null) {
				Window.alert("Kindly select your category..");
				isValid = false;
			}
		}

		if (!isValid && isEmailValid) {
			issuesPanel.removeStyleName("hide");
		} else {
			issuesPanel.addStyleName("hide");
		}

		return isValid && isEmailValid;
	}

	public TextField getEmail() {
		return txtEmailAddress;
	}

	public ApplicationFormHeaderDto getApplicationForm() {
		ApplicationFormHeaderDto dto = new ApplicationFormHeaderDto();
		dto.setSurname(txtSurname.getValue());
		dto.setOtherNames(txtOtherNames.getValue());
		dto.setEmail(txtEmailAddress.getValue());
		dto.setEmployer(txtEmployer.getValue());
		dto.setCity1(txtCity.getValue());
		dto.setAddress1(txtAddress.getValue());
		dto.setTelephone1(txtPhone.getValue());
		dto.setPostCode(txtPostalCode.getValue());
		dto.setApplicationType(type);
		return dto;
	}

	public void setEmailValid(boolean isEmailValid) {
		issuesPanel.clear();
		this.isEmailValid = isEmailValid;
		if (!isEmailValid) {
			issuesPanel.addError("e-Mail " + txtEmailAddress.getValue()
					+ " is already registered");
			issuesPanel.removeStyleName("hide");
		}
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public void setType(ApplicationType type) {
		this.type = type;
	}

	public void bind(ApplicationFormHeaderDto application) {
		txtSurname.setValue(application.getSurname());
		txtOtherNames.setValue(application.getOtherNames());
		txtEmailAddress.setValue(application.getEmail());
		txtEmployer.setValue(application.getEmployer());
		txtCity.setValue(application.getCity1());
		txtPhone.setValue(application.getTelephone1());
		txtAddress.setValue(application.getAddress1());
		txtPostalCode.setValue(application.getPostCode());
		type = application.getApplicationType();
	}

}
