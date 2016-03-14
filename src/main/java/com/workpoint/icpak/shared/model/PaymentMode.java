package com.workpoint.icpak.shared.model;

import java.io.Serializable;

public enum PaymentMode implements Serializable, Listable {
	MPESA("M-Pesa"), CARDS("Cards"), CHEQUE("Cheque"), DIRECTBANKING(
			"Direct Banking"), BANKTRANSFER("Bank Transfer");
	private String displayName;

	PaymentMode(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	@Override
	public String getName() {
		return displayName;
	}
}
