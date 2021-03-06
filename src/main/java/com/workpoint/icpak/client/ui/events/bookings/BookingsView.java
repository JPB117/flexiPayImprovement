package com.workpoint.icpak.client.ui.events.bookings;

import static com.workpoint.icpak.client.ui.util.DateUtils.MONTHDAYFORMAT;
import static com.workpoint.icpak.client.ui.util.DateUtils.MONTHDAYYEARFORMAT;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.component.TableView;
import com.workpoint.icpak.client.ui.eventsandseminars.header.EventsHeader;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.events.AttendanceStatus;
import com.workpoint.icpak.shared.model.events.MemberBookingDto;

public class BookingsView extends ViewImpl implements
		BookingsPresenter.IBookingsView {

	private final Widget widget;

	@UiField
	HTMLPanel container;
	@UiField
	EventsHeader headerContainer;

	@UiField
	TableView tblEvents;

	public interface Binder extends UiBinder<Widget, BookingsView> {
	}

	@Inject
	public BookingsView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		tblEvents.setHeaders(Arrays.asList("Event Dates", "Event Name",
				"Event Location", "CPD Hours", "Accommodation",
				"Booking Status", "Payment Status", "Attendance Status"));
		tblEvents.setSearchSectionVisible(false);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void bindBookings(List<MemberBookingDto> result) {
		tblEvents.clearRows();
		for (MemberBookingDto dto : result) {
			tblEvents
					.addRow(new InlineLabel(MONTHDAYFORMAT.format(dto
							.getStartDate())
							+ "-"
							+ MONTHDAYYEARFORMAT.format(dto.getEndDate())),
							new InlineLabel(dto.getEventName()),
							new InlineLabel(dto.getLocation()),
							new InlineLabel(dto.getCpdHours()),
							new InlineLabel(dto.getAccommodation()),
							setBookingStatus(dto.getBookingStatus()),
							setPaymentStatus(dto.getPaymentStatus()),
							setAttendance(dto.getAttendance()));
		}
	}

	private InlineLabel setBookingStatus(Integer bookingStatus) {
		InlineLabel label = new InlineLabel();
		if (bookingStatus != null) {
			if (bookingStatus == 1) {
				label.setStyleName("label label-success");
				label.getElement().setInnerText("Active");

			} else {
				label.setStyleName("label label-danger");
				label.getElement().setInnerText("Cancelled");
			}
		}
		return label;
	}

	private InlineLabel setAttendance(AttendanceStatus attendance) {
		InlineLabel label = new InlineLabel();
		label.getElement().setInnerText(attendance.name());
		if (attendance != null) {
			if (attendance == AttendanceStatus.NOTATTENDED) {
				label.setStyleName("label label-danger");
			} else {
				label.setStyleName("label label-success");
			}
		}
		return label;
	}

	private InlineLabel setPaymentStatus(PaymentStatus paymentStatus) {
		InlineLabel label = new InlineLabel();
		if (paymentStatus != null) {
			label.getElement().setInnerText(paymentStatus.name());
			if (paymentStatus == PaymentStatus.NOTPAID) {
				label.setStyleName("label label-danger");
			} else {
				label.setStyleName("label label-success");
			}
		}
		return label;
	}

}
