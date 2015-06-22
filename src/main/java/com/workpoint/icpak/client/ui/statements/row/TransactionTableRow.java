package com.workpoint.icpak.client.ui.statements.row;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.RowWidget;

public class TransactionTableRow extends RowWidget {

	private static ActivitiesTableRowUiBinder uiBinder = GWT
			.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends
			UiBinder<Widget, TransactionTableRow> {
	}

	@UiField
	HTMLPanel row;
	@UiField
	HTMLPanel divDate;
	@UiField
	HTMLPanel divDocNum;
	@UiField
	HTMLPanel divDescription;
	@UiField
	HTMLPanel divDueDate;
	@UiField
	HTMLPanel divAmount;
	@UiField
	HTMLPanel divBalance;

	public TransactionTableRow() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
