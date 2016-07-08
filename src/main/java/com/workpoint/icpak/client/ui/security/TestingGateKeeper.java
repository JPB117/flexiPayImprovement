package com.workpoint.icpak.client.ui.security;

import java.util.Arrays;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;
import com.workpoint.icpak.client.util.AppContext;

@Singleton
public class TestingGateKeeper implements Gatekeeper {

	@Inject
	public TestingGateKeeper() {
	}

	String[] testingMemberNos = new String[] { "13727", "5305", "7087", "4906",
			"12073" };

	@Override
	public boolean canReveal() {
		boolean isContained = Arrays.asList(testingMemberNos).contains(
				(AppContext.getCurrentUser().getUser().getMemberNo()));
		return AppContext.isLoggedIn()
				&& (AppContext.isCurrentUserAdmin() || isContained);
	}
}
