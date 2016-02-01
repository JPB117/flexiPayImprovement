package com.workpoint.icpak.client.ui.profile.password;

import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
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
	ActionLink aSendActivation;

	@UiField
	ActionLink aProceedToLogin;

	@UiField
	ActionLink aContinue;

	@UiField
	HTMLPanel panelMessage;
	@UiField
	SpanElement spnMessageIcon;
	@UiField
	SpanElement spnIssues;

	@UiField
	Element divActionButtons;
	@UiField
	Element divInstructionItems;

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
		txtEmail.setValue("");
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

		if (isValid) {
			issues.removeStyleName("hide");
		} else {
			issues.addStyleName("hide");
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

	public HasClickHandlers getSendActivationLink() {
		return aSendActivation;
	}

	public void showProcessing(boolean isProcessing) {
		if (isProcessing) {
			aResendAct.getElement().setAttribute("disabled", "dsiabled");
			aResendAct.setText("Processing");
			aSendActivation.getElement().setAttribute("disabled", "dsiabled");
			aSendActivation.setText("Processing");
		} else {
			aResendAct.getElement().removeAttribute("disabled");
			aResendAct.setText("Resend Activation");
			aSendActivation.getElement().removeAttribute("disabled");
			aSendActivation.setText("Send Activation");
		}
	}

	public void setSaveEnabled(boolean enable) {

	}

	public HasClickHandlers getChangePasswordButton() {
		return null;
	}

	public void changeWidget(String reason) {
		showSuccess(false);
		if (reason.equals("forgot")) {
			this.doValidation = false;
			spnInfo.setInnerText("Enter the E-mail you used to do registration, and an email will be sent with Reset Instructions");
			txtEmail.getElement().removeAttribute("disabled");
			divConfirmPassword.addClassName("hide");
			divPassword.addClassName("hide");
			aResendAct.removeStyleName("hide");
			aSave.addStyleName("hide");
			aSendActivation.addStyleName("hide");
		} else if (reason.equals("activate")) {
			this.doValidation = false;
			spnInfo.setInnerText("Enter the E-mail you used to do registration, and an email will be sent with Activation Instructions.");
			txtEmail.getElement().removeAttribute("disabled");
			divConfirmPassword.addClassName("hide");
			divPassword.addClassName("hide");
			aResendAct.addStyleName("hide");
			aSave.addStyleName("hide");
			aSendActivation.removeStyleName("hide");
		} else {
			this.doValidation = true;
			spnInfo.setInnerText("This page allows you to create your password that you will use to access your account.");
			txtEmail.getElement().setAttribute("disabled", "disabled");
			divConfirmPassword.removeClassName("hide");
			divPassword.removeClassName("hide");
			aResendAct.addStyleName("hide");
			aSave.removeStyleName("hide");
			aSendActivation.addStyleName("hide");
		}
	}

	public String getEmail() {
		return txtEmail.getValue();
	}

	public HasKeyDownHandlers getEmailTextField() {
		return txtEmail;
	}

	public HasKeyDownHandlers getPasswordTextField() {
		return txtConfirmPassword;
	}

	public void showMessage(String errorMessage, String errorType) {
		if (errorType.equals("success")) {
			spnIssues.setInnerText(errorMessage);
			panelMessage.removeStyleName("hide");
			panelMessage.setStyleDependentName("alert-", true);
			panelMessage.addStyleDependentName(errorType);
			showSuccess(true);
			aProceedToLogin.setVisible(false);
		} else {
			issues.clear();
			issues.removeStyleName("hide");
			issues.addError(errorMessage);
		}
	}

	public HasClickHandlers getProceedToLogin() {
		showSuccess(true);
		return aProceedToLogin;
	}

	private void showSuccess(boolean show) {
		if (show) {
			aResendAct.addStyleName("hide");
			aSave.addStyleName("hide");
			aSendActivation.addStyleName("hide");
			aCancel.addStyleName("hide");
			divInstructionItems.addClassName("hide");
			issues.addStyleName("hide");
			panelMessage.removeStyleName("hide");
			aProceedToLogin.setVisible(true);
		} else {
			aProceedToLogin.setVisible(false);
			aResendAct.removeStyleName("hide");
			aSave.removeStyleName("hide");
			aSendActivation.removeStyleName("hide");
			aCancel.removeStyleName("hide");
			divInstructionItems.removeClassName("hide");
			panelMessage.addStyleName("hide");
		}
	}

	public void showContinueButton(boolean show) {
		aContinue.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.back();
			}
		});
		if (show) {
			aContinue.removeStyleName("hide");
		} else {
			aContinue.addStyleName("hide");
		}
	}
}
