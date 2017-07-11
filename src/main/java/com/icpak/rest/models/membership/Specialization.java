package com.icpak.rest.models.membership;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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

	public Specialization clone(String...details){
		Specialization s = new Specialization();
		s.setRefId(refId);
		s.setSpecialization(specialization);
		
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
