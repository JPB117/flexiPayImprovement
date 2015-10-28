package com.icpak.rest.models.membership;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.icpak.rest.models.base.PO;
import com.workpoint.icpak.shared.model.ApplicationFormTrainingDto;
import com.workpoint.icpak.shared.model.TrainingType;

@Entity
@Table(name="ApplicationFormTraining")
public class ApplicationFormTraining extends PO{

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
	

	@Column( name = "`ApplicationRefId`", length = 20)
	private String applicationRefId;
	
	public ApplicationFormTraining() {
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
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public String getResponsibility() {
		return responsibility;
	}
	public void setResponsibility(String responsibility) {
		this.responsibility = responsibility;
	}
	public TrainingType getTrainingType() {
		return trainingType;
	}
	public void setTrainingType(TrainingType trainingType) {
		this.trainingType = trainingType;
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
	
	public void copyFrom(ApplicationFormTrainingDto dto){
		setClients(dto.getClients());
		setDatePassed(dto.getDatePassed());
		setFromDate(dto.getFromDate());
		setOrganisationName(dto.getOrganisationName());
		setPosition(dto.getPosition() );
		setResponsibility(dto.getResponsibility());
		setTaskNature(dto.getTaskNature());
		setToDate(dto.getToDate());
		setTrainingType(dto.getTrainingType());
		
	}
	
	public ApplicationFormTrainingDto toDto(){
		ApplicationFormTrainingDto dto = new ApplicationFormTrainingDto();
		dto.setClients(clients);
		dto.setDatePassed(datePassed);
		dto.setFromDate(fromDate);
		dto.setOrganisationName(organisationName);
		dto.setPosition(position);
		dto.setRefId(applicationRefId);
		dto.setResponsibility(responsibility);
		dto.setTaskNature(taskNature);
		dto.setToDate(toDate);
		dto.setTrainingType(trainingType);
		
		return dto;
	}

	public String getApplicationRefId() {
		return applicationRefId;
	}

	public void setApplicationRefId(String applicationRefId) {
		this.applicationRefId = applicationRefId;
	}
	
	
}
