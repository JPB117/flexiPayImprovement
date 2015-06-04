package com.workpoint.icpak.client.place;

import com.google.gwt.place.shared.PlaceHistoryHandler.Historian;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManagerImpl;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.gwtplatform.mvp.shared.proxy.TokenFormatter;

public class ClientPlaceManager extends PlaceManagerImpl {

	private final PlaceRequest defaultPlaceRequest;

	@Inject
	public ClientPlaceManager(final EventBus eventBus,
			final TokenFormatter tokenFormatter,
			@DefaultPlace final String defaultPlaceNameToken,
			final Historian historian) {
		super(eventBus, tokenFormatter, historian);
		
		this.defaultPlaceRequest = new PlaceRequest.Builder().nameToken(defaultPlaceNameToken).build();
	}

	@Override
	public void revealDefaultPlace() {
		revealPlace(defaultPlaceRequest, true);
	}
	
	@Override
	public void revealErrorPlace(String invalidHistoryToken) {
		super.revealErrorPlace(NameTokens.error404);
	}
	
	@Override
	public void revealUnauthorizedPlace(String unauthorizedHistoryToken) {
		//Window.alert("Unauthorized place!!!!!!");
		PlaceRequest place = new PlaceRequest.Builder().nameToken("login")
				.with("redirect", unauthorizedHistoryToken).build();
		
		revealPlace(place,true);
	}
}
