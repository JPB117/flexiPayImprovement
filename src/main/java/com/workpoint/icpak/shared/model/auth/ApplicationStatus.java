package com.workpoint.icpak.shared.model.auth;

import com.workpoint.icpak.shared.model.Listable;

public enum ApplicationStatus implements Listable {
	SUBMITTED, PENDING, PROCESSING, APPROVED;

	@Override
	public String getName() {
		return name();
	}

	@Override
	public String getDisplayName() {
		return name();
	}
}
