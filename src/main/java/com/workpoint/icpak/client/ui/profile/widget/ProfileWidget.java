package com.workpoint.icpak.client.ui.profile.widget;

import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;
import gwtupload.client.IUploader;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

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
import com.workpoint.icpak.client.ui.component.BulletListPanel;
import com.workpoint.icpak.client.ui.component.BulletPanel;
import com.workpoint.icpak.client.ui.component.ProgressBar;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.ui.component.tabs.TabContent;
import com.workpoint.icpak.client.ui.component.tabs.TabHeader;
import com.workpoint.icpak.client.ui.component.tabs.TabPanel;
import com.workpoint.icpak.client.ui.component.tabs.TabPanel.TabPosition;
import com.workpoint.icpak.client.ui.profile.ProfilePresenter;
import com.workpoint.icpak.client.ui.profile.accountancy.AccountancyDetails;
import com.workpoint.icpak.client.ui.profile.basic.BasicDetails;
import com.workpoint.icpak.client.ui.profile.education.EducationDetails;
import com.workpoint.icpak.client.ui.profile.specialization.SpecializationDetails;
import com.workpoint.icpak.client.ui.profile.training.TrainingDetails;
import com.workpoint.icpak.client.ui.upload.custom.Uploader;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.ApplicationFormAccountancyDto;
import com.workpoint.icpak.shared.model.ApplicationFormEducationalDto;
import com.workpoint.icpak.shared.model.ApplicationFormEmploymentDto;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationFormSpecializationDto;
import com.workpoint.icpak.shared.model.ApplicationFormTrainingDto;
import com.workpoint.icpak.shared.model.Country;
import com.workpoint.icpak.shared.model.MemberStanding;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.auth.ApplicationStatus;

public class ProfileWidget extends Composite {

	private static ProfileWidgetUiBinder uiBinder = GWT
			.create(ProfileWidgetUiBinder.class);

	private static final Logger LOGGER = Logger
			.getLogger(ProfilePresenter.class.getName());

	@UiField
	TabPanel divTabs;
	@UiField
	ActionLink aChangePassword;
	@UiField
	ActionLink aEditPicture;
	@UiField
	ActionLink aSubmit;
	@UiField
	DivElement divSubmitApplication;
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
	ActionLink aChangePhoto;
	@UiField
	Element aBackToApplications;
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
	Element elSpace;
	@UiField
	ProgressBar progressBar;
	@UiField
	Anchor aDownloadCert;
	@UiField
	HTMLPanel panelIssues;
	@UiField
	BulletListPanel ulIssues;
	@UiField
	HTMLPanel panelBreadcrumb;
	@UiField
	ActionLink aPreviousApplication;
	@UiField
	ActionLink aNextApplication;
	@UiField
	ActionLink aCheckStandingStatus;
	@UiField
	DivElement divGoodStandingActions;
	@UiField
	DivElement divGoodStanding;
	@UiField
	DivElement divErpSync;
	@UiField
	TextField txtErpAppNo;
	@UiField
	ActionLink aErpSync;
	@UiField
	DivElement divApplicationStatus;
	@UiField
	DivElement divMemberShipStatus;
	@UiField
	DivElement divPaymentSection;
	@UiField
	SpanElement spnPaymentStatus;
	@UiField
	ActionLink aDownloadProforma;
	@UiField
	Element spnApplicationStatus;
	@UiField
	SpanElement spnMessage;
	@UiField
	SpanElement spnStatusDescription;

	@UiField
	ActionLink aPayLink;

	private BasicDetails basicDetail;
	private EducationDetails educationDetail;
	private SpecializationDetails specializationDetail;
	private TrainingDetails trainingDetail;
	private AccountancyDetails accountancyDetail;
	private String url;

	private boolean isCurrentUserMember;
	private boolean isCurrentUserAdmin;
	private boolean isCurrentUserBasicMember;

	private ApplicationFormHeaderDto applicationForm;

	interface ProfileWidgetUiBinder extends UiBinder<Widget, ProfileWidget> {
	}

	public ProfileWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		basicDetail = new BasicDetails();
		educationDetail = new EducationDetails();
		specializationDetail = new SpecializationDetails();
		trainingDetail = new TrainingDetails();
		accountancyDetail = new AccountancyDetails();
		setEditMode(true);
		setChangeProfilePicture(false);
		// aCheckStandingStatus.setVisible(true);
		// aDownloadCert.setVisible(false);
		// divAccountStatus.setClassName("hide");
		// spnHelpIcon.setClassName("hide");

