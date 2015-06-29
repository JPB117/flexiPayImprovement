package com.workpoint.icpak.shared.model;

public class AccommodationDto extends SerializableObj{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String hotel;
	private String description;
	private Double fee;
	
	public AccommodationDto() {
	}

	public String getHotel() {
		return hotel;
	}

	public void setHotel(String hotel) {
		this.hotel = hotel;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}
	
}
