package com.workpoint.icpak.client.ui.payment;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.workpoint.icpak.client.service.AbstractAsyncCallback;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent;
import com.workpoint.icpak.client.ui.events.ProcessingEvent;
import com.workpoint.icpak.shared.api.CountriesResource;
import com.workpoint.icpak.shared.api.CreditCardResource;
import com.workpoint.icpak.shared.api.InvoiceResource;
import com.workpoint.icpak.shared.model.Country;
import com.workpoint.icpak.shared.model.CreditCardDto;
import com.workpoint.icpak.shared.model.CreditCardResponse;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.TransactionDto;

public class PaymentPresenter extends PresenterWidget<PaymentPresenter.MyView> {

	public interface MyView extends View {
		HasClickHandlers getPayButton();

		HasClickHandlers getMpesaCompleteButton();

		CreditCardDto getCreditCardDetails();

		boolean isPaymentValid();

		void showmask(boolean show);

		void setCardResponse(CreditCardResponse response);

		void setInvoiceResult(InvoiceDto result);

		void setAmount(String amount);

		void bindTransation(InvoiceDto invoice);

		void setAttachmentUploadContext(String applicationRefId, String type);

		HasClickHandlers getStartUploadButton();

		boolean isOfflineValid();

		void showUploaderWidget(boolean b);

		TransactionDto getOfflineTransactionoBject();

		void bindOfflineTransaction(TransactionDto result);

		void setCountries(List<Country> countries);
	}

	private ResourceDelegate<CreditCardResource> creditCardResource;
	private InvoiceDto invoice;
	private ResourceDelegate<InvoiceResource> invoiceResource;
	private ResourceDelegate<CountriesResource> countriesResource;

	@Inject
	PaymentPresenter(EventBus eventBus, MyView view,
			ResourceDelegate<CreditCardResource> creditCardResource,
			ResourceDelegate<CountriesResource> countriesResource,
			ResourceDelegate<InvoiceResource> invoiceResource) {
		super(eventBus, view);
		this.creditCardResource = creditCardResource;
		this.invoiceResource = invoiceResource;
		this.countriesResource = countriesResource;

	}

	protected void onBind() {
		super.onBind();
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

		getView().getPayButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (getView().isPaymentValid()) {
					fireEvent(new ProcessingEvent());
					creditCardResource.withCallback(
							new AbstractAsyncCallback<CreditCardResponse>() {
								@Override
								public void onSuccess(
										CreditCardResponse response) {
									getView().setCardResponse(response);
									fireEvent(new ProcessingCompletedEvent());
								}
							}).postPayment(getView().getCreditCardDetails());
				}
			}
		});

		getView().getMpesaCompleteButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new ProcessingEvent());
				invoiceResource.withCallback(
						new AbstractAsyncCallback<InvoiceDto>() {
							@Override
							public void onSuccess(InvoiceDto result) {
								fireEvent(new ProcessingCompletedEvent());
								getView().setInvoiceResult(result);
							}

							@Override
							public void onFailure(Throwable caught) {
								fireEvent(new ProcessingCompletedEvent());
								Window.alert("Something went wrong..Please report this to Administrator..");
								super.onFailure(caught);
							}
						}).checkPaymentStatus(invoice.getRefId());
			}
		});

		getView().getStartUploadButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (getView().isOfflineValid()) {
					// Submit the transaction
					fireEvent(new ProcessingEvent());
					TransactionDto trx = getView()
							.getOfflineTransactionoBject();
					invoiceResource.withCallback(
							new AbstractAsyncCallback<TransactionDto>() {
								@Override
								public void onSuccess(TransactionDto result) {
									getView().bindOfflineTransaction(result);
									getView().showUploaderWidget(true);
									fireEvent(new ProcessingCompletedEvent());
								}

								@Override
								public void onFailure(Throwable caught) {
									fireEvent(new ProcessingCompletedEvent());
									Window.alert("Something went wrong..Please report this to Administrator..");
									super.onFailure(caught);
								}
							}).create(trx);

				}
			}
		});
	}

	public void setAmount(String amount) {
		getView().setAmount(amount);
	}

	public void bindTransaction(InvoiceDto invoice) {
		this.invoice = invoice;
		getView().bindTransation(invoice);
	}

	public void bindOfflineTransaction(TransactionDto trx) {
		getView().bindOfflineTransaction(trx);
	}

	public void setAttachmentUploadContext(String applicationRefId, String type) {
		getView().setAttachmentUploadContext(applicationRefId, type);
	}

}