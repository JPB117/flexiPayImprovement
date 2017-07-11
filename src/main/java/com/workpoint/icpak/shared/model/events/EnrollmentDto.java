package com.workpoint.icpak.shared.model.events;

import java.util.Date;

import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.SerializableObj;

public class EnrollmentDto extends SerializableObj{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ContactDto contact;
	private Date registrationDate;
	private String paymentMode;//MPesa, VISA etc
	private String currency;
	private Long bookingDate;
	private String paymentRef; //TrxNumber
	private Long paymentDate;
	private Double amountDue;
	private Double amountPaid;
	private String eventRefId;
	private String invoiceRef;
	private String status; //DRAFT/ PAID
	private PaymentStatus paymentStatus= PaymentStatus.NOTPAID;
	private String memberId;
	
	public EnrollmentDto() {
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPaymentRef() {
		return paymentRef;
	}

	public void setPaymentRef(String paymentRef) {
		this.paymentRef = paymentRef;
	}

	public Double getAmountDue() {
		return amountDue;
	}

	public void setAmountDue(Double amountDue) {
		this.amountDue = amountDue;
	}

	public Double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getEventRefId() {
		return eventRefId;
	}

	public void setEventRefId(String eventRefId) {
		this.eventRefId = eventRefId;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ContactDto getContact() {
		return contact;
	}

	public void setContact(ContactDto contact) {
		this.contact = contact;
	}

	public String getInvoiceRef() {
		return invoiceRef;
	}

	public void setInvoiceRef(String invoiceRef) {
		this.invoiceRef = invoiceRef;
	}

	public Long getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(Long bookingDate) {
		this.bookingDate = bookingDate;
	}

	public Long getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Long paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}
	
}
