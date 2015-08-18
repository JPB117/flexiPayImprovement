package com.workpoint.icpak.shared.model;

public enum ApplicationType implements Listable {

	NON_PRACTISING("Non Practising"), PRACTISING("Practising Member"), PRACTISING_RT(
			"Practising Member"), OVERSEAS("Overseas Member"), ASSOCIATE(
			"Associate Member"), RETIRED("Retired Member");

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
