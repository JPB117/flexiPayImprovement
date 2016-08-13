package com.icpak.rest.models.sms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.icpak.rest.models.base.PO;

@Entity
@Table(name = "smsLog", indexes = { @Index(columnList = "smsId", name = "idx_smsLog_smsId") })
public class SMSLog extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date tstamp;

	private String smsId;
	@Type(type = "text")
	private String text;
	private String smsFrom;
	private String smsTo;
	private Double cost;
	@Column(columnDefinition = "varchar(100) default 'FAILED'")
	@Enumerated(EnumType.STRING)
	private SmsStatus status;
	@Enumerated(EnumType.STRING)
	private FailureReason failureReason;

	public SMSLog() {
	}

	public Date getTstamp() {
		return tstamp;
	}

	public void setTstamp(Date tstamp) {
		this.tstamp = tstamp;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public SmsStatus getStatus() {
		return status;
	}

	public void setStatus(SmsStatus status) {
		this.status = status;
	}

	public String getSmsId() {
		return smsId;
	}

	public void setSmsId(String smsId) {
		this.smsId = smsId;
	}

	public String getSmsFrom() {
		return smsFrom;
	}

	public void setSmsFrom(String smsFrom) {
		this.smsFrom = smsFrom;
	}

	public String getSmsTo() {
		return smsTo;
	}

	public void setSmsTo(String smsTo) {
		this.smsTo = smsTo;
	}

	public FailureReason getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(FailureReason failureReason) {
		this.failureReason = failureReason;
	}

}
