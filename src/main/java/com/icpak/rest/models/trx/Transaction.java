package com.icpak.rest.models.trx;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.icpak.rest.models.base.PO;
import com.wordnik.swagger.annotations.ApiModel;
import com.workpoint.icpak.shared.trx.TrxType;

/**
 * Transaction model 
 * @author duggan
 *
 */

@ApiModel(value="Transaction Model", description="An transaction represents an ICPAK transaction/Seminar/Webinar/Course")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

@Entity
@Table(name="transaction")
public class Transaction extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userId;
	private Date date;
	private String description;
	private Date dueDate;
	private Double amount;
	private Double balance;
	
	@Column(nullable=false)
	private TrxType type;
	private String documentNo;
	
	public Transaction() {
	
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public TrxType getType() {
		return type;
	}

	public void setType(TrxType type) {
		this.type = type;
	}

	public String getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}
	
	
}
