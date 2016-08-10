package com.workpoint.icpak.client.ui.payment.collective.collectivepayments;

import javax.inject.Inject;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.client.ui.profile.paysubscription.PaymentSubscription;

public class CollectivePaymentsView extends ViewImpl implements CollectivePaymentsPresenter.MyView {
	interface Binder extends UiBinder<Widget, CollectivePaymentsView> {
	}

	@UiField
	PaymentSubscription divPaymentWidget;
	@UiField
	HTMLPanel panelSubscription;
	@UiField
	HTMLPanel panelWizardContainer;
	@UiField
	HTMLPanel divContainer;
	@UiField
	HTMLPanel panelMiddleContainer;

	@UiField
	HTMLPanel divHeaderContainer;

	@UiField
	IssuesPanel issuesPanel;

	@UiField
	HTMLPanel panelPayment;
	@UiField
	ActionLink aProceed;
	@UiField
	ActionLink aBackToPage1;

	@UiField
	Element elBackHeading;

	@UiField
	Element elTopLegend;

	@Inject
	CollectivePaymentsView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
		divPaymentWidget.showCollectivePaymentSection(true);
		// showSubscriptionPanel(false);
		aBackToPage1.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showSubscriptionPanel(true);
			}
		});

	}

	public ActionLink getProceedButton() {
		return aProceed;
	}

	public PaymentSubscription getSubscriptionWidget() {
		return divPaymentWidget;
	}

	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if (slot == CollectivePaymentsPresenter.PAYMENTS_SLOT) {
			panelPayment.clear();
			if (content != null) {
				panelPayment.add(content);
			}
			super.setInSlot(slot, content);
		}
	}

	@Override
	public void showSubscriptionPanel(boolean show) {
		if (show) {
			panelSubscription.removeStyleName("hide");
			panelPayment.addStyleName("hide");
			elBackHeading.addClassName("hide");
			panelWizardContainer.setStyleName("col-md-6 col-md-offset-3 wizard-container set-full-height");
		} else {
			panelPayment.removeStyleName("hide");
			panelSubscription.addStyleName("hide");
			elBackHeading.removeClassName("hide");
			panelWizardContainer.setStyleName("col-md-8 col-md-offset-2 wizard-container set-full-height");
		}
	}

	public void showError(String error) {
		issuesPanel.clear();
		if (!error.isEmpty()) {
			issuesPanel.removeStyleName("hide");
			issuesPanel.addError(error);
		} else {
			issuesPanel.addStyleName("hide");
		}
	}

	public void setLegendText(String title) {
		elTopLegend.setInnerText(title);
	}

	@Override
	public void showmask(boolean processing) {
		if (processing) {
			divContainer.addStyleName("whirl traditional");
		} else {
			divContainer.removeStyleName("whirl traditional");
		}
	}

	@Override
	public void setMiddleHeight() {
		int totalHeight = Window.getClientHeight();
		int topHeight = divHeaderContainer.getOffsetHeight();
		int middleHeight = totalHeight - topHeight;

		// Window.alert("\nTotalHeight:" + totalHeight + "MiddleHeight>>" +
		// middleHeight + "TopHeight" + topHeight);

		if (middleHeight > 0) {
			panelMiddleContainer.setHeight(middleHeight + "px");
		}
	}

}