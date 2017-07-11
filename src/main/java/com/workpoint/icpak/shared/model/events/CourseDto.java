package com.workpoint.icpak.shared.model.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.workpoint.icpak.shared.model.EventStatus;
import com.workpoint.icpak.shared.model.EventType;
import com.workpoint.icpak.shared.model.Listable;
import com.workpoint.icpak.shared.model.SerializableObj;

public class CourseDto extends SerializableObj implements Listable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
	private String code;
	private Double cpdHours;
	private String venue;
	private String startDate;
	private String endDate;
	private EventStatus status;
	private EventType type;
	private Double memberPrice;
	private Double nonMemberPrice;
	private int delegateCount;
	private Double totalPaid;
	private Double totalUnpaid;
	private Integer lmsCourseId;

	public CourseDto() {
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

	public Double getCpdHours() {
		return cpdHours;
	}

	public void setCpdHours(Double cpdHours) {
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

	@JsonIgnore
	public String getDisplayName() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CourseDto)) {
			return false;
		}

		CourseDto other = (CourseDto) obj;
		if (other.getRefId() == null || getRefId() == null) {
			return false;
		}

		return other.getRefId().equals(getRefId());
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getCourseId() {
		return lmsCourseId;
	}

	public void setCourseId(Integer lmsCourseId) {
		this.lmsCourseId = lmsCourseId;
	}

}
