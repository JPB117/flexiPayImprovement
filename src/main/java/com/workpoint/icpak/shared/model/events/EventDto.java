package com.workpoint.icpak.shared.model.events;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.workpoint.icpak.shared.model.EventStatus;
import com.workpoint.icpak.shared.model.EventType;
import com.workpoint.icpak.shared.model.Listable;
import com.workpoint.icpak.shared.model.SerializableObj;

public class EventDto extends SerializableObj implements Listable{

	private String name;
	private String description;
	private Integer cpdHours;
	private String venue;
	private Long startDate;
	private Long endDate;
	private EventStatus status;
	private EventType type;
	private Double memberPrice;
	private Double associatePrice;
	private Double nonMemberPrice;
	private String categoryName;
	private List<AccommodationDto> accommodation;
	private int delegateCount;
	private Double totalPaid;
	private Double totalUnpaid;

	public EventDto() {
		accommodation = new ArrayList<AccommodationDto>();
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

	public Double getTotalPaid() {
		return totalPaid;
	}

	public void setTotalPaid(Double totalPaid) {
		this.totalPaid = totalPaid;
	}

	public Double getTotalUnpaid() {
		return totalUnpaid;
	}

	public void setTotalUnpaid(Double totalUnpaid) {
		this.totalUnpaid = totalUnpaid;
	}

	public Long getStartDate() {
		return startDate;
	}

	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	@JsonIgnore
	public String getDisplayName() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof EventDto)){
			return false;
		}
		
		EventDto other = (EventDto)obj;
		if(other.getRefId()==null || getRefId()==null){
			return false;
		}
		
		return other.getRefId().equals(getRefId());
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Double getAssociatePrice() {
		return associatePrice;
	}

	public void setAssociatePrice(Double associatePrice) {
		this.associatePrice = associatePrice;
	}

}
