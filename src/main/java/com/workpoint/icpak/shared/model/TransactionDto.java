package com.workpoint.icpak.shared.model;

import java.util.Date;
import java.util.List;

public class TransactionDto extends SerializableObj {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date createdDate;
	private PaymentMode paymentMode;
	private PaymentType paymentType;
	private String description;
	private String trxNumber;
	private String accountNo;
	private Double invoiceAmt;
	private Double chargableAmnt;
	private Double totalPreviousPayments;
	private Double amountPaid;
	private Double totalBalance;
	private String invoiceRef;
	private List<AttachmentDto> allAttachment;

	public TransactionDto() {
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public PaymentMode getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(PaymentMode paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTrxNumber() {
		return trxNumber;
	}

	public void setTrxNumber(String trxNumber) {
		this.trxNumber = trxNumber;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Double getInvoiceAmt() {
		return invoiceAmt;
	}

	public void setInvoiceAmt(Double invoiceAmt) {
		this.invoiceAmt = invoiceAmt;
	}

	public Double getChargableAmnt() {
		return chargableAmnt;
	}

	public void setChargableAmnt(Double chargableAmnt) {
		this.chargableAmnt = chargableAmnt;
	}

	public Double getTotalPreviousPayments() {
		return totalPreviousPayments;
	}

	public void setTotalPreviousPayments(Double totalPreviousPayments) {
		this.totalPreviousPayments = totalPreviousPayments;
	}

	public Double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public Double getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(Double totalBalance) {
		this.totalBalance = totalBalance;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public String getInvoiceRef() {
		return invoiceRef;
	}

	public void setInvoiceRef(String invoiceRef) {
		this.invoiceRef = invoiceRef;
	}

	public List<AttachmentDto> getAllAttachment() {
		return allAttachment;
	}

	public void setAllAttachment(List<AttachmentDto> allAttachment) {
		this.allAttachment = allAttachment;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
