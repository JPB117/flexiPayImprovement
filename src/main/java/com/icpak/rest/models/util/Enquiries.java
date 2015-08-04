package com.icpak.rest.models.util;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.icpak.rest.models.base.PO;
import com.workpoint.icpak.shared.model.EnquiriesCategory;
import com.workpoint.icpak.shared.model.EnquiriesDto;
import com.workpoint.icpak.shared.model.EnquiryStatus;

@Entity
public class Enquiries extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String fullNames;
	private String memberRefId;
	private String membershipNo;
	private String emailAddress;
	private String phone;
	
	@Enumerated(EnumType.STRING)
	private EnquiriesCategory category;
	private String subject;
	
	@Column(length=2500)
	private String message;
	
	private EnquiryStatus status;
	
	@Column(length=2500)
	private String reply;
	private Date replyDate;
	
	//Replied By
	private String userId;
	
	public Enquiries() {
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
	
	public void copyFrom(EnquiriesDto dto){
		setCategory(dto.getCategory());
		setEmailAddress(dto.getEmailAddress());
		setFullNames(dto.getFullNames());
		setMemberRefId(dto.getMemberRefId());
		setMembershipNo(dto.getMembershipNo());
		setMessage(dto.getMessage());
		setPhone(dto.getPhone());
		setSubject(dto.getSubject());
		setStatus(dto.getStatus()==null? EnquiryStatus.DRAFT: dto.getStatus());
		setReply(dto.getReply());
		setUserId(dto.getUserId());
		setReplyDate(dto.getReplyDate());
	}
	
	public EnquiriesDto toDto(){
		EnquiriesDto dto = new EnquiriesDto();
		dto.setCategory(category);
		dto.setEmailAddress(emailAddress);
		dto.setFullNames(fullNames);
		dto.setMemberRefId(memberRefId);
		dto.setMembershipNo(membershipNo);
		dto.setMessage(message);
		dto.setPhone(phone);
		dto.setSubject(subject);
		dto.setDate(getCreated());
		dto.setStatus(status);
		dto.setReplyDate(replyDate);
		dto.setReply(reply);
		dto.setUserId(userId);
		return dto;
	}

	public EnquiriesCategory getCategory() {
		return category;
	}

	public void setCategory(EnquiriesCategory category) {
		this.category = category;
	}

	public EnquiryStatus getStatus() {
		return status;
	}

	public void setStatus(EnquiryStatus status) {
		this.status = status;
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

	public Date getReplyDate() {
		return replyDate;
	}

	public void setReplyDate(Date replyDate) {
		this.replyDate = replyDate;
	}
}
