package com.workpoint.icpak.shared.model.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.workpoint.icpak.shared.model.Listable;
import com.workpoint.icpak.shared.model.SerializableObj;

public class AccommodationDto extends SerializableObj implements Listable{

	private String hotel;
	private String description;
	private AccommodationType type = AccommodationType.HB;
	private Double fee;
	private int nights;
	private EventDto event;
	
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

	@JsonIgnore
	public String getName() {
		return getRefId();
	}

	@JsonIgnore
	public String getDisplayName() {
		return hotel+"- Ksh "+fee;
	}

	public EventDto getEvent() {
		return event;
	}

	public void setEvent(EventDto event) {
		this.event = event;
	}

	public int getNights() {
		return nights;
	}

	public void setNights(int nights) {
		this.nights = nights;
	}

	public AccommodationType getType() {
		return type;
	}

	public void setType(AccommodationType type) {
		this.type = type;
	}
	
}
