package com.workpoint.icpak.client.ui.eventsandseminars.delegates.row;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
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
import com.workpoint.icpak.client.ui.events.EditModelEvent;
import com.workpoint.icpak.client.ui.events.TableActionEvent;
import com.workpoint.icpak.client.ui.events.cpd.MemberCPDEvent;
import com.workpoint.icpak.client.ui.eventsandseminars.delegates.updatepayment.UpdatePaymentWidget;
import com.workpoint.icpak.client.ui.eventsandseminars.resendProforma.ResendProforma;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.EventType;
import com.workpoint.icpak.shared.model.MemberDto;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.TableActionType;
import com.workpoint.icpak.shared.model.events.AttendanceStatus;
import com.workpoint.icpak.shared.model.events.DelegateDto;
import com.workpoint.icpak.shared.model.events.EventDto;

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
	HTMLPanel divErnNo;
	@UiField
	Element divDelegateNames;
	@UiField
	HTMLPanel divAccomodation;
	@UiField
	HTMLPanel divAction;
	@UiField
	SpanElement spnPaymentStatus;
	@UiField
	SpanElement spnBookingStatus;
	@UiField
	SpanElement spnAttendance;
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
	@UiField
	ActionLink aEditBooking;
	@UiField
	ActionLink aCancelBooking;
	@UiField
	ActionLink aUndoCancelBooking;
	@UiField
	ActionLink aMemberCPD;

	private DelegateDto delegate;
	private EventDto event;

	public void initDisplay() {
		if (AppContext.isCurrentUserEventEdit() || AppContext.isCurrentUserFinanceEdit()) {
			divAction.removeStyleName("hide");
		} else {
			divAction.addStyleName("hide");
		}
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
		aUndoCancelBooking.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				undoCancellation();
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
				UploadContext ctx = new UploadContext("getreport");
				ctx.setContext("bookingRefId", delegate.getBookingRefId());
				ctx.setAction(UPLOADACTION.GETPROFORMA);
				Window.open(ctx.toUrl(), "Get Proforma", null);
			}
		});

		if (delegate != null) {
			final String editUrl = "#eventBooking;eventId=" + event.getRefId() + ";bookingId="
					+ delegate.getBookingRefId() + ";byPass=true";
			// aEditBooking.setHref(editUrl);
			aEditBooking.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Window.open(editUrl, "Edit Booking", null);
				}
			});

			aCancelBooking.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					AppManager.showPopUp("Confirm",
							"Are you sure you want to cancel this booking - both the sponsor and the delegates will be notified.",
							new OnOptionSelected() {
								@Override
								public void onSelect(String name) {
									if (name.equals("Cancel Entire Booking")) {
										AppContext.fireEvent(new EditModelEvent(delegate.getBookingRefId()));
									} else if (name.equals("Cancel Individual Delegate")) {
										AppContext.fireEvent(new EditModelEvent(delegate.getRefId()));
									}
								}
							}, "Cancel Entire Booking", "Cancel Individual Delegate");
				}
			});
		}

	}

	protected void undoCancellation() {
		AppManager.showPopUp("Confirm", "Undo this cancellation?", new OnOptionSelected() {
			@Override
			public void onSelect(String name) {
				if (name.equals("Confirm")) {
					delegate.setIsBookingActive(1);
					AppContext.fireEvent(new EditModelEvent(delegate));
				}
			}
		}, "Confirm", "Cancel");
	}

	protected void updatePaymentInfo() {
		final UpdatePaymentWidget paymentWidget = new UpdatePaymentWidget(delegate);
		AppManager.showPopUp("Update Payment Info", paymentWidget, new OnOptionSelected() {
			@Override
			public void onSelect(String name) {
				if (name.equals("Save")) {
					AppContext.fireEvent(new EditModelEvent(paymentWidget.getDelegate()));
				}
			}
		}, "Save", "Cancel");
	}

	protected void onAttendanceChanged(final AttendanceStatus attendanceStatus) {
		AppManager.showPopUp("Confirm",
				"Confirm " + delegate.getFullName() + " " + attendanceStatus.getDisplayName() + " this event?",
				new OnOptionSelected() {
					@Override
					public void onSelect(String name) {
						if (name.equals("Confirm")) {
							delegate.setAttendance(attendanceStatus);
							// setAttendance(delegate.getAttendance());
							AppContext.fireEvent(new EditModelEvent(delegate));
						}
					}
				}, "Confirm", "Cancel");

	}

	public DelegateTableRow(final DelegateDto delegate, EventDto event) {
		initWidget(uiBinder.createAndBindUi(this));
		this.delegate = delegate;
		this.event = event;

		// Ensure that you instanciate all variables before calling the next
		// method!
		initDisplay();
		divBookingDate.getElement().setInnerText(DateUtils.READABLETIMESTAMP.format(delegate.getCreatedDate()));
		if (delegate.getCompanyName() != null) {
			ActionLink invoiceLink = new ActionLink(delegate.getInvoiceNo());
			invoiceLink.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					UploadContext ctx = new UploadContext("getreport");
					ctx.setContext("bookingRefId", delegate.getBookingRefId());
					ctx.setAction(UPLOADACTION.GETPROFORMA);
					Window.open(ctx.toUrl(), "Get Proforma", null);
				}
			});
			divSponsorNames.getElement().setInnerHTML(delegate.getCompanyName() + " (" + invoiceLink + ")");
		}

		if (delegate.getErn() != null) {
			divErnNo.getElement().setInnerText(delegate.getErn());
		}
		divContactName.getElement().setInnerHTML(delegate.getContactName() + "<br/><small class='text-muted'>"
				+ delegate.getContactEmail() + "</small>");

		InlineLabel spnIsMember = new InlineLabel();
		if (delegate.getMemberNo() != null) {
			spnIsMember.setStyleName("label label-info");
			spnIsMember.setText("M");
		}

		InlineLabel spnMemberNo = new InlineLabel();
		if (delegate.getMemberNo() != null) {
			spnMemberNo.getElement().setInnerText(" - " + delegate.getMemberNo());
		}

		if (delegate.getFullName() != null) {
			divDelegateNames.setInnerHTML(delegate.getFullName() + spnMemberNo + " " + spnIsMember);
		} else {
			divDelegateNames.setInnerText((delegate.getTitle() == null ? "" : delegate.getTitle() + " ")
					+ (delegate.getSurname() == null ? "" : delegate.getSurname() + " ")
					+ (delegate.getOtherNames() == null ? "" : delegate.getOtherNames() + " "));
		}

		if (delegate.getHotel() != null) {
			divAccomodation.add(new InlineLabel(delegate.getHotel() + ""));
		} else {
			divAccomodation.getElement().setInnerText("None");
		}

		if (delegate.getIsBookingActive() == 1) {
			spnBookingStatus.setClassName("label label-success");
			spnBookingStatus.setInnerText("Active");
		} else {
			spnBookingStatus.setClassName("label label-danger");
			spnBookingStatus.setInnerText("Cancelled");
		}

		if (delegate.getMemberRefId() != null) {
			// aMemberCPD.setHref("#cpdmgt;p=memberCPD;refId=" +
			// delegate.getMemberRefId());
			aMemberCPD.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					MemberDto member = new MemberDto();
					member.setFullName(delegate.getFullName());
					member.setMemberNo(delegate.getMemberNo());
					member.setRefId(delegate.getMemberRefId());
					AppContext.fireEvent(new MemberCPDEvent(member));
				}
			});

		} else {

		}

		determinePaymentStatus(delegate.getBookingPaymentStatus(), delegate.getDelegatePaymentStatus());
		setAttendance(delegate.getAttendance());
		if (event.getType() != null) {
			setActionButtons(EventType.valueOf(event.getType()), delegate);
		}
	}

	private void setActionButtons(EventType eventType, DelegateDto delegate) {
		boolean isUpdatePaymentVisible = (AppContext.isCurrentUserFinanceEdit() && delegate.getIsBookingActive() == 1
				? true : false);

		boolean isEditBookingVisible = (AppContext.isCurrentUserEventEdit()
				&& delegate.getAttendance() == AttendanceStatus.NOTATTENDED && delegate.getIsBookingActive() == 1 ? true
						: false);

		boolean isCancelBookingVisible = (AppContext.isCurrentUserEventEdit()
				&& delegate.getAttendance() == AttendanceStatus.NOTATTENDED && delegate.getIsBookingActive() == 1 ? true
						: false);

		boolean isUndoCancelVisible = (AppContext.isCurrentUserEventEdit()
				&& delegate.getAttendance() == AttendanceStatus.NOTATTENDED && delegate.getIsBookingActive() == 0 ? true
						: false);

		boolean isAttendedVisible = (eventType != EventType.COURSE && AppContext.isCurrentUserEventEdit()
				&& delegate.getAttendance() == AttendanceStatus.NOTATTENDED && delegate.getIsBookingActive() == 1 ? true
						: false);

		boolean isNotAttendedVisible = (eventType != EventType.COURSE && AppContext.isCurrentUserEventEdit()
				&& delegate.getAttendance() == AttendanceStatus.ATTENDED && delegate.getIsBookingActive() == 1 ? true
						: false);

		boolean isEnrolVisible = (eventType == EventType.COURSE && AppContext.isCurrentUserEventEdit()
				&& (delegate.getAttendance() == AttendanceStatus.NOTENROLLED
						|| delegate.getAttendance() == AttendanceStatus.NOTATTENDED)
				&& delegate.getIsBookingActive() == 1 ? true : false);

		// boolean isMemberCPDVisible = (delegate.getMemberRefId() != null ?
		// true : false);

		aUpdatePayment.setVisible(isUpdatePaymentVisible);
		aEditBooking.setVisible(isEditBookingVisible);
		aCancelBooking.setVisible(isCancelBookingVisible);
		aUndoCancelBooking.setVisible(isUndoCancelVisible);
		aAttended.setVisible(isAttendedVisible);
		aNotAttended.setVisible(isNotAttendedVisible);
		aEnrol.setVisible(isEnrolVisible);
		// aMemberCPD.setVisible(isMemberCPDVisible);
	}

	private void setAttendance(AttendanceStatus attendance) {
		if (attendance != null) {
			if (attendance == AttendanceStatus.NOTATTENDED || attendance == AttendanceStatus.NOTENROLLED) {
				spnAttendance.setClassName("fa fa-times");
			} else {
				spnAttendance.setClassName("fa fa-check");
			}
		}
	}

	private void determinePaymentStatus(PaymentStatus bookingPaymentStatus, PaymentStatus delegatePaymentStatus) {
		if (bookingPaymentStatus != null) {
			if (bookingPaymentStatus == PaymentStatus.PAID || bookingPaymentStatus == PaymentStatus.Credit) {
				setPaymentStatus(bookingPaymentStatus);
			} else {
				setPaymentStatus(delegatePaymentStatus);
			}
		}
	}

	private void setPaymentStatus(PaymentStatus paymentStatus) {
		if (paymentStatus == PaymentStatus.NOTPAID || paymentStatus == null) {
			spnPaymentStatus.setClassName("fa fa-times");
		} else if (paymentStatus == PaymentStatus.Credit) {
			spnPaymentStatus.setClassName("fa fa-check");
			spnPaymentStatus.setInnerHTML("<br/><span class='text-muted' style='font-size:11px;'>Credit</span>");
		} else if (paymentStatus == PaymentStatus.PAID) {
			spnPaymentStatus.setClassName("fa fa-check");
		}
	}
}
