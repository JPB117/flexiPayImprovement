package com.workpoint.icpak.shared.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationFormSpecializationDto extends SerializableObj {

	private enum SpecializationCategory {
		PRACTISE("Practise"), COMMERCEANDINDUSTRY("Commerce and Industry"), PUBLICSECTOR(
				"Public Sector"), TRAINING("Training");

		private String category;

		SpecializationCategory(String category) {
			this.category = category;
		}

		public String getCategory() {
			return category;
		}

	}

	private String specializationName;
	private SpecializationCategory category;

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
