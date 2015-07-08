package com.workpoint.icpak.shared.model.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.workpoint.icpak.shared.model.EventStatus;
import com.workpoint.icpak.shared.model.EventType;
import com.workpoint.icpak.shared.model.SerializableObj;

public class EventDto extends SerializableObj{


	/**
	 * 
	 */
	private static final long serialVersionUID = 100L;
	private String name;
	private String description;
	private Integer cpdHours;
	private String venue;
	private Date startDate;
	private Date endDate;
	private EventStatus status;
	private EventType type;
	private Double memberPrice;
	private Double nonMemberPrice;
	private List<AccommodationDto> accommodation = new ArrayList<AccommodationDto>();
	private int delegateCount;
	private double totalPaid;
	private double totalUnpaid;
	
	public EventDto() {
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

	public Integer getCpdHours() {
		return cpdHours;
	}

	public void setCpdHours(Integer cpdHours) {
		this.cpdHours = cpdHours;
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

	public List<AccommodationDto> getAccommodation() {
		return accommodation;
	}

	public void setAccommodation(List<AccommodationDto> accommodation) {
		this.accommodation = accommodation;
	}

	public int getDelegateCount() {
		return delegateCount;
	}

	public void setDelegateCount(int delegateCount) {
		this.delegateCount = delegateCount;
	}

	public double getTotalPaid() {
		return totalPaid;
	}

	public void setTotalPaid(double totalPaid) {
		this.totalPaid = totalPaid;
	}

	public double getTotalUnpaid() {
		return totalUnpaid;
	}

	public void setTotalUnpaid(double totalUnpaid) {
		this.totalUnpaid = totalUnpaid;
	}
	
}
