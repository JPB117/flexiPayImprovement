package com.icpak.rest.models.trx;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.icpak.rest.models.base.PO;
import com.wordnik.swagger.annotations.ApiModel;
import com.workpoint.icpak.shared.model.PaymentMode;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.PaymentType;
import com.workpoint.icpak.shared.trx.OldTransactionDto;

/**
 * Transaction model
 * 
 * @author duggan
 *
 */

@ApiModel(value = "Transaction Model", description = "A transaction model")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "transaction")
public class Transaction extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String memberId;
	private Date date;
	private String description;
	private Date dueDate;
	private Double amount;
	private Double invoiceAmount;
	private Double chargableAmount;
	private Double totalPreviousPayments;
	private Double balance;

	@Enumerated(EnumType.STRING)
	private PaymentMode paymentMode;
	// The type of Payment - Event Payment OR Registration Payment etc
	@Enumerated(EnumType.STRING)
	private PaymentType paymentType;
	@Enumerated(EnumType.STRING)
	private PaymentStatus status = PaymentStatus.NOTPAID;
	private String businessNo;
	private String accountNo;
	private String trxNumber;
	private String documentNo;
	private String invoiceRef;
	private String payerNames;

	public Transaction() {
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public OldTransactionDto toDto() {
		OldTransactionDto dto = new OldTransactionDto();
		dto.setAmount(amount);
		dto.setDate(date);
		dto.setDescription(description);
		dto.setDueDate(dueDate);
		dto.setUserId(memberId);
		dto.setAccountNo(accountNo);
		dto.setBusinessNo(businessNo);
		dto.setPaymentMode(paymentMode);
		dto.setStatus(status);
		dto.setTrxNumber(trxNumber);
		dto.setRefId(getRefId());
		dto.setDocumentNo(documentNo);
		return dto;
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}

	public String getBusinessNo() {
		return businessNo;
	}

	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getTrxNumber() {
		return trxNumber;
	}

	public void setTrxNumber(String trxNumber) {
		this.trxNumber = trxNumber;
	}

	public String getDocumentNo() {
		return documentNo;
	}

	public PaymentMode getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(PaymentMode paymentMode) {
		this.paymentMode = paymentMode;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

	public String getInvoiceRef() {
		return invoiceRef;
	}

	public void setInvoiceRef(String invoiceRef) {
		this.invoiceRef = invoiceRef;
	}

	public String getPayerNames() {
		return payerNames;
	}

	public void setPayerNames(String payerNames) {
		this.payerNames = payerNames;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public Double getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(Double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public Double getChargableAmount() {
		return chargableAmount;
	}

	public void setChargableAmount(Double chargableAmount) {
		this.chargableAmount = chargableAmount;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Double getTotalPreviousPayments() {
		return totalPreviousPayments;
	}

	public void setTotalPreviousPayments(Double totalPreviousPayments) {
		this.totalPreviousPayments = totalPreviousPayments;
	}

}
