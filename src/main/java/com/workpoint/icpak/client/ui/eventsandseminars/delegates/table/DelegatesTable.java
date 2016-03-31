package com.workpoint.icpak.client.ui.eventsandseminars.delegates.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.component.PagingTable;
import com.workpoint.icpak.client.ui.component.TableHeader;
import com.workpoint.icpak.client.ui.eventsandseminars.delegates.row.DelegateTableRow;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.BookingStatus;
import com.workpoint.icpak.shared.model.events.AccommodationDto;

public class DelegatesTable extends Composite {

	private static TransactionTableUiBinder uiBinder = GWT
			.create(TransactionTableUiBinder.class);

	interface TransactionTableUiBinder extends UiBinder<Widget, DelegatesTable> {
	}

	@UiField
	PagingTable tblView;
	CheckBox selected = null;

	public DelegatesTable() {
		initWidget(uiBinder.createAndBindUi(this));
		tblView.setAutoNumber(false);
		tblView.showDelegateFilters(true);
		createHeader();

		tblView.getLstBookingStatus().setItems(
				Arrays.asList(BookingStatus.values()),
				"-Select Booking Status-");

	}

	public void createHeader() {
		List<TableHeader> th = new ArrayList<TableHeader>();
		th.add(new TableHeader("Booking Date"));
		th.add(new TableHeader("Proforma Invoice"));
		th.add(new TableHeader("ERN No"));
		th.add(new TableHeader("Sponsor Name"));
		th.add(new TableHeader("Contact Names"));
		th.add(new TableHeader("Contact Email"));
		th.add(new TableHeader("ICPAK Member"));
		th.add(new TableHeader("Member No"));
		th.add(new TableHeader("Delegate Names"));
		th.add(new TableHeader("Accomodation"));
		th.add(new TableHeader("Booking Status"));
		th.add(new TableHeader("Payment Status"));
		th.add(new TableHeader("Attendance"));
		if (AppContext.isCurrentUserEventEdit()) {
			th.add(new TableHeader("Actions"));
		}
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

	public PagingPanel getPagingPanel() {
		return tblView.getPagingPanel();
	}

	public ActionLink getDownloadPDFLink() {
		return tblView.getDownloadPdf();
	}

	public ActionLink getDownloadXLSLink() {
		return tblView.getDownloadXls();
	}

	public String getDelegateSearchValue() {
		return tblView.getSearchValue();
	}

	public HasValueChangeHandlers<String> getDelegateSearchValueChangeHandler() {
		return tblView.getSearchValueChangeHander();
	}

	public HasKeyDownHandlers getSearchKeyDownHandler() {
		return tblView.getSearchKeyDownHandler();
	}

	public HasValueChangeHandlers<BookingStatus> getBookingStatusValueChangeHandler() {
		return tblView.getBookingStatusValueChangeHandler();
	}

	public HasValueChangeHandlers<AccommodationDto> getAccomodationValueChangeHandler() {
		return tblView.getAccomodationValueChangeHandler();
	}

	public DropDownList<AccommodationDto> getLstAccomodation() {
		return tblView.getLstAccomodation();
	}
}