		divTabs.setHeaders(Arrays.asList(new TabHeader("Basic Information",
				true, "basic_details"), new TabHeader("Education Background",
				false, "education_details"), new TabHeader(
				"Practical Training", false, "training_details"),
				new TabHeader("Accountancy Examinations", false,
						"accountancy_details"),
				new TabHeader("Specialization Areas", false,
						"specialisation_details")));
		divTabs.setPosition(TabPosition.PILLS);
		divTabs.setContent(Arrays
				.asList(new TabContent(basicDetail, "basic_details", true),
						new TabContent(educationDetail, "education_details",
								false), new TabContent(specializationDetail,
								"specialisation_details", false),
						new TabContent(accountancyDetail,
								"accountancy_details", false), new TabContent(
								trainingDetail, "training_details", false)));

		/* Set Edit Mode */
		aEditPicture.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setChangeProfilePicture(true);
			}
		});
		aChangePhoto.addClickHandler(new ClickHandler() {
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
				setChangeProfilePicture(false);
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

	public void initDisplay(ApplicationStatus applicationStatus,
			PaymentStatus paymentStatus) {
		ulIssues.clear();
		isCurrentUserMember = AppContext.isCurrentUserMember();
		isCurrentUserAdmin = AppContext.isCurrentUserAdmin();
		isCurrentUserBasicMember = !isCurrentUserAdmin && !isCurrentUserMember;

		// Window.alert("Is User Admin>>>" + isCurrentUserAdmin);
		// Window.alert("Is User Member>>>" + isCurrentUserMember);

		hideAllDisplays();
		if (isCurrentUserMember) {
			divGoodStanding.removeClassName("hide");
			divGoodStandingActions.removeClassName("hide");
			aCheckStandingStatus.removeStyleName("hide");
			divMemberShipStatus.removeClassName("hide");

		} else if (isCurrentUserBasicMember) {
			divPaymentSection.removeClassName("hide");
			divApplicationStatus.removeClassName("hide");
			divSubmitApplication.removeClassName("hide");
			bindApplicationAndPaymentStatus(applicationStatus, paymentStatus);

			// Application Status - Specific to basic Member
			if (applicationStatus == ApplicationStatus.PENDING) {
				divSubmitApplication.removeClassName("hide");
				aSubmit.setStyleName("btn btn-fill btn-gold");
				aPayLink.setStyleName("btn btn-fill btn-default");
				aDownloadProforma.setStyleName("btn btn-fill btn-default");
				spnStatusDescription
						.setInnerText("(Fill all sections here and click submit to forward your application)");
			} else {
				spnApplicationStatus.addClassName("label label-success");
				spnStatusDescription.removeClassName("hide");
				// Application Status - If Not Paid- Tell User to Pay
				if (paymentStatus != PaymentStatus.PAID) {
					spnStatusDescription
							.setInnerText("(Please pay your application fee to begin processing for your application.)");
					spnStatusDescription.addClassName("text-muted");
				}

				// If Application is processing - User should not be able to
				// Edit
				if (applicationStatus == ApplicationStatus.PROCESSING) {
					setEditMode(false);
				}
				// Action Area
				divSubmitApplication.removeClassName("hide");
				aSubmit.addStyleName("hide");
				aPayLink.setStyleName("btn btn-fill btn-gold");
				aDownloadProforma.setStyleName("btn btn-fill btn-default");
			}
		} else if (isCurrentUserAdmin) {
			divApplicationStatus.removeClassName("hide");
			bindApplicationAndPaymentStatus(applicationStatus, paymentStatus);
			aSubmit.addStyleName("hide");
			aPayLink.addStyleName("hide");
			divApplicationStatus.removeClassName("hide");
			divPaymentSection.removeClassName("hide");
			divErpSync.removeClassName("hide");
			elSpace.removeClassName("hide");
			aBackToApplications.removeClassName("hide");
			panelBreadcrumb.removeStyleName("hide");
		}
	}

	private void bindApplicationAndPaymentStatus(
			ApplicationStatus applicationStatus, PaymentStatus paymentStatus) {
		// Payment Status
		if (paymentStatus == PaymentStatus.PAID) {
			spnPaymentStatus.setInnerText(PaymentStatus.PAID.name());
			spnPaymentStatus.addClassName("label label-successs");
		} else {
			spnPaymentStatus.setInnerText(PaymentStatus.NOTPAID.name());
			spnPaymentStatus.addClassName("label label-danger");

			if (applicationForm.getRefId() != null) {
				aPayLink.removeStyleName("hide");
				aPayLink.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						aPayLink.setHref("#signup;applicationId="
								+ applicationForm.getRefId());
					}
				});
			}

			if (applicationForm.getInvoiceRef() != null) {
				aDownloadProforma.removeStyleName("hide");
				aDownloadProforma.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						UploadContext ctx = new UploadContext("getreport");
						ctx.setContext("invoiceRefId",
								applicationForm.getInvoiceRef());
						ctx.setAction(UPLOADACTION.GETPROFORMA);
						// ctx.setContext(key, value)
						Window.open(ctx.toUrl(), "Get Proforma", null);
					}
				});
			}
		}

		// Application status
		spnApplicationStatus.setInnerText(applicationStatus.name()
				.toUpperCase());
		// Application Status - Specific to basic Member
		if (applicationStatus == ApplicationStatus.PENDING) {
			spnApplicationStatus.addClassName("label label-default");
		} else if (applicationStatus == ApplicationStatus.SUBMITTED) {
			spnApplicationStatus.addClassName("label label-warning");
		} else if (applicationStatus == ApplicationStatus.PROCESSING) {
			spnApplicationStatus.addClassName("label label-info");
		} else {
			spnApplicationStatus.addClassName("label label-success");
		}

	}

	private void hideAllDisplays() {
		panelBreadcrumb.addStyleName("hide");
		divSubmitApplication.addClassName("hide");
		divGoodStanding.addClassName("hide");
		divStandingStatus.addClassName("hide");
		divGoodStandingActions.addClassName("hide");
		aCheckStandingStatus.addStyleName("hide");
		aDownloadCert.addStyleName("hide");
		divErpSync.addClassName("hide");
		panelIssues.addStyleName("hide");
		divApplicationStatus.addClassName("hide");
		divPaymentSection.addClassName("hide");
		aDownloadProforma.addStyleName("hide");
		aPayLink.addStyleName("hide");
		divMemberShipStatus.addClassName("hide");
		spnStatusDescription.addClassName("hide");
	}

	public void bindPaymentStatus() {

	}

	public void setEditMode(boolean editMode) {
		basicDetail.setEditMode(editMode);
		educationDetail.setEditMode(editMode);
		trainingDetail.setEditMode(editMode);
		specializationDetail.setEditMode(editMode);
		panelBreadcrumb.getElement().scrollIntoView();
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
		this.applicationForm = result;
		basicDetail.bindDetails(result);
		if (result.getRefId() != null) {
			String fullName = AppContext.getCurrentUser().getUser()
					.getFullName();
			if (fullName != null && !fullName.isEmpty()) {
				spnNames.setInnerText(AppContext.getCurrentUser().getUser()
						.getFullName());
			} else {
				if (result.getSurname() != null
						&& result.getOtherNames() != null) {
					spnNames.setInnerText(result.getSurname() + " "
							+ result.getOtherNames());
				}
			}
			if (result.getApplicationType() != null) {
				spnApplicationType.setInnerText(result.getApplicationType()
						.getDisplayName());
			}
			result.setPercCompletion(50);
			if (result.getPercCompletion() != null) {
				progressBar.setProgress(result.getPercCompletion());
			}
		}

	}

	public void bindCurrentUser(CurrentUser user) {
		String refId = user.getUser().getRefId();
		UploadContext ctx = new UploadContext();
		ctx.setContext("userRefId", refId);
		ctx.setAction(UPLOADACTION.UPLOADUSERIMAGE);
		uploader.setContext(ctx);

		spnMembershipStatus.addClassName("hide");
		if (user.getUser().getStatus() != null) {
			// spnMembershipStatus.setInnerText(user.getUser().get.name());
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

	public void bindAccountancyDetails(
			List<ApplicationFormAccountancyDto> result) {
		accountancyDetail.bindDetails(result);
	}

	public void bindMemberStanding(MemberStanding standing) {
		if (standing.getMembershipStatus() != null) {
			spnMembershipStatus.setInnerText(standing.getMembershipStatus()
					.getDisplayName());
		} else {
			return;
		}

		showGoodStandingPanel(true);

		if (standing.getStanding() == 0) {
			String info = "<ul>";
			for (String reason : standing.getReasons()) {
				info = info.concat("<li>");
				info = info.concat(reason + "</li>");
			}
			info = info.concat("</ul>");

			spnAccountStatus.setInnerText("Not in Goodstanding");
			divAccountStatus.addClassName("label label-danger");
			divAccountStatus.removeClassName("label label-success");
			spnHelpIcon.setAttribute("data-content", info);
			spnHelpIcon.removeClassName("hide");
			iconFail.removeClassName("hide");
			iconSuccess.addClassName("hide");
			aDownloadCert.addStyleName("hide");
		} else {
			spnAccountStatus.setInnerText("In good standing");
			divAccountStatus.addClassName("label label-success");
			divAccountStatus.removeClassName("label-danger");
			iconFail.addClassName("hide");
			iconSuccess.removeClassName("hide");
			spnHelpIcon.removeClassName("hide");
			aDownloadCert.removeStyleName("hide");
			spnHelpIcon
					.setAttribute(
							"data-content",
							"Your account is in Good-Standing, You can proceed to download "
									+ "your good-standing certificate for your own use.");

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
		// if (show) {
		// // spnMembershipNo.getParentElement().addClassName("hide");
		// spnMembershipStatus.setInnerText("Not Confirmed");
		// divSubmit.removeClassName("hide");
		// aCheckStandingStatus.addStyleName("hide");
		// divStandingStatus.addClassName("hide");
		// } else {
		// // spnMembershipNo.getParentElement().removeClassName("hide");
		// // spnRefreshSection.removeClassName("hide");
		// // divStandingStatus.removeClassName("hide");
		// divSubmit.addClassName("hide");
		// aCheckStandingStatus.removeStyleName("hide");
		// divStandingStatus.removeClassName("hide");
		// }
	}

	public HasClickHandlers getAccountancyAddButton() {
		return accountancyDetail.getAddButton();
	}

	public HasClickHandlers getSubmitButton() {
		return aSubmit;
	}

	public boolean validateAllIssues() {
		ulIssues.clear();
		boolean isBasicDetailOK = basicDetail.getBasicDetailIssues().size() == 0 ? true
				: false;
		boolean isEducationDetailOk = educationDetail
				.getEducationDetailIssues().size() == 0 ? true : false;
		boolean isTrainingDetailOk = trainingDetail.getTrainingDetailIssues()
				.size() == 0 ? true : false;
		boolean isExaminationDetailOk = accountancyDetail
				.getExaminationDetailIssues().size() == 0 ? true : false;
		boolean isSpecializationOk = specializationDetail
				.getSpecializationDetailIssues().size() == 0 ? true : false;

		// Window.alert("Is Basic Details Ok:" + isBasicDetailOK
		// + "Education Detail OK:" + isEducationDetailOk
		// + "Training Detail OK:" + isTrainingDetailOk
		// + "Examination Detail OK:" + isExaminationDetailOk);

		if (isBasicDetailOK && isEducationDetailOk && isTrainingDetailOk
				&& isExaminationDetailOk && isSpecializationOk) {
			panelIssues.addStyleName("hide");
			return true;
		} else {
			for (String issue : basicDetail.getBasicDetailIssues()) {
				BulletPanel listItem = new BulletPanel();
				listItem.setText(issue);
				ulIssues.add(listItem);
			}
			for (String issue : educationDetail.getEducationDetailIssues()) {
				BulletPanel listItem = new BulletPanel();
				listItem.setText(issue);
				ulIssues.add(listItem);
			}
			for (String issue : trainingDetail.getTrainingDetailIssues()) {
				BulletPanel listItem = new BulletPanel();
				listItem.setText(issue);
				ulIssues.add(listItem);
			}
			for (String issue : accountancyDetail.getExaminationDetailIssues()) {
				BulletPanel listItem = new BulletPanel();
				listItem.setText(issue);
				ulIssues.add(listItem);
			}

			for (String issue : specializationDetail
					.getSpecializationDetailIssues()) {
				BulletPanel listItem = new BulletPanel();
				listItem.setText(issue);
				ulIssues.add(listItem);
			}
			panelIssues.removeStyleName("hide");
			return false;
		}
	}

	public void setLastUpdatedToNow() {
		// spnLastUpdated.setInnerText(DateUtils.CREATEDFORMAT.format(new
		// Date()));
	}

	public void setNavigationLinks(String previousRefId, String nextRefId,
			int maxSize) {
		if ((Integer.parseInt(nextRefId) + 1) < maxSize) {
			aNextApplication.removeStyleName("hide");
			aNextApplication.setHref("#members;counter=" + nextRefId);
		} else {
			aNextApplication.addStyleName("hide");
		}

		if (Integer.parseInt(previousRefId) > 0) {
			aPreviousApplication.removeStyleName("hide");
			aPreviousApplication.setHref("#members;counter=" + previousRefId);
		} else {
			aPreviousApplication.addStyleName("hide");
		}

	}

	public ActionLink getAcheckStandingStatus() {
		return aCheckStandingStatus;
	}

	public void showGoodStandingPanel(boolean show) {
		if (show) {
			divStandingStatus.removeClassName("hide");
			aDownloadCert.removeStyleName("hide");
			aCheckStandingStatus.addStyleName("hide");
		} else {
			divStandingStatus.addClassName("hide");
			aDownloadCert.addStyleName("hide");
			aCheckStandingStatus.removeStyleName("hide");
		}
	}

	public void showApplicationIssues(boolean show) {
		ulIssues.clear();
		if (show) {
			panelIssues.removeStyleName("hide");
		} else {
			panelIssues.addStyleName("hide");
		}
	}

	public void bindEmployment(List<ApplicationFormEmploymentDto> result) {
		specializationDetail.bindEmployment(result);
	}

	public HasClickHandlers getErpSync() {
		return aErpSync;
	}

	public String getErpApplicationNumber() {
		if (!isNullOrEmpty(txtErpAppNo.getText())) {
			return txtErpAppNo.getText();
		} else {
			Window.alert("Please Enter an Application Number");
			return null;
		}
	}
}
