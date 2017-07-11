package com.workpoint.icpak.shared.model.events;

public enum AccommodationType {

	FB("FULLBOARD"),
	HB("HALFBOARD");
	
	String displayName;
	
	private AccommodationType(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
	
}
