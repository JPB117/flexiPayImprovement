package com.workpoint.icpak.client.ui.eventsandseminars.delegates.table;

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
import com.workpoint.icpak.client.ui.eventsandseminars.delegates.row.DelegateTableRow;

public class DelegatesTable extends Composite {

	private static TransactionTableUiBinder uiBinder = GWT
			.create(TransactionTableUiBinder.class);

	interface TransactionTableUiBinder extends UiBinder<Widget, DelegatesTable> {
	}

	@UiField
	PagingTable tblView;
	CheckBox selected = null;
	boolean isSalesTable = false;

	public DelegatesTable() {
		initWidget(uiBinder.createAndBindUi(this));
		tblView.setAutoNumber(false);
		createHeader();
		createRow(new DelegateTableRow());

	}

	public void createHeader() {
		List<TableHeader> th = new ArrayList<TableHeader>();
		th.add(new TableHeader("Member No"));
		th.add(new TableHeader("Title"));
		th.add(new TableHeader("SurNames"));
		th.add(new TableHeader("Other Names"));
		th.add(new TableHeader("Email"));
		th.add(new TableHeader("Accomodation"));
		th.add(new TableHeader("Total Amount"));
		th.add(new TableHeader("Payment Status"));
		th.add(new TableHeader("Attendance"));
		th.add(new TableHeader("Action"));

		tblView.setTableHeaders(th);
	}

	public void createRow(DelegateTableRow row) {
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
	
	public PagingPanel getPagingPanel(){
		return tblView.getPagingPanel();
	}
	
	public String getDelegateSearchValue() {
		return tblView.getSearchValue();
	}

	public HasKeyDownHandlers getDelegateSearchKeyDownHander() {
		return tblView.getSearchKeyDownHander();
	}

}
