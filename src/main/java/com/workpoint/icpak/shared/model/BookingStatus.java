package com.workpoint.icpak.shared.model;

import java.io.Serializable;

public enum BookingStatus implements Serializable, Listable {
	ACTIVE("Active"), CANCELLED("Cancelled");
	private String displayName;

	BookingStatus(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	@Override
	public String getName() {
		return name();
	}
}
