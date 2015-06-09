package com.workpoint.icpak.client.ui.profile.education;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.RowWidget;

public class EducationTableRow extends RowWidget {

	private static EducationTableRowUiBinder uiBinder = GWT
			.create(EducationTableRowUiBinder.class);

	interface EducationTableRowUiBinder extends
			UiBinder<Widget, EducationTableRow> {
	}

	@UiField
	HTMLPanel row;
	@UiField
	HTMLPanel divInstitution;
	@UiField
	HTMLPanel divFrom;
	@UiField
	HTMLPanel divTo;
	@UiField
	HTMLPanel divExamBody;
	@UiField
	HTMLPanel divDivision;
	@UiField
	HTMLPanel divAwarded;
	@UiField
	HTMLPanel divRegNo;
	@UiField
	HTMLPanel divSectionPassed;

	public EducationTableRow() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public EducationTableRow(String institution, String from, String to,
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
