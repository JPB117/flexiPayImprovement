package com.workpoint.icpak.shared.model;

import java.util.Date;

public class CPDDto extends SerializableObj {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date created;
	private Date startDate; // Copied from Event details;
	private Date endDate;
	private String title;
	private String organizer;
	private CPDCategory category;
	private double cpdHours;
	private CPDStatus status;
	private String memberRefId;
	private String fullNames;
	private String eventId;
	private String eventLocation;
	private String memberRegistrationNo;

	public CPDDto() {
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public CPDStatus getStatus() {
		return status;
	}

	public void setStatus(CPDStatus status) {
		this.status = status;
	}

	public String getMemberRefId() {
		return memberRefId;
	}

	public void setMemberRefId(String memberId) {
		this.memberRefId = memberId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOrganizer() {
		return organizer;
	}

	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}

	public CPDCategory getCategory() {
		return category;
	}

	public void setCategory(CPDCategory category) {
		this.category = category;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getFullNames() {
		return fullNames;
	}

	public void setFullNames(String fullNames) {
		this.fullNames = fullNames;
	}

	public void setEventId(String eventRefId) {
		this.eventId = eventRefId;
	}

	public String getEventId() {
		return eventId;
	}

	public double getCpdHours() {
		return cpdHours;
	}

	public void setCpdHours(double cpdHours) {
		this.cpdHours = cpdHours;
	}

	public String getEventLocation() {
		return eventLocation;
	}

	public void setEventLocation(String eventLocation) {
		this.eventLocation = eventLocation;
	}

	public String getMemberRegistrationNo() {
		return memberRegistrationNo;
	}

	public void setMemberRegistrationNo(String memberRegistrationNo) {
		this.memberRegistrationNo = memberRegistrationNo;
	}
}
