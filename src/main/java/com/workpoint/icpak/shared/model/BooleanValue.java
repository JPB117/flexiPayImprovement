package com.workpoint.icpak.shared.model;

import java.io.Serializable;

public class BooleanValue implements Value, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Boolean value;
	private String key;
	private Long id;
	
	public BooleanValue(){		
	}
	
	public BooleanValue(Long id, String key, Boolean value){
		this.id=id;
		this.key=key;
		this.value=value;
	}
	
	public BooleanValue(Boolean val){
		setValue(val);
	}

	@Override
	public void setValue(Object value) {
		this.value=(Boolean)value;
	}

	@Override
	public Object getValue() {
		return value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setValue(Boolean value) {
		this.value = value;
	}
	
	@Override
	public DataType getDataType() {
		return DataType.BOOLEAN;
	}
	
	public BooleanValue clone(boolean fullClone){
		Long identify=null;
		if(fullClone){
			identify = id;
		}
		BooleanValue bvalue = new BooleanValue(identify, key, value);
		return bvalue;
	}
	
	@Override
	public String toString() {
		return key+":"+value;
	}

}
