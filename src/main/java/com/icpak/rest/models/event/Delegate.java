package com.icpak.rest.models.event;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import com.icpak.rest.models.base.PO;
import com.wordnik.swagger.annotations.ApiModel;
import com.workpoint.icpak.shared.model.events.DelegateDto;

@ApiModel(value="Event delegates", description="List of delegates sharing a single booking")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Booking.class})

@Entity
@Table(name="delegate")
public class Delegate extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String memberRegistrationNo;
	private String title;
	private String surname;
	private String otherNames;
	private String email;
	private boolean isMember;
	
	@Transient
	private String bookingId;
	
	@ManyToOne
	@JoinColumn(name="booking_id")
	@XmlTransient
	private Booking booking;

	public String getMemberRegistrationNo() {
		return memberRegistrationNo;
	}

	public void setMemberRegistrationNo(String memberRegistrationNo) {
		this.memberRegistrationNo = memberRegistrationNo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getOtherNames() {
		return otherNames;
	}

	public void setOtherNames(String otherNames) {
		this.otherNames = otherNames;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public void copyFrom(DelegateDto delegateDto) {
		setEmail(delegateDto.getEmail());
		setMemberRegistrationNo(delegateDto.getMemberRegistrationNo());
		setOtherNames(delegateDto.getOtherNames());
		setSurname(delegateDto.getSurname());
		setTitle(delegateDto.getTitle());
		setRefId(delegateDto.getRefId());
	}

	public DelegateDto toDto() {
		DelegateDto dto  = new DelegateDto();
		dto.setEmail(email);
		dto.setMemberRegistrationNo(memberRegistrationNo);
		dto.setOtherNames(otherNames);
		dto.setRefId(bookingId);
		dto.setSurname(surname);
		dto.setTitle(title);
		
		return dto;
	}

	public boolean isMember() {
		return isMember;
	}

	public void setMember(boolean isMember) {
		this.isMember = isMember;
	}

}
