package com.icpak.rest.models.event;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import com.icpak.rest.models.base.PO;
import com.wordnik.swagger.annotations.ApiModel;

/**
 * Event model 
 * @author duggan
 *
 */

@ApiModel(value="Events Model", description="An event represents an ICPAK Event/Seminar/Webinar/Course")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Booking.class})

@Entity
@Table(name="event")
public class Event extends PO{

	/**	
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(length=255, nullable=false)
	private String name;
	@Column(length=5000)
	private String description;
	@Column(length=255)
	private Integer cpdHours;
	private String venue;
	
	private Date startDate;
	private Date endDate;
	private EventStatus status;
	private EventType type;
	private Double memberPrice;
	private Double nonMemberPrice;
	
	@XmlTransient
	@OneToMany(mappedBy="event")
	Set<Booking> bookings=new HashSet<>();
	
	public Event() {
	}
	
	public Event(String name, String venue, String uri){
		this.name= name;
		this.venue = venue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Set<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(Set<Booking> bookings) {
		this.bookings = bookings;
	}
	
	public Event clone(String ... expand){
		Event event = new Event();
		
		event.setDescription(description);
		event.setStartDate(startDate);
		event.setStartDate(startDate);
		event.setRefId(getRefId());
		event.setName(name);
		event.setVenue(venue);
		
		for(String token: expand){
			if(token.equals("bookings")){
				Set<Booking> eventBookings = new HashSet<>();
				for(Booking booking: bookings){
					eventBookings.add(booking.clone());
				}
				event.setBookings(eventBookings);
			}
		}
		
		return event;
	}

	public EventStatus getStatus() {
		return status;
	}

	public void setStatus(EventStatus status) {
		this.status = status;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public Integer getCpdHours() {
		return cpdHours;
	}

	public void setCpdHours(Integer cpdHours) {
		this.cpdHours = cpdHours;
	}

	public Double getMemberPrice() {
		return memberPrice;
	}

	public void setMemberPrice(Double memberPrice) {
		this.memberPrice = memberPrice;
	}

	public Double getNonMemberPrice() {
		return nonMemberPrice;
	}

	public void setNonMemberPrice(Double nonMemberPrice) {
		this.nonMemberPrice = nonMemberPrice;
	}

}
