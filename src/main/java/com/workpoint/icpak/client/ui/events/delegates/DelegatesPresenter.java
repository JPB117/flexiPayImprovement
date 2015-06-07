package com.workpoint.icpak.client.ui.events.delegates;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class DelegatesPresenter extends
		Presenter<DelegatesPresenter.MyView, DelegatesPresenter.MyProxy> {

	public interface MyView extends View {
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<DelegatesPresenter> {
	}

	@Inject
	PlaceManager placeManager;

	@Inject
	public DelegatesPresenter(final EventBus eventBus, final MyView view,
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
