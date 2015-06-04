package com.icpak.rest.models.membership;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.icpak.rest.models.auth.BioData;
import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.base.PO;
import com.icpak.rest.models.cpd.CPD;
import com.wordnik.swagger.annotations.ApiModel;


@ApiModel(description="Membership details of a user")

//@JsonInclude(Include.NON_NULL)
//@JsonSerialize(include=Inclusion.NON_NULL)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Specialization.class, Education.class,TrainingAndExperience.class, CriminalOffense.class,CPD.class})

@Entity
@Table(name="member")
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class Member extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private MembershipStatus status;
	private int hasConvictions=0;
	private String pin;
	private MemberType memberType; // Member, AssociateMember, Practicing Member
	
	@Embedded
	private Contact contact;
	
	@Embedded
	BioData bioData;
	
	@Transient
	private String userId;
	
	@JsonIgnore
	@XmlTransient
	@OneToOne(mappedBy="member",cascade=CascadeType.ALL)
	@JoinColumn(name="userId")
	private User user;//A member is a system user
	
	@XmlTransient
	@OneToMany(mappedBy="member", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	private Collection<Education> education = new HashSet<>();
	
	@XmlTransient
	@OneToMany(mappedBy="member", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	private Collection<Specialization> specializations = new HashSet<>();
	
	@XmlTransient
	@OneToMany(mappedBy="member", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	private Collection<TrainingAndExperience> trainingAndExperience = new HashSet<>();
	
	@XmlTransient
	@OneToMany(mappedBy="member", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	private Collection<CriminalOffense> offenses = new HashSet<>();
	
	@XmlTransient
	@OneToMany(mappedBy="member", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	private Collection<Partner> partners = new HashSet<>();
	
	@XmlTransient
	@OneToMany(mappedBy="member", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	private Collection<Partner> partnerships = new HashSet<>();
	
	@XmlTransient
	@OneToMany(mappedBy="member", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	private Collection<Application> applications = new HashSet<>();
	
	@XmlTransient
	@OneToMany(mappedBy="member", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	private Collection<Practice> firms = new HashSet<>();
	
	@XmlTransient
	@OneToMany(mappedBy="member", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	private Collection<CPD> cpd = new HashSet<>();
	
	public Member() {
	}

	public Member(String memberId) {
		this.refId = memberId;
	}

	public MembershipStatus getStatus() {
		return status;
	}

	public void setStatus(MembershipStatus status) {
		this.status = status;
	}

	public boolean getHasConvictions() {
		return hasConvictions==0;
	}

	public void setHasConvictions(boolean hasConvictions) {
		this.hasConvictions = hasConvictions? 1: 0;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public MemberType getMemberType() {
		return memberType;
	}

	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		if(this.getUser()!=null){
			this.user.copy(user);
		}else{
			this.user = user;
		}
		
//		this.user.setMember(this);
//		if(userData!=null){
//			user.setUserData(userData);
//		}
	}

	public Collection<Application> getApplications() {
		return applications;
	}

	public void setApplications(Collection<Application> applications) {
		this.applications = applications;
	}

	public Collection<Practice> getFirms() {
		return firms;
	}

	public void setFirms(Collection<Practice> firms) {
		this.firms = firms;
	}

	public Collection<Education> getEducation() {
		return education;
	}

	public void setEducation(Collection<Education> education) {
		for(Education edu: education){
			edu.setMember(this);
			this.education.add(edu);
		}
	}

	public Collection<Specialization> getSpecializations() {
		return specializations;
	}

	public void setSpecializations(Collection<Specialization> specializations) {
		this.specializations = specializations;
	}

	public Collection<TrainingAndExperience> getTrainingAndExperience() {
		return trainingAndExperience;
	}

	public void setTrainingAndExperience(
			Collection<TrainingAndExperience> trainingAndExperience) {
		this.trainingAndExperience = trainingAndExperience;
	}

	public Collection<CriminalOffense> getOffenses() {
		return offenses;
	}

	public void setOffenses(Collection<CriminalOffense> offenses) {
		this.offenses = offenses;
	}

	public Collection<Partner> getPartners() {
		return partners;
	}

	public void setPartners(Collection<Partner> partners) {
		this.partners = partners;
	}

	public Collection<Partner> getPartnerships() {
		return partnerships;
	}

	public void setPartnerships(Collection<Partner> partnerships) {
		this.partnerships = partnerships;
	}

	public Member clone(String ... expand) {
		Member member = this;
		member.setUserId(userId);
		return member;
	}

	public Collection<CPD> getCpd() {
		return cpd;
	}

	public void setCpd(Collection<CPD> cpd) {
		this.cpd = cpd;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFullNames() {
		
		return this.bioData.getFullNames();
	}

}
