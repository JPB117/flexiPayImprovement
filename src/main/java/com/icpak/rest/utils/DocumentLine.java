package com.icpak.rest.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentLine {

	String name;
	Map<String,Object> values  = new HashMap<String, Object>();
	
	public DocumentLine(String name,Map<String, Object> values) {
		this.values = values;
		this.name = name;
	}

	public Map<String, Object> getValues() {
		return values;
	}

	public void setValues(Map<String, Object> values) {
		this.values = values;
	}

	public List<HashMap<String, Object>> getDetail(String gridName) {

		return (List<HashMap<String, Object>>) values.get(gridName);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
