package com.icpak.rest.models.membership;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.icpak.rest.models.base.PO;
import com.wordnik.swagger.annotations.ApiModel;

@ApiModel(description="Partners of a firm")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Member.class})

@Entity
@Table(name="partner")
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class Partner extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name="memberid")
	private Member member;
	
	@ManyToOne
	@JoinColumn(name="practiceid")
	private Practice practice;
	
	private String practicingCertNo;
	private String residentialStatus;
	private String yearAdmitted;
	private int practiceTimeType; //Full Time or Part type ?? Too granuallym, should be applied to the firm
	private int propAuditTime; //Prop of time partner gives to audit and assurance
	private String pin;
	
	public Partner(){}
	
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	public Practice getPractice() {
		return practice;
	}
	public void setPractice(Practice practice) {
		this.practice = practice;
	}
	public String getPracticingCertNo() {
		return practicingCertNo;
	}
	public void setPracticingCertNo(String practicingCertNo) {
		this.practicingCertNo = practicingCertNo;
	}
	public String getResidentialStatus() {
		return residentialStatus;
	}
	public void setResidentialStatus(String residentialStatus) {
		this.residentialStatus = residentialStatus;
	}
	public String getYearAdmitted() {
		return yearAdmitted;
	}
	public void setYearAdmitted(String yearAdmitted) {
		this.yearAdmitted = yearAdmitted;
	}
	public int getPracticeTimeType() {
		return practiceTimeType;
	}
	public void setPracticeTimeType(int practiceTimeType) {
		this.practiceTimeType = practiceTimeType;
	}
	public int getPropAuditTime() {
		return propAuditTime;
	}
	public void setPropAuditTime(int propAuditTime) {
		this.propAuditTime = propAuditTime;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
}
