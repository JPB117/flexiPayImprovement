package com.workpoint.icpak.client.ui.payment.collective.collectivepayments;

import javax.inject.Inject;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.payment.widget.PaymentWidget;
import com.workpoint.icpak.client.ui.profile.ProfilePresenter;
import com.workpoint.icpak.client.ui.profile.paysubscription.PaymentSubscription;

public class CollectivePaymentsView extends ViewImpl implements
		CollectivePaymentsPresenter.MyView {
	interface Binder extends UiBinder<Widget, CollectivePaymentsView> {
	}

	@UiField
	PaymentSubscription divPaymentWidget;
	@UiField
	HTMLPanel panelSubscription;
	@UiField
	HTMLPanel panelPayment;
	@UiField
	ActionLink aProceed;

	@Inject
	CollectivePaymentsView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
		divPaymentWidget.showCollectivePaymentSection(true);

	}

	public HasClickHandlers getProceedButton() {
		return aProceed;
	}

	public PaymentSubscription getSubscriptionWidget() {
		return divPaymentWidget;
	}

	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if (slot == ProfilePresenter.PAYMENTS_SLOT) {
			panelSubscription.clear();
			if (content != null) {
				panelSubscription.add(content);
			}
			super.setInSlot(slot, content);
		}
	}

}