package com.workpoint.icpak.shared.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationFormTrainingDto extends SerializableObj {
	/**
	 * 
	 */
	public enum TrainingType implements Listable {
		BEFOREQUALIFYING("Before Qualifying"), AFTERQUALIFYING(
				"After Qualifying");

		private String displayName;

		TrainingType(String displayName) {
			this.displayName = displayName;
		}

		@Override
		public String getName() {
			return displayName;
		}

		@Override
		public String getDisplayName() {
			return displayName;
		}

	}

	private static final long serialVersionUID = 1L;
	private String organisationName;
	private String position;
	private String taskNature;
	private Date toDate;
	private Date fromDate;
	private String responsibility;
	private TrainingType trainingType;
	private String clients;
	private Date datePassed;

	public ApplicationFormTrainingDto() {
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getTaskNature() {
		return taskNature;
	}

	public void setTaskNature(String taskNature) {
		this.taskNature = taskNature;
	}

	public String getResponsibility() {
		return responsibility;
	}

	public void setResponsibility(String responsibility) {
		this.responsibility = responsibility;
	}

	public String getClients() {
		return clients;
	}

	public void setClients(String clients) {
		this.clients = clients;
	}

	public Date getDatePassed() {
		return datePassed;
	}

	public void setDatePassed(Date datePassed) {
		this.datePassed = datePassed;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public TrainingType getTrainingType() {
		return trainingType;
	}

	public void setTrainingType(TrainingType trainingType) {
		this.trainingType = trainingType;
	}

}
