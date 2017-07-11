package com.icpak.rest.models.membership;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.icpak.rest.models.base.PO;

@Entity
@Table(name="`Application Form Academic`")
public class ApplicationFormAcademic extends PO{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="`timestamp`",columnDefinition="timestamp NOT NULL default current_timestamp")
	private Timestamp timestamp;
	
	@Column(nullable=false, name="`Line No_`")
	private int lineNo;
	
	@Column(nullable=false, name="`Application No_`", length=20)
	private String applicationNo;

	@Column(nullable=false, name="`Subject Code`", length=20)
	private String subjectCode;
	
	@Column(nullable=false, name="`Min Grade`", length=20)
	private String meanGrade;

	@Column(nullable=false, name="`Grade`", length=20)
	private String grade;
	
	public ApplicationFormAcademic() {
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

	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getMeanGrade() {
		return meanGrade;
	}

	public void setMeanGrade(String meanGrade) {
		this.meanGrade = meanGrade;
	}

	
}
