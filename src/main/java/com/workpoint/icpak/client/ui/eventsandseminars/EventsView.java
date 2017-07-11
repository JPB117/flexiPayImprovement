package com.workpoint.icpak.client.ui.eventsandseminars;

import java.util.List;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.model.UploadContext;
import com.workpoint.icpak.client.model.UploadContext.UPLOADACTION;
import com.workpoint.icpak.client.ui.AppManager;
import com.workpoint.icpak.client.ui.OnOptionSelected;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.eventsandseminars.csvimport.CsvImport;
import com.workpoint.icpak.client.ui.eventsandseminars.delegates.row.DelegateTableRow;
import com.workpoint.icpak.client.ui.eventsandseminars.delegates.table.DelegatesTable;
import com.workpoint.icpak.client.ui.eventsandseminars.header.EventsHeader;
import com.workpoint.icpak.client.ui.eventsandseminars.row.EventsTableRow;
import com.workpoint.icpak.client.ui.eventsandseminars.table.EventsTable;
import com.workpoint.icpak.client.ui.upload.custom.Uploader;
import com.workpoint.icpak.client.ui.util.NumberUtils;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.BookingStatus;
import com.workpoint.icpak.shared.model.EventSummaryDto;
import com.workpoint.icpak.shared.model.events.AccommodationDto;
import com.workpoint.icpak.shared.model.events.BookingSummaryDto;
import com.workpoint.icpak.shared.model.events.DelegateDto;
import com.workpoint.icpak.shared.model.events.EventDto;

public class EventsView extends ViewImpl implements EventsPresenter.IEventsView {

	private final Widget widget;

	@UiField
	HTMLPanel container;

	@UiField
	HTMLPanel panelEvents;

	@UiField
	DivElement divBookings;
	@UiField
	DivElement divSummary;

	@UiField
	HTMLPanel panelEventDrillDown;
	@UiField
	SpanElement spnEventTitle;
	@UiField
	EventsHeader headerContainer;
	@UiField
	EventsTable tblView;
	@UiField
	DelegatesTable tblDelegates;
	@UiField
	ActionLink aCreateBooking;
	@UiField
	ActionLink aSyncWithServer;
	@UiField
	ActionLink aBookingstab;
	@UiField
	ActionLink aSummarytab;
	@UiField
	ActionLink aImportCsv;

	@UiField
	Element elTotalBooking;
	@UiField
	Element divTotalBookingMetric;

	@UiField
	Element elTotalMembers;
	@UiField
	Element divMembersMetric;

	@UiField
	Element elTotalNonMembers;
	@UiField
	Element divNonMemberMetric;

	@UiField
	Element divPaymentMetric;
	@UiField
	Element divAccomodationMetric;
	@UiField
	Element divCancellationMetric;
	@UiField
	Element divAttendanceMetric;

	@UiField
	Element liSummary;
	@UiField
	Element liBooking;

	@UiField
	Element spnPaid;
	@UiField
	Element spnAccomodated;
	@UiField
	Element spnCancellation;
	@UiField
	Element elMpesaPayments;
	@UiField
	Element elCardPayments;
	@UiField
	Element elCreditPayments;
	@UiField
	Element elReceiptPayments;
	@UiField
	Element elOfflinePayments;
	@UiField
	Element spnTotalActive;
	@UiField
	Element spnTotalActiveMembers;
	@UiField
	Element spnTotalActiveNonMembers;
	@UiField
	Element spnAttendance;
	@UiField
	ActionLink aRefresh;
	private EventDto event;
	final CsvImport csvImport = new CsvImport();

	private BookingSummaryDto summary;

	public interface Binder extends UiBinder<Widget, EventsView> {
	}

	@Inject
	public EventsView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		showAdvancedView(false);

		tblDelegates.getDownloadPDFLink().addStyleName("hide");
		tblDelegates.getDownloadXLSLink().removeStyleName("hide");

