package com.workpoint.icpak.client.ui.cpd.member.table;

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
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.component.PagingTable;
import com.workpoint.icpak.client.ui.component.TableHeader;
import com.workpoint.icpak.client.ui.cpd.member.table.footer.CPDMemberFooterRow;
import com.workpoint.icpak.client.ui.cpd.member.table.row.CPDMemberTableRow;

public class CPDMemberTable extends Composite {

	private static TransactionTableUiBinder uiBinder = GWT
			.create(TransactionTableUiBinder.class);

	interface TransactionTableUiBinder extends UiBinder<Widget, CPDMemberTable> {
	}

	@UiField
	PagingTable tblView;

	public CPDMemberTable() {
		initWidget(uiBinder.createAndBindUi(this));
		tblView.setSearchSectionVisible(true);
		tblView.setDatesVisible(true);
		tblView.getDownloadPdf().removeStyleName("hide");
		createHeader();
	}

	public void createHeader() {
		List<TableHeader> th = new ArrayList<TableHeader>();
		tblView.setSearchFieldVisible(false);
		th.add(new TableHeader("COURSE/EVENT NAME", 40.0));
		th.add(new TableHeader("BEGIN DATE:"));
		th.add(new TableHeader("END DATE:"));
		th.add(new TableHeader("UNITS"));
		th.add(new TableHeader("STATUS", 10.0));
		th.add(new TableHeader("ACTION", 8.0));
		tblView.setTableHeaders(th);
	}

	public void createRow(CPDMemberTableRow row) {
		tblView.addRow(row);
	}

	public void createFooter(CPDMemberFooterRow footer) {
		tblView.createFooter(footer);
	}

	public void clearRows() {
		tblView.clearRows();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
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

	public ActionLink getFilterButton() {
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

	public void clearFooter() {
		tblView.clearFooter();
	}
}
