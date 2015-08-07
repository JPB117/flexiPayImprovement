package com.workpoint.icpak.shared.model;

import java.util.Date;

public class CPDDto extends SerializableObj{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date created;
	private Date startDate; //Copied from Event details;
	private Date endDate;
	private String title;
	private String organizer;
	private CPDCategory category;
	private int cpdHours;
	private CPDStatus status;
	private String memberId;
	private String fullNames;
	private String eventId;
	
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

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
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

	public int getCpdHours() {
		return cpdHours;
	}

	public void setCpdHours(int cpdHours) {
		this.cpdHours = cpdHours;
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
}
