package com.workpoint.icpak.shared.model;

import com.workpoint.icpak.shared.model.events.AccommodationDto;

public class InvoiceLineDto extends SerializableObj{

	private String description;
	private double unitPrice;
	private int quantity = 1;
	private double totalAmount;
	private AccommodationDto accommodation;
	private String memberId;
	private String eventDelegateRefId;
	private String delegateERN;

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

	public AccommodationDto getAccommodation() {
		return accommodation;
	}

	public void setAccommodation(AccommodationDto accommodation) {
		this.accommodation = accommodation;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getEventDelegateRefId() {
		return eventDelegateRefId;
	}

	public void setEventDelegateRefId(String eventDelegateRefId) {
		this.eventDelegateRefId = eventDelegateRefId;
	}

	public String getDelegateERN() {
		return delegateERN;
	}

	public void setDelegateERN(String delegateERN) {
		this.delegateERN = delegateERN;
	}

}
