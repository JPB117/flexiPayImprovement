package com.workpoint.icpak.client.ui.payment.collective.collectivepayments;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
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
import com.workpoint.icpak.client.service.AbstractAsyncCallback;
import com.workpoint.icpak.client.service.ServiceCallback;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.events.ClientDisconnectionEvent;
import com.workpoint.icpak.client.ui.events.ClientDisconnectionEvent.ClientDisconnectionHandler;
import com.workpoint.icpak.client.ui.events.PaymentCompletedEvent;
import com.workpoint.icpak.client.ui.events.PaymentCompletedEvent.PaymentCompletedHandler;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent.ProcessingCompletedHandler;
import com.workpoint.icpak.client.ui.events.ProcessingEvent;
import com.workpoint.icpak.client.ui.events.ProcessingEvent.ProcessingHandler;
import com.workpoint.icpak.client.ui.payment.PaymentPresenter;
import com.workpoint.icpak.client.ui.profile.paysubscription.PaymentSubscription;
import com.workpoint.icpak.shared.api.MemberResource;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.MemberDto;
import com.workpoint.icpak.shared.model.PaymentType;
import com.workpoint.icpak.shared.model.TransactionDto;

public class CollectivePaymentsPresenter
		extends Presenter<CollectivePaymentsPresenter.MyView, CollectivePaymentsPresenter.MyProxy>
		implements ProcessingHandler, ProcessingCompletedHandler, PaymentCompletedHandler, ClientDisconnectionHandler {
	public interface MyView extends View {
		ActionLink getProceedButton();

		PaymentSubscription getSubscriptionWidget();

		void showSubscriptionPanel(boolean b);

		void showError(String error);

		void setLegendText(String title);

		void showmask(boolean b);

		void setMiddleHeight();
	}

	@ContentSlot
	public static final Type<RevealContentHandler<?>> SLOT_CollectivePayments = new Type<RevealContentHandler<?>>();

	private IndirectProvider<PaymentPresenter> paymentFactory;

	@ContentSlot
	public static final Type<RevealContentHandler<?>> PAYMENTS_SLOT = new Type<RevealContentHandler<?>>();

	protected final ResourceDelegate<MemberResource> memberDelegate;

	@NameToken(NameTokens.payments)
	@ProxyCodeSplit
	@NoGatekeeper
	public interface MyProxy extends ProxyPlace<CollectivePaymentsPresenter> {
	}

	@Inject
	CollectivePaymentsPresenter(EventBus eventBus, MyView view, MyProxy proxy,
			Provider<PaymentPresenter> paymentProvider, final ResourceDelegate<MemberResource> memberDelegate) {
		super(eventBus, view, proxy, RevealType.Root);
		this.paymentFactory = new StandardProvider<PaymentPresenter>(paymentProvider);
		this.memberDelegate = memberDelegate;
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
	}

	protected void onBind() {
		super.onBind();
		addRegisteredHandler(ProcessingEvent.TYPE, this);
		addRegisteredHandler(ProcessingCompletedEvent.TYPE, this);
		addRegisteredHandler(PaymentCompletedEvent.TYPE, this);
		addRegisteredHandler(ClientDisconnectionEvent.TYPE, this);

		getView().getProceedButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getView().showError("");// Clear Errors
				final String amountToPay = getView().getSubscriptionWidget().getAmountToPay();
				String memberNo = getView().getSubscriptionWidget().getMemberNo();
				if (amountToPay != null && !amountToPay.equals("") && memberNo != null && !memberNo.equals("")) {
					getView().getProceedButton().setText("Proceed to Pay");
					getView().getProceedButton().setLoadingState(true);
					memberDelegate.withCallback(new AbstractAsyncCallback<MemberDto>() {
						@Override
						public void onSuccess(final MemberDto member) {
							getView().getProceedButton().setLoadingState(false);
							if (member.getRefId() != null) {
								getView().setLegendText("Paying for " + member.getFullName());
								final InvoiceDto invoice = new InvoiceDto();
								invoice.setRefId("SUBSCRIPTION");
								invoice.setAmount(Double.valueOf(amountToPay));
								invoice.setDocumentNo(getView().getSubscriptionWidget().getMemberNo());
								invoice.setInvoiceRefId(getView().getSubscriptionWidget().getMemberNo());

								getView().showSubscriptionPanel(false);

								paymentFactory.get(new ServiceCallback<PaymentPresenter>() {
									@Override
									public void processResult(PaymentPresenter result) {
										result.bindTransaction(invoice);
										result.bindOfflineTransaction(initTransaction(member));
										setInSlot(PAYMENTS_SLOT, result);
									}
								});
							} else {
								getView().showError("Member Number not found. Please check and try again.");
							}
						}

						public void onFailure(Throwable caught) {
							getView().getProceedButton().setLoadingState(false);
							getView().showError("Member Number not found. Please check and try again.");
						};

					}).searchIndividualByMemberNo(memberNo);

				} else {
					getView().showError("MemberNo and Amount are mandatory.");
				}
			}
		});
	}

	private TransactionDto initTransaction(MemberDto member) {
		TransactionDto trx = new TransactionDto();
		trx.setDescription("Subscription payments for " + member.getFullName());
		trx.setPaymentType(PaymentType.SUBSCRIPTION);
		return trx;
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
		// getView().getANext().removeStyleName("hide");
	}

	@Override
	public void onClientDisconnection(ClientDisconnectionEvent event) {
		Window.alert("Internet Connection Lost. Kindly check and try again");
	}

	@Override
	protected void onReset() {
		super.onReset();
		getView().setMiddleHeight();
	}

}