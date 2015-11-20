package com.workpoint.icpak.client.ui.frontmember.table;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.component.PagingTable;
import com.workpoint.icpak.client.ui.component.TableHeader;
import com.workpoint.icpak.client.ui.frontmember.table.row.FrontMemberTableRow;

public class FrontMemberTable extends Composite {

	private static TransactionTableUiBinder uiBinder = GWT.create(TransactionTableUiBinder.class);

	interface TransactionTableUiBinder extends UiBinder<Widget, FrontMemberTable> {
	}

	@UiField
	PagingTable tblView;

	public FrontMemberTable() {
		initWidget(uiBinder.createAndBindUi(this));
		tblView.setAutoNumber(false);
		tblView.setSearchSectionVisible(true);
		tblView.setSearchFieldVisible(true);
		tblView.setDatesVisible(false);
		tblView.getDownloadPdf().setVisible(false);
		createHeader();
	}

	public void createHeader() {
		List<TableHeader> th = new ArrayList<TableHeader>();
		th.add(new TableHeader("NAME"));
		th.add(new TableHeader("Category"));
		th.add(new TableHeader("Membership"));
		th.add(new TableHeader("Practising Status"));
		tblView.setTableHeaders(th);
	}

	public void createRow(FrontMemberTableRow row) {
		tblView.addRow(row);
	}

	public void clearRows() {
		tblView.clearRows();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}

	public void setNoRecords(int size) {
		tblView.setNoRecords(size);
	}

	public PagingPanel getPagingPanel() {
		return tblView.getPagingPanel();
	}

	public String getSearchValue() {
		return tblView.getSearchValue();
	}

	public HasValueChangeHandlers<String> getSearchKeyDownHander() {
		return tblView.getSearchKeyDownHander();
	}
}
