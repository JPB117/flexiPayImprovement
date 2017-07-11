package com.workpoint.icpak.shared.model;

import java.io.Serializable;

public enum PaymentType implements Serializable, Listable {
	BOOKING("Booking Payments"), REGISTRATION("Registration Payments"), SUBSCRIPTION(
			"Subscription Renewal Payments"), UNKNOWN("UnKnown Payments");
	private String displayName;

	PaymentType(String displayName) {
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
