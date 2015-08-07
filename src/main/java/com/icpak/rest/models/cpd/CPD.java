package com.icpak.rest.models.cpd;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.icpak.rest.models.base.PO;
import com.wordnik.swagger.annotations.ApiModel;
import com.workpoint.icpak.shared.model.CPDCategory;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.shared.model.CPDStatus;

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

	private Date startDate; //Copied from Event details;
	private Date endDate;
	private String title;
	private String organizer;
	private CPDCategory category;
	private int cpdHours;
	private CPDStatus status = CPDStatus.UNCONFIRMED;
	private String memberId;
	private String eventId;
	
	public CPD() {
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

	public CPDStatus getStatus() {
		return status;
	}

	public void setStatus(CPDStatus status) {
		this.status = status;
	}
	
	public void copyFrom(CPDDto dto){
		setCpdHours(dto.getCpdHours());
		setEndDate(dto.getEndDate());
		setStartDate(dto.getStartDate());
		
		if(dto.getStatus()!=null)
			setStatus(dto.getStatus());
		
		setCategory(dto.getCategory());
		setMemberId(dto.getMemberId());
		setOrganizer(dto.getOrganizer());
		setTitle(dto.getTitle());
		setEventId(dto.getEventId());
	}

	public CPDDto toDTO() {
		
		CPDDto dto = new CPDDto();
		dto.setCreated(getCreated());
		dto.setMemberId(memberId);
		dto.setRefId(getRefId());
		dto.setCategory(category);
		dto.setCpdHours(cpdHours);
		dto.setEndDate(endDate);
		dto.setOrganizer(organizer);
		dto.setStartDate(startDate);
		dto.setStatus(status);
		dto.setTitle(title);
		dto.setEventId(eventId);
		
		return dto;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOrganizer() {
		return organizer;
	}

	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}

	public CPDCategory getCategory() {
		return category;
	}

	public void setCategory(CPDCategory category) {
		this.category = category;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public int getCpdHours() {
		return cpdHours;
	}

	public void setCpdHours(int cpdHours) {
		this.cpdHours = cpdHours;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	
}
