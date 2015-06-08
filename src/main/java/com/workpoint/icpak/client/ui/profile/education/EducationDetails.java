package com.workpoint.icpak.client.ui.profile.education;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.TableHeader;
import com.workpoint.icpak.client.ui.component.TableView;

public class EducationDetails extends Composite {

	private static EducationDetailsUiBinder uiBinder = GWT
			.create(EducationDetailsUiBinder.class);

	@UiField
	TableView tblEducationalDetail;

	List<TableHeader> tblHeaders = new ArrayList<TableHeader>();

	interface EducationDetailsUiBinder extends
			UiBinder<Widget, EducationDetails> {
	}

	public EducationDetails() {
		initWidget(uiBinder.createAndBindUi(this));

		createTableHeader();

		createRows();
	}

	private void createTableHeader() {
		tblHeaders.add(new TableHeader("Institution"));
		tblHeaders.add(new TableHeader("From"));
		tblHeaders.add(new TableHeader("To"));
		tblHeaders.add(new TableHeader("Exam Body"));
		tblHeaders.add(new TableHeader("Class/Division"));
		tblHeaders.add(new TableHeader("Awarded"));
		tblHeaders.add(new TableHeader("Reg No."));
		tblHeaders.add(new TableHeader("Section Passed"));
		tblHeaders.add(new TableHeader("Action"));

		tblEducationalDetail.setTableHeaders(tblHeaders);
	}

	private void createRows() {
		for (int i = 0; i < 10; i++) {
			EducationTableRow row = new EducationTableRow();
			tblEducationalDetail.addRow(row);
		}

	}
}
