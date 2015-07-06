package com.workpoint.icpak.client.ui.profile;

import gwtupload.client.IUploader;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.model.UploadContext;
import com.workpoint.icpak.client.security.CurrentUser;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.tabs.TabContent;
import com.workpoint.icpak.client.ui.component.tabs.TabHeader;
import com.workpoint.icpak.client.ui.component.tabs.TabPanel;
import com.workpoint.icpak.client.ui.component.tabs.TabPanel.TabPosition;
import com.workpoint.icpak.client.ui.profile.basic.BasicDetails;
import com.workpoint.icpak.client.ui.profile.education.EducationDetails;
import com.workpoint.icpak.client.ui.profile.password.PasswordWidget;
import com.workpoint.icpak.client.ui.profile.specialization.SpecializationDetails;
import com.workpoint.icpak.client.ui.profile.training.TrainingDetails;
import com.workpoint.icpak.client.ui.upload.custom.Uploader;
import com.workpoint.icpak.shared.model.ApplicationFormEducationalDto;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;

public class ProfileView extends ViewImpl implements
		ProfilePresenter.IProfileView {

	private final Widget widget;
	@UiField
	HTMLPanel container;
	@UiField
	TabPanel divTabs;

	@UiField
	ActionLink aChangePassword;

	@UiField
	ActionLink aEdit;
	
	@UiField
	ActionLink aSaveChanges;

	@UiField
	HTMLPanel divPasswordContent;

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
	
	@UiField Element spnNames;
	@UiField Element spnApplicationType;
	@UiField Element spnCompletion;

	@UiField
	PasswordWidget panelPasswordWidget;
	private BasicDetails basicDetail;
	private EducationDetails educationDetail;
	private SpecializationDetails specializationDetail;
	private TrainingDetails trainingDetail;
	private String url;

	public interface Binder extends UiBinder<Widget, ProfileView> {
	}

	@Inject
	public ProfileView(final Binder binder) {
		widget = binder.createAndBindUi(this);

		//Forms
		basicDetail = new BasicDetails();
		educationDetail = new EducationDetails();
		specializationDetail = new SpecializationDetails();
		trainingDetail = new TrainingDetails();
		
		//Uploader
		showChangePassword(false);
		setEditMode(false);

		divTabs.setHeaders(Arrays.asList(new TabHeader("Basic Information",
				true, "basic_details"), new TabHeader("Education Information",
				false, "education_details"), new TabHeader("Trainings", false,
				"training_details"), new TabHeader("Specialization", false,
				"specialisation_details")));

		divTabs.setPosition(TabPosition.PILLS);

		divTabs.setContent(Arrays.asList(new TabContent(basicDetail,
				"basic_details", true), new TabContent(educationDetail,
				"education_details", false), new TabContent(
				specializationDetail, "specialisation_details", false),
				new TabContent(trainingDetail, "training_details", false)));

		/* Change Password */
		aChangePassword.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showChangePassword(true);
			}
		});

		ClickHandler hidePasswordPanel = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showChangePassword(false);
			}
		};

		panelPasswordWidget.getSaveButton().addClickHandler(hidePasswordPanel);
		panelPasswordWidget.getCancelButton()
				.addClickHandler(hidePasswordPanel);

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

		basicDetail.setEditMode(editMode);
		educationDetail.setEditMode(editMode);
		specializationDetail.setEditMode(editMode);
		trainingDetail.setEditMode(editMode);
		
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
		basicDetail.bindDetails(result);
		spnNames.setInnerText(result.getSurname()+" "+result.getOtherNames());
		spnApplicationType.setInnerText(result.getApplicationType().getDisplayName());
		spnCompletion.setInnerText("80% Complete");
		
	}

	public void bindCurrentUser(CurrentUser user){
		String refId = user.getUser().getRefId();
		uploader.setContext(new UploadContext("api/users/"+refId+"/profile"));
		
		url = "api/users/"+refId+"/profile";
		imgUser.setUrl(url);
	}

	public HasClickHandlers getSaveButton(){
		return aSaveChanges;
	}
	
	public int getActiveTab(){
		return divTabs.getActiveTab();
	}

	@Override
	public ApplicationFormHeaderDto getBasicDetails() {
		return basicDetail.getApplicationForm();
	}
	
	public HasClickHandlers getSaveBasicDetailsButton(){
		return basicDetail.getSaveButton();
	}
	
	public HasClickHandlers getCancelDetailButton(){
		return basicDetail.getCancelButton();
	}
	
	@Override
	public boolean isValid() {

		if(getActiveTab()==0){
			//BasicDetails
			return basicDetail.isValid();
		}else if(getActiveTab()==1){
			return educationDetail.isValid();
		}
		
		return false;
	}

	@Override
	public ApplicationFormEducationalDto getEducationDetails() {
		
		return educationDetail.getEducationDto();
	}

	@Override
	public void bindEducationDetails(List<ApplicationFormEducationalDto> result) {
		educationDetail.bindDetails(result);
	}
	
	public HasClickHandlers getEducationDetailSaveButton(){
		return educationDetail.getSaveButton();
	}
}
