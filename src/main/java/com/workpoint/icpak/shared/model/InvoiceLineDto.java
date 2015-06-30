package com.workpoint.icpak.shared.model;

public class InvoiceLineDto extends SerializableObj{

	private String description;
	private double unitPrice;
	private int quantity = 1;
	private double totalAmount;

	public InvoiceLineDto() {
	}

	public InvoiceLineDto(String description, double unitPrice,
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
}
