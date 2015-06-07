package com.workpoint.icpak.client.ui.events.registration;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.workpoint.icpak.client.place.NameTokens;
import com.workpoint.icpak.client.ui.events.delegates.DelegatesPresenter;

public class EventBookingPresenter extends
		Presenter<EventBookingPresenter.MyView, EventBookingPresenter.MyProxy> {

	public interface MyView extends View {

	}

	@ProxyCodeSplit
	@NameToken(NameTokens.eventBooking)
	public interface MyProxy extends ProxyPlace<EventBookingPresenter> {
	}

	@ContentSlot
	public static final Type<RevealContentHandler<?>> DELEGATE_SLOT = new Type<RevealContentHandler<?>>();

	@Inject
	PlaceManager placeManager;

	@Inject
	DelegatesPresenter delegatePresenter;
	

	@Inject
	public EventBookingPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealRootContentEvent.fire(this, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		
		setInSlot(DELEGATE_SLOT, delegatePresenter);
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);

	}

	@Override
	protected void onReset() {
		super.onReset();
	}

}
