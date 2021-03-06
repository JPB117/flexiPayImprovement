package com.workpoint.icpak.client.ui.login.createpassword;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.profile.password.PasswordWidget;
import com.workpoint.icpak.shared.model.UserDto;

public class ActivateAccountView extends ViewImpl implements
		ActivateAccountPresenter.IActivateAccountView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, ActivateAccountView> {
	}

	@UiField
	PasswordWidget panelPasswordWidget;

	@Inject
	public ActivateAccountView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		panelPasswordWidget.setCreatePassword(true);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void bindUser(UserDto user) {
		panelPasswordWidget.setUser(user);
	}

	@Override
	public boolean isValid() {
		return panelPasswordWidget.isValid();
	}

	@Override
	public String getPassword() {
		return panelPasswordWidget.getPassword();
	}

	@Override
	public HasClickHandlers getSubmit() {
		return panelPasswordWidget.getSaveButton();
	}

	@Override
	public void setError(String error) {
		panelPasswordWidget.addError(error);
	}

	@Override
	public void setLoginButtonEnabled(boolean enable) {
		panelPasswordWidget.setSaveEnabled(enable);
	}

	@Override
	public void changeWidget(String reason) {
		panelPasswordWidget.changeWidget(reason);
	}

	@Override
	public HasClickHandlers getResendButton() {
		return panelPasswordWidget.getResendButton();
	}

	public HasClickHandlers getSendActivationLink() {
		return panelPasswordWidget.getSendActivationLink();
	}

	@Override
	public String getEmail() {
		return panelPasswordWidget.getEmail();
	}

	@Override
	public void showProcessing(boolean showProcessing) {
		panelPasswordWidget.showProcessing(showProcessing);
		showmask(showProcessing);
	}

	@Override
	public void addError(String message) {
		panelPasswordWidget.addError(message);
	}

	@Override
	public void showMessage(String errorMessage, String errorType) {
		panelPasswordWidget.showMessage(errorMessage, errorType);
	}

	@Override
	public HasClickHandlers getProceedToLogin() {
		return panelPasswordWidget.getProceedToLogin();
	}

	public HasKeyDownHandlers getEmailTextField() {
		return panelPasswordWidget.getEmailTextField();
	}

	public HasKeyDownHandlers getPasswordTextField() {
		return panelPasswordWidget.getPasswordTextField();
	}

	@Override
	public void showmask(boolean processing) {
		if (processing) {
			panelPasswordWidget.addStyleName("whirl traditional");
		} else {
			panelPasswordWidget.removeStyleName("whirl traditional");
		}
	}

	public void showContinueButton(boolean show) {
		panelPasswordWidget.showContinueButton(show);
	}
}
