package com.workpoint.icpak.client.ui.events.bookings;

import java.util.List;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rest.rebind.utils.Arrays;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.component.TableView;
import com.workpoint.icpak.client.ui.eventsandseminars.header.EventsHeader;
import com.workpoint.icpak.shared.model.events.MemberBookingDto;
import static com.workpoint.icpak.client.ui.util.DateUtils.*;

public class BookingsView extends ViewImpl implements BookingsPresenter.IBookingsView {

	private final Widget widget;

	@UiField HTMLPanel container;
	@UiField EventsHeader headerContainer;
	
	@UiField TableView tblEvents;

	public interface Binder extends UiBinder<Widget, BookingsView> {}

	@Inject
	public BookingsView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		tblEvents.setHeaders(Arrays.asList("Date", "Event Name","Location","Event Status",
				"CPD Hours","Attendance",
				"Payment","Accommodation"));
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void bindBookings(List<MemberBookingDto> result) {
		for(MemberBookingDto dto: result){
			tblEvents.addRow(
					new InlineLabel(MONTHDAYFORMAT.format(dto.getStartDate())+"-"+
							MONTHDAYYEARFORMAT.format(dto.getEndDate())),
					new InlineLabel(dto.getEventName()),
					new InlineLabel(dto.getLocation()),
					new InlineLabel(dto.getEventStatus().name()),
					new InlineLabel(dto.getCpdHours()),
					new InlineLabel(dto.getAttendance().getDisplayName()),
					new InlineLabel(dto.getPaymentStatus().name()),
					new InlineLabel(dto.getAccommodation())
					);
		}
	}

}
