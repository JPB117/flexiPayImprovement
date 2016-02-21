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
import com.workpoint.icpak.shared.model.ApplicationFormAccountancyDto;
import com.workpoint.icpak.shared.model.ApplicationFormEducationalDto;
import com.workpoint.icpak.shared.model.ApplicationFormEmploymentDto;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationFormSpecializationDto;
import com.workpoint.icpak.shared.model.ApplicationFormTrainingDto;
import com.workpoint.icpak.shared.model.Country;
import com.workpoint.icpak.shared.model.MemberStanding;
import com.workpoint.icpak.shared.model.auth.ApplicationStatus;

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
	private CurrentUser currentUser;

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
						panelPasswordWidget.changeWidget("default");
						panelPasswordWidget.setUser(currentUser.getUser());

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
		this.currentUser = user;
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

	// @Override
	// public ApplicationFormHeaderDto getBasicDetails() {
	// return divProfileContent.getBasicDetails();
	// }

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
	public void setApplicationId(String applicationRefId) {
		divProfileContent.setApplicationId(applicationRefId);
	}

	@Override
	public void clear() {
		divProfileContent.clear();
	}

	public void bindTrainingDetails(
			List<ApplicationFormTrainingDto> trainingDetails) {
		divProfileContent.bindTrainingDetails(trainingDetails);
	}

	@Override
	public void setCountries(List<Country> countries) {
		// divProfileContent.setCountries(countries);
	}

	@Override
	public void bindSpecializations(
			List<ApplicationFormSpecializationDto> result) {
		divProfileContent.bindSpecializations(result);
	}

	@Override
	public void bindEmployment(List<ApplicationFormEmploymentDto> result) {
		divProfileContent.bindEmployment(result);
	}

	@Override
	public void bindMemberStanding(MemberStanding standing) {
		divProfileContent.bindMemberStanding(standing);
	}

	@Override
	public HasClickHandlers getErpRefreshButton() {
		return divProfileContent.getRefreshButton();
	}

	@Override
	public void setApplicationStaus(ApplicationStatus applicationStatus) {
		divProfileContent.setApplicationStatus(applicationStatus);
	}

	@Override
	public void showBasicMember(boolean show) {
		divProfileContent.showBasicMember(show);
	}

	@Override
	public void bindAccountancyDetails(
			List<ApplicationFormAccountancyDto> result) {
		divProfileContent.bindAccountancyDetails(result);
	}

	@Override
	public HasClickHandlers getAccountancyAddButton() {
		return divProfileContent.getAccountancyAddButton();
	}

	@Override
	public HasClickHandlers getSubmitButton() {
		return divProfileContent.getSubmitButton();
	}

	@Override
	public boolean validateBasicDetailIssues() {
		return divProfileContent.validateAllIssues();
	}

	@Override
	public void setLastUpdateToNow() {
		divProfileContent.setLastUpdatedToNow();
	}

	@Override
	public HasClickHandlers getCertStatusButton() {
		return divProfileContent.getAcheckStandingStatus();
	}

	@Override
	public void updateStatement() {
	}

	@Override
	public void hideGoodStanding() {
		divProfileContent.showGoodStandingPanel(false);
	}

	public void showApplicationIssues(boolean show) {
		divProfileContent.showApplicationIssues(show);
	}

	public ProfileWidget getProfileWidget() {
		return divProfileContent;
	}

}
