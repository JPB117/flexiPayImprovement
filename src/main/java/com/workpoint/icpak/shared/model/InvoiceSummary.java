package com.workpoint.icpak.shared.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class InvoiceSummary implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PaymentStatus status;
	private Double paid;
	private Double unpaid;
	
	public InvoiceSummary() {
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}

	public Double getPaid() {
		return paid;
	}

	public void setPaid(Double paid) {
		this.paid = paid;
	}

	public Double getUnpaid() {
		return unpaid;
	}

	public void setUnpaid(Double unpaid) {
		this.unpaid = unpaid;
	}
}
