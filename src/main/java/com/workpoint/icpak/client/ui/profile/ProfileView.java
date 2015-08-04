package com.workpoint.icpak.client.ui.profile;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.security.CurrentUser;
import com.workpoint.icpak.client.ui.profile.password.PasswordWidget;
import com.workpoint.icpak.client.ui.profile.widget.ProfileWidget;
import com.workpoint.icpak.shared.model.ApplicationFormEducationalDto;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationFormTrainingDto;

public class ProfileView extends ViewImpl implements
		ProfilePresenter.IProfileView {

	private final Widget widget;
	@UiField
	HTMLPanel container;

	@UiField
	PasswordWidget panelPasswordWidget;

	@UiField
	ProfileWidget divProfileContent;

	@UiField
	HTMLPanel divPasswordContent;

	public interface Binder extends UiBinder<Widget, ProfileView> {
	}

	@Inject
	public ProfileView(final Binder binder) {
		widget = binder.createAndBindUi(this);

		ClickHandler hidePasswordPanel = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showChangePassword(false);
			}
		};

		panelPasswordWidget.getSaveButton().addClickHandler(hidePasswordPanel);
		panelPasswordWidget.getCancelButton()
				.addClickHandler(hidePasswordPanel);

		showChangePassword(false);

		/* Change Password */
		divProfileContent.getChangePasswordButton().addClickHandler(
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						showChangePassword(true);
					}
				});

	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public void showChangePassword(boolean show) {
		if (show) {
			divProfileContent.setVisible(false);
			divPasswordContent.setVisible(true);
		} else {
			divProfileContent.setVisible(true);
			divPasswordContent.setVisible(false);
		}
	}

	@Override
	public void bindBasicDetails(ApplicationFormHeaderDto result) {
		divProfileContent.bindBasicDetails(result);
	}

	@Override
	public void bindCurrentUser(CurrentUser user) {
		divProfileContent.bindCurrentUser(user);
	}

	@Override
	public HasClickHandlers getSaveButton() {
		return divProfileContent.getSaveButton();
	}

	@Override
	public HasClickHandlers getCancelDetailButton() {
		return divProfileContent.getCancelDetailButton();
	}

	@Override
	public HasClickHandlers getSaveBasicDetailsButton() {
		return divProfileContent.getSaveBasicDetailsButton();
	}

	@Override
	public int getActiveTab() {
		return divProfileContent.getActiveTab();
	}

	@Override
	public ApplicationFormHeaderDto getBasicDetails() {
		return divProfileContent.getBasicDetails();
	}

	@Override
	public boolean isValid() {
		return divProfileContent.isValid();
	}

	@Override
	public void setEditMode(boolean editMode) {
		divProfileContent.setEditMode(editMode);
	}

	@Override
	public void bindEducationDetails(List<ApplicationFormEducationalDto> result) {
		divProfileContent.bindEducationDetails(result);
	}

	@Override
	public HasClickHandlers getEducationDetailSaveButton() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasClickHandlers getProfileEditButton() {
		return divProfileContent.getProfileEditButton();
	}

	@Override
	public HasClickHandlers getEducationAddButton() {
		return divProfileContent.getEducationAddButton();
	}

	@Override
	public HasClickHandlers getTrainingAddButton() {
		return divProfileContent.getTrainingAddButton();
	}

	@Override
	public HasClickHandlers getSpecializationAddButton() {
		return divProfileContent.getSpecializationAddButton();
	}

	@Override
	public void bindTrainingDetails(
			List<ApplicationFormTrainingDto> trainingDetails) {
		divProfileContent.bindTrainingDetails(trainingDetails);
	}

}
