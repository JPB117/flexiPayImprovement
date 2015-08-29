package com.workpoint.icpak.client.ui.eventsandseminars.delegates.row;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.AppManager;
import com.workpoint.icpak.client.ui.OnOptionSelected;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.RowWidget;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.ui.events.EditModelEvent;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.events.AttendanceStatus;
import com.workpoint.icpak.shared.model.events.BookingDto;
import com.workpoint.icpak.shared.model.events.DelegateDto;

public class DelegateTableRow extends RowWidget {

	private static ActivitiesTableRowUiBinder uiBinder = GWT
			.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends
			UiBinder<Widget, DelegateTableRow> {
	}

	@UiField
	HTMLPanel row;

	@UiField
	HTMLPanel divMemberNo;
	@UiField
	HTMLPanel divTitle;
	@UiField
	HTMLPanel divSurName;
	@UiField
	HTMLPanel divOtherNames;
	@UiField
	HTMLPanel divEmail;
	@UiField
	HTMLPanel divAccomodation;
	@UiField
	HTMLPanel divAttendance;
	@UiField
	HTMLPanel divPaymentStatus;

	@UiField
	HTMLPanel divAmount;
	@UiField
	SpanElement spnPaymentStatus;
	@UiField
	SpanElement spnAttendance;

	ActionLink aRemove;

	private Integer rowId;

	@UiField
	ActionLink aAttended;
	@UiField
	ActionLink aNotAttended;

	private DelegateDto delegate;

	public DelegateTableRow() {
		initWidget(uiBinder.createAndBindUi(this));
		aAttended.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onAttendanceChanged(AttendanceStatus.ATTENDED);

			}
		});

		aNotAttended.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onAttendanceChanged(AttendanceStatus.NOTATTENDED);
			}
		});
	}

	protected void onAttendanceChanged(final AttendanceStatus attendanceStatus) {
		AppManager.showPopUp("Confirm", "Confirm " + delegate.getSurname()
				+ " " + attendanceStatus.getDisplayName() + " this event?",
				new OnOptionSelected() {
					@Override
					public void onSelect(String name) {
						if (name.equals("Confirm")) {
							delegate.setAttendance(attendanceStatus);
							setAttendance(delegate.getAttendance());
							AppContext.fireEvent(new EditModelEvent(delegate));
						}
					}
				}, "Confirm", "Cancel");

	}

	public DelegateTableRow(String memberNo, String title, String surName,
			String otherNames, String email, Integer rowId) {
		this();
		divMemberNo.getElement().setInnerHTML(memberNo);
		divTitle.getElement().setInnerHTML(title);
		divSurName.getElement().setInnerHTML(surName);
		divOtherNames.getElement().setInnerHTML(otherNames);
		divEmail.getElement().setInnerHTML(email);
		this.rowId = rowId;

	}

	public DelegateTableRow(BookingDto dto, DelegateDto delegate) {
		this(delegate.getMemberId(), delegate.getTitle(),
				delegate.getSurname(), delegate.getOtherNames(), delegate
						.getEmail(), null);
		delegate.setBookingId(dto.getRefId());
		delegate.setEventId(dto.getEventRefId());
		this.delegate = delegate;

		if (delegate.getAccommodation() != null) {
			divAccomodation.add(new InlineLabel(delegate.getAccommodation()
					.getHotel() + ""));
		}

		setPaymentStatus(dto.getPaymentStatus());
		setAttendance(delegate.getAttendance());
	}

	private void setAttendance(AttendanceStatus attendance) {
		if (attendance != null) {
			spnAttendance.setInnerText(attendance.getDisplayName());
			if (attendance == AttendanceStatus.NOTATTENDED) {
				spnAttendance.removeClassName("label-success");
				spnAttendance.addClassName("label-danger");
			} else {
				spnAttendance.removeClassName("label-danger");
				spnAttendance.addClassName("label-success");
			}
		}
	}

	private void setPaymentStatus(PaymentStatus paymentStatus) {
		if (paymentStatus != null) {
			spnPaymentStatus.setInnerText(paymentStatus.name());
			if (paymentStatus == PaymentStatus.NOTPAID) {
				spnPaymentStatus.removeClassName("label-success");
				spnPaymentStatus.addClassName("label-danger");
			}
		}

	}

	public void InsertParameters(TextField memberNo, TextField title,
			TextField surName, TextField otherNames, TextField email,
			Integer rowId) {
		divMemberNo.add(memberNo);
		divTitle.add(title);
		divSurName.add(surName);
		divOtherNames.add(otherNames);
		divEmail.add(email);
		this.rowId = rowId;
	}

	public void showAdvancedDetails(boolean show) {
		if (show) {
			divPaymentStatus.setVisible(true);
			divAttendance.setVisible(true);
		} else {
			divPaymentStatus.setVisible(false);
			divAttendance.setVisible(false);
		}
	}
}
