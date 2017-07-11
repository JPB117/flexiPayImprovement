package com.icpak.rest.models.event;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.icpak.rest.models.base.PO;
import com.wordnik.swagger.annotations.ApiModel;
import com.workpoint.icpak.shared.model.events.AccommodationDto;
import com.workpoint.icpak.shared.model.events.AccommodationType;

/**
 * Event model
 * 
 * @author duggan
 *
 */

@ApiModel(value = "Accommodation Model", description = "Accommodation model - Pre-negotiated Accomodation for an event")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

@Entity
@Table(name = "accommodation")
public class Accommodation extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String hotel;
	private String description;
	private Double fee;

	@Column(name = "accommodationType")
	@Enumerated(EnumType.STRING)
	private AccommodationType type = AccommodationType.HB;
	private int nights = 0;

	private int spaces;

	@XmlTransient
	@ManyToOne
	@JoinColumn(name = "eventId")
	private Event event;

	@OneToMany(mappedBy = "accommodationId")
	private Set<Delegate> delegates = new HashSet<>();

	public Accommodation() {
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

	public AccommodationDto toDto() {
		return toDto(false);
	}

	public AccommodationDto toDto(boolean isIncludeEvent) {
		AccommodationDto dto = new AccommodationDto();
		dto.setDescription(description);
		dto.setFee(fee);
		dto.setHotel(hotel);
		dto.setNights(nights);
		dto.setRefId(getRefId());
		dto.setType(type);
		dto.setSpaces(spaces);
		if (isIncludeEvent) {
			if (getEvent() != null) {
				Event e = getEvent();
				dto.setEvent(e.toDto(false));
			}
		}
		return dto;
	}

	public void copyFrom(AccommodationDto dto) {
		setDescription(dto.getDescription());
		setFee(dto.getFee());
		setHotel(dto.getHotel());
		setNights(dto.getNights());
		setSpaces(dto.getSpaces());

		if (type != null)
			setType(dto.getType());
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
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

	public Set<Delegate> getDelegates() {
		return delegates;
	}

	public void setDelegates(Set<Delegate> delegates) {
		this.delegates = delegates;
	}

}
