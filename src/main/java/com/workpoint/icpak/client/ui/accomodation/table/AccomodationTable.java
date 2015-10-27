package com.workpoint.icpak.client.ui.accomodation.table;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.accomodation.row.AccomodationTableRow;
import com.workpoint.icpak.client.ui.component.TableHeader;
import com.workpoint.icpak.client.ui.component.TableView;

public class AccomodationTable extends Composite {

	private static TransactionTableUiBinder uiBinder = GWT
			.create(TransactionTableUiBinder.class);

	interface TransactionTableUiBinder extends
			UiBinder<Widget, AccomodationTable> {
	}

	@UiField
	TableView tblView;
	CheckBox selected = null;
	boolean isSalesTable = false;

	public AccomodationTable() {
		initWidget(uiBinder.createAndBindUi(this));
		tblView.setAutoNumber(false);
		createHeader();
		createRow(new AccomodationTableRow());
	}

	public void createHeader() {
		List<TableHeader> th = new ArrayList<TableHeader>();
		th.add(new TableHeader("Event Name:"));
		th.add(new TableHeader("Hotel Name:"));
		th.add(new TableHeader("Nights:"));
		th.add(new TableHeader("Spaces:"));
		th.add(new TableHeader("Price:"));
		th.add(new TableHeader("Action:"));

		tblView.setTableHeaders(th);
	}

	public void createRow(AccomodationTableRow row) {
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

}
