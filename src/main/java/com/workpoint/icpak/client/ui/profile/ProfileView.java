package com.workpoint.icpak.client.ui.profile;

import java.util.Arrays;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.component.tabs.TabContent;
import com.workpoint.icpak.client.ui.component.tabs.TabHeader;
import com.workpoint.icpak.client.ui.component.tabs.TabPanel;
import com.workpoint.icpak.client.ui.profile.basic.BasicDetails;
import com.workpoint.icpak.client.ui.profile.education.EducationDetails;
import com.workpoint.icpak.client.ui.profile.specialization.SpecializationDetails;
import com.workpoint.icpak.client.ui.profile.training.TrainingDetails;

public class ProfileView extends ViewImpl implements
		ProfilePresenter.IProfileView {

	private final Widget widget;
	@UiField
	HTMLPanel container;
	@UiField
	TabPanel divTabs;

	public interface Binder extends UiBinder<Widget, ProfileView> {
	}

	@Inject
	public ProfileView(final Binder binder) {
		widget = binder.createAndBindUi(this);

		BasicDetails basicDetail = new BasicDetails();
		EducationDetails educationDetail = new EducationDetails();
		SpecializationDetails specializationDetail = new SpecializationDetails();
		TrainingDetails trainingDetail = new TrainingDetails();

		divTabs.setHeaders(Arrays.asList(new TabHeader("Basic Information",
				true, "basic_details"), new TabHeader("Education Information",
				false, "education_details"), new TabHeader("Trainings", false,
				"training_details"), new TabHeader("Specialization", false,
				"specialisation_details")));

		divTabs.setContent(Arrays.asList(new TabContent(basicDetail,
				"basic_details", true), new TabContent(educationDetail,
				"education_details", false), new TabContent(specializationDetail,
				"specialisation_details", false), new TabContent(trainingDetail,
				"training_details", false)));

	}

	@Override
	public Widget asWidget() {
		return widget;
	}

}
