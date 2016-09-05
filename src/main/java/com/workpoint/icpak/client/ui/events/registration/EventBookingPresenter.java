package com.workpoint.icpak.client.ui.events.registration;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.workpoint.icpak.client.model.UploadContext;
import com.workpoint.icpak.client.model.UploadContext.UPLOADACTION;
import com.workpoint.icpak.client.place.NameTokens;
import com.workpoint.icpak.client.service.AbstractAsyncCallback;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.AutoCompleteField;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.ui.component.autocomplete.ServerOracle;
import com.workpoint.icpak.client.ui.events.ClientDisconnectionEvent;
import com.workpoint.icpak.client.ui.events.EditModelEvent;
import com.workpoint.icpak.client.ui.events.ClientDisconnectionEvent.ClientDisconnectionHandler;
import com.workpoint.icpak.client.ui.events.PaymentCompletedEvent;
import com.workpoint.icpak.client.ui.events.PaymentCompletedEvent.PaymentCompletedHandler;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent.ProcessingCompletedHandler;
import com.workpoint.icpak.client.ui.events.ProcessingEvent;
import com.workpoint.icpak.client.ui.events.ProcessingEvent.ProcessingHandler;
import com.workpoint.icpak.client.ui.grid.ColumnConfig;
import com.workpoint.icpak.client.ui.payment.PaymentPresenter;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.api.CountriesResource;
import com.workpoint.icpak.shared.api.EventsResource;
import com.workpoint.icpak.shared.api.InvoiceResource;
import com.workpoint.icpak.shared.api.MemberResource;
//import com.workpoint.icpak.shared.api.EventsResource;
import com.workpoint.icpak.shared.model.Country;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.MemberDto;
import com.workpoint.icpak.shared.model.TransactionDto;
import com.workpoint.icpak.shared.model.events.AccommodationDto;
import com.workpoint.icpak.shared.model.events.BookingDto;
import com.workpoint.icpak.shared.model.events.DelegateDto;
import com.workpoint.icpak.shared.model.events.EventDto;

