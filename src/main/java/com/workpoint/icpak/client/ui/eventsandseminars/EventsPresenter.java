package com.workpoint.icpak.client.ui.eventsandseminars;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
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
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.workpoint.icpak.client.place.NameTokens;
import com.workpoint.icpak.client.service.AbstractAsyncCallback;
import com.workpoint.icpak.client.ui.AppManager;
import com.workpoint.icpak.client.ui.OptionControl;
import com.workpoint.icpak.client.ui.admin.TabDataExt;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.PagingConfig;
import com.workpoint.icpak.client.ui.component.PagingLoader;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.cpd.admin.CPDManagementPresenter;
import com.workpoint.icpak.client.ui.events.AfterSaveEvent;
import com.workpoint.icpak.client.ui.events.EditModelEvent;
import com.workpoint.icpak.client.ui.events.EditModelEvent.EditModelHandler;
import com.workpoint.icpak.client.ui.events.FullScreenEvent;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent;
import com.workpoint.icpak.client.ui.events.ProcessingEvent;
import com.workpoint.icpak.client.ui.events.TableActionEvent;
import com.workpoint.icpak.client.ui.events.TableActionEvent.TableActionHandler;
import com.workpoint.icpak.client.ui.events.cpd.MemberCPDEvent;
import com.workpoint.icpak.client.ui.events.cpd.MemberCPDEvent.MemberCPDHandler;
import com.workpoint.icpak.client.ui.eventsandseminars.resendProforma.ResendModel;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.security.EventsGateKeeper;
import com.workpoint.icpak.client.ui.upload.custom.Uploader;
import com.workpoint.icpak.shared.api.EventsResource;
import com.workpoint.icpak.shared.model.BookingStatus;
import com.workpoint.icpak.shared.model.EventSummaryDto;
import com.workpoint.icpak.shared.model.TableActionType;
import com.workpoint.icpak.shared.model.events.AccommodationDto;
import com.workpoint.icpak.shared.model.events.BookingDto;
import com.workpoint.icpak.shared.model.events.BookingSummaryDto;
import com.workpoint.icpak.shared.model.events.DelegateDto;
import com.workpoint.icpak.shared.model.events.EventDto;

import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;

