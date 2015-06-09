package com.workpoint.icpak.shared.model;

import net.sf.ehcache.search.aggregator.Count;


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
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null || ! (obj instanceof Country) || ((Country)obj).code==null){
			return false;
		}
		
		return code.equals(((Country)obj).code);
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	
}