public class EventBookingPresenter extends Presenter<EventBookingPresenter.MyView, EventBookingPresenter.MyProxy>
		implements ProcessingHandler, ProcessingCompletedHandler, PaymentCompletedHandler, ClientDisconnectionHandler {

	public interface MyView extends View {
		boolean isValid();

		void setCountries(List<Country> countries);

		Anchor getANext();

		int getCounter();

		void next();

		void addError(String error);

		void setEvent(EventDto event);

		BookingDto getBooking();

		void bindBooking(BookingDto booking);

		void setMiddleHeight();

		void bindInvoice(InvoiceDto invoice);

		void setActivePage(int index);

		void setLoadingState(ActionLink anchor, boolean isLoading);

		void showmask(boolean processing);

		ColumnConfig getMemberColumnConfig();

		void bindAccommodations(List<AccommodationDto> result);

		HasClickHandlers getaDownloadProforma();

		TextField getEmailTextBox();

		void setEmailValid(boolean b, String string);

		void showEmailValidating(boolean show);

		boolean isDelegateValid();

		Anchor browseOthersEventsButton();

		void scrollToPaymentsTop();

		void showClientDisconnection(boolean b);

		HasClickHandlers getConfirmCancelButton();

		void showCancellation(boolean show);

		void showInlineCancellationButton(boolean b);

		void showCancellationSuccess(boolean b);

		void showBookingCancelled(boolean show);

		HasClickHandlers getUndoCancellationButton();

	}

	@ProxyCodeSplit
	@NameToken(NameTokens.eventBooking)
	@NoGatekeeper
	public interface MyProxy extends ProxyPlace<EventBookingPresenter> {
	}

	@Inject
	PlaceManager placeManager;

	@Inject
	PaymentPresenter paymentPresenter;

	private static final Logger LOGGER = Logger.getLogger("ICPAK Logger..");
	@ContentSlot
	public static final Type<RevealContentHandler<?>> PAYMENTS_SLOT = new Type<RevealContentHandler<?>>();

	private String eventId;
	private String bookingId;
	private EventDto event;

	private ResourceDelegate<CountriesResource> countriesResource;
	private ResourceDelegate<EventsResource> eventsResource;
	private ResourceDelegate<InvoiceResource> invoiceResource;
	private ResourceDelegate<MemberResource> membersDelegate;
	private ResourceDelegate<EventsResource> eventsDelegate;

	final ActivityCallback accommodationCallback = new ActivityCallback() {
		private BookingDto bookingDto;
		private List<AccommodationDto> accommodations;

		@Override
		public void onComplete(BookingDto booking) {
			this.bookingDto = booking;
			bindData();
		}

		@Override
		public void onComplete(List<AccommodationDto> accommodations) {
			this.accommodations = accommodations;
			bindData();
		}

		public void bindData() {
			if (accommodations != null && bookingDto != null) {
				if (bookingDto.getIsActive() == 0) {
					bindCancellation(bookingDto);
				} else {
					getView().showmask(false);
					getView().bindAccommodations(accommodations);
					getView().bindBooking(bookingDto);
				}
			}
		}

	};

	private int requestCounter = 0;
	private int responseCounter = 0;

	@Inject
	public EventBookingPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy,
			ResourceDelegate<CountriesResource> countriesResource, ResourceDelegate<EventsResource> eventsResource,
			ResourceDelegate<InvoiceResource> invoiceResource, ResourceDelegate<MemberResource> membersDelegate,
			ResourceDelegate<EventsResource> eventsDelegate) {
		super(eventBus, view, proxy);
		this.countriesResource = countriesResource;
		this.eventsResource = eventsResource;
		this.invoiceResource = invoiceResource;
		this.membersDelegate = membersDelegate;
		this.eventsDelegate = eventsDelegate;
	}

	@Override
	protected void revealInParent() {
		RevealRootContentEvent.fire(this, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(ProcessingEvent.TYPE, this);
		addRegisteredHandler(ProcessingCompletedEvent.TYPE, this);
		addRegisteredHandler(PaymentCompletedEvent.TYPE, this);
		addRegisteredHandler(ClientDisconnectionEvent.TYPE, this);

		getView().getMemberColumnConfig().setLoader(new AutoCompleteField.Loader() {
			@Override
			public void onLoad(final ServerOracle source, final String query) {
				getView().getMemberColumnConfig().showSpinner(true);
				query.replaceAll("/", "%");
				requestCounter = requestCounter + 1;
				membersDelegate.withCallback(new AbstractAsyncCallback<List<MemberDto>>() {
					@Override
					public void onSuccess(List<MemberDto> members) {
						responseCounter = responseCounter + 1;
						if (requestCounter == responseCounter) {
							source.setValues(members);
							getView().getMemberColumnConfig().showSpinner(false);
						}
					}

				}).search(query, 0, 30);
			}
		});

		getView().getANext().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getView().browseOthersEventsButton().addStyleName("hide");
				// Are you editing this booking?
				if (bookingId != null) {
					if (getView().getCounter() == 0) {
						if (getView().isValid()) {
							getView().next();
						}
					} else if (getView().getCounter() == 1) {
						if (getView().isDelegateValid()) {
							BookingDto dto = getView().getBooking();
							dto.setEventRefId(eventId);
							submit(dto);
						}
					}
					// We are creating a new booking
				} else {
					if (getView().getCounter() == 0) {
						checkExists(getView().getBooking().getContact().getEmail());
					} else if (getView().getCounter() == 1) {
						if (getView().isDelegateValid()) {
							BookingDto dto = getView().getBooking();
							dto.setEventRefId(eventId);
							submit(dto);
						}
					}
				}
				// If counter is 2 - Take me to my Payments
				if (getView().getCounter() == 2) {
					getView().next();
				}

				// If counter is 3 - Take me to my bookings
				if (getView().getCounter() == 3) {
					getView().getANext().addStyleName("hide");
					// getView().getANext().setHref("#booking");
					getView().browseOthersEventsButton().removeStyleName("hide");
					getView().scrollToPaymentsTop();
				}
			}
		});

		getView().getConfirmCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (getView().getBooking().getRefId() == null) {
					Window.alert("No Booking set...");
				} else {
					getView().showmask(true);
					eventsDelegate.withCallback(new AbstractAsyncCallback<Boolean>() {
						@Override
						public void onSuccess(Boolean result) {
							if (result == true) {
								getView().showCancellationSuccess(true);
								getView().showmask(false);
							} else {
								Window.alert("A technical problem occured while doing this request..");
							}
						}
					}).bookings(eventId).cancelBooking(getView().getBooking().getRefId());
				}
			}
		});

	}

	protected void checkExists(String email) {
		getView().showEmailValidating(true);
		eventsDelegate.withCallback(new AbstractAsyncCallback<BookingDto>() {
			@Override
			public void onSuccess(BookingDto booking) {
				getView().showEmailValidating(false);
				if (booking == null) {
					getView().setEmailValid(true, "");
					if (getView().isValid()) {
						getView().next();
					}
				} else {
					getView().setEmailValid(false,
							"You have already booked for this event." + " We have resend the booking email to "
									+ booking.getContact().getEmail()
									+ " with instructions on how to ammend your booking.");
					eventsResource.withoutCallback().bookings(eventId).sendAlert(booking.getRefId());
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				super.onFailure(caught);
				getView().showEmailValidating(false);
			}
		}).delegates(eventId).checkExist(email.trim());

	}

	protected void submit(BookingDto dto) {
		getView().showmask(true);
		if (bookingId == null) {
			eventsResource.withCallback(new AbstractAsyncCallback<BookingDto>() {
				@Override
				public void onSuccess(BookingDto booking) {
					bindBooking(booking);
				}
			}).bookings(eventId).create(dto);

		} else {
			eventsResource.withCallback(new AbstractAsyncCallback<BookingDto>() {
				@Override
				public void onSuccess(BookingDto booking) {
					getView().showmask(false);
					bindBooking(booking);
				}
			}).bookings(eventId).update(bookingId, dto);

		}

	}

	protected void bindBooking(final BookingDto booking) {
		// bookingId = booking.getRefId();
		getView().bindBooking(booking);
		getInvoice(booking);

		// Set Download Proforma Link
		getView().getaDownloadProforma().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				UploadContext ctx = new UploadContext("getreport");
				ctx.setContext("bookingRefId", booking.getRefId());
				ctx.setAction(UPLOADACTION.GETPROFORMA);
				Window.open(ctx.toUrl(), "Get Proforma", null);
			}
		});
	}

	public void bindCancellation(final BookingDto booking) {
		getView().showBookingCancelled(true);
		// Undo Cancellation
		getView().getUndoCancellationButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getView().showmask(true);

				List<DelegateDto> delegates = booking.getDelegates();
				if (delegates.size() > 0) {
					DelegateDto d = delegates.get(0);
					d.setIsBookingActive(1);

					eventsResource.withCallback(new AbstractAsyncCallback<DelegateDto>() {
						@Override
						public void onSuccess(DelegateDto delegateDto) {
							getView().showmask(false);
							getView().bindBooking(booking);
							getView().showBookingCancelled(false);
						}
					}).bookings(eventId).updateDelegate(booking.getRefId(), d.getRefId(), d);
				}
			}
		});
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		eventId = request.getParameter("eventId", "");
		bookingId = request.getParameter("bookingId", null);
		String cancel = request.getParameter("cancel", null);

		countriesResource.withCallback(new AbstractAsyncCallback<List<Country>>() {
			public void onSuccess(List<Country> countries) {
				Collections.sort(countries, new Comparator<Country>() {
					@Override
					public int compare(Country o1, Country o2) {
						return o1.getDisplayName().compareTo(o2.getDisplayName());
					}
				});

				getView().setCountries(countries);
			};

			@Override
			public void onFailure(Throwable caught) {
				super.onFailure(caught);
			}
		}).getAll();

		eventsResource.withCallback(new AbstractAsyncCallback<EventDto>() {
			@Override
			public void onSuccess(EventDto event) {
				EventBookingPresenter.this.event = event;
				getView().setEvent(event);
			}

			@Override
			public void onFailure(Throwable caught) {
				StringBuffer buffer = new StringBuffer();
				for (StackTraceElement elem : caught.getStackTrace()) {
					buffer.append(elem.getLineNumber() + ">>" + elem.getClassName() + ">>" + elem.getMethodName());
				}
				super.onFailure(caught);
			}
		}).getById(eventId);

		// Editing a Booking - We are editing a booking
		if (bookingId != null) {
			getView().next();
			getView().showmask(true);

			getView().showInlineCancellationButton(true);

			// Is Cancel YES
			if (cancel.equals("yes")) {
				getView().showCancellation(true);
			}

			// Load Accommodations
			eventsResource.withCallback(new AbstractAsyncCallback<List<AccommodationDto>>() {
				@Override
				public void onSuccess(List<AccommodationDto> result) {
					accommodationCallback.onComplete(result);
				}
			}).accommodations(eventId).getAll(0, 100);

			// Load Booking
			eventsResource.withCallback(new AbstractAsyncCallback<BookingDto>() {
				@Override
				public void onSuccess(final BookingDto booking) {
					accommodationCallback.onComplete(booking);
					getInvoice(booking, false, false);

					getView().getaDownloadProforma().addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							UploadContext ctx = new UploadContext("getreport");
							ctx.setContext("bookingRefId", booking.getRefId());
							ctx.setAction(UPLOADACTION.GETPROFORMA);
							Window.open(ctx.toUrl(), "Get Proforma", null);
						}
					});

				}
			}).bookings(eventId).getById(bookingId);
		} else {
			// New booking....
			eventsResource.withCallback(new AbstractAsyncCallback<List<AccommodationDto>>() {
				@Override
				public void onSuccess(List<AccommodationDto> result) {
					getView().bindAccommodations(result);
				}
			}).accommodations(eventId).getAll(0, 100);
		}
	}

	protected void getInvoice(BookingDto booking) {
		getInvoice(booking, true, true);
	}

	protected void getInvoice(final BookingDto booking, final boolean moveNext, final boolean sendEmail) {
		invoiceResource.withCallback(new AbstractAsyncCallback<InvoiceDto>() {
			@Override
			public void onSuccess(InvoiceDto invoice) {
				getView().bindInvoice(invoice);
				getView().showmask(false);

				if (sendEmail) {
					eventsResource.withoutCallback().bookings(eventId).sendAlert(booking.getRefId());
				}

				/*
				 * Set the Amount to be paid based on Invoice
				 */
				bindPaymentValues(invoice);

				if (moveNext) {
					getView().next();
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				super.onFailure(caught);
			}
		}).getInvoice(booking.getInvoiceRef());

	}

	protected void bindPaymentValues(InvoiceDto invoice) {
		if (invoice.getChargeableAmount() != null) {
			invoice.setAmount(invoice.getChargeableAmount());
		}

		if (invoice.getDocumentNo() != null) {
			paymentPresenter.bindTransaction(invoice);
			TransactionDto trx = new TransactionDto();
			trx.setDescription("Payment for " + event.getName());
			paymentPresenter.bindOfflineTransaction(trx);
		}

	}

	@Override
	protected void onReset() {
		super.onReset();
		getView().setMiddleHeight();
		setInSlot(PAYMENTS_SLOT, paymentPresenter);
	}

	@Override
	public void onProcessingCompleted(ProcessingCompletedEvent event) {
		getView().showmask(false);
	}

	@Override
	public void onProcessing(ProcessingEvent event) {
		getView().showmask(true);
	}

	@Override
	public void onPaymentCompleted(PaymentCompletedEvent event) {
		getView().getANext().removeStyleName("hide");
	}

	@Override
	public void onClientDisconnection(ClientDisconnectionEvent event) {
		getView().showClientDisconnection(true);
		Window.alert("Internet Connection Lost. Kindly check and try again");
	}
}