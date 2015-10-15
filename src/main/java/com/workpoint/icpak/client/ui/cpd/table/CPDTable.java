package com.workpoint.icpak.client.ui.cpd.table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.component.PagingTable;
import com.workpoint.icpak.client.ui.component.TableHeader;
import com.workpoint.icpak.client.ui.cpd.table.row.CPDTableRow;
import com.workpoint.icpak.client.util.AppContext;

public class CPDTable extends Composite {

	private static TransactionTableUiBinder uiBinder = GWT
			.create(TransactionTableUiBinder.class);

	interface TransactionTableUiBinder extends UiBinder<Widget, CPDTable> {
	}

	@UiField
	PagingTable tblView;

	public CPDTable() {
		initWidget(uiBinder.createAndBindUi(this));
		tblView.setAutoNumber(false);
		tblView.setSearchSection(true);
		tblView.setSearchVisible(false);
		tblView.setDatesVisible(true);
		createHeader(AppContext.isCurrentUserAdmin());
	}

	public void createHeader(boolean isAdmin) {
		List<TableHeader> th = new ArrayList<TableHeader>();
		th.add(new TableHeader("Start Date:"));
		th.add(new TableHeader("End Date:"));
		if (isAdmin) {
			th.add(new TableHeader("Member Name:"));
		}
		th.add(new TableHeader("Course/Event Name"));
		th.add(new TableHeader("Organiser"));
		th.add(new TableHeader("Category"));
		th.add(new TableHeader("CPD Hours"));
		th.add(new TableHeader("Status"));
		th.add(new TableHeader("Action"));
		tblView.setTableHeaders(th);
	}

	public void createRow(CPDTableRow row) {
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

}
