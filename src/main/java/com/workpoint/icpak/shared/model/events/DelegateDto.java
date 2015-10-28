package com.workpoint.icpak.shared.model.events;

import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.SerializableObj;

public class DelegateDto extends SerializableObj {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String memberNo;
	private String memberRefId;
	private String title;
	private String surname;
	private String otherNames;
	private String email;
	private AccommodationDto accommodation;
	private Double amount;
	private AttendanceStatus attendance;
	private DelegateType delegateType;
	private String bookingId;
	private String eventRefId;
	private String ern;
	private PaymentStatus paymentStatus;
	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getHotel() {
		return hotel;
	}

	public void setHotel(String hotel) {
		this.hotel = hotel;
	}

	private String hotel;

	public DelegateDto() {
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

	public String getEventRefId() {
		return eventRefId;
	}

	public void setEventRefId(String eventRefId) {
		this.eventRefId = eventRefId;
	}

	public String getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}

	public String getMemberRefId() {
		return memberRefId;
	}

	public void setMemberRefId(String memberRefId) {
		this.memberRefId = memberRefId;
	}

	public String getErn() {
		return ern;
	}

	public void setErn(String ern) {
		this.ern = ern;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DelegateDto) || obj == null) {
			return false;
		}
		
		DelegateDto other = (DelegateDto) obj;
		if (other.getRefId() != null && getRefId() != null) {
			return other.getRefId().equals(getRefId());
		}

		return super.equals(obj);
	}
	
}
