package com.workpoint.icpak.shared.model;

public enum TrainingType implements Listable{

	BEFOREQUALIFYING("Before Qualifying"), AFTERQUALIFYING(
			"After Qualifying");

	private String displayName;

	TrainingType(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String getName() {
		return displayName;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}
}
