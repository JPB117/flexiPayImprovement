package com.workpoint.icpak.shared.model;

public enum CPDCategory {

	CATEGORY_A("Category A"),
	CATEGORY_B("Category B"),
	CATEGORY_C("Category C"),
	CATEGORY_D("Category D");
	
	
	private String displayName;

	private CPDCategory(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
	
}
