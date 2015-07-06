package com.icpak.rest.models.membership;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.icpak.rest.models.base.PO;
import com.workpoint.icpak.shared.model.ApplicationFormEducationalDto;
import com.workpoint.icpak.shared.model.EduType;

@Entity
@Table(name = "`Application Form Educational`")
public class ApplicationFormEducational extends PO {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "`timestamp`", columnDefinition = "timestamp NOT NULL default current_timestamp")
	private Timestamp timestamp;

	@Column( name = "`Line No_`")
	private int lineNo;

	@Column( name = "`Application No_`", length = 20)
	private String applicationNo;
	
	@Column( name = "`ApplicationRefId`", length = 20)
	private String applicationRefId;

	@Column( name = "`Where Obtained`", length = 100)
	private String whereObtained;

	@Column( name = "`From Date`", columnDefinition = "datetime")
	private Date fromDate;

	@Column( name = "`To Date`", columnDefinition = "datetime")
	private Date toDate;

	@Column( name = "`Class_Division Attained`", length = 50)
	private String classDivisionAttained;

	@Column( name = "`Certificate Awarded`")
	private int certificateAwarded;

	@Column( name = "`Examining Body`", length = 50)
	private String examiningBody;

	@Column( name = "`Qualification Description`", length = 80)
	private String qualificationDesc;

	@Column( name = "`Qualification code`", length = 20)
	private String qualificationCode;

//	@Column( name = "`Type`")
//	private int type;
	
	@Enumerated(EnumType.ORDINAL)
	@Column( name = "`Type`")
	private EduType type;

	@Column( name = "`Reg_No`", length = 15)
	private String regNo;

	@Column( name = "`Sections`", length = 30)
	private String sections;

	@Column( name = "`Description`", length = 50)
	private String description;

	public ApplicationFormEducational() {
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
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

	public int getCertificateAwarded() {
		return certificateAwarded;
	}

	public void setCertificateAwarded(int certificateAwarded) {
		this.certificateAwarded = certificateAwarded;
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

	public void copyFrom(ApplicationFormEducationalDto dto) {
		
		setCertificateAwarded(dto.getCertificateAwarded());
		setClassDivisionAttained(dto.getClassDivisionAttained());
		setDescription(dto.getDescription());
		setExaminingBody(dto.getExaminingBody());
		setFromDate(dto.getFromDate());
		setLineNo(dto.getLineNo());
		setQualificationCode(dto.getQualificationCode());
		setQualificationDesc(dto.getQualificationDesc());
		setRegNo(dto.getRegNo());
		setSections(dto.getSections());
		setToDate(dto.getToDate());
		setType(dto.getType());
		setWhereObtained(dto.getWhereObtained());
		//setApplicationNo(applicationNo);
		//setRefId(refId);
	}
	
	public ApplicationFormEducationalDto toDto() {
		ApplicationFormEducationalDto dto = new ApplicationFormEducationalDto();
		dto.setApplicationNo(applicationNo);
		dto.setApplicationRefId(applicationRefId);
		dto.setCertificateAwarded(certificateAwarded);
		dto.setClassDivisionAttained(classDivisionAttained);
		dto.setDescription(description);
		dto.setExaminingBody(examiningBody);
		dto.setFromDate(fromDate);
		dto.setLineNo(lineNo);
		dto.setQualificationCode(qualificationCode);
		dto.setQualificationDesc(qualificationDesc);
		dto.setRefId(applicationRefId);
		dto.setRegNo(regNo);
		dto.setSections(sections);
		dto.setToDate(toDate);
		dto.setType(type);
		dto.setWhereObtained(whereObtained);
		
		return dto;
	}

	public String getApplicationRefId() {
		return applicationRefId;
	}

	public void setApplicationRefId(String applicationRefId) {
		this.applicationRefId = applicationRefId;
	}

	public EduType getType() {
		return type;
	}

	public void setType(EduType type) {
		this.type = type;
	}


}
