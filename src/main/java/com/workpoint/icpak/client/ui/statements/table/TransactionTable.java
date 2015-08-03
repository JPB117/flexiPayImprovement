package com.workpoint.icpak.client.ui.statements.table;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.component.PagingTable;
import com.workpoint.icpak.client.ui.component.TableHeader;
import com.workpoint.icpak.client.ui.statements.row.StatementTableRow;

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
		th.add(new TableHeader("Date"));
		th.add(new TableHeader("Doc Number"));
		th.add(new TableHeader("Description"));
		th.add(new TableHeader("Due Date"));
		th.add(new TableHeader("Amount"));
		th.add(new TableHeader("Balance"));

		tblView.setTableHeaders(th);
	}

	public void createRow(StatementTableRow row) {
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

}
