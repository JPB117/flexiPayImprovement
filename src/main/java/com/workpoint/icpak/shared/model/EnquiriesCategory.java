package com.workpoint.icpak.shared.model;

public enum EnquiriesCategory implements Listable{

	COMPLAINT("Complaint"),
	COMMENT("Comment"),
	SUGGESTION("Suggestion");
	
	private String displayName;

	private EnquiriesCategory(String displayName) {
		this.displayName = displayName;
		
	}

	public String getDisplayName() {
		return displayName;
	}

	@Override
	public String getName() {
		return name();
	}
}
