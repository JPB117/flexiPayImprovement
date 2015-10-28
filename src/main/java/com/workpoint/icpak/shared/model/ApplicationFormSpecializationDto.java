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
	
	private Specializations specialization;
	
	public ApplicationFormSpecializationDto() {
	}

	public ApplicationFormSpecializationDto(Specializations specialization) {
		this.specialization = specialization;
	}

	public Specializations getSpecialization() {
		return specialization;
	}

	public void setSpecialization(Specializations specialization) {
		this.specialization = specialization;
	}
	
}
