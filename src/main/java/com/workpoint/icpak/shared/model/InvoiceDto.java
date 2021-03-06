package com.workpoint.icpak.shared.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class InvoiceDto extends SerializableObj implements HasKey {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long date;
	private String documentNo;
	private String companyName;
	private String companyAddress;
	private String contactName;
	private String phoneNumber;
	private Double invoiceAmount;
	private String bookingRefId;
	private String description;
	private List<InvoiceLineDto> lines = new ArrayList<InvoiceLineDto>();
	private String invoiceRefId;
	private Date invoiceDate;
	private Date transactionDate;
	private Date dueDate;
	private String paymentStatus;
	private String paymentMode;
	private Double transactionAmount;
	private Double totalDiscount;
	private Double totalPenalty;
	private Double chargeableAmount;
	private String trxRefId;
	private String userId;
	private PaymentStatus status;

	public InvoiceDto() {
	}

	public InvoiceDto(String refId, Double invoiceAmount, String documentNo, String description, Date invoiceDate,
			Date transactionDate, Date dueDate, String paymentStatus, String paymentMode, Double transactionAmount,
			String userId, String contactName) {
		this.invoiceRefId = refId;
		this.documentNo = documentNo;
		this.description = description;
		this.invoiceDate = invoiceDate;
		this.transactionDate = transactionDate;
		this.dueDate = dueDate;
		this.paymentStatus = paymentStatus;
		this.paymentMode = paymentMode;
		this.contactName = contactName;
		this.setInvoiceAmount(invoiceAmount);
		this.setUserId(userId);
		this.setTransactionAmount(transactionAmount);
	}

	public InvoiceDto(String refId, Double invoiceAmount2, String documentNo2, String description2, Date invoiceDate2,
			String contactName2, String trxRefId, String bookingRefId) {
		this.invoiceRefId = refId;
		invoiceAmount = invoiceAmount2;
		this.documentNo = documentNo2;
		this.description = description2;
		this.invoiceDate = invoiceDate2;
		this.contactName = contactName2;
		this.bookingRefId = bookingRefId;
		this.setTrxRefId(trxRefId);

	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Double getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setAmount(Double amount) {
		this.setInvoiceAmount(amount);
	}

	public List<InvoiceLineDto> getLines() {
		return lines;
	}

	public void setLines(List<InvoiceLineDto> lines) {
		this.lines = lines;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public void addLine(InvoiceLineDto invoiceLineDto) {
		lines.add(invoiceLineDto);
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

	public String getBookingRefId() {
		return bookingRefId;
	}

	public void setBookingRefId(String bookingRefId) {
		this.bookingRefId = bookingRefId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void addLines(Collection<InvoiceLineDto> values) {
		lines.addAll(values);
	}

	public String getInvoiceRefId() {
		return invoiceRefId;
	}

	public void setInvoiceRefId(String invoiceRefId) {
		this.invoiceRefId = invoiceRefId;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public Double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public Double getTotalPenalty() {
		return totalPenalty;
	}

	public void setTotalPenalty(Double totalPenalty) {
		this.totalPenalty = totalPenalty;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setInvoiceAmount(Double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	@Override
	public String getKey() {
		return getRefId();
	}

	public String getTrxRefId() {
		return trxRefId;
	}

	public void setTrxRefId(String trxRefId) {
		this.trxRefId = trxRefId;
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}

	public Double getChargeableAmount() {
		return chargeableAmount;
	}

	public void setChargeableAmount(Double chargeableAmount) {
		this.chargeableAmount = chargeableAmount;
	}

}