public class EventsPresenter extends Presenter<EventsPresenter.IEventsView, EventsPresenter.IEventsProxy>
		implements EditModelHandler, TableActionHandler, MemberCPDHandler {

	public interface IEventsView extends View {

		void showAdvancedView(boolean show);

		void bindEvents(List<EventDto> events);

		void bindEvent(EventDto event);

		void bindDelegates(List<DelegateDto> delegates);

		PagingPanel getEventsPagingPanel();

		PagingPanel getDelegatesPagingPanel();

		void bindEventSummary(EventSummaryDto result);

		HasValueChangeHandlers<String> getSearchValueChangeHander();

		String getSearchValue();

		String getDelegateSearchValue();

		HasValueChangeHandlers<String> getDelegateSearchValueChangeHandler();

		HasKeyDownHandlers getDelegateSearchKeyDownHandler();

		HasKeyDownHandlers getEventsSearchKeyDownHandler();

		HasValueChangeHandlers<AccommodationDto> getAccomodationValueChangeHandler();

		HasValueChangeHandlers<BookingStatus> getBookingStatusValueChangeHandler();

		DropDownList<AccommodationDto> getLstAccomodation();

		void setActiveTab(String page);

		void bindBookingSummary(BookingSummaryDto summary);

		Uploader getCsvUploader();

		HasClickHandlers getRefreshButton();
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.events)
	@UseGatekeeper(EventsGateKeeper.class)
	public interface IEventsProxy extends TabContentProxyPlace<EventsPresenter> {
	}

	private String delegateSearchTerm = "";
	protected String eventSearchTerm = "";
	private static String accomodationRefId = "";
	private static String bookingStatus = "1";
	private static String page = "summary";
	private PlaceManager placeManager;
	private ResourceDelegate<EventsResource> eventsDelegate;
	private String eventId;
	private BookingSummaryDto bookingSummary;

	@Inject
	CPDManagementPresenter mgmntPresenter;

	ValueChangeHandler<String> eventsValueChangeHandler = new ValueChangeHandler<String>() {
		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
			loadEvents(getView().getSearchValue().trim());
		}
	};

	KeyDownHandler delegateKeyHandler = new KeyDownHandler() {
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				delegateSearchTerm = getView().getDelegateSearchValue().trim();
				loadDelegatesCount(getView().getDelegateSearchValue().trim());
			}
		}
	};

	KeyDownHandler eventsKeyHandler = new KeyDownHandler() {
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				eventSearchTerm = getView().getSearchValue().trim();
				loadEvents(getView().getSearchValue().trim());
			}
		}
	};

	ValueChangeHandler<AccommodationDto> accomodationValueChangeHandler = new ValueChangeHandler<AccommodationDto>() {
		@Override
		public void onValueChange(ValueChangeEvent<AccommodationDto> event) {
			if (event.getValue() != null) {
				accomodationRefId = event.getValue().getRefId();
			} else {
				accomodationRefId = "";
			}
			Map<String, String> params = new HashMap<String, String>();
			params.put("accomodationRefId", accomodationRefId);
			params.put("eventId", eventId);
			params.put("bookingStatus", bookingStatus);
			params.put("page", page);
			PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(NameTokens.events).with(params).build();
			placeManager.revealPlace(placeRequest);
		}
	};

	ValueChangeHandler<BookingStatus> bookingStatusValueChangeHandler = new ValueChangeHandler<BookingStatus>() {
		@Override
		public void onValueChange(ValueChangeEvent<BookingStatus> event) {
			if (event.getValue() != null) {
				bookingStatus = event.getValue().getDisplayName();
				if (bookingStatus.equals("Active")) {
					bookingStatus = "1";
				} else if (bookingStatus.equals("Cancelled")) {
					bookingStatus = "0";
				}
			} else {
				bookingStatus = "";
			}
			Map<String, String> params = new HashMap<String, String>();
			params.put("accomodationRefId", accomodationRefId);
			params.put("eventId", eventId);
			params.put("bookingStatus", bookingStatus);
			params.put("page", page);
			PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(NameTokens.events).with(params).build();
			placeManager.revealPlace(placeRequest);
		}
	};
	private boolean hasEventChanged = false;

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(EventsGateKeeper gateKeeper) {
		String tabName = "Events & Courses";
		TabDataExt data = new TabDataExt(tabName, "fa fa-tags", 2, gateKeeper, true);
		return data;
	}

	@Inject
	public EventsPresenter(final EventBus eventBus, final IEventsView view, final IEventsProxy proxy,
			ResourceDelegate<EventsResource> eventsDelegate, final PlaceManager placeManager) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
		this.eventsDelegate = eventsDelegate;
		this.placeManager = placeManager;
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(EditModelEvent.TYPE, this);
		addRegisteredHandler(TableActionEvent.TYPE, this);
		addRegisteredHandler(MemberCPDEvent.getType(), this);

		getView().getDelegateSearchKeyDownHandler().addKeyDownHandler(delegateKeyHandler);
		getView().getEventsSearchKeyDownHandler().addKeyDownHandler(eventsKeyHandler);
		getView().getBookingStatusValueChangeHandler().addValueChangeHandler(bookingStatusValueChangeHandler);
		getView().getAccomodationValueChangeHandler().addValueChangeHandler(accomodationValueChangeHandler);

		getView().getEventsPagingPanel().setLoader(new PagingLoader() {
			@Override
			public void onLoad(int offset, int limit) {
				loadEvents(offset, limit, eventSearchTerm);
			}
		});
		getView().getDelegatesPagingPanel().setLoader(new PagingLoader() {
			@Override
			public void onLoad(int offset, int limit) {
				loadDelegates(offset, limit, delegateSearchTerm);
			}
		});

		getView().getCsvUploader().addOnFinishUploaderHandler(new OnFinishUploaderHandler() {
			@Override
			public void onFinish(IUploader uploader) {
				eventsDelegate.withCallback(new AbstractAsyncCallback<BookingSummaryDto>() {
					@Override
					public void onSuccess(BookingSummaryDto summary) {
						int totalMarkedAsAttended = summary.getTotalAttended() - bookingSummary.getTotalAttended();
						Window.alert("Total Imported:" + totalMarkedAsAttended);
						loadData();
					}
				}).bookings(eventId).getBookingSummary(eventId, "true");
			}
		});

		getView().getRefreshButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new ProcessingEvent());
				eventsDelegate.withCallback(new AbstractAsyncCallback<BookingSummaryDto>() {

					@Override
					public void onSuccess(BookingSummaryDto summary) {
						bookingSummary = summary;
						getView().bindBookingSummary(summary);
						fireEvent(new ProcessingCompletedEvent());
					}
				}).bookings(eventId).getBookingSummary(eventId, "true");
			}
		});
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		hasEventChanged = (eventId != request.getParameter("eventId", "") ? true : false);
		eventId = request.getParameter("eventId", null);
		accomodationRefId = request.getParameter("accomodationRefId", "");
		bookingStatus = request.getParameter("bookingStatus", "");
		page = request.getParameter("page", "summary");

		// Load Event Details to View
		getView().setActiveTab(page);

		if (eventId != null) {
			getView().showAdvancedView(true);
		} else {
			getView().showAdvancedView(false);
		}
		loadData();
	}

	private void loadData() {
		fireEvent(new ProcessingEvent());

		/*
		 * Load a Single Event
		 */
		if (eventId != null) {
			fireEvent(new FullScreenEvent("show"));
			/*
			 * Load the Event - This is the first step
			 */
			eventsDelegate.withCallback(new AbstractAsyncCallback<EventDto>() {
				@Override
				public void onSuccess(EventDto event) {
					getView().bindEvent(event);
				}
			}).getById(eventId);

			/*
			 * If you have navigated to bookings - Load the bookings
			 */
			if (page.equals("booking")) {
				eventsDelegate.withCallback(new AbstractAsyncCallback<List<AccommodationDto>>() {
					@Override
					public void onSuccess(List<AccommodationDto> result) {
						getView().getLstAccomodation().setItems(result, "--Select Accomodation--");
					}
				}).accommodations(eventId).getAll(0, 100);

				eventsDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
					@Override
					public void onSuccess(Integer aCount) {
						PagingPanel panel = getView().getDelegatesPagingPanel();
						panel.setTotal(aCount);
						PagingConfig config = panel.getConfig();
						loadDelegates(config.getOffset(), config.getLimit(), getView().getDelegateSearchValue());
					}
				}).bookings(eventId).getBookingCount(getView().getDelegateSearchValue(), accomodationRefId,
						bookingStatus);

			} else if (page.equals("summary")) {
				fireEvent(new ProcessingEvent());
				eventsDelegate.withCallback(new AbstractAsyncCallback<BookingSummaryDto>() {

					@Override
					public void onSuccess(BookingSummaryDto summary) {
						bookingSummary = summary;
						getView().bindBookingSummary(summary);
						fireEvent(new ProcessingCompletedEvent());
					}
				}).bookings(eventId).getBookingSummary(eventId, "false");

			}
			/*
			 * Load multiple events
			 */
		} else {

			fireEvent(new FullScreenEvent("hide"));
			eventsDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
				@Override
				public void onSuccess(Integer aCount) {
					fireEvent(new ProcessingCompletedEvent());
					PagingPanel panel = getView().getEventsPagingPanel();
					panel.setTotal(aCount);
					PagingConfig config = panel.getConfig();
					loadEvents(config.getOffset(), config.getPAGE_LIMIT(), "");
				}
			}).getCount();
		}
	}

	protected void loadDelegates(int offset, int limit, String searchTerm) {
		fireEvent(new ProcessingEvent());
		// Window.alert(">>" + bookingStatus);
		eventsDelegate.withCallback(new AbstractAsyncCallback<List<DelegateDto>>() {
			@Override
			public void onSuccess(List<DelegateDto> delegates) {
				getView().bindDelegates(delegates);
				fireEvent(new ProcessingCompletedEvent());
			}
		}).delegates(eventId).getAll(offset, limit, searchTerm, accomodationRefId, bookingStatus);
	}

	protected void loadDelegatesCount(final String searchTerm) {
		fireEvent(new ProcessingEvent());
		eventsDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
			@Override
			public void onSuccess(Integer count) {
				PagingPanel panel = getView().getDelegatesPagingPanel();
				panel.setTotal(count);
				PagingConfig config = panel.getConfig();
				config.setPAGE_LIMIT(50);
				loadDelegates(config.getOffset(), config.getLimit(), searchTerm);
			}
		}).delegates(eventId).getSearchCount(searchTerm, accomodationRefId, bookingStatus);
	}

	private void loadEvents(final String searchTerm) {
		fireEvent(new ProcessingEvent());
		eventsDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
			@Override
			public void onSuccess(Integer aCount) {
				PagingPanel panel = getView().getEventsPagingPanel();
				panel.setTotal(aCount);
				PagingConfig config = panel.getConfig();
				loadEvents(config.getOffset(), config.getLimit(), searchTerm);
			}
		}).getSearchCount(searchTerm);
	}

	protected void loadEvents(int offset, int limit, String searchTerm) {
		fireEvent(new ProcessingEvent());
		eventsDelegate.withCallback(new AbstractAsyncCallback<List<EventDto>>() {
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
		} else if (event.getModel() instanceof String) {
			fireEvent(new ProcessingEvent());
			eventsDelegate.withCallback(new AbstractAsyncCallback<Boolean>() {
				@Override
				public void onSuccess(Boolean result) {
					if (result != null) {
						fireEvent(new ProcessingCompletedEvent());
						if (result) {
							fireEvent(new AfterSaveEvent("Cancellation was successful"));
						}
						loadData();
					}
				}
			}).bookings(eventId).cancelBooking((String) event.getModel());
		}
	}

	private void save(final DelegateDto model) {
		assert model.getBookingId() != null && model.getEventRefId() != null;
		fireEvent(new ProcessingEvent());
		eventsDelegate.withCallback(new AbstractAsyncCallback<DelegateDto>() {
			@Override
			public void onSuccess(DelegateDto result) {
				fireEvent(new ProcessingCompletedEvent());
				fireEvent(new AfterSaveEvent("Changes successfully saved!"));
				delegateSearchTerm = result.getContactEmail();
				loadData();
			}
		}).bookings(eventId).updateDelegate(model.getBookingId(), model.getRefId(), model);
	}

	@Override
	public void onTableAction(TableActionEvent event) {
		if (event.getAction() == TableActionType.RESENDPROFORMA) {
			final ResendModel resendModel = (ResendModel) event.getModel();
			fireEvent(new ProcessingEvent());
			// Resend Proforma for that Booking
			eventsDelegate.withCallback(new AbstractAsyncCallback<BookingDto>() {
				@Override
				public void onSuccess(BookingDto booking) {
					fireEvent(new ProcessingCompletedEvent());
					Window.alert("Email sent successfully..");
				}

				@Override
				public void onFailure(Throwable caught) {
					callPopOver();
					super.onFailure(caught);
				}
			}).bookings(eventId).resendProforma(resendModel.getEmails(), resendModel.getDelegate().getBookingRefId());
		}

		if (event.getAction() == TableActionType.ENROLTOLMS) {
			Window.alert("Tesssttt");
			final ResendModel resendModel = (ResendModel) event.getModel();
			// eventsDelegate.withCallback(new AbstractAsyncCallback<String>() {
			// @Override
			// public void onSuccess(String result) {
			// Window.alert(result);
			// }
			// }).enrolToLMS(resendModel.getDelegate().getRefId());

			// eventsDelegate.withCallback(new AbstractAsyncCallback<String>() {
			// @Override
			// public void onSuccess(String result) {
			// Window.alert(result);
			// }
			// }).getSearchCount("Test");
		}

	}

	public void callPopOver() {
		AppManager.showPopUp("Sorry ..", "", new OptionControl() {
			@Override
			public void onSelect(String name) {
				super.onSelect(name);
				hide();
			}
		}, "Proceed");
	}

	@Override
	public void onMemberCPD(MemberCPDEvent event) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("p", "memberCPD");
		params.put("refId", event.getMember().getRefId());
		PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(NameTokens.cpdmgt).with(params).build();
		placeManager.revealPlace(placeRequest);
	}

}
