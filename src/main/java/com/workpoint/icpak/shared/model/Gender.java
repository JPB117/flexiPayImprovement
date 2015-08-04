package com.workpoint.icpak.shared.model;

public enum Gender implements Listable{

	MALE,
	FEMALE;

	@Override
	public String getName() {
		return name();
	}

	@Override
	public String getDisplayName() {
		return name();
	}
	
	
}
