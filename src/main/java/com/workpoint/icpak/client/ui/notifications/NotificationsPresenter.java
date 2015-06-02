package com.workpoint.icpak.client.ui.notifications;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.workpoint.icpak.client.ui.events.BeforeNotificationsLoadEvent;
import com.workpoint.icpak.client.ui.events.BeforeNotificationsLoadEvent.BeforeNotificationsLoadHandler;

public class NotificationsPresenter extends
		PresenterWidget<NotificationsPresenter.MyView> implements
		BeforeNotificationsLoadHandler {

	public interface MyView extends View {
	}

	@Inject
	DispatchAsync dispatcher;


	public static final Object NOTE_SLOT = new Object();

	@Inject
	public NotificationsPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(BeforeNotificationsLoadEvent.TYPE, this);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
	}

	
	@Override
	public void onBeforeNotificationsLoad(BeforeNotificationsLoadEvent event) {
		NotificationsPresenter.this.setInSlot(NOTE_SLOT, null);
	}
}
