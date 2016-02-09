package com.workpoint.icpak.client.ui.eventsandseminars.table;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.component.PagingTable;
import com.workpoint.icpak.client.ui.component.TableHeader;
import com.workpoint.icpak.client.ui.eventsandseminars.row.EventsTableRow;

public class EventsTable extends Composite {

	private static TransactionTableUiBinder uiBinder = GWT
			.create(TransactionTableUiBinder.class);

	interface TransactionTableUiBinder extends UiBinder<Widget, EventsTable> {
	}

	@UiField
	PagingTable tblView;
	CheckBox selected = null;
	boolean isSalesTable = false;

	public EventsTable() {
		initWidget(uiBinder.createAndBindUi(this));
		tblView.setAutoNumber(false);
		createHeader();
	}

	public void createHeader() {
		List<TableHeader> th = new ArrayList<TableHeader>();
		th.add(new TableHeader("Dates"));
		th.add(new TableHeader("Event Name"));
		th.add(new TableHeader("Event Type"));
		th.add(new TableHeader("Location"));
		th.add(new TableHeader("CPD Hours"));
		th.add(new TableHeader("Total Delegates"));
		th.add(new TableHeader("Total Paid"));
		th.add(new TableHeader("Total UnPaid"));

		tblView.setTableHeaders(th);
	}

	public void createRow(EventsTableRow row) {
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

	public HasValueChangeHandlers<String> getSearchKeyDownHander() {
		return tblView.getSearchValueChangeHander();
	}

	public HasKeyDownHandlers getSearchKeyDownHandler() {
		return tblView.getSearchKeyDownHandler();
	}
}
