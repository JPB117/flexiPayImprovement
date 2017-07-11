package com.workpoint.icpak.shared.model.events;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.workpoint.icpak.shared.model.EventStatus;
import com.workpoint.icpak.shared.model.PaymentStatus;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class MemberBookingDto implements Serializable, IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String delegateRefId;
	private String bookingRefId;
	private String eventRefId;
	private Date startDate;
	private Date endDate;
	private String eventName;
	private String location;
	private EventStatus eventStatus;
	private String cpdHours;
	private AttendanceStatus attendance;
	private PaymentStatus paymentStatus;
	private Integer bookingStatus;
	private String accommodation;

	public MemberBookingDto() {
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public EventStatus getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(EventStatus eventStatus) {
		this.eventStatus = eventStatus;
	}

	public String getCpdHours() {
		return cpdHours;
	}

	public void setCpdHours(String cpdHours) {
		this.cpdHours = cpdHours;
	}

	public AttendanceStatus getAttendance() {
		return attendance;
	}

	public void setAttendance(AttendanceStatus attendance) {
		this.attendance = attendance;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getAccommodation() {
		return accommodation;
	}

	public void setAccommodation(String accommodation) {
		this.accommodation = accommodation;
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

	public String getDelegateRefId() {
		return delegateRefId;
	}

	public void setDelegateRefId(String delegateRefId) {
		this.delegateRefId = delegateRefId;
	}

	public String getBookingRefId() {
		return bookingRefId;
	}

	public void setBookingRefId(String bookingRefId) {
		this.bookingRefId = bookingRefId;
	}

	public String getEventRefId() {
		return eventRefId;
	}

	public void setEventRefId(String eventRefId) {
		this.eventRefId = eventRefId;
	}

	public Integer getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(Integer bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

}
