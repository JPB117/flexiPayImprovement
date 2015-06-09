package com.icpak.rest.models.membership;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.wordnik.swagger.annotations.ApiModel;
import com.workpoint.icpak.shared.model.events.ContactDto;


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
	private boolean isPrimaryContact=true;
	
	private String company;
	
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

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getTelexNo() {
		return telexNo;
	}

	public void setTelexNo(String telexNo) {
		this.telexNo = telexNo;
	}

	public String getTerritoryCode() {
		return territoryCode;
	}

	public void setTerritoryCode(String territoryCode) {
		this.territoryCode = territoryCode;
	}

	public String getTelexAnswerBack() {
		return telexAnswerBack;
	}

	public void setTelexAnswerBack(String telexAnswerBack) {
		this.telexAnswerBack = telexAnswerBack;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public void copyFrom(ContactDto contactDto) {
		setAddress(contactDto.getAddress() );
		setAddress2(contactDto.getAddress2());
		setCompany(contactDto.getCompany());
		setCity(contactDto.getCity());
		setCompany(contactDto.getCompany());
		setContactName(contactDto.getContactName());
		setCountry(contactDto.getCountry());
		setEmail(contactDto.getEmail());
		setFax(contactDto.getFax());
		setPhysicalAddress(contactDto.getPhysicalAddress());
		setPostCode(contactDto.getPostCode());
		setTelephoneNumbers(contactDto.getTelephoneNumbers());
		setTelexNo(contactDto.getTelexNo());
		setTerritoryCode(contactDto.getTerritoryCode());
		setWebsite(contactDto.getWebsite());
	}

	public ContactDto toDto() {
		ContactDto contact = new ContactDto();
		contact.setAddress(address);
		contact.setAddress2(address2);
		contact.setCompany(company);
		contact.setCity(city);
		contact.setCompany(company);
		contact.setContactName(contactName);
		contact.setCountry(country);
		contact.setEmail(email);
		contact.setFax(fax);
		contact.setPhysicalAddress(physicalAddress);
		contact.setPostCode(postCode);
		contact.setTelephoneNumbers(telephoneNumbers);
		contact.setTelexNo(telexNo);
		contact.setTerritoryCode(territoryCode);
		contact.setWebsite(website);
		
		return contact;
	}

}
