package com.icpak.rest.models.membership;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.icpak.rest.models.auth.Gender;
import com.icpak.rest.models.base.PO;
import com.wordnik.swagger.annotations.ApiModel;

@ApiModel(description="Application Forms Filled by a member")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(Category.class)

@Entity
@Table(name="`Application Form Header`")
public class ApplicationFormHeader extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="`timestamp`",columnDefinition="timestamp  default current_timestamp")
	private Timestamp timestamp;

	@Column(name="`Application No_`")
	private String applicationNo;
	
	@Column(name="`Date`", columnDefinition="datetime")
	private Date date;
	
	@Column(name="`Application Date`", columnDefinition="datetime")
	private String applicationDate;
	
	@Column(name="`Surname`", length=30)
	private String surname;
	
	@Column(name="`Other Names`", length=30)
	private String otherNames;
	
	@Column(name="`Date Of Birth`", columnDefinition="datetime")
	private Date dob;
	
	@Column(name="`Gender`")
	@Enumerated(EnumType.ORDINAL)
	private Gender gender;
	
	@Column(name="`Marital Status`")
	private int maritalStatus;
	
	@Column(name="`Nationality`", columnDefinition="varchar(20) ")
	private String nationality;
	
	@Column(name="`Country of Origin`", columnDefinition="varchar(20) ")
	private String country;
	
	@Column(name="`Address for Correspondence1`", columnDefinition="varchar(100) ")
	private String address1;
	
	@Column(name="`Address for Correspondence2`", columnDefinition="varchar(100) ")
	private String address2;

	@Column(name="`Address for Correspondence3`", columnDefinition="varchar(100) ")
	private String address3;
			
	@Column(name="`Telephone No_ 1`", columnDefinition="varchar(50) ")
	private String telephone1;
	
	@Column(name="`Telephone No_ 2`", columnDefinition="varchar(50) ")
	private String telephone2;

	@Column(name="`Application Type`", columnDefinition="varchar(20) ")
	@Enumerated(EnumType.STRING)
	private ApplicationType applicationType;
	
	@Column(name="`Receipt No_`", columnDefinition="varchar(20)")
	private String receiptNo;
	
	@Column(name="`Date of Receipt`", columnDefinition="datetime")
	private Date receiptDate;
	
	@Column(name="`User ID`", columnDefinition="varchar(20)")
	private String userId;
	
	@Column(name="`Region`", columnDefinition="varchar(20)")
	private String region;
	
	@Column(name="`No_ Series`", columnDefinition="varchar(20)")
	private String series;
	
	@Column(name="`HOD User ID`", columnDefinition="varchar(20)")
	private String hodUserId;
	
	@Column(name="`HOD Date`", columnDefinition="datetime")
	private Date hodDate;
	
	@Column(name="`HOD Time`", columnDefinition="datetime")
	private Date hodTime;
	
	@Column(name="`HOD Recommendations`", columnDefinition="varchar(200)")
	private String hodRecommendations;
	
	@Column(name="`Dean User ID`", columnDefinition="varchar(20)")
	private String deanUserId;
	
	@Column(name="`Dean Date`", columnDefinition="datetime")
	private Date deanDate;
	
	@Column(name="`Dean Time`", columnDefinition="datetime")
	private Date deanTime;
	
	@Column(name="`Dean Recommendations`", length=200)
	private String deanRecommendations;
	
	@Column(name="`Status`")
	private int status;
	
	@Column(name="`Select`", columnDefinition="tinyint")
	private int select;
	
	@Column(name="`Batch No_`", length=20)
	private String batchNo;
	
	@Column(name="`Batch Date`", columnDefinition="datetime")
	private Date batchDate;
	
	@Column(name="`Batch Time`", columnDefinition="datetime")
	private Date batchTime;
	
	@Column(name="`Admission Board Recommendation`", length=200)
	private String boardRecommendation;
	
	@Column(name="`Admission Board Date`", columnDefinition="datetime")
	private Date boardDate;
	
	@Column(name="`Admission Board Time`", columnDefinition="datetime")
	private Date boardTime;
	
	@Column(name="`Date Of Meeting`", columnDefinition="datetime")
	private Date meetingDate;
	
	@Column(name="`Date Of Receipt Slip`", columnDefinition="datetime")
	private Date receiptSlipDate;
	
	@Column(name="`Transaction No_`", length=20)
	private String transactionNo;
	
	@Column(name="`Academic Year`", length=20)
	private String academicYear;
	
	@Column(name="`Intake Code`", length=20)
	private String intakeCode;
	
	@Column(name="`Settlement Type`", length=20)
	private String settlementType;
	
	@Column(name="`ID Number`", length=8)
	private String idNumber;
	
	@Column(name="`Date Sent for Approval`", columnDefinition="datetime")
	private Date dateSentForApproval;
	
	@Column(name="`Issued Date`", columnDefinition="datetime")
	private String issuedDate;
	
	@Column(name="`Campus`", length=50)
	private String campus;
	
	@Column(name="`E-mail`", length=120)
	private String email;
	
	@Column(name="`Honorific`")
	private int honorific;
	
	@Column(name="`Conviction`", columnDefinition="tinyint  default 0")
	private int conviction;
	
	@Column(name="`Offence`", length=250)
	private String offence;
	
	@Column(name="`Date and Place`", length=250)
	private String convictionDateAndPlace;
	
	@Column(name="`Sentence`", length=250)
	private String sentence;
	
	@Column(name="`Contact Name`", length=50)
	private String contactName;
	
	@Column(name="`Contact Address`", length=60)
	private String contactAddress;
	
	@Column(name="`Contact Telephone`", length=40)
	private String contactTelephone;
	
	@Column(name="`Contact Email`", length=150)
	private String contactEmail;
	
	@Column(name="`KASNEB_FAQ No_`", length=50)
	private String KASNEBFAQNo;
	
	@Column(name="`CPA_F_A_Q_`")
	private String CPAFAQ;
	
	@Column(name="`Examination Body`", length=20)
	private String examinationBody;
	
	@Column(name="`Payment Mode`")
	private int paymentMode;
	
	@Column(name="`Post Code`", length=10)
	private String postCode;
	
	@Column(name="`Foreign _ Local Applicant`", nullable=false)
	private int applicantType;
	
	@Column(name="`Responsibility Center`", length=20)
	private String responsibilityCenter;
	
	@Column(name="`Document Type`", nullable=false)
	private int documentType;
	
	@Column(name="`Customer Type`", length=20)
	private String customerType;
	
	@Column(name="`Fax No_`", length=30)
	private String faxNo;
	
	@Column(name="`Age`", columnDefinition="datetime")
	private Date age;
	
	@Column(name="`City1`", length=30)
	private String city1;
	
	@Column(name="`Designation`", length=70)
	private String designation;
	
	@Column(name="`Position`", length=70)
	private String position;
	
	@Column(name="`Employer Code`", length=20)
	private String employerCode;
	
	private String employer;
	
	@Column(name="`Qualification`", length=100)
	private String qualification;
	
	@Column(name="`Contact person`", length=50)
	private String contactPerson;
	
	@Column(name="`District`", length=20)
	private String district;
	
	@Column(name="`Gen_ Bus_ Posting Group`", length=10)
	private String genBusPostingGroup;
	
	@Column(name="`VAT Bus_ Posting Group`", length=10)
	private String vatBusPostingGroup;
	
	@Column(name="`Application Method`", length=10)
	private int applicationMethod;
	
	@Transient
	private Category category;
	
	public ApplicationFormHeader() {
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(String applicationDate) {
		this.applicationDate = applicationDate;
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

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public int getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(int maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		nationality = nationality;
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

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getTelephone1() {
		return telephone1;
	}

	public void setTelephone1(String telephone1) {
		this.telephone1 = telephone1;
	}

	public String getTelephone2() {
		return telephone2;
	}

	public void setTelephone2(String telephone2) {
		this.telephone2 = telephone2;
	}

	public ApplicationType getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(ApplicationType applicationType) {
		this.applicationType = applicationType;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public Date getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getHodUserId() {
		return hodUserId;
	}

	public void setHodUserId(String hodUserId) {
		this.hodUserId = hodUserId;
	}

	public Date getHodDate() {
		return hodDate;
	}

	public void setHodDate(Date hodDate) {
		this.hodDate = hodDate;
	}

	public Date getHodTime() {
		return hodTime;
	}

	public void setHodTime(Date hodTime) {
		this.hodTime = hodTime;
	}

	public String getHodRecommendations() {
		return hodRecommendations;
	}

	public void setHodRecommendations(String hodRecommendations) {
		this.hodRecommendations = hodRecommendations;
	}

	public String getDeanUserId() {
		return deanUserId;
	}

	public void setDeanUserId(String deanUserId) {
		this.deanUserId = deanUserId;
	}

	public Date getDeanDate() {
		return deanDate;
	}

	public void setDeanDate(Date deanDate) {
		this.deanDate = deanDate;
	}

	public Date getDeanTime() {
		return deanTime;
	}

	public void setDeanTime(Date deanTime) {
		this.deanTime = deanTime;
	}

	public String getDeanRecommendations() {
		return deanRecommendations;
	}

	public void setDeanRecommendations(String deanRecommendations) {
		this.deanRecommendations = deanRecommendations;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getSelect() {
		return select;
	}

	public void setSelect(int select) {
		this.select = select;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getBatchDate() {
		return batchDate;
	}

	public void setBatchDate(Date batchDate) {
		this.batchDate = batchDate;
	}

	public Date getBatchTime() {
		return batchTime;
	}

	public void setBatchTime(Date batchTime) {
		this.batchTime = batchTime;
	}

	public String getBoardRecommendation() {
		return boardRecommendation;
	}

	public void setBoardRecommendation(String boardRecommendation) {
		this.boardRecommendation = boardRecommendation;
	}

	public Date getBoardDate() {
		return boardDate;
	}

	public void setBoardDate(Date boardDate) {
		this.boardDate = boardDate;
	}

	public Date getBoardTime() {
		return boardTime;
	}

	public void setBoardTime(Date boardTime) {
		this.boardTime = boardTime;
	}

	public Date getMeetingDate() {
		return meetingDate;
	}

	public void setMeetingDate(Date meetingDate) {
		this.meetingDate = meetingDate;
	}

	public Date getReceiptSlipDate() {
		return receiptSlipDate;
	}

	public void setReceiptSlipDate(Date receiptSlipDate) {
		this.receiptSlipDate = receiptSlipDate;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public String getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}

	public String getIntakeCode() {
		return intakeCode;
	}

	public void setIntakeCode(String intakeCode) {
		this.intakeCode = intakeCode;
	}

	public String getSettlementType() {
		return settlementType;
	}

	public void setSettlementType(String settlementType) {
		this.settlementType = settlementType;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public Date getDateSentForApproval() {
		return dateSentForApproval;
	}

	public void setDateSentForApproval(Date dateSentForApproval) {
		this.dateSentForApproval = dateSentForApproval;
	}

	public String getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(String issuedDate) {
		this.issuedDate = issuedDate;
	}

	public String getCampus() {
		return campus;
	}

	public void setCampus(String campus) {
		this.campus = campus;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getHonorific() {
		return honorific;
	}

	public void setHonorific(int honorific) {
		this.honorific = honorific;
	}

	public int getConviction() {
		return conviction;
	}

	public void setConviction(int conviction) {
		this.conviction = conviction;
	}

	public String getOffence() {
		return offence;
	}

	public void setOffence(String offence) {
		this.offence = offence;
	}

	public String getConvictionDateAndPlace() {
		return convictionDateAndPlace;
	}

	public void setConvictionDateAndPlace(String convictionDateAndPlace) {
		this.convictionDateAndPlace = convictionDateAndPlace;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	public String getContactTelephone() {
		return contactTelephone;
	}

	public void setContactTelephone(String contactTelephone) {
		this.contactTelephone = contactTelephone;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getKASNEBFAQNo() {
		return KASNEBFAQNo;
	}

	public void setKASNEBFAQNo(String kASNEBFAQNo) {
		KASNEBFAQNo = kASNEBFAQNo;
	}

	public String getCPAFAQ() {
		return CPAFAQ;
	}

	public void setCPAFAQ(String cPAFAQ) {
		CPAFAQ = cPAFAQ;
	}

	public String getExaminationBody() {
		return examinationBody;
	}

	public void setExaminationBody(String examinationBody) {
		this.examinationBody = examinationBody;
	}

	public int getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(int paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public int getApplicantType() {
		return applicantType;
	}

	public void setApplicantType(int applicantType) {
		this.applicantType = applicantType;
	}

	public String getResponsibilityCenter() {
		return responsibilityCenter;
	}

	public void setResponsibilityCenter(String responsibilityCenter) {
		this.responsibilityCenter = responsibilityCenter;
	}

	public int getDocumentType() {
		return documentType;
	}

	public void setDocumentType(int documentType) {
		this.documentType = documentType;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getFaxNo() {
		return faxNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	public Date getAge() {
		return age;
	}

	public void setAge(Date age) {
		this.age = age;
	}

	public String getCity1() {
		return city1;
	}

	public void setCity1(String city1) {
		this.city1 = city1;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getEmployerCode() {
		return employerCode;
	}

	public void setEmployerCode(String employerCode) {
		this.employerCode = employerCode;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getGenBusPostingGroup() {
		return genBusPostingGroup;
	}

	public void setGenBusPostingGroup(String genBusPostingGroup) {
		this.genBusPostingGroup = genBusPostingGroup;
	}

	public String getVatBusPostingGroup() {
		return vatBusPostingGroup;
	}

	public void setVatBusPostingGroup(String vatBusPostingGroup) {
		this.vatBusPostingGroup = vatBusPostingGroup;
	}

	public int getApplicationMethod() {
		return applicationMethod;
	}

	public void setApplicationMethod(int applicationMethod) {
		this.applicationMethod = applicationMethod;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getEmployer() {
		return employer;
	}

	public void setEmployer(String employer) {
		this.employer = employer;
	}
	
}
