package com.workpoint.icpak.client.ui.eventsandseminars;

import java.util.List;

import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.eventsandseminars.delegates.row.DelegateTableRow;
import com.workpoint.icpak.client.ui.eventsandseminars.delegates.table.DelegatesTable;
import com.workpoint.icpak.client.ui.eventsandseminars.header.EventsHeader;
import com.workpoint.icpak.client.ui.eventsandseminars.row.EventsTableRow;
import com.workpoint.icpak.client.ui.eventsandseminars.table.EventsTable;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.EventSummaryDto;
import com.workpoint.icpak.shared.model.events.BookingDto;
import com.workpoint.icpak.shared.model.events.DelegateDto;
import com.workpoint.icpak.shared.model.events.EventDto;

public class EventsView extends ViewImpl implements EventsPresenter.IEventsView {

	private final Widget widget;

	@UiField
	HTMLPanel container;

	@UiField
	HTMLPanel panelEvents;

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

	private EventDto event;

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
				UploadContext ctx = new UploadContext("getreport");
				ctx.setContext("eventRefId", EventsView.this.event.getRefId());
				ctx.setContext("docType", "xls");
				ctx.setAction(UPLOADACTION.GETDELEGATESREPORT);
				Window.open(ctx.toUrl(), "", null);
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
		spnEventTitle.setInnerText(event.getName());
	}

	@Override
	public void bindDelegates(List<DelegateDto> delegates) {
		tblDelegates.clearRows();
		for (DelegateDto dto : delegates) {
			tblDelegates.createRow(new DelegateTableRow(dto, event.getType()));
		}
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
		headerContainer.setCounts(
				eventSummary.getClosed() + eventSummary.getOpen(),
				eventSummary.getClosed(), eventSummary.getOpen());
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

}
