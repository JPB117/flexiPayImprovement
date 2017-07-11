package com.workpoint.icpak.client.ui.profile.specialization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.RowWidget;

public class SpecializationTableRow extends RowWidget {

	private static ActivitiesTableRowUiBinder uiBinder = GWT
			.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends
			UiBinder<Widget, SpecializationTableRow> {
	}

	@UiField
	HTMLPanel row;
	@UiField
	HTMLPanel divSpecialization;
	@UiField
	HTMLPanel divUpdated;

	public SpecializationTableRow() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public SpecializationTableRow(String institution, String from, String to,
			String examBody, String division, String awarded, String regNo,
			String sectionPassed) {
/*		
		divInstitution.getElement().setInnerHTML(transaction.getCustomerName());
		divFrom.getElement().setInnerHTML(transaction.getCustomerName());
		divTo.getElement().setInnerHTML(transaction.getCustomerName());
		divExamBody.getElement().setInnerHTML(transaction.getCustomerName());
		divDivision.getElement().setInnerHTML(transaction.getCustomerName());
		divAwarded.getElement().setInnerHTML(transaction.getCustomerName());
		divRegNo.getElement().setInnerHTML(transaction.getCustomerName());
		divRegNo.getElement().setInnerHTML(transaction.getCustomerName());*/
		
	}

}
