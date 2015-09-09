package com.workpoint.icpak.client.ui.statements.row;

import static com.workpoint.icpak.client.ui.util.DateUtils.DATEFORMAT;
import static com.workpoint.icpak.client.ui.util.NumberUtils.NUMBERFORMAT;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.RowWidget;
import com.workpoint.icpak.shared.model.InvoiceDto;

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
	@UiField
	HTMLPanel divContact;
	@UiField
	HTMLPanel divPaymentMode;
	@UiField
	SpanElement spnStatus;

	public StatementTableRow() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public StatementTableRow(InvoiceDto invoice) {
		this();
		if (invoice.getContactName() != null) {
			divContact.add(new InlineLabel(invoice.getContactName()));
		}
		if (invoice.getInvoiceDate() != null) {
			divDate.add(new InlineLabel(DATEFORMAT.format(invoice
					.getInvoiceDate())));
		}
		if (invoice.getDueDate() != null) {
			divDueDate.add(new InlineLabel(DATEFORMAT.format(invoice
					.getDueDate())));
		}
		divDocNum.add(new InlineLabel(invoice.getDocumentNo()));
		divDescription.add(new InlineLabel(invoice.getDescription()));

		Double amount = (invoice.getInvoiceAmount() == null ? 0 : invoice
				.getInvoiceAmount());
		divAmount.add(new InlineLabel(NUMBERFORMAT.format(amount) + ""));

		divPaymentMode.add(new InlineLabel(
				invoice.getPaymentMode() == null ? "N/A" : invoice
						.getPaymentMode()));

		Double balance = amount;

		if (invoice.getPaymentStatus() != null
				&& !invoice.getPaymentStatus().equals("NOTPAID")) {
			spnStatus.addClassName("label label-success");
			spnStatus.setInnerText(invoice.getPaymentStatus());

			if (invoice.getPaymentStatus().equals("PAID")) {
				balance = (invoice.getInvoiceAmount() == null ? 0 : invoice
						.getInvoiceAmount())
						- (invoice.getTransactionAmount() == null ? 0 : invoice
								.getTransactionAmount());
			}
		} else {
			spnStatus.addClassName("label label-danger");
			spnStatus.setInnerText("NOT PAID");
		}

		divBalance.add(new InlineLabel(NUMBERFORMAT.format(balance) + ""));
	}
}
