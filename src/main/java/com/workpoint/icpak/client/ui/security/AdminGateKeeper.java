package com.workpoint.icpak.client.ui.security;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;
import com.workpoint.icpak.client.util.AppContext;

@Singleton
public class AdminGateKeeper implements Gatekeeper{

	@Inject
	public AdminGateKeeper() {
	}
	
	@Override
	public boolean canReveal() {
		return AppContext.isLoggedIn() && AppContext.isCurrentUserAdmin();
	}
}
