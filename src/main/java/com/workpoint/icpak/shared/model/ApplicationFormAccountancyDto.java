package com.workpoint.icpak.shared.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationFormAccountancyDto extends SerializableObj {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String examiningBody;
	private String registrationNo;
	private String sectionPassed;
	private Date datePassed;
	private String applicationRefId;
	private List<AttachmentDto> attachments = new ArrayList<>();

	public List<AttachmentDto> getAttachments() {
		return attachments;
	}

	public String getExaminingBody() {
		return examiningBody;
	}

	public void setExaminingBody(String examiningBody) {
		this.examiningBody = examiningBody;
	}

	public String getRegistrationNo() {
		return registrationNo;
	}

	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
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

	public void setAttachments(List<AttachmentDto> attachments) {
		this.attachments = attachments;
	}

	public String getApplicationRefId() {
		return applicationRefId;
	}

	public void setApplicationRefId(String applicationRefId) {
		this.applicationRefId = applicationRefId;
	}

}
