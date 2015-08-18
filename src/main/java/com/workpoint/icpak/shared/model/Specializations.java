package com.workpoint.icpak.shared.model;

public enum Specializations {

	AUDIT(SpecializationCategory.PRACTISE), TAXATION(
			SpecializationCategory.PRACTISE), FINANCIAL(
			SpecializationCategory.PRACTISE), HRCONSULTANCY(
			SpecializationCategory.PRACTISE),

	BANKING(SpecializationCategory.COMMERCEANDINDUSTRY), FINANCE(
			SpecializationCategory.COMMERCEANDINDUSTRY), INSURANCE(
			SpecializationCategory.COMMERCEANDINDUSTRY), MANUFACTURING(
			SpecializationCategory.COMMERCEANDINDUSTRY), HOTEL(
			SpecializationCategory.COMMERCEANDINDUSTRY), OTHER(
			SpecializationCategory.COMMERCEANDINDUSTRY),

	CENTRALGVT(SpecializationCategory.PUBLICSECTOR), LOCALGVT(
			SpecializationCategory.PUBLICSECTOR), STATECOOP(
			SpecializationCategory.PUBLICSECTOR), COOP(
			SpecializationCategory.PUBLICSECTOR),

	EDUCATION(SpecializationCategory.TRAINING), NGO(
			SpecializationCategory.TRAINING)

	;

	private SpecializationCategory category;

	private Specializations(SpecializationCategory category) {
		this.category = category;

	}

	public SpecializationCategory getCategory() {
		return category;
	}
}
