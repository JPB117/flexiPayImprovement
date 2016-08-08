package com.workpoint.icpak.client.ui.profile.paysubscription;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.ui.util.NumberUtils;

public class PaymentSubscription extends Composite {

	private static PaymentSubscriptionUiBinder uiBinder = GWT.create(PaymentSubscriptionUiBinder.class);

	@UiField
	HTMLPanel panelParent1;
	@UiField
	Element elCurrentBalance;
	@UiField
	TextField txtAmountToPay;
	@UiField
	TextField txtMemberNo;
	@UiField
	Element divMemberBalance;
	@UiField
	Element divMemberNo;

	interface PaymentSubscriptionUiBinder extends UiBinder<Widget, PaymentSubscription> {
	}

	public PaymentSubscription() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setCurrentBalance(Double amount) {
		elCurrentBalance.setInnerText(NumberUtils.CURRENCYFORMAT.format(amount));
	}

	public String getAmountToPay() {
		return txtAmountToPay.getValue();
	}

	public String getMemberNo() {
		return txtMemberNo.getValue();
	}

	public void showCollectivePaymentSection(boolean show) {
		if (show) {
			divMemberNo.removeClassName("hide");
			divMemberBalance.addClassName("hide");
			panelParent1.setStyleName("col-lg-12");
		} else {
			divMemberNo.addClassName("hide");
			divMemberBalance.removeClassName("hide");
			panelParent1.setStyleName("col-lg-8");
		}
	}

}
