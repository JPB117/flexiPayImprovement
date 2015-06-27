package com.workpoint.icpak.client.ui.events.registration.proforma;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.TableHeader;
import com.workpoint.icpak.client.ui.component.TableView;
import com.workpoint.icpak.client.ui.events.registration.proforma.details.ProformaTableRow;
import com.workpoint.icpak.client.ui.events.registration.proforma.last.ProformaLastRow;
import com.workpoint.icpak.client.ui.statements.row.StatementTableRow;

public class ProformaInvoice extends Composite {

	private static ProformaInvoiceUiBinder uiBinder = GWT
			.create(ProformaInvoiceUiBinder.class);

	interface ProformaInvoiceUiBinder extends UiBinder<Widget, ProformaInvoice> {
	}

	@UiField
	TableView tblProforma;

	public ProformaInvoice() {
		initWidget(uiBinder.createAndBindUi(this));

		createHeader();
		createRow(new ProformaTableRow());
		createLastRow(new ProformaLastRow());
	}

	public void createHeader() {
		List<TableHeader> th = new ArrayList<TableHeader>();
		th.add(new TableHeader("Description"));
		th.add(new TableHeader("Quantity"));
		th.add(new TableHeader("Amount"));
		tblProforma.setTableHeaders(th);
	}

	public void createRow(ProformaTableRow row) {
		tblProforma.addRow(row);
	}

	private void createLastRow(ProformaLastRow row) {
		tblProforma.addRow(row);
	}

	public void clearRows() {
		tblProforma.clearRows();
	}

}
