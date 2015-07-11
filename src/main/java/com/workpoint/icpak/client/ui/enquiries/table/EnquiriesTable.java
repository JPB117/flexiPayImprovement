package com.workpoint.icpak.client.ui.enquiries.table;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.TableHeader;
import com.workpoint.icpak.client.ui.component.TableView;
import com.workpoint.icpak.client.ui.enquiries.table.row.EnquiriesTableRow;

public class EnquiriesTable extends Composite {

	private static TransactionTableUiBinder uiBinder = GWT
			.create(TransactionTableUiBinder.class);

	interface TransactionTableUiBinder extends UiBinder<Widget, EnquiriesTable> {
	}

	@UiField
	TableView tblView;

	public EnquiriesTable() {
		initWidget(uiBinder.createAndBindUi(this));
		tblView.setAutoNumber(false);
		createHeader();
		createRow(new EnquiriesTableRow());
	}

	public void createHeader() {
		List<TableHeader> th = new ArrayList<TableHeader>();
		th.add(new TableHeader("#"));
		th.add(new TableHeader("Member No"));
		th.add(new TableHeader("Member Name"));
		th.add(new TableHeader("Email Address"));
		th.add(new TableHeader("Mobile"));
		th.add(new TableHeader("Category"));
		th.add(new TableHeader("Subject"));
		th.add(new TableHeader("Message"));
		th.add(new TableHeader("Status"));
		th.add(new TableHeader("Action"));

		tblView.setTableHeaders(th);
	}

	public void createRow(EnquiriesTableRow row) {
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
