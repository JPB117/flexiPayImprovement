package com.workpoint.icpak.client.ui.invoices.table;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.component.PagingTable;
import com.workpoint.icpak.client.ui.component.TableHeader;
import com.workpoint.icpak.client.ui.invoices.row.InvoiceTableRow;

public class TransactionTable extends Composite {

	private static TransactionTableUiBinder uiBinder = GWT
			.create(TransactionTableUiBinder.class);

	interface TransactionTableUiBinder extends
			UiBinder<Widget, TransactionTable> {
	}

	@UiField
	PagingTable tblView;
	CheckBox selected = null;
	boolean isSalesTable = false;

	public TransactionTable() {
		initWidget(uiBinder.createAndBindUi(this));
		tblView.setAutoNumber(false);
		createHeader();
	}

	public void createHeader() {
		List<TableHeader> th = new ArrayList<TableHeader>();
		th.add(new TableHeader("Transaction Date"));
		th.add(new TableHeader("Type"));
		th.add(new TableHeader("Description"));
		th.add(new TableHeader("Invoice"));
		th.add(new TableHeader("Amount Paid"));
		th.add(new TableHeader("Amount Charged"));
		th.add(new TableHeader("Previous Payments"));
		th.add(new TableHeader("Balance"));
		// th.add(new TableHeader("Payment Status"));
		tblView.setTableHeaders(th);
	}

	public void createRow(InvoiceTableRow row) {
		tblView.addRow(row);
	}

	public void clearRows() {
		tblView.clearRows();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}

	public void setAutoNumber(boolean autoNumber) {
		tblView.setAutoNumber(false);
	}

	public PagingPanel getPagingPanel() {
		return tblView.getPagingPanel();
	}

	public void setNoRecords(int size) {
		tblView.setNoRecords(size);
	}

}
