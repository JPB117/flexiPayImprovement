package com.workpoint.icpak.client.ui.events.registration;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.workpoint.icpak.client.place.NameTokens;
import com.workpoint.icpak.client.service.AbstractAsyncCallback;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.AutoCompleteField;
import com.workpoint.icpak.client.ui.component.autocomplete.ServerOracle;
import com.workpoint.icpak.client.ui.grid.ColumnConfig;
import com.workpoint.icpak.shared.api.CountriesResource;
import com.workpoint.icpak.shared.api.CreditCardResource;
import com.workpoint.icpak.shared.api.EventsResource;
import com.workpoint.icpak.shared.api.InvoiceResource;
import com.workpoint.icpak.shared.api.MemberResource;
//import com.workpoint.icpak.shared.api.EventsResource;
import com.workpoint.icpak.shared.model.Country;
import com.workpoint.icpak.shared.model.CreditCardDto;
import com.workpoint.icpak.shared.model.CreditCardResponse;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.MemberDto;
import com.workpoint.icpak.shared.model.events.BookingDto;
import com.workpoint.icpak.shared.model.events.EventDto;

//import com.workpoint.icpak.shared.model.events.BookingDto;

public class EventBookingPresenter extends
		Presenter<EventBookingPresenter.MyView, EventBookingPresenter.MyProxy> {

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

		HasClickHandlers getPayButton();

		CreditCardDto getCreditCardDetails();

		boolean isPaymentValid();

		void setCardResponse(CreditCardResponse response);

		HasClickHandlers getMpesaCompleteButton();

		void setInvoiceResult(InvoiceDto result);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.eventBooking)
	@NoGatekeeper
	public interface MyProxy extends ProxyPlace<EventBookingPresenter> {
	}

	@Inject
	PlaceManager placeManager;

	private String eventId;
	private String bookingId;
	private InvoiceDto invoice;

	private ResourceDelegate<CountriesResource> countriesResource;

	private ResourceDelegate<EventsResource> eventsResource;

	private ResourceDelegate<CreditCardResource> creditCardResource;

	private ResourceDelegate<InvoiceResource> invoiceResource;

	private ResourceDelegate<MemberResource> membersDelegate;

	@Inject
	public EventBookingPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy,
			ResourceDelegate<CountriesResource> countriesResource,
			ResourceDelegate<EventsResource> eventsResource,
			ResourceDelegate<InvoiceResource> invoiceResource,
			ResourceDelegate<CreditCardResource> creditCardResource,
			ResourceDelegate<MemberResource> membersDelegate) {
		super(eventBus, view, proxy);
		this.countriesResource = countriesResource;
		this.eventsResource = eventsResource;
		this.invoiceResource = invoiceResource;
		this.membersDelegate = membersDelegate;
		this.creditCardResource = creditCardResource;
	}

	@Override
	protected void revealInParent() {
		RevealRootContentEvent.fire(this, this);
	}

	@Override
	protected void onBind() {
		super.onBind();

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
						  
					} else {
						getView().next();
					}

				} else if (getView().getCounter() == 1) {
					getView().addError("Kindly specify at least one delegate");
				}

			}
		});

		getView().getPayButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (getView().isPaymentValid()) {
					getView().showmask(true);
					creditCardResource.withCallback(
							new AbstractAsyncCallback<CreditCardResponse>() {
								@Override
								public void onSuccess(
										CreditCardResponse response) {
									getView().showmask(false);
									getView().setCardResponse(response);

								}
							}).postPayment(getView().getCreditCardDetails());
				}
			}
		});

		getView().getMpesaCompleteButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getView().showmask(true);
				invoiceResource.withCallback(
						new AbstractAsyncCallback<InvoiceDto>() {
							@Override
							public void onSuccess(InvoiceDto result) {
								getView().showmask(false);
								getView().setInvoiceResult(result);
							}

							@Override
							public void onFailure(Throwable caught) {
								super.onFailure(caught);

							}
						}).getInvoice(invoice.getRefId());
			}
		});
	}

	protected void submit(BookingDto dto) {
		getView().showmask(true);

		if (bookingId != null) {
			eventsResource
					.withCallback(new AbstractAsyncCallback<BookingDto>() {
						@Override
						public void onSuccess(BookingDto booking) {
							getView().showmask(false);
							bindBooking(booking);
						}
					}).bookings(eventId).update(bookingId, dto);

		} else {
			eventsResource
					.withCallback(new AbstractAsyncCallback<BookingDto>() {
						@Override
						public void onSuccess(BookingDto booking) {
							getView().showmask(false);
							bindBooking(booking);
						}
					}).bookings(eventId).create(dto);
		}
	}

	protected void bindBooking(BookingDto booking) {
		bookingId = booking.getRefId();
		getView().bindBooking(booking);
		getInvoice(booking.getInvoiceRef());
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

				Window.alert(caught + " " + caught.getMessage() + " "
						+ caught.getStackTrace().toString());

				super.onFailure(caught);
			}
		}).getById(eventId);

		if (bookingId != null) {
			getView().setActivePage(2);
			eventsResource
					.withCallback(new AbstractAsyncCallback<BookingDto>() {
						@Override
						public void onSuccess(BookingDto booking) {
							getView().bindBooking(booking);
							getInvoice(booking.getInvoiceRef(), false);
						}
					}).bookings(eventId).getById(bookingId);
		}

	}

	protected void getInvoice(String invoiceRef) {
		getInvoice(invoiceRef, true);
	}

	protected void getInvoice(String invoiceRef, final boolean moveNext) {

		invoiceResource.withCallback(new AbstractAsyncCallback<InvoiceDto>() {

			@Override
			public void onSuccess(InvoiceDto invoice) {
				EventBookingPresenter.this.invoice = invoice;
				getView().bindInvoice(invoice);
				if (moveNext)
					getView().next();
			}

			@Override
			public void onFailure(Throwable caught) {
				super.onFailure(caught);
				Window.alert("Ooops..! Something went wrong and we ve noted. Report to ICPAK Customer Care");
			}
		}).getInvoice(invoiceRef);

	}

	@Override
	protected void onReset() {
		super.onReset();
		getView().setMiddleHeight();
	}

}