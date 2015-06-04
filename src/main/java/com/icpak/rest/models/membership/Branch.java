package com.icpak.rest.models.membership;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.icpak.rest.models.base.PO;
import com.wordnik.swagger.annotations.ApiModel;


@ApiModel(description="Firm under which a member practices")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Member.class})

@Entity
@Table(name="branch")
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class Branch extends PO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	
	@ManyToOne
	@JoinColumn(name="practiceid")
	private Practice practice;
	
	private String memberRegNo; //Member incharge of this branch must be a CPA older
	
	private String practicingCertNo;
	
//	@OneToMany(mappedBy="branch")
//	private Collection<Contact> contacts = new HashSet<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMemberRegNo() {
		return memberRegNo;
	}

	public void setMemberRegNo(String memberRegNo) {
		this.memberRegNo = memberRegNo;
	}

	public String getPracticingCertNo() {
		return practicingCertNo;
	}

	public void setPracticingCertNo(String practicingCertNo) {
		this.practicingCertNo = practicingCertNo;
	}

//	public Collection<Contact> getContacts() {
//		return contacts;
//	}
//
//	public void setContacts(Collection<Contact> contacts) {
//		this.contacts = contacts;
//	}

	public Practice getPractice() {
		return practice;
	}

	public void setPractice(Practice practice) {
		this.practice = practice;
	}

	public Branch clone(String ... tokens){
		return this;
	}
}
