package com.workpoint.icpak.shared.model;

public enum Gender implements Listable{

	MALE(22),
	FEMALE(23);

	private int code; //ICPAK LMS Code

	private Gender(int code) {
		this.code = code;
	}
	
	@Override
	public String getName() {
		return name();
	}
	@Override
	public String getDisplayName() {
		return name();
	}
	public int getCode() {
		return code;
	}
	
	
}
