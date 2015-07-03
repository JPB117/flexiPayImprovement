package com.workpoint.icpak.client.ui.cpd.confirmed;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.cpd.table.CPDTable;
import com.workpoint.icpak.client.ui.cpd.table.row.CPDTableRow;

public class ConfirmedCPD extends Composite {

	private static TillDetailsUiBinder uiBinder = GWT
			.create(TillDetailsUiBinder.class);

	@UiField
	CPDTable tblView;

	interface TillDetailsUiBinder extends UiBinder<Widget, ConfirmedCPD> {
	}

	public ConfirmedCPD() {
		initWidget(uiBinder.createAndBindUi(this));

		createRow(new CPDTableRow());
	}

	private void createRow(CPDTableRow cpdTableRow) {
		tblView.createRow(cpdTableRow);
	}

}
