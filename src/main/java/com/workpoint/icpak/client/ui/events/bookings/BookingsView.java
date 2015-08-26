package com.workpoint.icpak.client.ui.events.bookings;

import java.util.List;

import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.eventsandseminars.delegates.row.DelegateTableRow;
import com.workpoint.icpak.client.ui.eventsandseminars.delegates.table.DelegatesTable;
import com.workpoint.icpak.client.ui.eventsandseminars.header.EventsHeader;
import com.workpoint.icpak.client.ui.eventsandseminars.row.EventsTableRow;
import com.workpoint.icpak.client.ui.eventsandseminars.table.EventsTable;
import com.workpoint.icpak.shared.model.events.BookingDto;
import com.workpoint.icpak.shared.model.events.DelegateDto;
import com.workpoint.icpak.shared.model.events.EventDto;

public class BookingsView extends ViewImpl implements BookingsPresenter.IBookingsView {

	private final Widget widget;

	@UiField
	HTMLPanel container;

	@UiField
	HTMLPanel panelEvents;

	@UiField
	HTMLPanel panelEventDrillDown;
	
	@UiField SpanElement spnEventTitle;
	@UiField EventsHeader headerContainer;
	@UiField EventsTable tblView;
	@UiField DelegatesTable tblDelegates;

	public interface Binder extends UiBinder<Widget, BookingsView> {
		
	}

	@Inject
	public BookingsView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		showAdvancedView(false);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void showAdvancedView(boolean show) {
		if (show) {
			panelEventDrillDown.setVisible(true);
			panelEvents.setVisible(false);
		} else {
			panelEventDrillDown.setVisible(false);
			panelEvents.setVisible(true);
		}
	}

	@Override
	public void bindEvents(List<EventDto> events) {
		headerContainer.setCounts(events.size(),0,0);
		tblView.clearRows();
		for(EventDto event: events){
			tblView.createRow(new EventsTableRow(event));
		}
	}

	@Override
	public void bindEvent(EventDto event) {
		spnEventTitle.setInnerText(event.getName());
	}

	@Override
	public void bindBookings(List<BookingDto> bookings) {
		tblDelegates.clearRows();
		for(BookingDto dto: bookings){
			for(DelegateDto delegate: dto.getDelegates()){
				tblDelegates.createRow(new DelegateTableRow(dto, delegate));
			}
		}
	}

	@Override
	public PagingPanel getEventsPagingPanel() {
		return tblView.getPagingPanel();
	}

	@Override
	public PagingPanel getBookingsPagingPanel() {
		return tblDelegates.getPagingPanel();
	}

}
