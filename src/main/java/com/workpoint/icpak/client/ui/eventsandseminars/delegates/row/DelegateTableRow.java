package com.workpoint.icpak.client.ui.eventsandseminars.delegates.row;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.RowWidget;
import com.workpoint.icpak.client.ui.component.TextField;

public class DelegateTableRow extends RowWidget {

	private static ActivitiesTableRowUiBinder uiBinder = GWT
			.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends
			UiBinder<Widget, DelegateTableRow> {
	}

	@UiField
	HTMLPanel row;

	@UiField
	HTMLPanel divMemberNo;
	@UiField
	HTMLPanel divTitle;
	@UiField
	HTMLPanel divSurName;
	@UiField
	HTMLPanel divOtherNames;
	@UiField
	HTMLPanel divEmail;
	@UiField
	HTMLPanel divAccomodation;
	@UiField
	HTMLPanel divAttendance;
	@UiField
	HTMLPanel divPaymentStatus;

	ActionLink aRemove;

	private Integer rowId;

	public DelegateTableRow() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public DelegateTableRow(String memberNo, String title, String surName,
			String otherNames, String email, Integer rowId) {
		divMemberNo.getElement().setInnerHTML(memberNo);
		divTitle.getElement().setInnerHTML(title);
		divSurName.getElement().setInnerHTML(surName);
		divOtherNames.getElement().setInnerHTML(otherNames);
		divEmail.getElement().setInnerHTML(email);
		this.rowId = rowId;

	}

	public void InsertParameters(TextField memberNo, TextField title,
			TextField surName, TextField otherNames, TextField email,
			Integer rowId) {
		divMemberNo.add(memberNo);
		divTitle.add(title);
		divSurName.add(surName);
		divOtherNames.add(otherNames);
		divEmail.add(email);
		this.rowId = rowId;
	}

	public void showAdvancedDetails(boolean show) {
		if (show) {
			divPaymentStatus.setVisible(true);
			divAttendance.setVisible(true);
		} else {
			divPaymentStatus.setVisible(false);
			divAttendance.setVisible(false);
		}
	}
}
