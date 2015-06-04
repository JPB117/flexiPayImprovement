package com.icpak.rest.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Doc {

	Map<String,Object> values  = new HashMap<String, Object>();
	Map<String,List<DocumentLine>> details = new HashMap<>();
	
	public Doc(Map<String, Object> values) {
		this.values = values;
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

	public Map<String, List<DocumentLine>> getDetails() {
		return details;
	}

	public void setDetails(Map<String, List<DocumentLine>> details) {
		this.details = details;
	}
	
	public void addDetail(DocumentLine line){
		String name = line.getName();
		
		List<DocumentLine> lines = details.get(name);
		if(lines==null){
			lines = new ArrayList<DocumentLine>();
			details.put(name, lines);
		}
		
		lines.add(line);
	}

	public String getCaseNo() {
		return "";
	}

}
