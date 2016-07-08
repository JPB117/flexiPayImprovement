package com.workpoint.icpak.shared.model;

public enum ApplicationType implements Listable {

	MEMBER("MEMBER"), NON_PRACTISING("NON-PRACTICING"),PRAC_MEMBER("PRAC-MEMBER"), FOREIGN("FOREIGN"), 
	NEWPRAC("NEWPRAC"), PRACTISING(	"PRACTICING"), PRACTISING_RT("PRACTICING-RETIRED"), 
	OVERSEAS("OVERSEAS"), ASSOCIATE("ASSOCIATE"), RETIRED("RETIRED");

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
