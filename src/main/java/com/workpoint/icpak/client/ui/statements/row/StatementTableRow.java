package com.workpoint.icpak.client.ui.statements.row;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.RowWidget;
import com.workpoint.icpak.shared.model.InvoiceDto;

import static com.workpoint.icpak.client.ui.util.DateUtils.*;
import static com.workpoint.icpak.client.ui.util.NumberUtils.*;

public class StatementTableRow extends RowWidget {

	private static ActivitiesTableRowUiBinder uiBinder = GWT
			.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends
			UiBinder<Widget, StatementTableRow> {
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

	public StatementTableRow() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public StatementTableRow(InvoiceDto invoice) {
		this();
		if (invoice.getDate() != null) {
			divDate.add(new InlineLabel(DATEFORMAT.format(new Date(invoice
					.getDate()))));
			divDueDate.add(new InlineLabel(DATEFORMAT.format(new Date(invoice
					.getDate()))));
		}
		divDocNum.add(new InlineLabel(invoice.getDocumentNo()));
		divDescription.add(new InlineLabel(invoice.getDescription()));

		if (invoice.getAmount() != null) {
			divAmount.add(new InlineLabel(NUMBERFORMAT.format(invoice
					.getAmount()) + ""));
			divBalance.add(new InlineLabel(NUMBERFORMAT.format(invoice
					.getAmount()) + ""));
		}
	}

}
