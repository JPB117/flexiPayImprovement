package com.workpoint.icpak.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class PaymentCompletedEvent extends
		GwtEvent<PaymentCompletedEvent.PaymentCompletedHandler> {

	public static Type<PaymentCompletedHandler> TYPE = new Type<PaymentCompletedHandler>();

	public interface PaymentCompletedHandler extends EventHandler {
		void onPaymentCompleted(PaymentCompletedEvent event);
	}

	public PaymentCompletedEvent() {
	}

	@Override
	protected void dispatch(PaymentCompletedHandler handler) {
		handler.onPaymentCompleted(this);
	}

	@Override
	public Type<PaymentCompletedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<PaymentCompletedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new PaymentCompletedEvent());
	}
}
