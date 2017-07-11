package com.workpoint.icpak.shared.model;

public class StringValue implements Value{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String value;
	private String key;
	private Long id;
	
	public StringValue() {
	}
	
	public StringValue(Long id, String key, String value){
		this.id=id;
		this.key=key;
		this.value=value;
	}
	
	public StringValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = (value==null? "": value.toString());
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
	
	@Override
	public DataType getDataType() {
		return DataType.STRING;
	}
	
	public StringValue clone(boolean fullClone){
		
		Long identify=null;
		if(fullClone){
			identify = id;
		}
		StringValue svalue = new StringValue(identify, key, value);
		
		return svalue;
	}
	
	@Override
	public String toString() {
		return key+":"+value;
	}
}
