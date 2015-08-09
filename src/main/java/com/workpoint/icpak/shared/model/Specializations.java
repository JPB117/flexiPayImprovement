package com.workpoint.icpak.shared.model;

public enum Specializations {

	AUDIT(SpecializationCategory.PRACTISE),
	TAXATION(SpecializationCategory.PRACTISE),
	FINANCIAL(SpecializationCategory.PRACTISE);
	
	private SpecializationCategory category;

	private Specializations(SpecializationCategory category) {
		this.category = category;
		
	}

	public SpecializationCategory getCategory() {
		return category;
	}
}
