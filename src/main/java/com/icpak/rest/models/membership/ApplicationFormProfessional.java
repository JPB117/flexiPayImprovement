package com.icpak.rest.models.membership;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.icpak.rest.models.base.PO;

@Entity
@Table(name="`Application Form Professional`")
public class ApplicationFormProfessional extends PO{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="`timestamp`",columnDefinition="timestamp NOT NULL default current_timestamp")
	private Timestamp timestamp;
	
	@Column(nullable = false, name = "`Line No_`")
	private int lineNo;

	@Column(nullable = false, name = "`Application No_`", length = 20)
	private String applicationNo;
	
	@Column(nullable = false, name = "`Examining Body`", length = 100)
	private String examiningBody;
	
	@Column(nullable = false, name = "`From Date`", columnDefinition = "datetime")
	private Date fromDate;

	@Column(nullable = false, name = "`To Date`", columnDefinition = "datetime")
	private Date toDate;
	
	@Column(nullable = false, name = "`Registration No_`", length = 20)
	private String regNo;
	
	@Column(nullable = false, name = "`Sections Passed`", length = 20)
	private String sectionsPassed;
	
	@Column(nullable = false, name = "`Date Passed`", columnDefinition = "datetime")
	private Date datePassed;
	
	public ApplicationFormProfessional() {
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

	public String getExaminingBody() {
		return examiningBody;
	}

	public void setExaminingBody(String examiningBody) {
		this.examiningBody = examiningBody;
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

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	public String getSectionsPassed() {
		return sectionsPassed;
	}

	public void setSectionsPassed(String sectionsPassed) {
		this.sectionsPassed = sectionsPassed;
	}

	public Date getDatePassed() {
		return datePassed;
	}

	public void setDatePassed(Date datePassed) {
		this.datePassed = datePassed;
	}
}
