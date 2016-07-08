package com.workpoint.icpak.client.ui.profile.paysubscription;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.ui.util.NumberUtils;

public class PaymentSubscription extends Composite {

	private static PaymentSubscriptionUiBinder uiBinder = GWT
			.create(PaymentSubscriptionUiBinder.class);

	@UiField
	Element elCurrentBalance;
	@UiField
	TextField txtAmountToPay;

	interface PaymentSubscriptionUiBinder extends
			UiBinder<Widget, PaymentSubscription> {
	}

	public PaymentSubscription() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setCurrentBalance(Double amount) {
		elCurrentBalance
				.setInnerText(NumberUtils.CURRENCYFORMAT.format(amount));
	}

	public String getAmountToPay() {
		return txtAmountToPay.getValue();
	}

}
