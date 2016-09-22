package com.icpak.rest.models.membership;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.icpak.rest.models.base.PO;
import com.wordnik.swagger.annotations.ApiModel;
import com.workpoint.icpak.shared.model.ApplicationCategoryDto;
import com.workpoint.icpak.shared.model.ApplicationType;

@ApiModel(description = "Application Category charges")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "ApplicationCategory")
public class ApplicationCategory extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	private ApplicationType type;
	private String description;
	private double applicationAmount;
	private Double renewalAmount;
	private Date renewalDueDate;

	public ApplicationCategory() {
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

	public ApplicationCategoryDto toDto() {
		ApplicationCategoryDto category = new ApplicationCategoryDto();
		category.setRefId(refId);
		category.setApplicationAmount(applicationAmount);
		category.setDescription(description);
		category.setRenewalAmount(renewalAmount);
		category.setType(type);

		return category;
	}

	public void copyFrom(ApplicationCategoryDto dto) {
		setType(dto.getType());
		setDescription(dto.getDescription());
		setApplicationAmount(dto.getApplicationAmount());
		setRenewalAmount(dto.getRenewalAmount());
	}

	public Date getRenewalDueDate() {
		return renewalDueDate;
	}

	public void setRenewalDueDate(Date renewalDueDate) {
		this.renewalDueDate = renewalDueDate;
	}
}
