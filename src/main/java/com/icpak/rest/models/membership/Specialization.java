package com.icpak.rest.models.membership;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.icpak.rest.models.base.PO;
import com.wordnik.swagger.annotations.ApiModel;

@ApiModel(description="Specialization details of a member")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

@Entity
@Table(name="specialization")
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class Specialization extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlTransient
	@ManyToOne
	@JoinColumn(name="memberid")
	private Member member;

	@XmlElement
	private String specialization;

	@XmlElement
	@Transient
	private String memberId;	

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	public Specialization clone(String...details){
		Specialization s = new Specialization();
		s.setRefId(refId);
		s.setSpecialization(specialization);
		if(this.member!=null){
			s.setMemberId(member.getRefId());
		}
		
		return s;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public void copyFrom(Specialization offenseEntry) {
		this.setSpecialization(offenseEntry.getSpecialization());
	}
}
