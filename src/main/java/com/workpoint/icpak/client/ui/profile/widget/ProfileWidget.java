package com.workpoint.icpak.client.ui.profile.widget;

import gwtupload.client.IUploader;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
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
import com.workpoint.icpak.client.model.UploadContext.UPLOADACTION;
import com.workpoint.icpak.client.security.CurrentUser;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.ProgressBar;
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
import com.workpoint.icpak.shared.model.ApplicationFormSpecializationDto;
import com.workpoint.icpak.shared.model.ApplicationFormTrainingDto;
import com.workpoint.icpak.shared.model.Country;

public class ProfileWidget extends Composite {

	private static ProfileWidgetUiBinder uiBinder = GWT
			.create(ProfileWidgetUiBinder.class);

	@UiField
	TabPanel divTabs;

	@UiField
	ActionLink aChangePassword;

	@UiField
	ActionLink aEditPicture;

	@UiField
	ActionLink aPayNow;

	@UiField
	DivElement divPayNow;

	@UiField
	ActionLink aSaveChanges;

	@UiField
	HTMLPanel divProfileContent;

	@UiField
	HTMLPanel panelProfile;
	@UiField
	HTMLPanel PanelProfileDisplay;
	@UiField
	HTMLPanel panelApplicationType;

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
	ProgressBar progressBar;

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

		setEditMode(true);
		setChangeProfilePicture(false);

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

		/* Set Edit Mode */
		aEditPicture.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setChangeProfilePicture(true);
			}
		});

		aSaveChanges.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setChangeProfilePicture(false);
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
				imgUser.setUrl(url + "&version=" + Random.nextInt());
				uploader.clear();
			}
		});
	}

	public void setEditMode(boolean editMode) {
		basicDetail.setEditMode(editMode);
		educationDetail.setEditMode(editMode);
		trainingDetail.setEditMode(editMode);
		specializationDetail.setEditMode(editMode);

		if (editMode) {
			divEditDropDown.setVisible(true);
			divSavePanel.setVisible(true);
			divPayNow.removeClassName("hide");
			// divProfileContent.setVisible(false);
		} else {
			divEditDropDown.setVisible(false);
			divSavePanel.setVisible(false);
			divPayNow.addClassName("hide");
		}
	}

	public void setChangeProfilePicture(boolean change) {
		if (change) {
			PanelProfileDisplay.setVisible(false);
			panelProfile.setVisible(true);
			divSavePanel.setVisible(true);
			divEditDropDown.setVisible(false);
		} else {
			PanelProfileDisplay.setVisible(true);
			panelProfile.setVisible(false);
			divSavePanel.setVisible(false);
			divEditDropDown.setVisible(true);
		}

	}

	public void bindBasicDetails(ApplicationFormHeaderDto result) {
		basicDetail.bindDetails(result);
		// test
		if (result.getRefId() != null) {
			spnNames.setInnerText(result.getSurname() + " "
					+ result.getOtherNames());
			spnApplicationType.setInnerText(result.getApplicationType()
					.getDisplayName());

			panelApplicationType.getElement().setInnerText(
					result.getApplicationType().getDisplayName());

			result.setPercCompletion(50);
			progressBar.setProgress(result.getPercCompletion());
		}
	}

	public void bindCurrentUser(CurrentUser user) {
		String refId = user.getUser().getRefId();
		UploadContext ctx = new UploadContext();
		ctx.setContext("userRefId", refId);
		ctx.setAction(UPLOADACTION.UPLOADUSERIMAGE);
		uploader.setContext(ctx);

		setUserImage(user.getUser().getRefId());
	}

	public void setUserImage(String refId) {
		url = "getreport?userRefId=" + refId + "&action=getuserimage";
		imgUser.setUrl(url);
	}

	public void setApplicationId(String applicationRefId) {
		if (applicationRefId == null) {
			aPayNow.removeStyleName("btn-success");
			aPayNow.addStyleName("btn-warning");
			aPayNow.setText("No Application Found");
		} else {
			aPayNow.addStyleName("btn-success");
			aPayNow.removeStyleName("btn-warning");
			aPayNow.setText("Pay Now");
			aPayNow.setHref("#signup;applicationId=" + applicationRefId);
		}

	}

	public void clear() {
		progressBar.clear();
	}

	public HasClickHandlers getSaveButton() {
		return aSaveChanges;
	}

	public int getActiveTab() {
		return divTabs.getActiveTab();
	}

	// public ApplicationFormHeaderDto getBasicDetails() {
	// return basicDetail.getApplicationForm();
	// }

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

	public void setCountries(List<Country> countries) {
		basicDetail.setCountries(countries);
	}

	public void bindSpecializations(
			List<ApplicationFormSpecializationDto> result) {
		specializationDetail.bindSpecializations(result);
	}

}
