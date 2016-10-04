package com.workpoint.icpak.shared.model.events;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.workpoint.icpak.shared.model.Listable;
import com.workpoint.icpak.shared.model.SerializableObj;

/**
 * Event DTO
 * <p>
 * For simplicity, all dates will be transmitted as strings and parsed by
 * corresponding recipients <br/>
 * The utilized format for the dates is yyyy-MM-dd hh:mm:ss
 * <p>
 * 
 * @author duggan
 *
 */
public class EventDto extends SerializableObj implements Listable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
	private Double cpdHours;
	private String venue;
	private String startDate;
	private String endDate;
	private String status;
	private String type;
	private Double memberPrice;
	private Double discountMemberPrice;
	private Double penaltyMemberPrice;
	private Double associatePrice;
	private Double discountAssociatePrice;
	private Double penaltyAssociatePrice;
	private Double nonMemberPrice;
	private Double discountNonMemberPrice;
	private Double penaltyNonMemberPrice;
	private String categoryName;
	private String discountDate;
	private String penaltyDate;
	private List<AccommodationDto> accommodation;
	private int delegateCount;
	private Double totalPaid;
	private Double totalUnpaid;
	private Integer paidCount;
	private Integer unPaidCount;
	private Integer courseId;
	private Integer isEventActive;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
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

	public Double getDiscountMemberPrice() {
		return discountMemberPrice;
	}

	public void setDiscountMemberPrice(Double discountMemberPrice) {
		this.discountMemberPrice = discountMemberPrice;
	}

	public Double getPenaltyMemberPrice() {
		return penaltyMemberPrice;
	}

	public void setPenaltyMemberPrice(Double penaltyMemberPrice) {
		this.penaltyMemberPrice = penaltyMemberPrice;
	}

	public Double getDiscountAssociatePrice() {
		return discountAssociatePrice;
	}

	public void setDiscountAssociatePrice(Double discountAssociatePrice) {
		this.discountAssociatePrice = discountAssociatePrice;
	}

	public Double getPenaltyAssociatePrice() {
		return penaltyAssociatePrice;
	}

	public void setPenaltyAssociatePrice(Double penaltyAssociatePrice) {
		this.penaltyAssociatePrice = penaltyAssociatePrice;
	}

	public Double getDiscountNonMemberPrice() {
		return discountNonMemberPrice;
	}

	public void setDiscountNonMemberPrice(Double discountNonMemberPrice) {
		this.discountNonMemberPrice = discountNonMemberPrice;
	}

	public Double getPenaltyNonMemberPrice() {
		return penaltyNonMemberPrice;
	}

	public void setPenaltyNonMemberPrice(Double penaltyNonMemberPrice) {
		this.penaltyNonMemberPrice = penaltyNonMemberPrice;
	}

	public String getDiscountDate() {
		return discountDate;
	}

	public void setDiscountDate(String discountDate) {
		this.discountDate = discountDate;
	}

	public String getPenaltyDate() {
		return penaltyDate;
	}

	public void setPenaltyDate(String penaltyDate) {
		this.penaltyDate = penaltyDate;
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

	@JsonIgnore
	public String getDisplayName() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof EventDto)) {
			return false;
		}

		EventDto other = (EventDto) obj;
		if (other.getRefId() == null || getRefId() == null) {
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

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	public Integer getUnPaidCount() {
		return unPaidCount;
	}

	public void setUnPaidCount(Integer unPaidCount) {
		this.unPaidCount = unPaidCount;
	}

	public Integer getPaidCount() {
		return paidCount;
	}

	public void setPaidCount(Integer paidCount) {
		this.paidCount = paidCount;
	}

	public Integer getIsEventActive() {
		return isEventActive;
	}

	public void setIsEventActive(Integer isEventActive) {
		this.isEventActive = isEventActive;
	}

}
