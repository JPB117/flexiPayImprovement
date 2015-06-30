package com.workpoint.icpak.client.ui.profile;

import java.util.Arrays;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
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
	HTMLPanel divPasswordContent;

	@UiField
	HTMLPanel divProfileContent;

	@UiField
	PasswordWidget panelPasswordWidget;
	private BasicDetails basicDetail;
	private EducationDetails educationDetail;
	private SpecializationDetails specializationDetail;
	private TrainingDetails trainingDetail;

	public interface Binder extends UiBinder<Widget, ProfileView> {
	}

	@Inject
	public ProfileView(final Binder binder) {
		widget = binder.createAndBindUi(this);

		basicDetail = new BasicDetails();
		educationDetail = new EducationDetails();
		specializationDetail = new SpecializationDetails();
		trainingDetail = new TrainingDetails();

		showChangePassword(false);

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

		/* Set Edit Mode */
		aEdit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				basicDetail.setEditMode(true);
				educationDetail.setEditMode(true);
				specializationDetail.setEditMode(true);
				trainingDetail.setEditMode(true);
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

}
