package com.workpoint.icpak.client.ui.security;

import com.workpoint.icpak.client.util.AppContext;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;

@Singleton
public class LoginGateKeeper implements Gatekeeper {

	@Inject
	public LoginGateKeeper(){	
	}
	
	@Override
	public boolean canReveal() {
		
		return AppContext.isValid();
	}

}
