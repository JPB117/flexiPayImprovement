package com.workpoint.icpak.shared.model;

public enum CPDAction implements Listable {

	APPROVED("Approve"), REJECTED("Reject");

	private String displayName;

	private CPDAction(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String getName() {
		return name();
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

}
