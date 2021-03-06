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
	private String code;
	private String applicationNo;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

}
