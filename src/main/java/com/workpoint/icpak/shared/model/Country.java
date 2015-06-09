package com.workpoint.icpak.shared.model;


public class Country extends SerializableObj implements Listable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String displayName;

	public Country() {
	}
	
	public Country(String code,String displayName) {
		this.code = code;
		this.displayName = displayName;
	}
	
	@Override
	public String getName() {
		return code;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}
	
	@Override
	public String toString() {
		
		return "{name:"+code+",displayName:"+displayName+"}";
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	
}
