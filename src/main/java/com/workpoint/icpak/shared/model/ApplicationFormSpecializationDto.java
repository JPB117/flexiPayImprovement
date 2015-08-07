package com.workpoint.icpak.shared.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationFormSpecializationDto extends SerializableObj {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String specializationName;
	private SpecializationCategory category;
	
	public ApplicationFormSpecializationDto() {
	}

	public String getSpecializationName() {
		return specializationName;
	}

	public void setSpecializationName(String specializationName) {
		this.specializationName = specializationName;
	}

	public SpecializationCategory getCategory() {
		return category;
	}

	public void setCategory(SpecializationCategory category) {
		this.category = category;
	}

}
