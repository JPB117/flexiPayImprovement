package com.workpoint.icpak.client.ui.members.table;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.TableHeader;
import com.workpoint.icpak.client.ui.component.TableView;
import com.workpoint.icpak.client.ui.members.row.MembersTableRow;
import com.workpoint.icpak.client.ui.statements.row.StatementTableRow;

public class MembersTable extends Composite {

	private static TransactionTableUiBinder uiBinder = GWT
			.create(TransactionTableUiBinder.class);

	interface TransactionTableUiBinder extends UiBinder<Widget, MembersTable> {
	}

	@UiField
	TableView tblView;
	CheckBox selected = null;
	boolean isSalesTable = false;

	public MembersTable() {
		initWidget(uiBinder.createAndBindUi(this));
		tblView.setAutoNumber(false);
		createHeader();
	}

	public void createHeader() {
		List<TableHeader> th = new ArrayList<TableHeader>();
		th.add(new TableHeader("Registration Date"));
		th.add(new TableHeader("Member No"));
		th.add(new TableHeader("Member Name"));
		th.add(new TableHeader("Email"));
		th.add(new TableHeader("Profile"));
		th.add(new TableHeader("Status"));

		tblView.setTableHeaders(th);
	}

	public void createRow(MembersTableRow row) {
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
