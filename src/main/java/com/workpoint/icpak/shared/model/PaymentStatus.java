package com.workpoint.icpak.shared.model;

import java.io.Serializable;

public enum PaymentStatus implements Serializable {
	NOTPAID("Not Paid"), PAID("Paid");

	private String displayName;

	PaymentStatus(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
