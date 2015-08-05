package com.workpoint.icpak.client.ui.profile.password;

import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.client.ui.component.PasswordField;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.shared.model.UserDto;

public class PasswordWidget extends Composite {

	private static PasswordWidgetUiBinder uiBinder = GWT
			.create(PasswordWidgetUiBinder.class);

	interface PasswordWidgetUiBinder extends UiBinder<Widget, PasswordWidget> {
	}

	@UiField
	ActionLink aCancel;

	@UiField
	ActionLink aSave;

	@UiField
	IssuesPanel issues;
	@UiField
	TextField txtEmail;
	@UiField
	PasswordField txtPassword;
	@UiField
	PasswordField txtConfirmPassword;

	@UiField
	HTMLPanel panelContainer;

	public PasswordWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public HasClickHandlers getSaveButton() {
		return aSave;
	}

	public HasClickHandlers getCancelButton() {
		return aCancel;
	}

	public void setUser(UserDto user) {
		txtEmail.setText(user.getEmail());
	}

	public boolean isValid() {
		boolean isValid = true;
		issues.clear();

		if (isNullOrEmpty(txtPassword.getValue())) {
			issues.addError("Password is required");
			isValid = false;
		}

		if (isNullOrEmpty(txtConfirmPassword.getValue())) {
			issues.addError("'Confirm Password' is required");
			isValid = false;
		}

		if (!txtPassword.getValue().equals(txtConfirmPassword.getValue())) {
			issues.addError("Password and 'Confirm Password' fields do not match");
			isValid = false;
		}

		return isValid;
	}

	public String getPassword() {

		return txtPassword.getValue();
	}

	public void addError(String error) {
		issues.addError(error);
	}

	public void setCreatePassword(boolean isCreate) {
		if (isCreate) {
			panelContainer.removeStyleName("panel panel-default");
		} else {
			panelContainer.addStyleName("panel panel-default");
		}
	}

	public void setSaveEnabled(boolean enable) {

	}

	public HasClickHandlers getChangePasswordButton() {
		return null;
	}

}
