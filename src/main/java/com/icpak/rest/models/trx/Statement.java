package com.icpak.rest.models.trx;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.icpak.rest.models.base.PO;
import com.workpoint.icpak.shared.model.statement.StatementDto;

/**
 * Created by wladek on 9/18/15.
 */
@Entity
@Table(name = "statement")
public class Statement extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String entryNo;
	private String custLedgerEntryNo;
	private String entryType;
	private Date postingDate;
	private String documentType;
	private String documentNo;
	private Double amount;
	private String description;
	private String customerNo;
	private Date dueDate;
	private Date lastUpdated;

	public String getEntryNo() {
		return entryNo;
	}

	public void setEntryNo(String entryNo) {
		this.entryNo = entryNo;
	}

	public String getCustLedgerEntryNo() {
		return custLedgerEntryNo;
	}

	public void setCustLedgerEntryNo(String custLedgerEntryNo) {
		this.custLedgerEntryNo = custLedgerEntryNo;
	}

	public String getEntryType() {
		return entryType;
	}

	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public void copyFrom(StatementDto statementDto) {

		setDueDate(statementDto.getDueDate());
		setCustomerNo(statementDto.getCustomerNo());
		setDescription(statementDto.getDescription());
		setAmount(statementDto.getAmount());
		setDocumentNo(statementDto.getDocumentNo());
		setDocumentType(statementDto.getDocumentType());
		setPostingDate(statementDto.getPostingDate());
		setEntryType(statementDto.getEntryType());
		setCustLedgerEntryNo(statementDto.getCustLedgerEntryNo());
		setEntryNo(statementDto.getEntryNo());

	}

	public StatementDto toStatementDto() {
		StatementDto statementDto = new StatementDto();

		statementDto.setDueDate(getDueDate());
		statementDto.setCustomerNo(getCustomerNo());
		statementDto.setDescription(getDescription());
		statementDto.setAmount(getAmount());
		statementDto.setDocumentNo(getDocumentNo());
		statementDto
				.setDocumentType(((getDocumentType() == null)
						|| (getDocumentType().equals("null")) ? ""
						: getDocumentType()));
		statementDto.setPostingDate(getPostingDate());
		statementDto.setEntryType(getEntryType());
		statementDto.setCustLedgerEntryNo(getCustLedgerEntryNo());
		statementDto.setEntryNo(getEntryNo());
		statementDto.setRefId(getRefId());

		return statementDto;
	}

	public Date getPostingDate() {
		return postingDate;
	}

	public void setPostingDate(Date postingDate) {
		this.postingDate = postingDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

}