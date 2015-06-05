package com.workpoint.icpak.shared.model;

public enum ApplicationType implements Listable{

	PRACTISING("Practising Member"),
	NON_PRACTISING("Non Practising"),
	OVERSEAS("Overseas Member"),
	ASSOCIATE("Associate Member");
	
	private String displayName;
	private ApplicationType(String displayName){
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
