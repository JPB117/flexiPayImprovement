package com.icpak.rest.models.util;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.icpak.rest.models.base.PO;

@Entity
public class EnquiriesDialogue extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String fromMemberRefId;// repondednt to an inquiry
	private String text;// the inqury response.

	@ManyToOne
	@JoinColumn(name = "inquiry_id")
	private Enquiries enquiry;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Enquiries getEnquiry() {
		return enquiry;
	}

	public void setEnquiry(Enquiries enquiry) {
		this.enquiry = enquiry;
	}

	public String getFromMemberRefId() {
		return fromMemberRefId;
	}

	public void setFromMemberRefId(String fromMemberRefId) {
		this.fromMemberRefId = fromMemberRefId;
	}

}
