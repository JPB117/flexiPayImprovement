package com.workpoint.icpak.shared.model;

import java.util.Date;

public class EnquiriesDto {

	private Date date;
	private String fullNames;
	private String memberRefId;
	private String membershipNo;
	private String emailAddress;
	private String phone;
	private EnquiriesCategory category=EnquiriesCategory.COMMENT;
	private String subject;
	private String message;
	private EnquiryStatus status;
	
	private String reply;
	private Date replyDate;
	private String userId;
	
	public EnquiriesDto() {
	}
	
	public String getFullNames() {
		return fullNames;
	}

	public void setFullNames(String fullNames) {
		this.fullNames = fullNames;
	}

	public String getMembershipNo() {
		return membershipNo;
	}

	public void setMembershipNo(String membershipNo) {
		this.membershipNo = membershipNo;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMemberRefId() {
		return memberRefId;
	}

	public void setMemberRefId(String memberRefId) {
		this.memberRefId = memberRefId;
	}

	public EnquiriesCategory getCategory() {
		return category;
	}

	public void setCategory(EnquiriesCategory category) {
		this.category = category;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public EnquiryStatus getStatus() {
		return status;
	}

	public void setStatus(EnquiryStatus status) {
		this.status = status;
	}

	public Date getReplyDate() {
		return replyDate;
	}

	public void setReplyDate(Date replyDate) {
		this.replyDate = replyDate;
	}
}
