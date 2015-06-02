package com.workpoint.icpak.client.ui.security;

import com.workpoint.icpak.client.util.AppContext;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;

@Singleton
public class AdminGateKeeper implements Gatekeeper{

	@Inject
	public AdminGateKeeper() {
	}
	
	@Override
	public boolean canReveal() {
		return AppContext.isValid() && AppContext.isCurrentUserAdmin();
	}
}
