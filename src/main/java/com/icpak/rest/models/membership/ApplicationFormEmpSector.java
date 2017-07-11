package com.icpak.rest.models.membership;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.icpak.rest.models.base.PO;
import com.workpoint.icpak.shared.model.ApplicationFormEmploymentDto;
import com.workpoint.icpak.shared.model.ApplicationFormSpecializationDto;
import com.workpoint.icpak.shared.model.Specializations;

@Entity
@Table(name = "`Application Form Emp Sector`")
public class ApplicationFormEmpSector extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "`timestamp`")
	private Timestamp timestamp;

	@Column(name = "`Description`", length = 50)
	private String description;

	@Enumerated(EnumType.STRING)
	private Specializations specialization;

	@Column(name = "`ApplicationRefId`", length = 20)
	private String applicationRefId;

	public ApplicationFormEmpSector() {
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ApplicationFormEmploymentDto toDto() {
		ApplicationFormEmploymentDto dto = new ApplicationFormEmploymentDto();
		dto.setRefId(getRefId());
		dto.setSpecialization(specialization);
		return dto;
	}

	public void copyFrom(ApplicationFormEmploymentDto eduEntry) {
		setSpecialization(eduEntry.getSpecialization());
	}

	public Specializations getSpecialization() {
		return specialization;
	}

	public void setSpecialization(Specializations specialization) {
		this.specialization = specialization;
	}

	public String getApplicationRefId() {
		return applicationRefId;
	}

	public void setApplicationRefId(String applicationRefId) {
		this.applicationRefId = applicationRefId;
	}

}
