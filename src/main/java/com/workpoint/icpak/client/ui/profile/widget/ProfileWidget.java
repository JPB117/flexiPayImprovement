package com.workpoint.icpak.client.ui.profile.widget;

import gwtupload.client.IUploader;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.model.UploadContext;
import com.workpoint.icpak.client.security.CurrentUser;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.tabs.TabContent;
import com.workpoint.icpak.client.ui.component.tabs.TabHeader;
import com.workpoint.icpak.client.ui.component.tabs.TabPanel;
import com.workpoint.icpak.client.ui.component.tabs.TabPanel.TabPosition;
import com.workpoint.icpak.client.ui.profile.basic.BasicDetails;
import com.workpoint.icpak.client.ui.profile.education.EducationDetails;
import com.workpoint.icpak.client.ui.profile.specialization.SpecializationDetails;
import com.workpoint.icpak.client.ui.profile.training.TrainingDetails;
import com.workpoint.icpak.client.ui.upload.custom.Uploader;
import com.workpoint.icpak.shared.model.ApplicationFormEducationalDto;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationFormTrainingDto;

public class ProfileWidget extends Composite {

	private static ProfileWidgetUiBinder uiBinder = GWT
			.create(ProfileWidgetUiBinder.class);

	@UiField
	TabPanel divTabs;

	@UiField
	ActionLink aChangePassword;

	@UiField
	ActionLink aEdit;

	@UiField
	ActionLink aPayNow;

	@UiField
	ActionLink aSaveChanges;

	@UiField
	HTMLPanel divProfileContent;

	@UiField
	HTMLPanel panelProfile;
	@UiField
	HTMLPanel PanelProfileDisplay;

	@UiField
	HTMLPanel divEditDropDown;

	@UiField
	HTMLPanel divSavePanel;

	@UiField
	Uploader uploader;

	@UiField
	FocusPanel panelPicture;

	@UiField
	Image imgUser;

	@UiField
	Element spnNames;
	@UiField
	Element spnApplicationType;
	@UiField
	Element spnCompletion;

	private BasicDetails basicDetail;
	private EducationDetails educationDetail;
	private SpecializationDetails specializationDetail;
	private TrainingDetails trainingDetail;
	private String url;

	interface ProfileWidgetUiBinder extends UiBinder<Widget, ProfileWidget> {
	}

	public ProfileWidget() {
		initWidget(uiBinder.createAndBindUi(this));

		// Forms
		basicDetail = new BasicDetails();
		educationDetail = new EducationDetails();
		specializationDetail = new SpecializationDetails();
		trainingDetail = new TrainingDetails();

		// Uploader
		setEditMode(false);

		divTabs.setHeaders(Arrays.asList(new TabHeader("Basic Information",
				true, "basic_details"), new TabHeader(
				"Education Background(From O-Levels)", false,
				"education_details"), new TabHeader("Practical Training",
				false, "training_details"), new TabHeader(
				"Specialization Detail", false, "specialisation_details")));

		divTabs.setPosition(TabPosition.PILLS);

		divTabs.setContent(Arrays.asList(new TabContent(basicDetail,
				"basic_details", true), new TabContent(educationDetail,
				"education_details", false), new TabContent(
				specializationDetail, "specialisation_details", false),
				new TabContent(trainingDetail, "training_details", false)));

		imgUser.setUrl("img/james.jpg");

		/* Set Edit Mode */
		aEdit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setEditMode(true);
			}
		});

		imgUser.addErrorHandler(new ErrorHandler() {
			@Override
			public void onError(ErrorEvent event) {
				imgUser.setUrl("img/blueman.png");
			}
		});

		uploader.addOnFinishUploaderHandler(new IUploader.OnFinishUploaderHandler() {
			@Override
			public void onFinish(IUploader uploaderRef) {
				imgUser.setUrl(url + "?version=" + Random.nextInt());
			}
		});
	}

	public void setEditMode(boolean editMode) {
		if (editMode) {
			PanelProfileDisplay.setVisible(false);
			panelProfile.setVisible(true);
			divEditDropDown.setVisible(false);
			divSavePanel.setVisible(true);
		} else {
			PanelProfileDisplay.setVisible(true);
			panelProfile.setVisible(false);
			divEditDropDown.setVisible(true);
			divSavePanel.setVisible(false);
		}
	}

	public void bindBasicDetails(ApplicationFormHeaderDto result) {
		basicDetail.bindDetails(result);
		spnNames.setInnerText(result.getSurname() + " "
				+ result.getOtherNames());
		spnApplicationType.setInnerText(result.getApplicationType()
				.getDisplayName());
		spnCompletion.setInnerText("80% Complete");
	}

	public void bindCurrentUser(CurrentUser user) {
		String refId = user.getUser().getRefId();
		uploader.setContext(new UploadContext("api/users/" + refId + "/profile"));

		url = "api/users/" + refId + "/profile";
		imgUser.setUrl(url);
	}

	public HasClickHandlers getSaveButton() {
		return aSaveChanges;
	}

	public int getActiveTab() {
		return divTabs.getActiveTab();
	}

	public ApplicationFormHeaderDto getBasicDetails() {
		return basicDetail.getApplicationForm();
	}

	public HasClickHandlers getSaveBasicDetailsButton() {
		return basicDetail.getSaveButton();
	}

	public HasClickHandlers getCancelDetailButton() {
		return basicDetail.getCancelButton();
	}

	public boolean isValid() {
		if (getActiveTab() == 0) {
			// BasicDetails
			return basicDetail.isValid();
		}
		return false;
	}

	public void bindEducationDetails(List<ApplicationFormEducationalDto> result) {
		educationDetail.bindDetails(result);
	}

	public void bindTrainingDetails(List<ApplicationFormTrainingDto> result) {
		trainingDetail.bindDetails(result);
	}

	public HasClickHandlers getEducationDetailSaveButton() {
		return educationDetail.getSaveButton();
	}

	public HasClickHandlers getProfileEditButton() {
		return basicDetail.getEditButton();
	}

	public HasClickHandlers getEducationAddButton() {
		return educationDetail.getAddButton();
	}

	public HasClickHandlers getTrainingAddButton() {
		return trainingDetail.getAddButton();
	}

	public HasClickHandlers getSpecializationAddButton() {
		return specializationDetail.getAddButton();
	}

	public HasClickHandlers getChangePasswordButton() {
		return aChangePassword;
	}

}
