package com.workpoint.icpak.shared.model;

public class CPDFooterDto extends SerializableObj {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String description;
	private Double cpdUnits;

	public CPDFooterDto() {
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getCpdUnits() {
		return cpdUnits;
	}

	public void setCpdUnits(Double cpdUnits) {
		this.cpdUnits = cpdUnits;
	}

}
