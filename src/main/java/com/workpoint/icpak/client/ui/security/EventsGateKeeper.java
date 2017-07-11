package com.workpoint.icpak.client.ui.security;

import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;
import com.workpoint.icpak.client.util.AppContext;

public class EventsGateKeeper implements Gatekeeper {

	@Inject
	public EventsGateKeeper() {
	}

	@Override
	public boolean canReveal() {
		return AppContext.isLoggedIn()
				&& (AppContext.isCurrentUserEventEdit()
						|| AppContext.isCurrentUserEventRead()
						|| AppContext.isCurrentUserFinanceEvents() || AppContext
							.isCurrentUserFinanceEdit());
	}
}