		tblDelegates.getDownloadXLSLink().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final UploadContext ctx = new UploadContext("getreport");
				ctx.setContext("eventRefId", EventsView.this.event.getRefId());
				ctx.setContext("docType", "xls");
				ctx.setAction(UPLOADACTION.GETDELEGATESREPORT);
				AppManager.showPopUp("Update PhoneNumbers",
						"Do you want to update delegate phoneNumbers from the Members Database?",
						new OnOptionSelected() {
							@Override
							public void onSelect(String name) {
								if (name.equals("Yes")) {
									ctx.setContext("updatePhones", "true");
									Window.open(ctx.toUrl(), "", null);
								} else {
									Window.open(ctx.toUrl(), "", null);
								}
							}
						}, "Yes", "No");

			}
		});

		aSyncWithServer.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				UploadContext ctx = new UploadContext("getreport");
				ctx.setContext("eventRefId", EventsView.this.event.getRefId());
				ctx.setAction(UPLOADACTION.SYNCTOSERVER);
				Window.open(ctx.toUrl(), "", null);
			}
		});

		aImportCsv.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				csvImport.setBookingSummary(summary);
				AppManager.showPopUp("Upload Csv", csvImport, new OnOptionSelected() {
					@Override
					public void onSelect(String name) {

					}
				}, "Done");
			}
		});

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
		tblView.clearRows();
		for (EventDto event : events) {
			tblView.createRow(new EventsTableRow(event));
		}
	}

	@Override
	public void bindEvent(EventDto event) {
		this.event = event;
		csvImport.setUploadContext(EventsView.this.event.getRefId());
		spnEventTitle.setInnerText(event.getName());
		if (AppContext.isCurrentUserEventEdit()) {
			aCreateBooking.setHref("#eventBooking;eventId=" + event.getRefId() + ";byPass=true;");
		}
		aBookingstab.setHref("#events;eventId=" + event.getRefId() + ";page=booking");
		aSummarytab.setHref("#events;eventId=" + event.getRefId() + ";page=summary");
	}

	@Override
	public void bindDelegates(List<DelegateDto> delegates) {
		tblDelegates.clearRows();
		for (DelegateDto dto : delegates) {
			tblDelegates.createRow(new DelegateTableRow(dto, event));
		}
	}

	@Override
	public void bindBookingSummary(BookingSummaryDto summary) {
		this.summary = summary;
		if (summary != null) {
			Integer activeDelegates = summary.getTotalDelegates() - summary.getTotalCancelled();

			Integer totalNonMembers = 0;
			int totalActiveMembers = 0;
			int totalActiveNonMembers = 0;
			elTotalBooking.setInnerText(NumberUtils.NUMBERFORMAT.format(summary.getTotalDelegates()) + "");
			spnTotalActive.setInnerText(NumberUtils.NUMBERFORMAT.format(activeDelegates) + " Active");

			// Members
			if (summary.getTotalMembers() != null) {
				totalNonMembers = summary.getTotalDelegates() - summary.getTotalMembers();
				totalActiveMembers = summary.getTotalMembers() - summary.getTotalCancelledMembers();
				elTotalMembers.setInnerText(NumberUtils.NUMBERFORMAT.format(summary.getTotalMembers()) + "");
				spnTotalActiveMembers.setInnerText(NumberUtils.NUMBERFORMAT.format(totalActiveMembers) + " Active");

			}

			// Non-Members
			if (summary.getTotalNonMembers() != null) {
				totalActiveNonMembers = summary.getTotalNonMembers() - summary.getTotalCancelledNonMembers();
				elTotalNonMembers.setInnerText(NumberUtils.NUMBERFORMAT.format(totalNonMembers) + "");
				spnTotalActiveNonMembers
						.setInnerText(NumberUtils.NUMBERFORMAT.format(totalActiveNonMembers) + " Active");
			}

			// Payment metric
			if (summary.getTotalPaid() != null) {
				Integer percentagePaid = (summary.getTotalPaid() * 100) / summary.getTotalDelegates();
				String converted = convertToReadableProgress(percentagePaid);
				divPaymentMetric.setClassName("progress-bar progress-bar-green progress-" + converted);
				spnPaid.setInnerText(percentagePaid + "%");
				divPaymentMetric.setTitle(summary.getTotalPaid() + " out of " + activeDelegates + " have paid.");

			}

			/* TOTAL Active METRIC */
			if (summary.getTotalDelegates() != null) {
				Integer percentagePaid = (activeDelegates * 100) / summary.getTotalDelegates();
				String converted = convertToReadableProgress(percentagePaid);
				divTotalBookingMetric.setClassName("progress-bar progress-bar-green progress-" + converted);
				divTotalBookingMetric
						.setTitle(activeDelegates + " out of " + summary.getTotalDelegates() + " are active.");
			}

			// Member Active metric
			if (summary.getTotalCancelledMembers() != null) {
				int activeMembers = summary.getTotalMembers() - summary.getTotalCancelledMembers();
				Integer percentageActiveMembers = (activeMembers * 100) / summary.getTotalMembers();
				String converted = convertToReadableProgress(percentageActiveMembers);
				divMembersMetric.setClassName("progress-bar progress-bar-green progress-" + converted);
				divMembersMetric.setTitle(activeMembers + " out of " + summary.getTotalMembers() + " are active.");
			}

			// Non-member Payment Metric
			if (summary.getTotalPaidMembers() != null) {
				int activeNonMembers = summary.getTotalNonMembers() - summary.getTotalCancelledNonMembers();
				Integer percentageNonPaidMembers = (activeNonMembers * 100) / summary.getTotalNonMembers();
				String converted = convertToReadableProgress(percentageNonPaidMembers);
				divNonMemberMetric.setClassName("progress-bar progress-bar-green progress-" + converted);
				divNonMemberMetric.setTitle(activeNonMembers + " out of" + totalNonMembers + " are active.");
			}

			// Accomodation metric
			if (summary.getTotalWithAccomodation() != null) {
				Integer percentageAccomodated = (summary.getTotalWithAccomodation() * 100)
						/ summary.getTotalDelegates();
				String converted = convertToReadableProgress(percentageAccomodated);
				divAccomodationMetric.setClassName("progress-bar progress-bar-green progress-" + converted);
				spnAccomodated.setInnerText(percentageAccomodated + "%");
				divAccomodationMetric.setTitle(
						summary.getTotalWithAccomodation() + " out of " + activeDelegates + " have accomodation.");
			}

			// Cancellation metric
			if (summary.getTotalCancelled() != null) {
				Integer percentageCancelled = (summary.getTotalCancelled() * 100) / summary.getTotalDelegates();
				String converted = convertToReadableProgress(percentageCancelled);
				divCancellationMetric.setClassName("progress-bar progress-bar-green progress-" + converted);
				spnCancellation.setInnerText(percentageCancelled + "%");
				divCancellationMetric.setTitle(
						summary.getTotalCancelled() + " out of " + activeDelegates + " have cancelled their booking.");
			}

			// Attendance metric
			if (summary.getTotalAttended() != null) {
				Integer percentageAttended = (summary.getTotalAttended() * 100) / summary.getTotalDelegates();
				String converted = convertToReadableProgress(percentageAttended);
				divAttendanceMetric.setClassName("progress-bar progress-bar-green progress-" + converted);
				spnAttendance.setInnerText(percentageAttended + "%");
				divAttendanceMetric.setTitle(
						summary.getTotalAttended() + " out of " + activeDelegates + " have been marked as attended.");
			}

			// Payments Breakdown
			elMpesaPayments.setInnerText(NumberUtils.NUMBERFORMAT.format(summary.getTotalMpesaPayments()) + "");
			elCardPayments.setInnerText(NumberUtils.NUMBERFORMAT.format(summary.getTotalCardsPayment()) + "");
			elCreditPayments.setInnerText(NumberUtils.NUMBERFORMAT.format(summary.getTotalCredit()) + "");
			elReceiptPayments.setInnerText(NumberUtils.NUMBERFORMAT.format(summary.getTotalReceiptPayment()) + "");
			elOfflinePayments.setInnerText(NumberUtils.NUMBERFORMAT.format(summary.getTotalOfflinePayment()) + "");

		}
	}

	private String convertToReadableProgress(Integer percentagePaid) {
		String readable = "";
		if (percentagePaid > 10) {
			if (!(String.valueOf(percentagePaid).endsWith("0"))) {
				readable = (String.valueOf(percentagePaid)).substring(0, 1) + "0";
			} else {
				readable = String.valueOf(percentagePaid);
			}
		} else if (percentagePaid == 0) {
			readable = "0";
		} else {
			readable = "10";
		}
		return readable;
	}

	@Override
	public PagingPanel getEventsPagingPanel() {
		return tblView.getPagingPanel();
	}

	@Override
	public PagingPanel getDelegatesPagingPanel() {
		return tblDelegates.getPagingPanel();
	}

	@Override
	public void bindEventSummary(EventSummaryDto eventSummary) {
		headerContainer.setCounts(eventSummary.getClosed() + eventSummary.getOpen(), eventSummary.getClosed(),
				eventSummary.getOpen());
	}

	public String getSearchValue() {
		return tblView.getSearchValue();
	}

	public HasValueChangeHandlers<String> getDelegateSearchValueChangeHandler() {
		return tblDelegates.getDelegateSearchValueChangeHandler();
	}

	public String getDelegateSearchValue() {
		return tblDelegates.getDelegateSearchValue();
	}

	@Override
	public HasValueChangeHandlers<String> getSearchValueChangeHander() {
		return tblView.getSearchKeyDownHander();
	}

	public HasKeyDownHandlers getDelegateSearchKeyDownHandler() {
		return tblDelegates.getSearchKeyDownHandler();
	}

	public HasKeyDownHandlers getEventsSearchKeyDownHandler() {
		return tblView.getSearchKeyDownHandler();
	}

	@Override
	public HasValueChangeHandlers<AccommodationDto> getAccomodationValueChangeHandler() {
		return tblDelegates.getAccomodationValueChangeHandler();
	}

	@Override
	public HasValueChangeHandlers<BookingStatus> getBookingStatusValueChangeHandler() {
		return tblDelegates.getBookingStatusValueChangeHandler();
	}

	public DropDownList<AccommodationDto> getLstAccomodation() {
		return tblDelegates.getLstAccomodation();
	}

	public void setActiveTab(String page) {
		if (page.equals("summary")) {
			divSummary.addClassName("active");
			divBookings.removeClassName("active");
			liBooking.removeClassName("active");
			liSummary.addClassName("active");
		} else if (page.equals("booking")) {
			divSummary.removeClassName("active");
			divBookings.addClassName("active");
			liBooking.addClassName("active");
			liSummary.removeClassName("active");
		}
	}

	public Uploader getCsvUploader() {
		return csvImport.getUploader();
	}

	public HasClickHandlers getRefreshButton() {
		return aRefresh;
	}

}
