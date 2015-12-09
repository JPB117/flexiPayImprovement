package com.workpoint.icpak.client.ui.eventsandseminars.delegates.row;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.model.UploadContext;
import com.workpoint.icpak.client.model.UploadContext.UPLOADACTION;
import com.workpoint.icpak.client.ui.AppManager;
import com.workpoint.icpak.client.ui.OnOptionSelected;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.RowWidget;
import com.workpoint.icpak.client.ui.cpd.table.row.CPDTableRow.TableActionType;
import com.workpoint.icpak.client.ui.events.EditModelEvent;
import com.workpoint.icpak.client.ui.events.TableActionEvent;
import com.workpoint.icpak.client.ui.eventsandseminars.delegates.updatepayment.UpdatePaymentWidget;
import com.workpoint.icpak.client.ui.eventsandseminars.resendProforma.ResendProforma;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.EventType;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.events.AttendanceStatus;
import com.workpoint.icpak.shared.model.events.DelegateDto;

public class DelegateTableRow extends RowWidget {

	private static ActivitiesTableRowUiBinder uiBinder = GWT.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends UiBinder<Widget, DelegateTableRow> {
	}

	@UiField
	HTMLPanel row;
	@UiField
	HTMLPanel divBookingDate;
	@UiField
	HTMLPanel divSponsorNames;
	@UiField
	HTMLPanel divContactName;
	@UiField
	HTMLPanel divSponsorEmail;
	@UiField
	HTMLPanel divMemberNo;
	@UiField
	HTMLPanel divErnNo;
	@UiField
	HTMLPanel divDelegateNames;
	@UiField
	HTMLPanel divAccomodation;
	@UiField
	SpanElement spnPaymentStatus;
	@UiField
	SpanElement spnAttendance;
	@UiField
	SpanElement spnIsMember;
	@UiField
	ActionLink aEnrol;
	@UiField
	ActionLink aAttended;
	@UiField
	ActionLink aUpdatePayment;
	@UiField
	ActionLink aNotAttended;
	@UiField
	ActionLink aProforma;
	@UiField
	ActionLink aResendProforma;
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

		aEnrol.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onAttendanceChanged(AttendanceStatus.ENROLLED);
			}
		});

		aUpdatePayment.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				updatePaymentInfo();
			}
		});

		aResendProforma.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final ResendProforma resendWidget = new ResendProforma(delegate);
				AppManager.showPopUp("Resend Proforma", resendWidget, new OnOptionSelected() {
					@Override
					public void onSelect(String name) {
						if (name.equals("Resend")) {
							AppContext.fireEvent(new TableActionEvent(resendWidget.getResendObject(),
									TableActionType.RESENDPROFORMA));

						}
					}
				}, "Resend", "Cancel");

			}
		});

		aProforma.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// Window.alert("Proforma" + delegate.getBookingRefId());
				UploadContext ctx = new UploadContext("getreport");
				ctx.setContext("bookingRefId", delegate.getBookingRefId());
				ctx.setAction(UPLOADACTION.GETPROFORMA);

				// ctx.setContext(key, value)
				Window.open(ctx.toUrl(), "Get Proforma", null);
			}
		});

	}

	protected void updatePaymentInfo() {
		final UpdatePaymentWidget paymentWidget = new UpdatePaymentWidget(delegate);
		AppManager.showPopUp("Update Payment Info", paymentWidget, new OnOptionSelected() {
			@Override
			public void onSelect(String name) {
				if (name.equals("Save")) {
					DelegateDto d = paymentWidget.getDelegate();
					AppContext.fireEvent(new EditModelEvent(paymentWidget.getDelegate()));
				}
			}
		}, "Save", "Cancel");
	}

	protected void onAttendanceChanged(final AttendanceStatus attendanceStatus) {
		AppManager.showPopUp("Confirm",
				"Confirm " + delegate.getSurname() + " " + attendanceStatus.getDisplayName() + " this event?",
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

	public DelegateTableRow(DelegateDto delegate, EventType eventType) {
		this();
		this.delegate = delegate;
		divBookingDate.getElement().setInnerText(DateUtils.CREATEDFORMAT.format(delegate.getCreatedDate()));
		if (delegate.getCompanyName() != null) {
			divSponsorNames.getElement().setInnerText(delegate.getCompanyName());
		}

		if (delegate.getErn() != null) {
			divErnNo.getElement().setInnerText(delegate.getErn());
		}
		divContactName.getElement().setInnerText(delegate.getContactName());
		divSponsorEmail.getElement().setInnerText(delegate.getContactEmail());
		if (delegate.getMemberNo() != null) {
			divMemberNo.getElement().setInnerText(delegate.getMemberNo());
		}
		divDelegateNames.getElement()
				.setInnerText((delegate.getTitle() == null ? "" : delegate.getTitle() + " ")
						+ (delegate.getSurname() == null ? "" : delegate.getSurname() + " ")
						+ (delegate.getOtherNames() == null ? "" : delegate.getOtherNames() + " "));

		if (delegate.getAccommodation() != null) {
			divAccomodation.add(new InlineLabel(delegate.getAccommodation().getHotel() + ""));
		} else {
			divAccomodation.getElement().setInnerText("None");
		}

		if (delegate.getMemberNo() == null) {
			spnIsMember.setClassName("fa fa-times");
		} else {
			spnIsMember.setClassName("fa fa-check");
		}

		setPaymentStatus(delegate.getPaymentStatus());
		setAttendance(delegate.getAttendance());
		setActionButtons(eventType);
	}

	private void setActionButtons(EventType eventType) {
		if (eventType == EventType.COURSE) {
			aEnrol.setVisible(true);
			aAttended.setVisible(false);
			aNotAttended.setVisible(false);
		} else {
			aEnrol.setVisible(false);
			aAttended.setVisible(true);
			aNotAttended.setVisible(true);
		}
	}

	private void setAttendance(AttendanceStatus attendance) {
		if (attendance != null) {
			// spnAttendance.setInnerText(attendance.getDisplayName());
			if (attendance == AttendanceStatus.NOTATTENDED || attendance == AttendanceStatus.NOTENROLLED) {
				// spnAttendance.removeClassName("label-success");
				// spnAttendance.addClassName("label-danger");
				spnAttendance.setClassName("fa fa-times");
			} else {
				// spnAttendance.removeClassName("label-danger");
				// spnAttendance.addClassName("label-success");
				spnAttendance.setClassName("fa fa-check");
			}
		}
	}

	private void setPaymentStatus(PaymentStatus paymentStatus) {
		if (paymentStatus != null) {
			// spnPaymentStatus.setInnerText(paymentStatus.name());
			if (paymentStatus == PaymentStatus.NOTPAID) {
				// spnPaymentStatus.removeClassName("label-success");
				// spnPaymentStatus.addClassName("label-danger");
				spnPaymentStatus.setClassName("fa fa-times");
			} else {
				spnPaymentStatus.setClassName("fa fa-check");
			}
		}

	}

	// public void InsertParameters(TextField memberNo, TextField title,
	// TextField surName, TextField otherNames, TextField email,
	// Integer rowId) {
	// divMemberNo.add(memberNo);
	// divTitle.add(title);
	// divSurName.add(surName);
	// divOtherNames.add(otherNames);
	// divEmail.add(email);
	// this.rowId = rowId;
	// }
	//
	// public void showAdvancedDetails(boolean show) {
	// if (show) {
	// divPaymentStatus.setVisible(true);
	// divAttendance.setVisible(true);
	// } else {
	// divPaymentStatus.setVisible(false);
	// divAttendance.setVisible(false);
	// }
	// }
}
