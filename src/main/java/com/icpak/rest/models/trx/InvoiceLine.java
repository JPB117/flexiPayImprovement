package com.icpak.rest.models.trx;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.icpak.rest.models.base.PO;
import com.workpoint.icpak.shared.model.InvoiceLineDto;

@Entity
public class InvoiceLine extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String description;
	private double unitPrice;
	private int quantity = 1;
	private double totalAmount;
	
	@ManyToOne
	@JoinColumn(name="invoiceId")
	private Invoice invoice;

	public InvoiceLine() {
	}

	public InvoiceLine(String description, double unitPrice,
			double totalAmount) {
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
		
		dto.setDescription(description);
		dto.setQuantity(quantity);
		dto.setTotalAmount(totalAmount);
		dto.setUnitPrice(unitPrice);
		
		return dto;
	}

	public void copyFrom(InvoiceLineDto lineDto) {
		setDescription(lineDto.getDescription());
		setQuantity(lineDto.getQuantity());
		setUnitPrice(lineDto.getUnitPrice());
		setTotalAmount(lineDto.getTotalAmount());
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}
}
