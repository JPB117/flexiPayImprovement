package com.workpoint.icpak.shared.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.icpak.rest.models.base.PO;

public class EnquiriesDialogueDto{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String fromRefId;// repondednt to an inquiry
	private String memberRefId;// owner of inquiry
	private String text;// the inqury response.
		

	public String getFromRefId() {
		return fromRefId;
	}

	public void setFromRefId(String fromRefId) {
		this.fromRefId = fromRefId;
	}

	public String getMemberRefId() {
		return memberRefId;
	}

	public void setMemberRefId(String memberRefId) {
		this.memberRefId = memberRefId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
