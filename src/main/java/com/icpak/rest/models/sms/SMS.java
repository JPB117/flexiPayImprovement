package com.icpak.rest.models.sms;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.icpak.rest.models.base.PO;

@Entity
@Table(name = "sms")
public class SMS extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String subject;

	private String pin;

	private String telNo;
	
	public SMS() {
	}

	public SMS(String subject, String to, String pin) {
		this.pin = pin;
		this.subject = subject;
		this.telNo = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

}
