package com.icpak.rest.models.event;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
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

/**
 * Event model 
 * @author duggan
 *
 */

@ApiModel(value="Accommodation Model", description="Accommodation model - Pre-negotiated Accomodation for an event")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

@Entity
@Table(name="accommodation")
public class Accommodation extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String hotel;
	private String description;
	private Double fee;
	
	@XmlTransient
	@ManyToOne
	@JoinColumn(name="eventId")
	private Event event;
	
	@OneToMany(mappedBy="accommodation")
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
		AccommodationDto dto  = new AccommodationDto();
		dto.setDescription(description);
		dto.setFee(fee);
		dto.setHotel(hotel);
		dto.setRefId(getRefId());
		
		return dto;
	}

	public void copyFrom(AccommodationDto dto) {
		setDescription(dto.getDescription());
		setFee(dto.getFee());
		setHotel(dto.getHotel());
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
}
