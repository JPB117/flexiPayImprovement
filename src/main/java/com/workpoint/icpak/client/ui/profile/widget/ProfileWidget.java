package com.workpoint.icpak.client.ui.profile.widget;

import gwtupload.client.IUploader;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
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
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.client.ui.util.NumberUtils;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.ApplicationFormEducationalDto;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationFormSpecializationDto;
import com.workpoint.icpak.shared.model.ApplicationFormTrainingDto;
import com.workpoint.icpak.shared.model.Country;
import com.workpoint.icpak.shared.model.MemberStanding;
import com.workpoint.icpak.shared.model.auth.ApplicationStatus;

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
	ActionLink aSubmit;

	@UiField
	DivElement divSubmit;

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
	SpanElement spnLastUpdated;
	@UiField
	ActionLink aRefresh;

	@UiField
	Uploader uploader;

	@UiField
	FocusPanel panelPicture;

	@UiField
	Image imgUser;

	@UiField
	Element spnNames;
	@UiField
	Element iconSuccess;
	@UiField
	Element iconFail;

	@UiField
	SpanElement spnMembershipNo;
	@UiField
	SpanElement spnMembershipStatus;

	@UiField
	SpanElement spnApplicationType;

	@UiField
	SpanElement spnAccountStatus;
	@UiField
	DivElement divAccountStatus;
	@UiField
	DivElement divMembershipNo;
	@UiField
	DivElement divStandingStatus;

	@UiField
	SpanElement spnRefreshSection;
	@UiField
	Element spnHelpIcon;

	@UiField
	ProgressBar progressBar;

	@UiField
	Anchor aDownloadCert;

	private BasicDetails basicDetail;
	private EducationDetails educationDetail;
	private SpecializationDetails specializationDetail;
	private TrainingDetails trainingDetail;
	private String url;

	interface ProfileWidgetUiBinder extends UiBinder<Widget, ProfileWidget> {
	}

	public ProfileWidget() {
		initWidget(uiBinder.createAndBindUi(this));

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

		aDownloadCert.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				UploadContext ctx = new UploadContext("getreport");
				ctx.setAction(UPLOADACTION.DownloadCertGoodStanding);
				ctx.setContext("memberRefId", AppContext.getContextUser()
						.getMemberRefId());
				Window.open(ctx.toUrl(), "Certificate Of Good Standing", "");
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
		} else {
			divEditDropDown.setVisible(false);
			divSavePanel.setVisible(false);
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

			String str = result.getApplicationType().getDisplayName();
			String[] vals = str.split(" ");
			String value = "";
			if (vals.length == 2) {
				value = vals[0];
			} else if (vals.length > 2) {
				value = vals[0] + vals[1];
			}
			panelApplicationType.getElement().setInnerText(value);

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

		spnMembershipNo.setInnerText(user.getUser().getMemberNo());
		spnMembershipStatus.setInnerText(user.getUser().getMemberNo());

		if (user.getUser().getLastDateUpdateFromErp() != null) {
			spnLastUpdated.setInnerText(DateUtils.CREATEDFORMAT.format(user
					.getUser().getLastDateUpdateFromErp()));

		}

		setUserImage(user.getUser().getRefId());
	}

	public void setUserImage(String refId) {
		url = "getreport?userRefId=" + refId + "&action=getuserimage";
		imgUser.setUrl(url);
	}

	public void setApplicationId(String applicationRefId) {
		// if (applicationRefId == null) {
		// aPayNow.removeStyleName("btn-success");
		// aPayNow.addStyleName("btn-warning");
		// aPayNow.setText("No Application Found");
		// } else {
		// aPayNow.removeStyleName("btn-warning");
		// aPayNow.setText("Pay Now");
		// aPayNow.setHref("#signup;applicationId=" + applicationRefId);
		// }
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

	public HasClickHandlers getSaveBasicDetailsButton() {
		return basicDetail.getSaveButton();
	}

	public HasClickHandlers getCancelDetailButton() {
		return basicDetail.getCancelButton();
	}

	public boolean isValid() {
		if (getActiveTab() == 0) {
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

	public void bindMemberStanding(MemberStanding standing) {

		if (standing.getMembershipStatus() != null) {
			spnMembershipStatus.setInnerText(standing.getMembershipStatus()
					.getDisplayName());
		}

		if (standing.getStanding() == 0) {
			String info = "<ul>";
			for (String reason : standing.getReasons()) {
				info = info.concat("<li>");
				info = info.concat(reason + "</li>");
			}
			info = info.concat("</ul>");

			spnAccountStatus.setInnerText("Not In GoodStanding");
			divAccountStatus.addClassName("label-danger");
			divAccountStatus.removeClassName("label-success");
			spnHelpIcon.setAttribute("data-content", info);
			spnHelpIcon.removeClassName("hide");
			iconFail.removeClassName("hide");
			iconSuccess.addClassName("hide");
			aDownloadCert.setVisible(false);

		} else {
			spnAccountStatus.setInnerText("In good standing");
			divAccountStatus.addClassName("label-success");
			divAccountStatus.removeClassName("label-danger");
			iconFail.addClassName("hide");
			iconSuccess.removeClassName("hide");
			spnHelpIcon
					.setAttribute(
							"data-content",
							"Your account is in Good-Standing, You can download proceed to download the certificate for your own use.");
			aDownloadCert.setVisible(true);
		}
	}

	public HasClickHandlers getRefreshButton() {
		return aRefresh;
	}

	/*
	 * TODO: Delete this method
	 */
	public void setApplicationStatus(ApplicationStatus applicationStatus) {
	}

	public void showBasicMember(boolean show) {
		if (show) {
			spnMembershipNo.getParentElement().addClassName("hide");
			spnMembershipStatus.setInnerText("Pending");
			spnRefreshSection.addClassName("hide");
			divStandingStatus.addClassName("hide");
			divSubmit.removeClassName("hide");
		} else {
			spnMembershipNo.getParentElement().removeClassName("hide");
			spnRefreshSection.removeClassName("hide");
			divStandingStatus.removeClassName("hide");
			divSubmit.addClassName("hide");
		}
	}
}
