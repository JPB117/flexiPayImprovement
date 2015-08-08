package com.workpoint.icpak.shared.model;

public enum SpecializationCategory {

	PRACTISE("Practise"), 
	COMMERCEANDINDUSTRY("Commerce and Industry"),
	PUBLICSECTOR("Public Sector"), 
	TRAINING("Training");

	private String category;

	SpecializationCategory(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
	}

}
