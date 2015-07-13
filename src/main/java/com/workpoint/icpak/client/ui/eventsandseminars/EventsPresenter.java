package com.workpoint.icpak.client.ui.eventsandseminars;

import java.util.List;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.workpoint.icpak.client.place.NameTokens;
import com.workpoint.icpak.client.service.AbstractAsyncCallback;
import com.workpoint.icpak.client.ui.admin.TabDataExt;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.security.AdminGateKeeper;
import com.workpoint.icpak.shared.api.EventsResource;
import com.workpoint.icpak.shared.model.events.BookingDto;
import com.workpoint.icpak.shared.model.events.EventDto;

public class EventsPresenter extends
		Presenter<EventsPresenter.IEventsView, EventsPresenter.IEventsProxy> {

	public interface IEventsView extends View {

		void showAdvancedView(boolean show);

		void bindEvents(List<EventDto> events);

		void bindEvent(EventDto event);

		void bindBookings(List<BookingDto> events);

	}

	@ProxyCodeSplit
	@NameToken(NameTokens.events)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface IEventsProxy extends TabContentProxyPlace<EventsPresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(AdminGateKeeper adminGatekeeper) {
		TabDataExt data = new TabDataExt("Events and Seminars", "fa fa-tags",
				2, adminGatekeeper, true);
		return data;
	}

	private ResourceDelegate<EventsResource> eventsDelegate;

	@Inject
	public EventsPresenter(final EventBus eventBus, final IEventsView view,
			final IEventsProxy proxy,
			ResourceDelegate<EventsResource> eventsDelegate) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
		this.eventsDelegate = eventsDelegate;
	}

	@Override
	protected void onBind() {
		super.onBind();
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		clear();
		final String eventId = request.getParameter("eventId", null);

		// Load Event Details to View
		if (eventId!=null) {
			getView().showAdvancedView(true);
		} else {
			getView().showAdvancedView(false);
		}

		loadData(eventId);
	}

	private void clear() {
		// TODO Auto-generated method stub

	}

	private void loadData(String eventId) {

		if (eventId != null) {

			eventsDelegate.withCallback(new AbstractAsyncCallback<EventDto>() {
				@Override
				public void onSuccess(EventDto event) {
					getView().bindEvent(event);					
				}
			}).getById(eventId);
			
			loadBookings(eventId);
		} else {
			eventsDelegate.withCallback(
					new AbstractAsyncCallback<List<EventDto>>() {
						@Override
						public void onSuccess(List<EventDto> events) {
							getView().bindEvents(events);
						}
					}).getAll(0, 100);
		}
	}

	private void loadBookings(String eventId) {
		eventsDelegate.withCallback(
				new AbstractAsyncCallback<List<BookingDto>>() {
					@Override
					public void onSuccess(List<BookingDto> events) {
						getView().bindBookings(events);
					}
				}).bookings(eventId)
				.getAll(0, 100);
	}

	protected void save() {
	}

	@Override
	protected void onReset() {
		super.onReset();
	}

}
