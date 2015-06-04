package com.icpak.rest.models.membership;

import java.util.Collection;
import java.util.Date;

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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.icpak.rest.models.base.PO;
import com.icpak.rest.models.util.Attachment;
import com.wordnik.swagger.annotations.ApiModel;

@ApiModel(description="Education details of a member")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Member.class})

@Entity
@Table(name="education")
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class Education extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlTransient
	@ManyToOne
	@JoinColumn(name = "memberid")
	private Member member;
	
	@Transient
	private String memberId;
	
	private EduType type = EduType.ACADEMIA;
	
	@Column(length=2000)
	private String institution;
	
	private Date startDate;
	private Date dateCompleted;
	private String examiningBody;
	private String classOrDivision;
	private String award; //Degree or Diploma awarded
	
	//For Professional Acc Exams
	private String registrationNo;
	private String sectionsPassed;
	
	@OneToMany(mappedBy="education",fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	Collection<Attachment> attachments;

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getExaminingBody() {
		return examiningBody;
	}

	public void setExaminingBody(String examiningBody) {
		this.examiningBody = examiningBody;
	}

	public String getClassOrDivision() {
		return classOrDivision;
	}

	public void setClassOrDivision(String classOrDivision) {
		this.classOrDivision = classOrDivision;
	}

	public String getAward() {
		return award;
	}

	public void setAward(String award) {
		this.award = award;
	}

	public Collection<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(Collection<Attachment> attachments) {
		this.attachments = attachments;
	}

	public Date getDateCompleted() {
		return dateCompleted;
	}

	public void setDateCompleted(Date dateCompleted) {
		this.dateCompleted = dateCompleted;
	}

	public String getRegistrationNo() {
		return registrationNo;
	}

	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}

	public String getSectionsPassed() {
		return sectionsPassed;
	}

	public void setSectionsPassed(String sectionsPassed) {
		this.sectionsPassed = sectionsPassed;
	}

	public EduType getType() {
		return type;
	}

	public void setType(EduType type) {
		this.type = type;
	}
	
	public Education clone(String ... details){
		return this;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public void copyFrom(Education eduEntry) {
		this.type = eduEntry.getType();
		this.institution = eduEntry.getInstitution();
		this.startDate = eduEntry.getStartDate();
		this.dateCompleted = eduEntry.getDateCompleted();
		this.examiningBody = eduEntry.getExaminingBody();
		this.classOrDivision =eduEntry.getClassOrDivision();
		this.award = eduEntry.getAward();
		this.registrationNo = eduEntry.getRegistrationNo();
		this.sectionsPassed = eduEntry.getSectionsPassed();
	}
		
}
