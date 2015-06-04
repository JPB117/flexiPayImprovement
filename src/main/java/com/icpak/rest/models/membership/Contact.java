package com.icpak.rest.models.membership;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.icpak.rest.models.base.PO;
import com.icpak.rest.models.event.Booking;
import com.wordnik.swagger.annotations.ApiModel;


@ApiModel(description="A Persons or organizations contacts")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Member.class})

//@Entity
//@Table(name="contact")
@Embeddable
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class Contact{

	/**
	 * 
	 */
	private ContactType type;
	private boolean isPrimaryContact;
	
	@Column(name="`Address`",  columnDefinition="varchar(50)")
	private String address;
	
	@Column(name="`Address 2`", columnDefinition="varchar(50)")
	private String address2;
	
	@Column(name="`City`", columnDefinition="varchar(30)")
	private String city;
	
	@Column(name="`Contact`", columnDefinition="varchar(50)")
	private String contactName;
	
	@Column(name="`Phone No_`", columnDefinition="varchar(37)")
	private String telephoneNumbers;
	
	@Column(name="`Telex No_`", columnDefinition="varchar(20)")
	private String telexNo;
	
	@Column(name="`Territory Code`", columnDefinition="varchar(10)")
	private String territoryCode;
	
	@Column(name="`Fax No_`", columnDefinition="varchar(30)")
	private String fax;
	
	@Column(name="`Telex Answer Back`", columnDefinition="varchar(20)")
	private String telexAnswerBack;
	
	@Column(name="`Post Code`", columnDefinition="varchar(20)")
	private String postCode;
	
	@Column(name="`County`", columnDefinition="varchar(20)")
	private String county;
	
	@Column(name="`E-Mail`", columnDefinition="varchar(80)")
	private String email;
	
	@Column(name="`Home Page`", columnDefinition="varchar(60)")
	private String website;
	
	private String mobileNumbers;
	
	@Column(length=500)
	private String physicalAddress;
	
	private String postalCode;
	
	private String country;
	
//	@OneToOne
//	@XmlTransient
//	private Booking booking;
	

	public ContactType getType() {
		return type;
	}

	public void setType(ContactType type) {
		this.type = type;
	}

	public boolean isPrimaryContact() {
		return isPrimaryContact;
	}

	public void setPrimaryContact(boolean isPrimaryContact) {
		this.isPrimaryContact = isPrimaryContact;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhysicalAddress() {
		return physicalAddress;
	}

	public void setPhysicalAddress(String physicalAddress) {
		this.physicalAddress = physicalAddress;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTelephoneNumbers() {
		return telephoneNumbers;
	}

	public void setTelephoneNumbers(String telephoneNumbers) {
		this.telephoneNumbers = telephoneNumbers;
	}

	public String getMobileNumbers() {
		return mobileNumbers;
	}

	public void setMobileNumbers(String mobileNumbers) {
		this.mobileNumbers = mobileNumbers;
	}

}
