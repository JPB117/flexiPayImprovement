package com.workpoint.icpak.shared.model.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.workpoint.icpak.shared.model.SerializableObj;

public class DelegateDto extends SerializableObj{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String memberRegistrationNo;
	private String title;
	private String surname;
	private String otherNames;
	private String email;
	private AccommodationDto accommodation;
	private Double amount;
	private AttendanceStatus attendance;
	private DelegateType delegateType;
	private String bookingId;
	private String eventId;
	
	public DelegateDto() {
	}

	public String getMemberRegistrationNo() {
		return memberRegistrationNo;
	}

	public void setMemberRegistrationNo(String memberRegistrationNo) {
		this.memberRegistrationNo = memberRegistrationNo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getOtherNames() {
		return otherNames;
	}

	public void setOtherNames(String otherNames) {
		this.otherNames = otherNames;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public AccommodationDto getAccommodation() {
		return accommodation;
	}

	public void setAccommodation(AccommodationDto accommodation) {
		this.accommodation = accommodation;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public AttendanceStatus getAttendance() {
		return attendance;
	}

	public void setAttendance(AttendanceStatus attendance) {
		this.attendance = attendance;
	}

	@JsonIgnore
	public boolean isMember() {
		return delegateType==DelegateType.MEMBER;
	}

	public DelegateType getDelegateType() {
		return delegateType;
	}

	public void setDelegateType(DelegateType delegateType) {
		this.delegateType = delegateType;
	}

	public String getBookingId() {
		return bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

}
