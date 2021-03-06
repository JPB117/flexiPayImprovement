package com.workpoint.icpak.shared.model;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationFormEducationalDto extends SerializableObj {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int lineNo;
	private String applicationNo;
	private String applicationRefId;
	private String whereObtained;
	private CertificateAwarded certificateAwarded;
	private Date fromDate;
	private Date toDate;
	private String classDivisionAttained;
	private String examiningBody;
	private String qualificationDesc; // Msc, BSC, KCSE
	private String qualificationCode;
	private EduType type;
	private String regNo;
	private String sections;
	private String description;
	private List<AttachmentDto> attachments;

	public ApplicationFormEducationalDto() {
	}

	public int getLineNo() {
		return lineNo;
	}

	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getApplicationRefId() {
		return applicationRefId;
	}

	public void setApplicationRefId(String applicationRefId) {
		this.applicationRefId = applicationRefId;
	}

	public String getWhereObtained() {
		return whereObtained;
	}

	public void setWhereObtained(String whereObtained) {
		this.whereObtained = whereObtained;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getClassDivisionAttained() {
		return classDivisionAttained;
	}

	public void setClassDivisionAttained(String classDivisionAttained) {
		this.classDivisionAttained = classDivisionAttained;
	}

	public String getExaminingBody() {
		return examiningBody;
	}

	public void setExaminingBody(String examiningBody) {
		this.examiningBody = examiningBody;
	}

	public String getQualificationDesc() {
		return qualificationDesc;
	}

	public void setQualificationDesc(String qualificationDesc) {
		this.qualificationDesc = qualificationDesc;
	}

	public String getQualificationCode() {
		return qualificationCode;
	}

	public void setQualificationCode(String qualificationCode) {
		this.qualificationCode = qualificationCode;
	}

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	public String getSections() {
		return sections;
	}

	public void setSections(String sections) {
		this.sections = sections;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public EduType getType() {
		return type;
	}

	public void setType(EduType type) {
		this.type = type;
	}

	public List<AttachmentDto> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AttachmentDto> attachments) {
		this.attachments = attachments;
	}

	public CertificateAwarded getCertificateAwarded() {
		return certificateAwarded;
	}

	public void setCertificateAwarded(CertificateAwarded certificateAwarded) {
		this.certificateAwarded = certificateAwarded;
	}
}
