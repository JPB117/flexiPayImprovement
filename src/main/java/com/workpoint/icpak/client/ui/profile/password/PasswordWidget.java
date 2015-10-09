package com.workpoint.icpak.client.ui.profile.password;

import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
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
	ActionLink aResendAct;

	@UiField
	IssuesPanel issues;
	@UiField
	TextField txtEmail;
	@UiField
	PasswordField txtPassword;
	@UiField
	PasswordField txtConfirmPassword;

	@UiField
	Element divPassword;
	@UiField
	Element divConfirmPassword;

	@UiField
	SpanElement spnInfo;

	@UiField
	HTMLPanel panelContainer;

	private boolean doValidation;

	public PasswordWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public HasClickHandlers getSaveButton() {
		return aSave;
	}

	public HasClickHandlers getResendButton() {
		return aResendAct;
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

		if (!doValidation) {
			return true;
		}

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

	public void showProcessing(boolean isProcessing) {
		if (isProcessing) {
			aResendAct.getElement().setAttribute("disabled", "dsiabled");
			aResendAct.setText("Processing");
		} else {
			aResendAct.getElement().removeAttribute("disabled");
			aResendAct.setText("Resend Activation");
		}
	}

	public void setSaveEnabled(boolean enable) {

	}

	public HasClickHandlers getChangePasswordButton() {
		return null;
	}

	public void changeWidget(String reason) {
		if (reason.equals("forgot")) {
			this.doValidation = false;
			spnInfo.setInnerText("Enter the E-mail you used to do registration, and email will be sent with Reset Instructions");
			txtEmail.getElement().removeAttribute("disabled");
			divConfirmPassword.addClassName("hide");
			divPassword.addClassName("hide");
			aResendAct.removeStyleName("hide");
			aSave.addStyleName("hide");
		} else if (reason.equals("activate")) {
			this.doValidation = false;
			spnInfo.setInnerText("Enter the E-mail you used to do registration, and email will be sent with Activation Instructions.");
			txtEmail.getElement().removeAttribute("disabled");
			divConfirmPassword.addClassName("hide");
			divPassword.addClassName("hide");
			aResendAct.removeStyleName("hide");
			aSave.addStyleName("hide");
			aResendAct.setText("Send Activation Email");
		} else {
			this.doValidation = true;
			spnInfo.setInnerText("This page allows you to create your password that you will use to access your account.");
			txtEmail.getElement().setAttribute("disabled", "disabled");
			divConfirmPassword.removeClassName("hide");
			divPassword.removeClassName("hide");
			aResendAct.addStyleName("hide");
			aSave.removeStyleName("hide");
		}
	}

	public String getEmail() {
		return txtEmail.getValue();
	}

}
