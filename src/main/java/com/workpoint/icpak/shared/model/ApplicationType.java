package com.workpoint.icpak.shared.model;

public enum ApplicationType implements Listable {

	NON_PRACTISING("NON-PRACTICING"), PRACTISING("PRACTICING"), PRACTISING_RT(
			"PRACTICING-RETIRED"), OVERSEAS("OVERSEAS"), ASSOCIATE("ASSOCIATE"), RETIRED(
			"RETIRED");

	private String displayName;

	private ApplicationType(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String getName() {
		return name();
	}

	public String getDisplayName() {
		return displayName;
	}
}
