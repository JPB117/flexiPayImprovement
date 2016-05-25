package com.workpoint.icpak.shared.model;

public class ApplicationCategoryDto extends SerializableObj {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ApplicationType type;
	private String description;
	private double applicationAmount;
	private Double renewalAmount;

	public ApplicationCategoryDto() {
	}

	public ApplicationType getType() {
		return type;
	}

	public void setType(ApplicationType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

}
