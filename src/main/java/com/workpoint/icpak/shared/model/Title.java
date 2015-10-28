package com.workpoint.icpak.shared.model;

public enum Title implements Listable{

	Mr(38), 
	Miss(39), 
	Mrs(40),
	Dr(42); 
	
	private int code;//ICPAK LMS Code
	private Title(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
	@Override
	public String getDisplayName() {
		return name();
	}
	
	@Override
	public String getName() {
		return name();
	}
	
}
