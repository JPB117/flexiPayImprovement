package com.icpak.rest.models.util;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.icpak.rest.models.base.PO;
import com.icpak.rest.models.membership.Member;
import com.wordnik.swagger.annotations.ApiModel;


/**
 * Program Calendar Periods</p>
 * All the program targets/ outcomes/ finances are managed within 
 * a single calendar period, usually 1st Jan - 31st Dec or 1 July - 30 June
 *  
 * @author duggan
 *
 */


@ApiModel(description="Calendar period")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Member.class})

@Entity
@NamedQueries({
	@NamedQuery(name="Period.findActive", query="SELECT p FROM Period p where p.isActive=:isActive and p.startDate<:now and p.endDate>:now order by p.description"),
	@NamedQuery(name="Period.findAll", query="SELECT p FROM Period p"),
	@NamedQuery(name="Period.findById", query=" SELECT p FROM Period p where p.id=:id")
	})
public class Period extends PO{

	private static final long serialVersionUID = -5038433776061027590L;
	
	private String description;
	
	@Column(nullable=false)
	@Temporal(TemporalType.DATE)
	private Date startDate;
	
	@Column(nullable=false)
	@Temporal(TemporalType.DATE)
	private Date endDate;
	
	public Period() {
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDescription() {
		return description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}
	
	@Override
	public String toString() {
		return getDescription();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null  || !(obj instanceof Period)){
			return false;
		}
		
		Period period = (Period)obj;
		if(period.getId()!=null && getId()!=null){
			return period.getId().equals(getId());
		}else{
			return period.getStartDate().equals(startDate) && period.getEndDate().equals(endDate); 
		}
	
	}
}
