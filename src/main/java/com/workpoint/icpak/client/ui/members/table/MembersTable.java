package com.workpoint.icpak.client.ui.members.table;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.component.PagingTable;
import com.workpoint.icpak.client.ui.component.TableHeader;
import com.workpoint.icpak.client.ui.members.row.MembersTableRow;

public class MembersTable extends Composite {

	private static TransactionTableUiBinder uiBinder = GWT
			.create(TransactionTableUiBinder.class);

	interface TransactionTableUiBinder extends UiBinder<Widget, MembersTable> {
	}

	@UiField
	PagingTable tblView;
	CheckBox selected = null;

	public MembersTable() {
		initWidget(uiBinder.createAndBindUi(this));
		tblView.setAutoNumber(false);
		createHeader();
		tblView.setSearchSectionVisible(true);
		tblView.getDownloadPdf().setVisible(false);
	}

	public void createHeader() {
		List<TableHeader> th = new ArrayList<TableHeader>();
		th.add(new TableHeader("Registration Date"));
		th.add(new TableHeader("Applicant Name"));
		th.add(new TableHeader("Email"));
		th.add(new TableHeader("Profile Completion"));
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

	public PagingPanel getPagingPanel() {
		return tblView.getPagingPanel();
	}

	public String getSearchValue() {
		return tblView.getSearchValue();
	}

	public HasKeyDownHandlers getSearchKeyDownHandler() {
		return tblView.getSearchKeyDownHandler();
	}

}
