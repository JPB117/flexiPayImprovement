package com.icpak.rest.models.membership;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name="practice")
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class Practice extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name; //Practice Name
	private PracticeStyle type; // Sole, Partnership
	private int isFullTime;
	private String propTime; //Proportion of time spent in practice **check
	private int isOtherServicesProvided; //Other services provided by your firm
	
	@Column(length=500)
	private String otherServices;//Other Services Offered by you/ your firm
	private Date practiceCommenceDate;
	private Date registrationDate; // Date firm registered (With registrar of companies or icpak?)
	private String pin; // KRA PIN
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="memberid")
	private Member member;
	
	@ElementCollection(fetch=FetchType.LAZY)
    @CollectionTable(name="practice_categories")
    @Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	private Collection<String> practiceCategories;
	
	@OneToMany(mappedBy="practice", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private Collection<Client> clients;
	
	@OneToMany(mappedBy="practice", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private Collection<Branch> branches;
	
	@OneToMany(mappedBy="practice", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private Collection<Partner> partners;
	
//	@OneToMany(mappedBy="practice", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
//	private Collection<Contact> contacts;

	public Practice clone(String string) {
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PracticeStyle getType() {
		return type;
	}

	public void setType(PracticeStyle type) {
		this.type = type;
	}

	public boolean isFullTime() {
		return isFullTime==1;
	}

	public void setFullTime(boolean isFullTime) {
		this.isFullTime = isFullTime? 1:0;
	}

	public String getPropTime() {
		return propTime;
	}

	public void setPropTime(String propTime) {
		this.propTime = propTime;
	}

	public boolean isOtherServicesProvided() {
		return isOtherServicesProvided==1;
	}

	public void setOtherServicesProvided(boolean isOtherServicesProvided) {
		this.isOtherServicesProvided = isOtherServicesProvided? 1:0;
	}

	public String getOtherServices() {
		return otherServices;
	}

	public void setOtherServices(String otherServices) {
		this.otherServices = otherServices;
	}

	public Date getPracticeCommenceDate() {
		return practiceCommenceDate;
	}

	public void setPracticeCommenceDate(Date practiceCommenceDate) {
		this.practiceCommenceDate = practiceCommenceDate;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Collection<String> getPracticeCategories() {
		return practiceCategories;
	}

	public void setPracticeCategories(Collection<String> practiceCategories) {
		this.practiceCategories = practiceCategories;
	}

	public Collection<Client> getClients() {
		return clients;
	}

	public void setClients(Collection<Client> clients) {
		this.clients = clients;
	}

	public int getIsFullTime() {
		return isFullTime;
	}

	public void setIsFullTime(int isFullTime) {
		this.isFullTime = isFullTime;
	}

	public int getIsOtherServicesProvided() {
		return isOtherServicesProvided;
	}

	public void setIsOtherServicesProvided(int isOtherServicesProvided) {
		this.isOtherServicesProvided = isOtherServicesProvided;
	}

	public Collection<Branch> getBranches() {
		return branches;
	}

	public void setBranches(Collection<Branch> branches) {
		this.branches = branches;
	}
	
}
