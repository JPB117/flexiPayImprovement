package com.icpak.rest.models.membership;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.icpak.rest.models.base.PO;

@Entity
@Table(name="`Application Form Employment`")
public class ApplicationFormEmployment extends PO{

	
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
	
	@Column(nullable=false, name="`Organisation`", length=120)
	private String organization;

	@Column(nullable=false, name="`Job Title`", length=30)
	private String jobTitle;

	@Column(nullable=false, name="`From`", columnDefinition="datetime")
	private Date fromDate;

	@Column(nullable=false, name="`To Date`", columnDefinition="datetime")
	private Date toDate;

	@Column(nullable=false, name="`Training_Tasks`", length=250)
	private String trainingTasks;
	
	public ApplicationFormEmployment() {
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

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
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

	public String getTrainingTasks() {
		return trainingTasks;
	}

	public void setTrainingTasks(String trainingTasks) {
		this.trainingTasks = trainingTasks;
	}
}
