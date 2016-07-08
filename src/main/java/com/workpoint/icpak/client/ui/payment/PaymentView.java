package com.workpoint.icpak.client.ui.payment;

import java.util.List;

import javax.inject.Inject;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.payment.widget.PaymentWidget;
import com.workpoint.icpak.shared.model.Country;
import com.workpoint.icpak.shared.model.CreditCardDto;
import com.workpoint.icpak.shared.model.CreditCardResponse;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.TransactionDto;

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
	public ActionLink getPayButton() {
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
		// panelPayment.setAttachmentUploadContext(applicationRefId, type);
	}

	@Override
	public HasClickHandlers getStartUploadButton() {
		return panelPayment.getStartUploadButton();
	}

	@Override
	public boolean isOfflineValid() {
		return panelPayment.isOfflineValid();
	}

	@Override
	public void showUploaderWidget(boolean show) {
		panelPayment.showUploadPanel(show);
	}

	@Override
	public TransactionDto getOfflineTransactionoBject() {
		return panelPayment.getTransactionObject();
	}

	@Override
	public void bindOfflineTransaction(TransactionDto result) {
		panelPayment.bindOfflineTransaction(result);
	}

	@Override
	public void setCountries(List<Country> countries) {
		panelPayment.setCountries(countries);
	}

}