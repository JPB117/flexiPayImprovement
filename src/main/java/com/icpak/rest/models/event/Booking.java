package com.icpak.rest.models.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.icpak.rest.models.base.ExpandTokens;
import com.icpak.rest.models.base.PO;
import com.icpak.rest.models.membership.Contact;
import com.wordnik.swagger.annotations.ApiModel;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.events.BookingDto;
import com.workpoint.icpak.shared.model.events.DelegateDto;
import com.workpoint.icpak.shared.model.events.EnrollmentDto;

/**
 * Booking Model
 * 
 * @author duggan
 *
 */
@ApiModel(value = "A Booking Model", description = "Represents a booking of an event by a user")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({ Delegate.class, Contact.class })
@Entity
@Table(name = "booking")
public class Booking extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Embedded
	private Contact contact;
	private String paymentMode;// MPesa, VISA etc
	private String currency;
	private Date bookingDate = new Date();
	private String userId; // Member who made the booking
	@Transient
	private String eventId;
	private String status; // DRAFT/ PAID
	private int delegatesCount;
	// Payment
	@Column(unique = true)
	private String paymentRef; // TrxNumber
	private Date paymentDate;
	private Double amountDue;
	private PaymentStatus paymentStatus = PaymentStatus.NOTPAID;

	@OneToMany(mappedBy = "booking", fetch = FetchType.LAZY, cascade = {
			CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE,
			CascadeType.REFRESH })
	private Collection<Delegate> delegates = new HashSet<>();
	@ManyToOne
	private Event event;
	private Date registrationDate = new Date();
	private String memberId; // For Courses, may not have delegates

	public Date getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}

	public String getPaymentRef() {
		return paymentRef;
	}

	public void setPaymentRef(String paymentRef) {
		this.paymentRef = paymentRef;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Booking clone(String... expand) {

		Booking booking = new Booking();
		booking.setBookingDate(bookingDate);
		booking.setRefId(getRefId());
		booking.setStatus(status);
		booking.setPaymentRef(paymentRef);
		booking.setPaymentDate(paymentDate);
		booking.setPaymentMode(paymentMode);

		// booking.setContact(contact);
		booking.setCurrency(currency);
		if (delegates != null)
			for (Delegate delegate : delegates) {
				booking.addDelegate(delegate);
			}

		booking.setDelegatesCount(delegatesCount);
		booking.setEvent(event);
		// booking.setUser(user.clone());
		booking.setEventId(eventId);

		if (expand != null)
			for (String token : expand) {
				if (token.equals("user")) {

				}

				if (token.equals("event")) {

				}

				if (token.equalsIgnoreCase(ExpandTokens.META.name())) {
					// booking.setCreated(created);
					// booking.setCreatedBy(createdBy);
				}

			}

		return booking;
	}

	private void addDelegate(Delegate delegate) {
		delegates.add(delegate);
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getDelegatesCount() {
		return delegatesCount;
	}

	public void setDelegatesCount(int delegatesCount) {
		this.delegatesCount = delegatesCount;
	}

	public Collection<Delegate> getDelegates() {
		return delegates;
	}

	public void setDelegates(Collection<Delegate> delegates) {
		this.delegates.clear();
		for (Delegate delegate : delegates) {
			this.delegates.add(delegate);
			delegate.setBooking(this);
		}
		setDelegatesCount(delegates.size());
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public Double getAmountDue() {
		return amountDue;
	}

	public void setAmountDue(Double amountDue) {
		this.amountDue = amountDue;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public BookingDto toDto() {

		BookingDto dto = new BookingDto();
		dto.setAmountDue(amountDue);

		if (bookingDate != null)
			dto.setBookingDate(bookingDate.getTime());

		dto.setContact(contact.toDto());
		dto.setCurrency(currency);

		if (getDelegates() != null) {
			List<DelegateDto> dtos = new ArrayList<>();
			for (Delegate delegate : delegates) {
				dtos.add(delegate.toDto());
			}
			dto.setDelegates(dtos);
		}

		if (getEvent() != null) {
			dto.setEventRefId(getEvent().getRefId());
		}

		if (paymentDate != null)
			dto.setPaymentDate(paymentDate.getTime());
		dto.setPaymentMode(paymentMode);
		dto.setPaymentRef(paymentRef);
		dto.setPaymentStatus(paymentStatus);
		dto.setRefId(getRefId());
		dto.setStatus(status);
		// dto.setInvoiceRef(invoiceRe);

		return dto;
	}

	public void copyFrom(BookingDto dto) {
		setBookingDate(dto.getBookingDate() == null ? null : new Date(
				dto.getBookingDate()));
		setPaymentStatus(dto.getPaymentStatus());
		setPaymentMode(dto.getPaymentMode());
		setPaymentDate(dto.getPaymentDate() == null ? null : new Date(
				dto.getPaymentDate()));
		setStatus(dto.getStatus());
		setCurrency(dto.getCurrency());
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public EnrollmentDto toEnrollmentDto() {
		EnrollmentDto dto = new EnrollmentDto();
		dto.setAmountDue(amountDue);
		dto.setRegistrationDate(registrationDate);

		if (bookingDate != null)
			dto.setBookingDate(bookingDate.getTime());

		dto.setContact(contact.toDto());
		dto.setCurrency(currency);

		if (getEvent() != null) {
			dto.setEventRefId(getEvent().getRefId());
		}

		if (paymentDate != null)
			dto.setPaymentDate(paymentDate.getTime());
		dto.setPaymentMode(paymentMode);
		dto.setPaymentRef(paymentRef);
		dto.setPaymentStatus(paymentStatus);
		dto.setRefId(getRefId());
		dto.setStatus(status);
		dto.setMemberId(memberId);
		return dto;
	}

	public void copyFrom(EnrollmentDto dto) {
		setBookingDate(dto.getBookingDate() == null ? null : new Date(
				dto.getBookingDate()));
		setPaymentMode(dto.getPaymentMode());
		setPaymentDate(dto.getPaymentDate() == null ? null : new Date(
				dto.getPaymentDate()));
		setStatus(dto.getStatus());
		setCurrency(dto.getCurrency());
		setMemberId(dto.getMemberId());
		setRegistrationDate(dto.getRegistrationDate());
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}
}
