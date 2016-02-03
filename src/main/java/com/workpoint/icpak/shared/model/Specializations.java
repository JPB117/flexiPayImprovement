package com.workpoint.icpak.shared.model;

public enum Specializations {

	AUDIT(SpecializationCategory.PRACTISE, "AUD"), TAXATION(
			SpecializationCategory.PRACTISE, "TAX"), FINANCIAL(
			SpecializationCategory.PRACTISE, "FC"), HRCONSULTANCY(
			SpecializationCategory.PRACTISE, "HR COUNLTANCY"),

	BANKING(SpecializationCategory.COMMERCEANDINDUSTRY, "BK"), FINANCE(
			SpecializationCategory.COMMERCEANDINDUSTRY, "FIN"), INSURANCE(
			SpecializationCategory.COMMERCEANDINDUSTRY, "IN"), MANUFACTURING(
			SpecializationCategory.COMMERCEANDINDUSTRY, "MA"), HOTEL(
			SpecializationCategory.COMMERCEANDINDUSTRY, "HO"), OTHER(
			SpecializationCategory.COMMERCEANDINDUSTRY, "OS"),

	CENTRALGVT(SpecializationCategory.PUBLICSECTOR, "PUBS"), LOCALGVT(
			SpecializationCategory.PUBLICSECTOR, "LG"), STATECOOP(
			SpecializationCategory.PUBLICSECTOR, "STC"), COOP(
			SpecializationCategory.PUBLICSECTOR, "CO-OP"),

	EDUCATION(SpecializationCategory.TRAINING, "ED"), NGO(
			SpecializationCategory.TRAINING, "NGOS"),

	PRACTICE(SpecializationCategory.EMPLOYMENT, "PR"), COMMERCE(
			SpecializationCategory.EMPLOYMENT, "COM"), PUBLICSECTOR(
			SpecializationCategory.EMPLOYMENT, "CO-OP"), PRIVATESECTOR(
			SpecializationCategory.EMPLOYMENT, "PRS"), NONPROFIT(
			SpecializationCategory.EMPLOYMENT, "NPS"), BANKINGANDFINANCE(
			SpecializationCategory.EMPLOYMENT, "BF"), EDUCATIONANDTRAINING(
			SpecializationCategory.EMPLOYMENT, "ED"), ;

	private SpecializationCategory category;
	private String code;

	private Specializations(SpecializationCategory category, String code) {
		this.category = category;
		this.code = code;

	}

	public SpecializationCategory getCategory() {
		return category;
	}

	public String getCode() {
		return code;
	}
}
