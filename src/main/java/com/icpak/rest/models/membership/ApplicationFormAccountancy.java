package com.icpak.rest.models.membership;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.icpak.rest.models.base.PO;
import com.icpak.rest.models.util.Attachment;
import com.workpoint.icpak.shared.model.ApplicationFormAccountancyDto;
import com.workpoint.icpak.shared.model.AttachmentDto;

@Entity
public class ApplicationFormAccountancy extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column
	private String examinationBody;
	@Column
	private String registrationNo;
	@Column(name = "`ApplicationRefId`", length = 20)
	private String applicationRefId;
	@Column
	private String sectionPassed;
	@Column
	private Date datePassed;
	@OneToMany(mappedBy = "applicationAccountancy", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE })
	private Set<Attachment> attachments = new HashSet<>();

	public String getExaminationBody() {
		return examinationBody;
	}

	public void setExaminationBody(String examinationBody) {
		this.examinationBody = examinationBody;
	}

	public String getRegistrationNo() {
		return registrationNo;
	}

	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}

	public String getApplicationRefId() {
		return applicationRefId;
	}

	public void setApplicationRefId(String applicationRefId) {
		this.applicationRefId = applicationRefId;
	}

	public String getSectionPassed() {
		return sectionPassed;
	}

	public void setSectionPassed(String sectionPassed) {
		this.sectionPassed = sectionPassed;
	}

	public Date getDatePassed() {
		return datePassed;
	}

	public void setDatePassed(Date datePassed) {
		this.datePassed = datePassed;
	}

	public ApplicationFormAccountancy() {
	}

	public ApplicationFormAccountancyDto toDto() {
		ApplicationFormAccountancyDto dto = new ApplicationFormAccountancyDto();
		dto.setRefId(refId);
		dto.setExaminingBody(examinationBody);
		dto.setRegistrationNo(registrationNo);
		dto.setSectionPassed(sectionPassed);
		dto.setDatePassed(datePassed);
		dto.setApplicationRefId(applicationRefId);
		List<AttachmentDto> attachmentDtos = new ArrayList<AttachmentDto>();
		for (Attachment attachment : attachments) {
			AttachmentDto attachmentDto = new AttachmentDto();
			attachmentDto.setAttachmentName(attachment.getName());
			attachmentDto.setRefId(attachment.getRefId());
			attachmentDtos.add(attachmentDto);
		}
		dto.setAttachments(attachmentDtos);
		return dto;
	}

	public void copyFrom(ApplicationFormAccountancyDto dto) {
		setExaminationBody(dto.getExaminingBody());
		setRegistrationNo(dto.getRegistrationNo());
		setSectionPassed(dto.getSectionPassed());
		setDatePassed(dto.getDatePassed());
	}

}
