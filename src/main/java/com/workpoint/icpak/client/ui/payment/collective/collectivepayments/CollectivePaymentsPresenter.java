package com.workpoint.icpak.client.ui.payment.collective.collectivepayments;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.workpoint.icpak.client.place.NameTokens;
import com.workpoint.icpak.client.service.ServiceCallback;
import com.workpoint.icpak.client.ui.payment.PaymentPresenter;
import com.workpoint.icpak.client.ui.profile.paysubscription.PaymentSubscription;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.PaymentType;
import com.workpoint.icpak.shared.model.TransactionDto;

public class CollectivePaymentsPresenter
		extends
		Presenter<CollectivePaymentsPresenter.MyView, CollectivePaymentsPresenter.MyProxy> {
	public interface MyView extends View {
		HasClickHandlers getProceedButton();

		PaymentSubscription getSubscriptionWidget();

		void showSubscriptionPanel(boolean b);
	}

	@ContentSlot
	public static final Type<RevealContentHandler<?>> SLOT_CollectivePayments = new Type<RevealContentHandler<?>>();

	private IndirectProvider<PaymentPresenter> paymentFactory;

	@ContentSlot
	public static final Type<RevealContentHandler<?>> PAYMENTS_SLOT = new Type<RevealContentHandler<?>>();

	@NameToken(NameTokens.payments)
	@ProxyCodeSplit
	@NoGatekeeper
	public interface MyProxy extends ProxyPlace<CollectivePaymentsPresenter> {
	}

	@Inject
	CollectivePaymentsPresenter(EventBus eventBus, MyView view, MyProxy proxy,
			Provider<PaymentPresenter> paymentProvider) {
		super(eventBus, view, proxy, RevealType.Root);
		this.paymentFactory = new StandardProvider<PaymentPresenter>(
				paymentProvider);
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
	}

	protected void onBind() {
		super.onBind();

		getView().getProceedButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String amountToPay = getView().getSubscriptionWidget()
						.getAmountToPay();
				if (amountToPay != null && !amountToPay.equals("")) {
					final InvoiceDto invoice = new InvoiceDto();
					invoice.setAmount(Double.valueOf(amountToPay));
					invoice.setDocumentNo(getView().getSubscriptionWidget()
							.getMemberNo());
					
					getView().showSubscriptionPanel(false);

					paymentFactory.get(new ServiceCallback<PaymentPresenter>() {
						@Override
						public void processResult(PaymentPresenter result) {
							result.bindTransaction(invoice);
							result.bindOfflineTransaction(initTransaction());
							setInSlot(PAYMENTS_SLOT, result);
						}

					});
				}else{
					Window.alert("Please enter a valid amount and valid memberNo.");;
				}
			}
		});
	}

	private TransactionDto initTransaction() {
		TransactionDto trx = new TransactionDto();
		trx.setDescription("Subscription payments for ");
		trx.setPaymentType(PaymentType.SUBSCRIPTION);
		return trx;
	}

}