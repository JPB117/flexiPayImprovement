package com.icpak.rest.models.event;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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

/**
 * Booking Model 
 * @author duggan
 *
 */
@ApiModel(value="A Booking Model", description="Represents a booking of an event by a user")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Delegate.class, Contact.class})

@Entity
@Table(name="booking")
public class Booking extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String companyName;
	
//	@OneToOne(mappedBy="booking", 
//			cascade={CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	@Embedded
	private Contact contact;
	
	private String paymentMode;//MPesa, VISA etc
	private String currency;
	private Date bookingDate;
	
	@Transient
	private String eventId;
	
	private String status; //DRAFT/ PAID
	private int delegatesCount;

	//Payment
	@Column(unique=true)
	private String paymentRef; //TrxNumber
	private Date paymentDate;
	private Double amountDue;
	private Double amountPaid;
	private PaymentStatus paymentStatus= PaymentStatus.PAID;
	
	@OneToMany(mappedBy="booking",fetch=FetchType.LAZY,cascade={CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private Collection<Delegate> delegates = new HashSet<>();
	
	@ManyToOne
	private Event event;
	
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

	public Booking clone(String ... expand){
		
		Booking booking = new Booking();
		booking.setBookingDate(bookingDate);
		booking.setRefId(getRefId());
		booking.setStatus(status);
		booking.setPaymentRef(paymentRef);
		booking.setPaymentDate(paymentDate);
		booking.setPaymentMode(paymentMode);
		
		//booking.setContact(contact);
		booking.setCompanyName(companyName);
		booking.setCurrency(currency);
		if(delegates!=null)
		for(Delegate delegate: delegates){
			booking.addDelegate(delegate);
		}
		
		booking.setDelegatesCount(delegatesCount);
		booking.setEvent(event);
		//booking.setUser(user.clone());
		booking.setEventId(eventId);
		
		if(expand!=null)
		for(String token: expand){
			if(token.equals("user")){
				
			}
			
			if(token.equals("event")){
				
			}
			
			if(token.equalsIgnoreCase(ExpandTokens.META.name())){
//				booking.setCreated(created);
//				booking.setCreatedBy(createdBy);
			}
			
			
		}
		
		return booking;
	}

	private void addDelegate(Delegate delegate) {
		delegates.add(delegate);
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

//	public Contact getContact() {
//		return contact;
//	}
//
//	public void setContact(Contact contact) {
//		this.contact = contact;
//	}

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
		delegates.clear();
		for(Delegate delegate: delegates){
			delegates.add(delegate);
			delegate.setBooking(this);
		}
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

	public Double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	
}
