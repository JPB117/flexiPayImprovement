package com.workpoint.icpak.client.ui.login.createpassword;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.client.RestDispatch;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealRootLayoutContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.workpoint.icpak.client.place.NameTokens;
import com.workpoint.icpak.client.security.CurrentUser;
import com.workpoint.icpak.shared.api.SessionResource;
import com.workpoint.icpak.shared.api.UsersResource;

public class CreatePasswordPresenter
		extends
		Presenter<CreatePasswordPresenter.ILoginView, CreatePasswordPresenter.ILoginProxy> {

	public interface ILoginView extends View {

	}

	@ProxyCodeSplit
	@NameToken(NameTokens.createpassword)
	@NoGatekeeper
	public interface ILoginProxy extends ProxyPlace<CreatePasswordPresenter> {
	}

	@Inject
	RestDispatch requestHelper;

	@Inject
	PlaceManager placeManager;

	@Inject
	public CreatePasswordPresenter(final EventBus eventBus,
			final ILoginView view, final ILoginProxy proxy,
			final CurrentUser currentUser,
			ResourceDelegate<UsersResource> usersDelegate,
			ResourceDelegate<SessionResource> sessionResource) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealRootLayoutContentEvent.fire(this, this);
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
	}

	@Override
	protected void onReveal() {
	}

	@Override
	protected void onBind() {
		super.onBind();

	}

	protected void onReset() {
	};

}