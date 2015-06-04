package com.icpak.rest.models.membership;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.icpak.rest.models.base.PO;
import com.wordnik.swagger.annotations.ApiModel;

@ApiModel(description="Application Category charges")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

@Entity
@Table(name="ApplicationCategory")
public class Category extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Enumerated(EnumType.ORDINAL)
	private ApplicationType type;
	private String description;
	private double applicationAmount;
	private Double renewalAmount;
	
	public Category() {
	}

	public ApplicationType getType() {
		return type;
	}

	public void setType(ApplicationType type) {
		this.type = type;
	}

	public double getApplicationAmount() {
		return applicationAmount;
	}

	public void setApplicationAmount(double applicationAmount) {
		this.applicationAmount = applicationAmount;
	}

	public Double getRenewalAmount() {
		return renewalAmount;
	}

	public void setRenewalAmount(Double renewalAmount) {
		this.renewalAmount = renewalAmount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
