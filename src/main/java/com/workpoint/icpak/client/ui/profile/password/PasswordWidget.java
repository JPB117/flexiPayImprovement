package com.workpoint.icpak.client.ui.profile.password;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.ActionLink;

public class PasswordWidget extends Composite {

	private static PasswordWidgetUiBinder uiBinder = GWT
			.create(PasswordWidgetUiBinder.class);

	interface PasswordWidgetUiBinder extends UiBinder<Widget, PasswordWidget> {
	}

	@UiField
	ActionLink aCancel;

	@UiField
	ActionLink aSave;

	public PasswordWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public PasswordWidget(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public HasClickHandlers getSaveButton() {
		return aSave;
	}

	public HasClickHandlers getCancelButton() {
		return aCancel;
	}

}
