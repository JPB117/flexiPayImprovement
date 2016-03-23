package com.icpak.rest.models.event;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import com.icpak.rest.models.base.PO;
import com.wordnik.swagger.annotations.ApiModel;
import com.workpoint.icpak.shared.model.events.AttendanceStatus;
import com.workpoint.icpak.shared.model.events.DelegateDto;

@ApiModel(value = "Event delegates", description = "List of delegates sharing a single booking")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({ Booking.class })
@Entity
@Table(name = "delegate")
public class Delegate extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String memberRegistrationNo;
	private String ern;
	private String memberRefId;
	private String title;
	private String surname;
	private String otherNames;
	private String fullName;
	private String email;
	private String phoneNumber;
	private String receiptNo;
	private String lpoNo;
	private int isCredit;
	private String clearanceNo;
	private String sponsorTelephoneNo;
	private String memberQrCode;
	private String lmsResponse;
	@Transient
	private String bookingId;
	@ManyToOne
	@JoinColumn(name = "booking_id")
	@XmlTransient
	private Booking booking;
	private Double amount;
	@Enumerated(EnumType.ORDINAL)
	private AttendanceStatus attendance = AttendanceStatus.NOTATTENDED;

	@ManyToOne
	@JoinColumn(name = "accommodationId")
	private Accommodation accommodationId;

	public String getBookingId() {
		return bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getLpoNo() {
		return lpoNo;
	}

	public void setLpoNo(String lpoNo) {
		this.lpoNo = lpoNo;
	}

	public int getIsCredit() {
		return isCredit;
	}

	public void setIsCredit(int isCredit) {
		this.isCredit = isCredit;
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

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public void copyFrom(DelegateDto delegateDto) {
		setEmail(delegateDto.getEmail());
		setMemberRegistrationNo(delegateDto.getMemberNo());
		setMemberRefId(delegateDto.getMemberRefId());
		setOtherNames(delegateDto.getOtherNames());
		setSurname(delegateDto.getSurname());
		setFullName(delegateDto.getFullName());
		setTitle(delegateDto.getTitle());
		if (delegateDto.getAttendance() != null) {
			setAttendance(delegateDto.getAttendance());
		}
		setMemberQrCode(delegateDto.getMemberQrCode());
		setIsCredit(delegateDto.getIsCredit());
		setLpoNo(delegateDto.getLpoNo());
		setReceiptNo(delegateDto.getLpoNo());
		setClearanceNo(delegateDto.getClearanceNo());
		setLmsResponse(delegateDto.getLmsResponse());
	}

	public DelegateDto toDto() {
		DelegateDto dto = new DelegateDto();
		dto.setEmail(email);
		dto.setMemberNo(memberRegistrationNo);
		dto.setMemberRefId(memberRefId);
		dto.setOtherNames(otherNames);
		dto.setFullName(fullName);
		dto.setRefId(getRefId());
		dto.setErn(ern);
		dto.setSurname(surname);
		dto.setTitle(title);
		dto.setBookingId(bookingId);
		dto.setBookingRefId(booking.getRefId());
		if (getAccommodation() != null) {
			dto.setAccommodation(getAccommodation().toDto());
		}
		dto.setAttendance(attendance);
		dto.setAmount(amount);
		dto.setIsCredit(isCredit);
		dto.setLpoNo(lpoNo);
		dto.setReceiptNo(receiptNo);
		dto.setClearanceNo(clearanceNo);
		return dto;
	}

	public Accommodation getAccommodation() {
		return accommodationId;
	}

	public void setAccommodation(Accommodation accommodation) {
		this.accommodationId = accommodation;
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

	public String getMemberRefId() {
		return memberRefId;
	}

	public void setMemberRefId(String memberRefId) {
		this.memberRefId = memberRefId;
	}

	public String getErn() {
		return ern;
	}

	@Override
	public String toString() {
		return fullName + " (" + ern + ")";
	}

	public void setErn(String ern) {
		this.ern = ern;
	}

	public String getClearanceNo() {
		return clearanceNo;
	}

	public void setClearanceNo(String clearanceNo) {
		this.clearanceNo = clearanceNo;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getSponsorTelephoneNo() {
		return sponsorTelephoneNo;
	}

	public void setSponsorTelephoneNo(String sponsorTelephoneNo) {
		this.sponsorTelephoneNo = sponsorTelephoneNo;
	}

	public String getMemberQrCode() {
		return memberQrCode;
	}

	public void setMemberQrCode(String memberQrCode) {
		this.memberQrCode = memberQrCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getLmsResponse() {
		return lmsResponse;
	}

	public void setLmsResponse(String lmsResponse) {
		this.lmsResponse = lmsResponse;
	}

}
