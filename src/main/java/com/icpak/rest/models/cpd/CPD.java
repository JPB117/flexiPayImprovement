package com.icpak.rest.models.cpd;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.icpak.rest.models.base.PO;
import com.icpak.rest.models.event.Event;
import com.icpak.rest.models.membership.Member;
import com.wordnik.swagger.annotations.ApiModel;

/**
 * Simple class that represents any User domain entity in any application.
 */

@ApiModel(value="CPD Model", description="This is a CPD instance model")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

@Entity
@Table(name="cpd")@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class CPD extends PO{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@XmlTransient
	private Member member;
	
	@ManyToOne
	@XmlTransient
	private Event event;
	
	private Date startDate; //Copied from Event details;
	private Date endDate;
	private String eventId;
	
	@Transient
	private String eventName;
	private String cpdHours;
	private CPDStatus status;
	@Transient
	private String memberId;
	
	public CPD() {
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
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

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getCpdHours() {
		return cpdHours;
	}

	public void setCpdHours(String cpdHours) {
		this.cpdHours = cpdHours;
	}

	public CPDStatus getStatus() {
		return status;
	}

	public void setStatus(CPDStatus status) {
		this.status = status;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	public CPD clone(){
		CPD cpd = new CPD();
		cpd.setCpdHours(cpdHours);
		cpd.setStartDate(startDate);
		cpd.setEndDate(endDate);
		cpd.setEventId(eventId);
		
		if(this.getEvent()!=null){
			cpd.setEventName(this.event.getName());
		}
		
		cpd.setRefId(refId);
		cpd.setStatus(status);
		cpd.setMemberId(memberId);
		return cpd;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
}
