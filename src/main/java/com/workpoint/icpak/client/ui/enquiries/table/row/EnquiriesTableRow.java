package com.workpoint.icpak.client.ui.enquiries.table.row;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.RowWidget;
import com.workpoint.icpak.shared.model.CPDDto;

public class EnquiriesTableRow extends RowWidget {

	private static ActivitiesTableRowUiBinder uiBinder = GWT
			.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends
			UiBinder<Widget, EnquiriesTableRow> {
	}

	@UiField
	HTMLPanel row;
	@UiField
	HTMLPanel divDate;
	@UiField
	ActionLink aSubject;
	@UiField
	ActionLink aMember;

	public EnquiriesTableRow() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public EnquiriesTableRow(CPDDto dto) {
		this();
		aMember.setText(dto.getFullNames());

	}

}
