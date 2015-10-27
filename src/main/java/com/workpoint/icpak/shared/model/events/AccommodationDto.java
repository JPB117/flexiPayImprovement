package com.workpoint.icpak.shared.model.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.workpoint.icpak.shared.model.Listable;
import com.workpoint.icpak.shared.model.SerializableObj;

public class AccommodationDto extends SerializableObj implements Listable {

	private String hotel;
	private String description;
	private AccommodationType type = AccommodationType.HB;
	private Double fee;
	private int nights;
	private EventDto event;
	private int spaces;
	private int totalBooking;

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
		return hotel + "-"
				+ ((totalBooking >= spaces) ? " No Accommodation"
						// "<span style=\"color:red;\">No Accommodation</span>"
						: " 	" + (spaces - totalBooking) + " spaces @Ksh" + fee);
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

	public int getSpaces() {
		return spaces;
	}

	public void setSpaces(int spaces) {
		this.spaces = spaces;
	}

	public int getTotalBooking() {
		return totalBooking;
	}

	public void setTotalBooking(int accommodationBookingCount) {
		this.totalBooking = accommodationBookingCount;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AccommodationDto) || obj == null) {
			return false;
		}
		
		AccommodationDto other = (AccommodationDto) obj;
		if (other.getRefId() != null && getRefId() != null) {
			return other.getRefId().equals(getRefId());
		}

		return super.equals(obj);
	}

}
