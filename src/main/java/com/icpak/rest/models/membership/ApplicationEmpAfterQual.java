package com.icpak.rest.models.membership;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.icpak.rest.models.base.PO;

@Entity
@Table(name="`Application Emp After Qual`")
public class ApplicationEmpAfterQual extends PO{

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
	
	@Column(nullable=false, name="`Organisation`", length=30)
	private String organization;
	
	@Column(nullable=false, name="`Job Title`", length=30)
	private String jobTitle;

	@Column(nullable=false, name="`From`", columnDefinition="datetime")
	private Date from;

	@Column(nullable=false, name="`to`", columnDefinition="datetime")
	private String to;

	@Column(nullable=false, name="`Responsibilities`", length=250)
	private String responsibilities;
	
	public ApplicationEmpAfterQual() {
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

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getResponsibilities() {
		return responsibilities;
	}

	public void setResponsibilities(String responsibilities) {
		this.responsibilities = responsibilities;
	}
}
