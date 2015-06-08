package com.workpoint.icpak.client.ui.profile.specialization;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.TableHeader;
import com.workpoint.icpak.client.ui.component.TableView;
import com.workpoint.icpak.client.ui.profile.education.EducationTableRow;

public class SpecializationDetails extends Composite {

	private static SpecialisationDetailsUiBinder uiBinder = GWT
			.create(SpecialisationDetailsUiBinder.class);

	List<TableHeader> tblHeaders = new ArrayList<TableHeader>();

	@UiField
	TableView tblSpecialization;

	interface SpecialisationDetailsUiBinder extends
			UiBinder<Widget, SpecializationDetails> {
	}

	public SpecializationDetails() {
		initWidget(uiBinder.createAndBindUi(this));
		createHeader();
		createRows();
	}

	private void createHeader() {
		tblHeaders.add(new TableHeader("Specialization"));
		tblHeaders.add(new TableHeader("Date Added"));
		tblHeaders.add(new TableHeader("Action"));

		tblSpecialization.setTableHeaders(tblHeaders);
	}

	private void createRows() {
		for (int i = 0; i < 10; i++) {
			SpecializationTableRow row = new SpecializationTableRow();
			tblSpecialization.addRow(row);
		}

	}

}
