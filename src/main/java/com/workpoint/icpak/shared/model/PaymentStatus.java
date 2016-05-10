package com.workpoint.icpak.shared.model;

import java.io.Serializable;

public enum PaymentStatus implements Serializable, Listable {
	NOTPAID("Not Paid"), PAID("Paid"), Credit("Credit");

	private String displayName;

	PaymentStatus(String displayName) {
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
