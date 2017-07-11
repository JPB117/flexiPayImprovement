package com.workpoint.icpak.client.ui.profile.training;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.RowWidget;

public class TrainingTableRow extends RowWidget {

	private static ActivitiesTableRowUiBinder uiBinder = GWT
			.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends
			UiBinder<Widget, TrainingTableRow> {
	}

	@UiField
	HTMLPanel row;
	@UiField
	HTMLPanel divOrganization;
	@UiField
	HTMLPanel divStartDate;
	@UiField
	HTMLPanel divEndDate;
	@UiField
	HTMLPanel divTaskNature;
	@UiField
	HTMLPanel divResponsibilities;
	@UiField
	HTMLPanel divClients;
	@UiField
	HTMLPanel divDatePassed;
	@UiField
	HTMLPanel divType;

	public TrainingTableRow() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
