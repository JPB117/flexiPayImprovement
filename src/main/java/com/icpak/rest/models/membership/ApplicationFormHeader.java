package com.icpak.rest.models.membership;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.icpak.rest.models.base.PO;
import com.icpak.rest.models.util.Attachment;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationType;
import com.workpoint.icpak.shared.model.AttachmentDto;
import com.workpoint.icpak.shared.model.auth.ApplicationStatus;

@Entity
@Table(name = "`Application Form Header`")
public class ApplicationFormHeader extends PO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "`Date`", columnDefinition = "datetime")
	private Date date;

	@Column(name = "`Application Date`", columnDefinition = "datetime")
	private Date applicationDate;

	@Column(name = "`Surname`", length = 30)
	private String surname;

	@Column(name = "`Other Names`", length = 30)
	private String otherNames;

	@Column(name = "`Date Of Birth`", columnDefinition = "datetime")
	private Date dob;

	@Column(name = "`Country of Origin`", columnDefinition = "varchar(20) ")
	private String country;

	@Column(name = "`Address for Correspondence1`", columnDefinition = "varchar(100) ")
	private String address1;

	@Column(name = "`Telephone No_ 1`", columnDefinition = "varchar(50) ")
	private String telephone1;

	@Column(name = "`Application Type`", columnDefinition = "varchar(20) ")
	@Enumerated(EnumType.STRING)
	private ApplicationType applicationType;

	@Column(name = "`Status`")
	private int status;

	@Column(name = "`ID Number`", length = 20)
	private String idNumber;

	@Column(name = "`E-mail`", length = 120)
	private String email;

	@Column(name = "`Post Code`", length = 10)
	private String postCode;

	@Column(name = "`City1`", length = 30)
	private String city1;

	@OneToMany(mappedBy = "application", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE })
	private Set<Attachment> attachments = new HashSet<>();

	private String employer;

	// @Basic(fetch = FetchType.LAZY)
	// @Embedded
	// private ApplicationFormHeaderDetail formDetails;

	@Column(name = "`Nationality`", columnDefinition = "varchar(20) ")
	private String nationality;

	@Enumerated(EnumType.STRING)
	private ApplicationStatus applicationStatus;

	private String memberNo;
	private String MobileNo;
	@Column(name = "`Employer Code`", length = 20)
	private String employerCode;
	@Column(name = "`Contact Telephone`", length = 40)
	private String contactTelephone;
	@Column(name = "`Contact Address`", length = 60)
	private String contactAddress;

	private String invoiceRef;
	private String userRefId;
	private String residence;

	public ApplicationFormHeader() {
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getOtherNames() {
		return otherNames;
	}

	public void setOtherNames(String otherNames) {
		this.otherNames = otherNames;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getTelephone1() {
		return telephone1;
	}

	public void setTelephone1(String telephone1) {
		this.telephone1 = telephone1;
	}

	public ApplicationType getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(ApplicationType applicationType) {
		this.applicationType = applicationType;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getCity1() {
		return city1;
	}

	public void setCity1(String city1) {
		this.city1 = city1;
	}

	// public ApplicationCategory getCategory() {
	// return category;
	// }
	//
	// public void setCategory(ApplicationCategory category) {
	// this.category = category;
	// }

	public String getEmployer() {
		return employer;
	}

	public void setEmployer(String employer) {
		this.employer = employer;
	}

	public ApplicationFormHeaderDto toDto() {
		ApplicationFormHeaderDto dto = new ApplicationFormHeaderDto();
		copyInto(dto);
		return dto;
	}

	public void copyFrom(ApplicationFormHeaderDto dto) {
		setSurname(dto.getSurname());
		setOtherNames(dto.getOtherNames());
		setEmail(dto.getEmail());
		setApplicationType(dto.getApplicationType());
		setAddress1(dto.getAddress1());
		setTelephone1(dto.getTelephone1());
		setCity1(dto.getCity1());
		setPostCode(dto.getPostCode());
		setEmployer(dto.getEmployer());
		setDob(dto.getDob());
		setCountry(dto.getCountry());
		setResidence(dto.getResidence());
		setIdNumber(dto.getIdNumber());
	}

	public void copyInto(ApplicationFormHeaderDto dto) {
		dto.setRefId(getRefId());
		dto.setCreated(getCreated());
		dto.setApplicationDate(applicationDate);
		dto.setDate(date);
		dto.setSurname(surname);
		dto.setOtherNames(otherNames);
		dto.setEmail(email);
		dto.setUserRefId(userRefId);
		dto.setEmployer(employer);
		dto.setCity1(city1);
		dto.setAddress1(address1);
		dto.setTelephone1(telephone1);
		dto.setPostCode(postCode);
		dto.setInvoiceRef(invoiceRef);
		dto.setApplicationType(applicationType);
		dto.setDob(dob);
		dto.setCountry(country);
		dto.setResidence(residence);
		dto.setIdNumber(idNumber);

		List<AttachmentDto> attachmentDtos = new ArrayList<AttachmentDto>();
		for (Attachment attachment : attachments) {
			AttachmentDto attachmentDto = new AttachmentDto();
			attachmentDto.setAttachmentName(attachment.getName());
			attachmentDto.setRefId(attachment.getRefId());
			attachmentDtos.add(attachmentDto);
		}

		dto.setAttachments(attachmentDtos);
	}

	public String getInvoiceRef() {
		return invoiceRef;
	}

	public void setInvoiceRef(String invoiceRef) {
		this.invoiceRef = invoiceRef;
	}

	public String getUserRefId() {
		return userRefId;
	}

	public void setUserRefId(String userRefId) {
		this.userRefId = userRefId;
	}

	public Date getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(Date applicationDate) {
		this.applicationDate = applicationDate;
	}

	public String getResidence() {
		return residence;
	}

	public void setResidence(String residence) {
		this.residence = residence;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public ApplicationStatus getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(ApplicationStatus applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

	public String getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}

	public String getMobileNo() {
		return MobileNo;
	}

	public void setMobileNo(String mobileNo) {
		MobileNo = mobileNo;
	}

	public String getEmployerCode() {
		return employerCode;
	}

	public void setEmployerCode(String employerCode) {
		this.employerCode = employerCode;
	}

	public String getContactTelephone() {
		return contactTelephone;
	}

	public void setContactTelephone(String contactTelephone) {
		this.contactTelephone = contactTelephone;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	public Set<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
	}
}
