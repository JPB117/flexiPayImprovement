package com.workpoint.icpak.client.ui.eventsandseminars;

import java.util.List;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
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
import com.workpoint.icpak.client.ui.component.PagingConfig;
import com.workpoint.icpak.client.ui.component.PagingLoader;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.cpd.table.row.CPDTableRow.TableActionType;
import com.workpoint.icpak.client.ui.events.EditModelEvent;
import com.workpoint.icpak.client.ui.events.EditModelEvent.EditModelHandler;
import com.workpoint.icpak.client.ui.events.TableActionEvent;
import com.workpoint.icpak.client.ui.events.TableActionEvent.TableActionHandler;
import com.workpoint.icpak.client.ui.eventsandseminars.resendProforma.ResendModel;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent;
import com.workpoint.icpak.client.ui.events.ProcessingEvent;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.security.AdminGateKeeper;
import com.workpoint.icpak.shared.api.EventsResource;
import com.workpoint.icpak.shared.model.EventSummaryDto;
import com.workpoint.icpak.shared.model.events.BookingDto;
import com.workpoint.icpak.shared.model.events.DelegateDto;
import com.workpoint.icpak.shared.model.events.EventDto;

public class EventsPresenter extends
		Presenter<EventsPresenter.IEventsView, EventsPresenter.IEventsProxy>
		implements EditModelHandler, TableActionHandler {

	public interface IEventsView extends View {

		void showAdvancedView(boolean show);

		void bindEvents(List<EventDto> events);

		void bindEvent(EventDto event);

		void bindDelegates(List<DelegateDto> delegates);

		PagingPanel getEventsPagingPanel();

		PagingPanel getBookingsPagingPanel();

		void bindEventSummary(EventSummaryDto result);

		HasValueChangeHandlers<String> getSearchValueChangeHander();

		String getSearchValue();

		String getDelegateSearchValue();

		HasValueChangeHandlers<String> getDelegateSearchValueChangeHandler();
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.events)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface IEventsProxy extends TabContentProxyPlace<EventsPresenter> {
	}

	ValueChangeHandler<String> eventsValueChangeHandler = new ValueChangeHandler<String>() {
		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
			loadEvents(getView().getSearchValue().trim());
		}
	};

	ValueChangeHandler<String> delegateTableValueChangeHandler = new ValueChangeHandler<String>() {
		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
			loadDelegatesCount(getView().getDelegateSearchValue().trim());
		}
	};

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(AdminGateKeeper gateKeeper) {
		String tabName = "Events & Courses";
		TabDataExt data = new TabDataExt(tabName, "fa fa-tags", 2, gateKeeper,
				true);
		return data;
	}

	private ResourceDelegate<EventsResource> eventsDelegate;
	private String eventId;

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
		addRegisteredHandler(EditModelEvent.TYPE, this);
		addRegisteredHandler(TableActionEvent.TYPE, this);
		
		getView().getSearchValueChangeHander().addValueChangeHandler(
				eventsValueChangeHandler);
		getView().getDelegateSearchValueChangeHandler().addValueChangeHandler(
				delegateTableValueChangeHandler);

	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		eventId = request.getParameter("eventId", null);

		// Load Event Details to View
		if (eventId != null) {
			getView().showAdvancedView(true);
		} else {
			getView().showAdvancedView(false);
		}

		loadData();
	}

	private void loadData() {
		fireEvent(new ProcessingEvent());
		eventsDelegate.withCallback(
				new AbstractAsyncCallback<EventSummaryDto>() {
					public void onSuccess(EventSummaryDto result) {
						getView().bindEventSummary(result);
					};
				}).getEventsSummary();

		if (eventId != null) {
			// Load Bookings
			eventsDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
				@Override
				public void onSuccess(Integer aCount) {
					fireEvent(new ProcessingCompletedEvent());
					PagingPanel panel = getView().getBookingsPagingPanel();
					panel.setTotal(aCount);
					PagingConfig config = panel.getConfig();
					config.setPAGE_LIMIT(100);
					loadDelegates(config.getOffset(), config.getLimit(), "");
				}
			}).bookings(eventId).getCount();

			eventsDelegate.withCallback(new AbstractAsyncCallback<EventDto>() {
				@Override
				public void onSuccess(EventDto event) {
					fireEvent(new ProcessingCompletedEvent());
					getView().bindEvent(event);
				}
			}).getById(eventId);

		} else {
			// Load Events
			eventsDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
				@Override
				public void onSuccess(Integer aCount) {
					fireEvent(new ProcessingCompletedEvent());
					PagingPanel panel = getView().getEventsPagingPanel();
					panel.setTotal(aCount);
					PagingConfig config = panel.getConfig();
					config.setPAGE_LIMIT(100);
					loadEvents(config.getOffset(), 100, "");
				}
			}).getCount();
		}
	}

	protected void loadDelegates(int offset, int limit, String searchTerm) {
		fireEvent(new ProcessingEvent());
		eventsDelegate
				.withCallback(new AbstractAsyncCallback<List<DelegateDto>>() {
					@Override
					public void onSuccess(List<DelegateDto> delegates) {
						fireEvent(new ProcessingCompletedEvent());
						getView().bindDelegates(delegates);
					}
				}).delegates(eventId).getAll(offset, limit, searchTerm);
	}

	protected void loadDelegatesCount(final String searchTerm) {
		fireEvent(new ProcessingEvent());
		eventsDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
			@Override
			public void onSuccess(Integer count) {
				fireEvent(new ProcessingCompletedEvent());

				PagingPanel panel = getView().getBookingsPagingPanel();
				panel.setTotal(count);
				PagingConfig config = panel.getConfig();
				config.setPAGE_LIMIT(50);
				loadDelegates(config.getOffset(), config.getLimit(), searchTerm);
				fireEvent(new ProcessingCompletedEvent());
			}
		}).delegates(eventId).getSearchCount(searchTerm);
	}

	private void loadEvents(final String searchTerm) {
		fireEvent(new ProcessingEvent());
		eventsDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
			@Override
			public void onSuccess(Integer aCount) {
				fireEvent(new ProcessingCompletedEvent());

				PagingPanel panel = getView().getEventsPagingPanel();
				panel.setTotal(aCount);
				PagingConfig config = panel.getConfig();

				loadEvents(config.getOffset(), config.getLimit(), searchTerm);
				fireEvent(new ProcessingCompletedEvent());
			}
		}).getSearchCount(searchTerm);
	}

	protected void loadEvents(int offset, int limit, String searchTerm) {
		fireEvent(new ProcessingEvent());
		eventsDelegate.withCallback(
				new AbstractAsyncCallback<List<EventDto>>() {
					@Override
					public void onSuccess(List<EventDto> events) {
						fireEvent(new ProcessingCompletedEvent());
						getView().bindEvents(events);
					}
				}).getAll(offset, limit, searchTerm);
	}

	@Override
	public void onEditModel(EditModelEvent event) {
		if (event.getModel() instanceof DelegateDto) {
			save((DelegateDto) event.getModel());
		}
	}

	private void save(DelegateDto model) {
		assert model.getBookingId() != null && model.getEventRefId() != null;
		fireEvent(new ProcessingEvent());
		eventsDelegate.withCallback(new AbstractAsyncCallback<DelegateDto>() {
			@Override
			public void onSuccess(DelegateDto result) {
				fireEvent(new ProcessingCompletedEvent());
				Window.alert("Successfully updated " + result.getSurname());
			}
		}).bookings(model.getEventRefId())
				.updateDelegate(model.getBookingId(), model.getRefId(), model);
	}

	@Override
	public void onTableAction(TableActionEvent event) {
		if (event.getAction() == TableActionType.RESENDPROFORMA) {
			final ResendModel resendModel = (ResendModel) event.getModel();

			fireEvent(new ProcessingEvent());
			// Resend Proforma for that Booking
			eventsDelegate
					.withCallback(new AbstractAsyncCallback<BookingDto>() {
						@Override
						public void onSuccess(BookingDto booking) {
							fireEvent(new ProcessingCompletedEvent());
							Window.alert("Email sent successfully..");
						}
					})
					.bookings(eventId)
					.resendProforma(resendModel.getEmails(),
							resendModel.getDelegate().getBookingRefId());

		}

	}

}
