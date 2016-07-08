package com.workpoint.icpak.shared.model.events;

import java.util.Date;

import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.SerializableObj;
import com.workpoint.icpak.shared.trx.OldTransactionDto;

public class DelegateDto extends SerializableObj {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date createdDate;
	private String companyName;
	private String contactName;
	private String contactEmail;
	private String contactPhoneNumber;
	private String delegatePhoneNumber;
	private String memberNo;
	private String memberRefId;
	private String title;
	private String surname;
	private String otherNames;
	private String fullName;
	private String email;
	private AccommodationDto accommodation;
	private Double amount;
	private AttendanceStatus attendance;
	private OldTransactionDto transaction;
	private DelegateType delegateType;
	private String bookingId;
	private String bookingRefId;
	private Integer isBookingActive;
	private String eventRefId;
	// PaymentStatus for the booking -Updated by Online Payment methods
	private PaymentStatus bookingPaymentStatus;
	// PaymentStatus for the booking -Updated by Online Payment methods
	private PaymentStatus delegatePaymentStatus;
	public String courseId;
	private String ern;
	private String hotel;
	private String lmsResponse;
	private String clearanceNo;
	private String receiptNo;
	private String lpoNo;
	private int isCredit;
	private String contact;
	private Date bookingDate;
	private Date lastUpdateDate;
	private String memberQrCode;
	private String invoiceNo;
	private String updatedBy;

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

	public String getHotel() {
		return hotel;
	}

	public void setHotel(String hotel) {
		this.hotel = hotel;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

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

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
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

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
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

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public Date getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
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

	public String getLmsResponse() {
		return lmsResponse;
	}

	public void setLmsResponse(String lmsResponse) {
		this.lmsResponse = lmsResponse;
	}

	public String getClearanceNo() {
		return clearanceNo;
	}

	public void setClearanceNo(String clearanceNo) {
		this.clearanceNo = clearanceNo;
	}

	public OldTransactionDto getTransaction() {
		return transaction;
	}

	public void setTransaction(OldTransactionDto transaction) {
		this.transaction = transaction;
	}

	public PaymentStatus getBookingPaymentStatus() {
		return bookingPaymentStatus;
	}

	public void setBookingPaymentStatus(PaymentStatus paymentStatus) {
		this.bookingPaymentStatus = paymentStatus;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getBookingRefId() {
		return bookingRefId;
	}

	public void setBookingRefId(String bookingRefId) {
		this.bookingRefId = bookingRefId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getMemberQrCode() {
		return memberQrCode;
	}

	public void setMemberQrCode(String memberQrCode) {
		this.memberQrCode = memberQrCode;
	}

	public String getContactPhoneNumber() {
		return contactPhoneNumber;
	}

	public void setContactPhoneNumber(String contactPhoneNumber) {
		this.contactPhoneNumber = contactPhoneNumber;
	}

	public String getDelegatePhoneNumber() {
		return delegatePhoneNumber;
	}

	public void setDelegatePhoneNumber(String delegatePhoneNumber) {
		this.delegatePhoneNumber = delegatePhoneNumber;
	}

	public Integer getIsBookingActive() {
		return isBookingActive;
	}

	public void setIsBookingActive(Integer isBookingActive) {
		this.isBookingActive = isBookingActive;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public PaymentStatus getDelegatePaymentStatus() {
		return delegatePaymentStatus;
	}

	public void setDelegatePaymentStatus(PaymentStatus delegatePaymentStatus) {
		this.delegatePaymentStatus = delegatePaymentStatus;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

}
