package com.workpoint.icpak.client.ui.cpd.admin.table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.component.PagingTable;
import com.workpoint.icpak.client.ui.component.TableHeader;
import com.workpoint.icpak.client.ui.cpd.admin.table.row.CPDAdminTableRow;

public class CPDAdminTable extends Composite {

	private static TransactionTableUiBinder uiBinder = GWT
			.create(TransactionTableUiBinder.class);

	interface TransactionTableUiBinder extends UiBinder<Widget, CPDAdminTable> {
	}

	@UiField
	PagingTable tblView;

	public CPDAdminTable() {
		initWidget(uiBinder.createAndBindUi(this));
		tblView.setAutoNumber(false);
		tblView.setSearchSectionVisible(true);
		tblView.getDownloadPdf().setVisible(false);
		createHeader();
	}

	public void createHeader() {
		List<TableHeader> th = new ArrayList<TableHeader>();
		th.add(new TableHeader("Id:", 6.0));
		th.add(new TableHeader("Record Date:", 19.0));
		th.add(new TableHeader("Member:", 30.0));
		th.add(new TableHeader("CPD Activity:", 40.0));
		th.add(new TableHeader("CPD Status:"));
		th.add(new TableHeader("Actions:"));
		tblView.setSearchFieldVisible(true);
		tblView.setTableHeaders(th);
	}

	public void createRow(CPDAdminTableRow row) {
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

	public HasClickHandlers getDownloadButton() {
		return tblView.getDownloadPdf();
	}

	public Date getStartDate() {
		return tblView.getStartDate();
	}

	public Date getEndDate() {
		return tblView.getEndDate();
	}

	public void setNoRecords(int size) {
		tblView.setNoRecords(size);
	}

	public void setInitialDates(Date startDate, Date endDate) {
		tblView.setInitialDates(startDate, endDate);
	}

	public HasClickHandlers getFilterButton() {
		return tblView.getFilterButton();
	}

	public HasValueChangeHandlers<String> getSearchField() {
		return tblView.getSearchValueChangeHander();
	}

	public String getSearchValue() {
		return tblView.getSearchValue();
	}

	public HasKeyDownHandlers getSearchKeyDownHandler() {
		return tblView.getSearchKeyDownHandler();
	}
}
