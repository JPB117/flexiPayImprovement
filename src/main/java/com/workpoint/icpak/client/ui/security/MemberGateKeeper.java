package com.workpoint.icpak.client.ui.security;

import com.gwtplatform.mvp.client.proxy.Gatekeeper;
import com.workpoint.icpak.client.util.AppContext;

public class MemberGateKeeper implements Gatekeeper {

	@Override
	public boolean canReveal() {
		return AppContext.isLoggedIn() && !AppContext.isCurrentUserAdmin()
				&& AppContext.isCurrentUserMember();
	}
}
