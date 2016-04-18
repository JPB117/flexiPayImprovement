package com.icpak.rest.models.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
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
import com.workpoint.icpak.server.util.ServerDateUtils;
import com.workpoint.icpak.shared.model.EventStatus;
import com.workpoint.icpak.shared.model.EventType;
import com.workpoint.icpak.shared.model.events.AccommodationDto;
import com.workpoint.icpak.shared.model.events.CourseDto;
import com.workpoint.icpak.shared.model.events.EventDto;

/**
 * Event model
 * 
 * @author duggan
 *
 */

@ApiModel(value = "Events Model", description = "An event represents an ICPAK Event/Course")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({ Booking.class, Accommodation.class })
@Entity
@Table(name = "event")
public class Event extends PO {
	/**	
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(length = 5000, nullable = false)
	private String name;
	@Column(length = 5000)
	private String description;
	@Column(length = 255)
	private Double cpdHours;
	private String venue;
	private String categoryName;
	private Date startDate;
	private Date endDate;
	private EventStatus status;
	@Column(nullable = false)
	private EventType type;
	@Column(nullable = false)
	private Double memberPrice;
	private Double discountMemberPrice;
	private Double penaltyMemberPrice;
	@Column(nullable = false)
	private Double nonMemberPrice;
	private Double discountNonMemberPrice;
	private Double penaltyNonMemberPrice;
	private Double associatePrice;
	private Double discountAssociatePrice;
	private Double penaltyAssociatePrice;
	private Date discountDate;
	private Date penaltyDate;
	private Integer lmsCourseId;
	private String code;
	private Date registrationDate;
	@XmlTransient
	@OneToMany(mappedBy = "event")
	Set<Booking> bookings = new HashSet<>();
	@OneToMany(mappedBy = "event", cascade = { CascadeType.PERSIST,
			CascadeType.REMOVE })
	Set<Accommodation> accommodation = new HashSet<>();

	public Event() {
	}

	public Event(String name, String venue, String uri) {
		this.name = name;
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

	public Event clone(String... expand) {
		Event event = new Event();

		event.setDescription(description);
		event.setStartDate(startDate);
		event.setStartDate(startDate);
		event.setRefId(getRefId());
		event.setName(name);
		event.setVenue(venue);

		for (String token : expand) {
			if (token.equals("bookings")) {
				Set<Booking> eventBookings = new HashSet<>();
				for (Booking booking : bookings) {
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

	public Double getCpdHours() {
		return cpdHours;
	}

	public void setCpdHours(Double cpdHours) {
		this.cpdHours = cpdHours;
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

	public void setAccommodation(Set<Accommodation> accommodation) {
		this.accommodation = accommodation;
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

	public EventDto toDto() {
		return toDto(false);
	}

	public EventDto toDto(boolean includeAccommodation) {
		EventDto dto = new EventDto();
		dto.setRefId(getRefId());
		dto.setCpdHours(cpdHours);
		dto.setDescription(description);
		dto.setCategoryName(categoryName);
		dto.setCourseId(lmsCourseId);

		if (endDate != null)
			dto.setEndDate(ServerDateUtils.format(endDate,
					ServerDateUtils.FULLTIMESTAMP));

		if (startDate != null) {
			dto.setStartDate(ServerDateUtils.format(startDate,
					ServerDateUtils.FULLTIMESTAMP));
		}

		dto.setMemberPrice(memberPrice);
		dto.setName(name);
		dto.setNonMemberPrice(nonMemberPrice);
		dto.setAssociatePrice(associatePrice);
		if (penaltyDate != null) {
			dto.setPenaltyDate(ServerDateUtils.DATEFORMAT_SYS
					.format(penaltyDate));
		}
		if (discountDate != null) {
			dto.setDiscountDate(ServerDateUtils.DATEFORMAT_SYS
					.format(discountDate));
		}
		dto.setStatus(status);
		dto.setType(type);
		dto.setVenue(venue);

		if (includeAccommodation) {
			if (getAccommodation() != null) {
				List<AccommodationDto> accommodations = new ArrayList<>();
				for (Accommodation a : getAccommodation()) {
					accommodations.add(a.toDto());
				}
				dto.setAccommodation(accommodations);
			}
		}

		return dto;

	}

	public void copyFrom(EventDto dto) {
		setCpdHours(dto.getCpdHours());
		setDescription(dto.getDescription());
		if (dto.getEndDate() != null) {
			setEndDate(ServerDateUtils.parse(dto.getEndDate(),
					ServerDateUtils.SHORTTIMESTAMP));
		}
		if (dto.getStartDate() != null) {
			setStartDate(ServerDateUtils.parse(dto.getStartDate(),
					ServerDateUtils.SHORTTIMESTAMP));
		}

		// Member Price
		setMemberPrice(dto.getMemberPrice());
		if (dto.getDiscountMemberPrice() != null) {
			setDiscountMemberPrice(dto.getDiscountMemberPrice());
		}
		if (dto.getPenaltyMemberPrice() != null) {
			setPenaltyMemberPrice(dto.getPenaltyMemberPrice());
		}

		// Non-Member Price
		setNonMemberPrice(dto.getNonMemberPrice());
		if (dto.getDiscountNonMemberPrice() != null) {
			setDiscountNonMemberPrice(dto.getDiscountNonMemberPrice());
		}
		if (dto.getPenaltyNonMemberPrice() != null) {
			setPenaltyNonMemberPrice(dto.getPenaltyNonMemberPrice());
		}

		// Associate Price
		setAssociatePrice(dto.getAssociatePrice());
		if (dto.getDiscountAssociatePrice() != null) {
			setDiscountAssociatePrice(dto.getDiscountAssociatePrice());
		}
		if (dto.getPenaltyAssociatePrice() != null) {
			setPenaltyAssociatePrice(dto.getPenaltyAssociatePrice());
		}

		// Discount Date
		if (dto.getDiscountDate() != null) {
			setDiscountDate(ServerDateUtils.parse(dto.getDiscountDate(),
					ServerDateUtils.SHORTTIMESTAMP));
		}

		// Penalty Date
		if (dto.getPenaltyDate() != null) {
			setPenaltyDate(ServerDateUtils.parse(dto.getPenaltyDate(),
					ServerDateUtils.SHORTTIMESTAMP));
		}

		setName(dto.getName());
		if (dto.getStatus() == null) {
			setStatus(EventStatus.OPEN);
		}
		if (dto.getType() == null) {
			setType(EventType.SEMINAR);
		}
		setStatus(dto.getStatus());
		setType(dto.getType());
		setVenue(dto.getVenue());
		setCategoryName(dto.getCategoryName());

		if (dto.getCourseId() != null) {
			setLmsCourseId(dto.getCourseId());
		}

		if (dto.getAccommodation() != null) {
			List<Accommodation> accommodations = new ArrayList<>();
			for (AccommodationDto a : dto.getAccommodation()) {
				Accommodation d = new Accommodation();
				d.copyFrom(a);
				accommodations.add(d);
			}
			setAccommodation(accommodations);
		}
	}

	public Set<Accommodation> getAccommodation() {
		return accommodation;
	}

	public void setAccommodation(Collection<Accommodation> accommodation) {
		this.accommodation.clear();
		for (Accommodation a : accommodation) {
			a.setEvent(this);
			this.accommodation.add(a);
		}
	}

	public CourseDto toCourseDto() {
		CourseDto dto = new CourseDto();
		dto.setRefId(getRefId());
		dto.setCpdHours(cpdHours);
		dto.setDescription(description);
		dto.setMemberPrice(memberPrice);
		dto.setName(name);
		dto.setNonMemberPrice(nonMemberPrice);
		dto.setStatus(status);
		dto.setType(type);
		dto.setVenue(venue);
		dto.setCourseId(lmsCourseId);
		dto.setStartDate(startDate + "");
		dto.setEndDate(endDate + "");
		return dto;
	}

	public void copyFromCourse(CourseDto dto) {

		if (dto.getCpdHours() != null) {
			setCpdHours(dto.getCpdHours());
		}

		if (dto.getDescription() != null) {
			setDescription(dto.getDescription());
		}

		if (dto.getMemberPrice() != null) {
			setMemberPrice(dto.getMemberPrice());
		}

		if (dto.getEndDate() != null) {
			setEndDate(ServerDateUtils.parse(dto.getEndDate(),
					ServerDateUtils.SHORTTIMESTAMP));
		}

		if (dto.getStartDate() != null) {
			setStartDate(ServerDateUtils.parse(dto.getStartDate(),
					ServerDateUtils.SHORTTIMESTAMP));
		}

		if (dto.getName() != null) {
			setName(dto.getName());
		}

		if (dto.getNonMemberPrice() != null) {
			setNonMemberPrice(dto.getNonMemberPrice());
		}

		if (dto.getStatus() != null) {
			setStatus(dto.getStatus());
		}

		if (dto.getType() != null) {
			setType(dto.getType());
		}

		if (dto.getVenue() != null) {
			setVenue(dto.getVenue());
		}

		if (dto.getCode() != null) {
			setCode(dto.getCode());
		}

		if (dto.getCourseId() != null) {
			setLmsCourseId(dto.getCourseId());
		}

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
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

	public Date getDiscountDate() {
		return discountDate;
	}

	public void setDiscountDate(Date discountDate) {
		this.discountDate = discountDate;
	}

	public Date getPenaltyDate() {
		return penaltyDate;
	}

	public void setPenaltyDate(Date penaltyDate) {
		this.penaltyDate = penaltyDate;
	}

	public Integer getLmsCourseId() {
		return lmsCourseId;
	}

	public void setLmsCourseId(Integer lmsCourseId) {
		this.lmsCourseId = lmsCourseId;
	}

}
