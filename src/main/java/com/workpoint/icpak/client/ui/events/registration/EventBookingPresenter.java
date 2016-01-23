package com.workpoint.icpak.client.ui.events.registration;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
import com.workpoint.icpak.client.ui.component.autocomplete.ServerOracle;
import com.workpoint.icpak.client.ui.events.PaymentCompletedEvent;
import com.workpoint.icpak.client.ui.events.PaymentCompletedEvent.PaymentCompletedHandler;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent.ProcessingCompletedHandler;
import com.workpoint.icpak.client.ui.events.ProcessingEvent;
import com.workpoint.icpak.client.ui.events.ProcessingEvent.ProcessingHandler;
import com.workpoint.icpak.client.ui.grid.ColumnConfig;
import com.workpoint.icpak.client.ui.payment.PaymentPresenter;
import com.workpoint.icpak.shared.api.CountriesResource;
import com.workpoint.icpak.shared.api.EventsResource;
import com.workpoint.icpak.shared.api.InvoiceResource;
import com.workpoint.icpak.shared.api.MemberResource;
//import com.workpoint.icpak.shared.api.EventsResource;
import com.workpoint.icpak.shared.model.Country;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.MemberDto;
import com.workpoint.icpak.shared.model.events.AccommodationDto;
import com.workpoint.icpak.shared.model.events.BookingDto;
import com.workpoint.icpak.shared.model.events.EventDto;

