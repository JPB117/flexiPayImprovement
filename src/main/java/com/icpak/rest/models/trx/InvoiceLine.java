package com.icpak.rest.models.trx;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

import com.icpak.rest.models.base.PO;
import com.workpoint.icpak.shared.model.InvoiceLineDto;
import com.workpoint.icpak.shared.model.InvoiceLineType;

@Entity
public class InvoiceLine extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Type(type = "text")
	private String description;
	private double unitPrice;
	private int quantity = 1;
	private double totalAmount;
	private String eventDelegateRefId;
	@Enumerated(EnumType.STRING)
	private InvoiceLineType type;

	@ManyToOne
	@JoinColumn(name = "invoiceId")
	private Invoice invoice;

	public InvoiceLine() {
	}

	public InvoiceLine(String description, double unitPrice, double totalAmount) {
		this.description = description;
		this.totalAmount = totalAmount;
		this.unitPrice = unitPrice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public InvoiceLineDto toDto() {
		InvoiceLineDto dto = new InvoiceLineDto();
		dto.setRefId(getRefId());
		dto.setDescription(description);
		dto.setQuantity(quantity);
		dto.setTotalAmount(totalAmount);
		dto.setUnitPrice(unitPrice);
		dto.setEventDelegateRefId(eventDelegateRefId);

		return dto;
	}

	public void copyFrom(InvoiceLineDto lineDto) {
		setDescription(lineDto.getDescription());
		setQuantity(lineDto.getQuantity());
		setUnitPrice(lineDto.getUnitPrice());
		setTotalAmount(lineDto.getTotalAmount());

		if (lineDto.getEventDelegateRefId() != null)
			setEventDelegateRefId(lineDto.getEventDelegateRefId());
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public String getEventDelegateRefId() {
		return eventDelegateRefId;
	}

	public void setEventDelegateRefId(String eventDelegateRefId) {
		this.eventDelegateRefId = eventDelegateRefId;
	}

	public InvoiceLineType getType() {
		return type;
	}

	public void setType(InvoiceLineType type) {
		this.type = type;
	}
}
