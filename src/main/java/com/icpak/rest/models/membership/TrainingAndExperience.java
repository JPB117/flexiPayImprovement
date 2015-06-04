package com.icpak.rest.models.membership;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.icpak.rest.models.base.PO;
import com.icpak.rest.models.util.Attachment;
import com.wordnik.swagger.annotations.ApiModel;

@ApiModel(description="Training and Experience of a member")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

@Entity
@Table(name="trainingandexperience")
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class TrainingAndExperience extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String organizationName;
	private Date startDate;
	private Date endDate;
	@Column(length=5000)
	private String positionHeld;
	private String natureOfTasksPerformed; // Training tasks
	private String responsibilities;
	
	@Basic(optional=false)
	private TrainingExperienceType type = TrainingExperienceType.TRAINING;
	private String clientsHandled;
	private Date datePassed;

	@XmlTransient
	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;
	
	@Transient
	private String memberId;
	
	@OneToMany(mappedBy="trainingAndExperience", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	Collection<Attachment> attachments;
	
	@OneToMany(mappedBy="trainingAndExperience",fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	Collection<Referee> referees;

	public TrainingAndExperience() {
	}
	
	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
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

	public String getPositionHeld() {
		return positionHeld;
	}

	public void setPositionHeld(String positionHeld) {
		this.positionHeld = positionHeld;
	}

	public String getNatureOfTasksPerformed() {
		return natureOfTasksPerformed;
	}

	public void setNatureOfTasksPerformed(String natureOfTasksPerformed) {
		this.natureOfTasksPerformed = natureOfTasksPerformed;
	}

	public String getResponsibilities() {
		return responsibilities;
	}

	public void setResponsibilities(String responsibilities) {
		this.responsibilities = responsibilities;
	}

	public TrainingExperienceType getType() {
		return type;
	}

	public void setType(TrainingExperienceType type) {
		this.type = type;
	}

	public String getClientsHandled() {
		return clientsHandled;
	}

	public void setClientsHandled(String clientsHandled) {
		this.clientsHandled = clientsHandled;
	}

	public Date getDatePassed() {
		return datePassed;
	}

	public void setDatePassed(Date datePassed) {
		this.datePassed = datePassed;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Collection<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(Collection<Attachment> attachments) {
		this.attachments = attachments;
	}

	public Collection<Referee> getReferees() {
		return referees;
	}

	public void setReferees(Collection<Referee> referees) {
		this.referees = referees;
	} 
	
	public TrainingAndExperience clone(String ...detail){
		if(this.member!=null)
		this.setMemberId(member.getRefId());
		return this;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public void copyFrom(TrainingAndExperience eduEntry) {
		this.setClientsHandled(eduEntry.getClientsHandled());
		this.setDatePassed(eduEntry.getDatePassed());
		this.setEndDate(eduEntry.getEndDate());
		this.setNatureOfTasksPerformed(eduEntry.getNatureOfTasksPerformed());
		this.setOrganizationName(eduEntry.getOrganizationName());
		this.setPositionHeld(eduEntry.getPositionHeld());
		this.setReferees(eduEntry.getReferees());
		this.setResponsibilities(eduEntry.getResponsibilities());
		this.setStartDate(eduEntry.getStartDate());
		this.setType(eduEntry.getType());		
	}
}