public class EventBookingPresenter extends
		Presenter<EventBookingPresenter.MyView, EventBookingPresenter.MyProxy>
		implements ProcessingHandler, ProcessingCompletedHandler,
		PaymentCompletedHandler {

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

	@ContentSlot
	public static final Type<RevealContentHandler<?>> PAYMENTS_SLOT = new Type<RevealContentHandler<?>>();

	private String eventId;
	private String bookingId;

	private ResourceDelegate<CountriesResource> countriesResource;

	private ResourceDelegate<EventsResource> eventsResource;

	private ResourceDelegate<InvoiceResource> invoiceResource;

	private ResourceDelegate<MemberResource> membersDelegate;

	@Inject
	public EventBookingPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy,
			ResourceDelegate<CountriesResource> countriesResource,
			ResourceDelegate<EventsResource> eventsResource,
			ResourceDelegate<InvoiceResource> invoiceResource,
			ResourceDelegate<MemberResource> membersDelegate) {
		super(eventBus, view, proxy);
		this.countriesResource = countriesResource;
		this.eventsResource = eventsResource;
		this.invoiceResource = invoiceResource;
		this.membersDelegate = membersDelegate;
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

		getView().getMemberColumnConfig().setLoader(
				new AutoCompleteField.Loader() {
					@Override
					public void onLoad(final ServerOracle source,
							final String query) {
						query.replaceAll("/", "%");
						membersDelegate.withCallback(
								new AbstractAsyncCallback<List<MemberDto>>() {
									@Override
									public void onSuccess(
											List<MemberDto> members) {
										source.setValues(members);
									}

								}).search(query, 0, 30);
					}
				});

		getView().getANext().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (getView().isValid()) {
					if (getView().getCounter() == 1) {
						// User has selected a category and clicked submit
						BookingDto dto = getView().getBooking();
						dto.setEventRefId(eventId);
						submit(dto);
					} else if (getView().getCounter() == 3) {
						getView().getANext().addStyleName("hide");
						getView().getANext().setHref("#booking");
					} else {
						getView().next();
					}

				} else if (getView().getCounter() == 1) {
					getView().addError("Kindly specify at least one delegate");
				}

			}
		});

	}

	protected void submit(BookingDto dto) {
		getView().showmask(true);

		if (bookingId == null) {
			eventsResource
					.withCallback(new AbstractAsyncCallback<BookingDto>() {
						@Override
						public void onSuccess(BookingDto booking) {
							bindBooking(booking);
						}
					}).bookings(eventId).create(dto);
		} else {
			eventsResource
					.withCallback(new AbstractAsyncCallback<BookingDto>() {
						@Override
						public void onSuccess(BookingDto booking) {
							getView().showmask(false);
							bindBooking(booking);
						}
					}).bookings(eventId).update(bookingId, dto);

		}

	}

	protected void bindBooking(final BookingDto booking) {
		bookingId = booking.getRefId();
		getView().bindBooking(booking);
		getInvoice(booking.getInvoiceRef());

		// Set Download Proforma Link
		getView().getaDownloadProforma().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				UploadContext ctx = new UploadContext("getreport");
				ctx.setContext("bookingRefId", booking.getRefId());
				ctx.setAction(UPLOADACTION.GETPROFORMA);

				// ctx.setContext(key, value)
				Window.open(ctx.toUrl(), "Get Proforma", null);
			}
		});
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		eventId = request.getParameter("eventId", "9J4oKtW898RyOClP");
		bookingId = request.getParameter("bookingId", null);

		countriesResource.withCallback(
				new AbstractAsyncCallback<List<Country>>() {
					public void onSuccess(List<Country> countries) {
						Collections.sort(countries, new Comparator<Country>() {
							@Override
							public int compare(Country o1, Country o2) {
								return o1.getDisplayName().compareTo(
										o2.getDisplayName());
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
				getView().setEvent(event);
			}

			@Override
			public void onFailure(Throwable caught) {

				StringBuffer buffer = new StringBuffer();
				for (StackTraceElement elem : caught.getStackTrace()) {
					buffer.append(elem.getLineNumber() + ">>"
							+ elem.getClassName() + ">>" + elem.getMethodName());
				}

				super.onFailure(caught);
			}
		}).getById(eventId);

		eventsResource
				.withCallback(
						new AbstractAsyncCallback<List<AccommodationDto>>() {
							@Override
							public void onSuccess(List<AccommodationDto> result) {
								getView().bindAccommodations(result);
							}
						}).accommodations(eventId).getAll(0, 100);

		if (bookingId != null) {
			getView().next();
			getView().next();
			getView().setActivePage(2);
			eventsResource
					.withCallback(new AbstractAsyncCallback<BookingDto>() {
						@Override
						public void onSuccess(final BookingDto booking) {
							getView().bindBooking(booking);
							getInvoice(booking.getInvoiceRef(), false, false);

							getView().getaDownloadProforma().addClickHandler(
									new ClickHandler() {
										@Override
										public void onClick(ClickEvent event) {
											UploadContext ctx = new UploadContext(
													"getreport");
											ctx.setContext("bookingRefId",
													booking.getRefId());
											ctx.setAction(UPLOADACTION.GETPROFORMA);

											// ctx.setContext(key, value)
											Window.open(ctx.toUrl(),
													"Get Proforma", null);
										}
									});
						}
					}).bookings(eventId).getById(bookingId);
		}

	}

	protected void getInvoice(String invoiceRef) {
		getInvoice(invoiceRef, true, true);
	}

	protected void getInvoice(String invoiceRef, final boolean moveNext,
			final boolean sendEmail) {
		invoiceResource.withCallback(new AbstractAsyncCallback<InvoiceDto>() {
			@Override
			public void onSuccess(InvoiceDto invoice) {
				getView().bindInvoice(invoice);
				getView().showmask(false);

				if (sendEmail) {
					eventsResource.withoutCallback().bookings(eventId)
							.sendAlert(bookingId);
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
		}).getInvoice(invoiceRef);

	}

	protected void bindPaymentValues(InvoiceDto invoice) {
		if (invoice.getInvoiceAmount() != null) {
			paymentPresenter.setAmount(invoice.getInvoiceAmount().toString());
		}

		if (invoice.getDocumentNo() != null) {
			paymentPresenter.bindTransaction(invoice);
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

}