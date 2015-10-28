package com.workpoint.icpak.client.ui.login.createpassword;

import com.google.gwt.event.dom.client.HasClickHandlers;
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
	}

}
