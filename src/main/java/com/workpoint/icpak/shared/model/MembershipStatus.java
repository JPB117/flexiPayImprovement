package com.workpoint.icpak.shared.model;

public enum MembershipStatus {
	DRAFTED("Drafted"), ACTIVE("Active"), RETIRED("Retired"), DEREGISTERED(
			"De-Registered");

	private String displayName;

	private MembershipStatus(String displayName) {
		this.displayName = displayName;

	}

	public String getDisplayName() {
		return displayName;
	}
}
