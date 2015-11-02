package com.workpoint.icpak.client.ui.users.item;

import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.shared.model.UserDto;

public class UserItemView extends ViewImpl implements UserItemPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, UserItemView> {
	}

	@UiField
	HTMLPanel panelFirstName;
	@UiField
	HTMLPanel panelLastName;
	@UiField
	HTMLPanel panelEmail;
	@UiField
	HTMLPanel panelGroups;
	@UiField
	HTMLPanel panelMemberNo;
	@UiField
	SpanElement spnLMSStatus;

	@UiField
	Anchor aEdit;
	@UiField
	Anchor aDelete;

	@Inject
	public UserItemView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public void setValues(UserDto user) {
		if (user.getMemberNo() != null) {
			panelMemberNo.getElement().setInnerText(user.getMemberNo());
		}

		if (user.getSurname() != null) {
			panelFirstName.getElement().setInnerText(user.getSurname());
		}

		if (user.getName() != null) {
			panelLastName.getElement().setInnerText(user.getName());
		}

		if (user.getEmail() != null) {
			panelEmail.getElement().setInnerText(user.getEmail());
		}

		if (user.getGroups() != null) {
			panelGroups.getElement().setInnerText(user.getGroupsAsString());
		}

		if (user.getLmsStatus() != null) {
			spnLMSStatus.setInnerText(user.getLmsStatus());
			if (user.getLmsStatus().equals("Success")) {
				spnLMSStatus.setClassName("label label-success popover-icon");
			} else {
				spnLMSStatus.setClassName("label label-danger popover-icon");
			}
			if (user.getLmsResponse() != null) {
				spnLMSStatus
						.setAttribute("data-content", user.getLmsResponse());
			}
		}

	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public HasClickHandlers getEdit() {
		return aEdit;
	}

	public HasClickHandlers getDelete() {
		return aDelete;
	}

}