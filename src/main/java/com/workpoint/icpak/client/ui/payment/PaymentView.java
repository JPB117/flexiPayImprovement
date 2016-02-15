package com.workpoint.icpak.client.ui.payment;

import javax.inject.Inject;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.payment.widget.PaymentWidget;
import com.workpoint.icpak.shared.model.CreditCardDto;
import com.workpoint.icpak.shared.model.CreditCardResponse;
import com.workpoint.icpak.shared.model.InvoiceDto;

public class PaymentView extends ViewImpl implements PaymentPresenter.MyView {

	interface Binder extends UiBinder<Widget, PaymentView> {
	}

	@UiField
	PaymentWidget panelPayment;

	@Inject
	PaymentView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public HasClickHandlers getPayButton() {
		return panelPayment.getCardPayButton();
	}

	@Override
	public HasClickHandlers getMpesaCompleteButton() {
		return panelPayment.getMpesaCompleteButton();
	}

	@Override
	public CreditCardDto getCreditCardDetails() {
		return panelPayment.getCardDetails();
	}

	@Override
	public boolean isPaymentValid() {
		return panelPayment.isValid();
	}

	@Override
	public void showmask(boolean show) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setCardResponse(CreditCardResponse response) {
		panelPayment.setCardResponse(response);
	}

	@Override
	public void setInvoiceResult(InvoiceDto invoiceRslt) {
		panelPayment.setInvoiceResult(invoiceRslt);
	}

	@Override
	public void setAmount(String amount) {
		panelPayment.setAmount(amount);
	}

	@Override
	public void bindTransation(InvoiceDto invoice) {
		panelPayment.bindTransaction(invoice);
	}

	@Override
	public void setAttachmentUploadContext(String applicationRefId, String type) {
		panelPayment.setAttachmentUploadContext(applicationRefId, type);
	}

}