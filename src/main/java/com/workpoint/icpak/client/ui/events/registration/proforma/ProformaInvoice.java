package com.workpoint.icpak.client.ui.events.registration.proforma;

import static com.workpoint.icpak.client.ui.util.NumberUtils.CURRENCYFORMAT;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.TableHeader;
import com.workpoint.icpak.client.ui.component.TableView;
import com.workpoint.icpak.client.ui.events.registration.proforma.details.ProformaTableRow;
import com.workpoint.icpak.client.ui.events.registration.proforma.last.ProformaLastRow;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.InvoiceLineDto;
import com.workpoint.icpak.shared.model.InvoiceLineType;

public class ProformaInvoice extends Composite {

	private static ProformaInvoiceUiBinder uiBinder = GWT
			.create(ProformaInvoiceUiBinder.class);

	interface ProformaInvoiceUiBinder extends UiBinder<Widget, ProformaInvoice> {
	}

	@UiField
	TableView tblProforma;
	@UiField
	TableView tblDiscounts;
	@UiField
	TableView tblPenalties;
	@UiField
	InlineLabel lblCompany;
	@UiField
	InlineLabel lblAddress;
	@UiField
	InlineLabel lblQuoteNo;
	@UiField
	InlineLabel lblInvoiceDate;

	public ProformaInvoice() {
		initWidget(uiBinder.createAndBindUi(this));
		createHeader();
		createRow(new ProformaTableRow());
		createLastRow(new ProformaLastRow());
	}

	public void createHeader() {
		List<TableHeader> th = new ArrayList<TableHeader>();
		th.add(new TableHeader("Description", 72.0));
		th.add(new TableHeader("Quantity"));
		th.add(new TableHeader("Unit Price"));
		th.add(new TableHeader("Amount"));
		tblProforma.setTableHeaders(th);
		tblDiscounts.setTableHeaders(th);
		tblPenalties.setTableHeaders(th);
	}

	public void createRow(ProformaTableRow row) {
		tblProforma.addRow(row);
	}

	private void createLastRow(ProformaLastRow row) {
		tblProforma.addRow(row);
	}

	public void clearRows() {
		tblProforma.clearRows();
		tblDiscounts.clearRows();
		tblPenalties.clearRows();
	}

	public void setInvoice(InvoiceDto invoice) {
		lblCompany.setText(invoice.getCompanyName());
		lblAddress.setText(invoice.getCompanyAddress());

		if (invoice.getDate() != null) {
			lblInvoiceDate.setText(DateUtils.DATEFORMAT.format(new Date(invoice
					.getDate())));
		}
		lblQuoteNo.setText(invoice.getDocumentNo());

		for (InvoiceLineDto line : invoice.getLines()) {
			if (line.getType() == null) {
				line.setType(InvoiceLineType.Normal);
			}

			if (line.getType() == InvoiceLineType.Discount) {
				tblDiscounts.addRow(
						new InlineLabel(line.getDescription()),
						new InlineLabel(line.getQuantity() + ""),
						new InlineLabel(CURRENCYFORMAT.format(line
								.getUnitPrice()) + ""), new InlineLabel(
								CURRENCYFORMAT.format(line.getTotalAmount())
										+ ""));
			} else if (line.getType() == InvoiceLineType.Penalty) {
				tblPenalties.addRow(
						new InlineLabel(line.getDescription()),
						new InlineLabel(line.getQuantity() + ""),
						new InlineLabel(CURRENCYFORMAT.format(line
								.getUnitPrice()) + ""), new InlineLabel(
								CURRENCYFORMAT.format(line.getTotalAmount())
										+ ""));
			} else {
				tblProforma.addRow(
						new InlineLabel(line.getDescription()),
						new InlineLabel(line.getQuantity() + ""),
						new InlineLabel(CURRENCYFORMAT.format(line
								.getUnitPrice()) + ""), new InlineLabel(
								CURRENCYFORMAT.format(line.getTotalAmount())
										+ ""));
			}
		}
		if (invoice.getInvoiceAmount() != null) {
			tblProforma.addRow(
					new InlineLabel(),
					new InlineLabel(),
					new InlineLabel("Total"),
					new InlineLabel(CURRENCYFORMAT.format(invoice
							.getInvoiceAmount()) + ""));
		}

		if (invoice.getTotalDiscount() != null) {
			tblDiscounts.addRow(new InlineLabel(), new InlineLabel(),
					new InlineLabel("Total Discount"), new InlineLabel(
							CURRENCYFORMAT.format(invoice.getTotalDiscount())
									+ ""));
		}

		if (invoice.getTotalPenalty() != null) {
			tblPenalties.addRow(new InlineLabel(), new InlineLabel(),
					new InlineLabel("Total Penalties"), new InlineLabel(
							CURRENCYFORMAT.format(invoice.getTotalPenalty())
									+ ""));
		}
	}
}
