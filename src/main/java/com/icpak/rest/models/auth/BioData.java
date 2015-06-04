package com.icpak.rest.models.auth;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.icpak.rest.models.membership.BloodGroup;
import com.icpak.rest.models.membership.MaritalStatus;
import com.icpak.rest.models.membership.Member;
import com.icpak.rest.models.membership.StudentType;
import com.wordnik.swagger.annotations.ApiModel;


@ApiModel(value="User Data Model", 
description="A UserData model represents data for any person")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

@Embeddable
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class BioData{
	
	@Column(length=255)
    private String firstName;
	
    @Column(length=255)
	private String lastName;
    
    @Column(name="`Name`", columnDefinition="varchar(50)")// - Full Names
    private String fullNames;
    
	@Column(name="`Search Name`", columnDefinition="varchar(50)")// - Full Names Caps
	private String searchName;
	
	@Column(name="`Name 2`", columnDefinition="varchar(40)") //- Empty
	private String otherNames;
	
	@Column(name="`Picture`", columnDefinition="longblob NULL")
	private byte[] photo;
	
    @Column(name="`Gender`", columnDefinition="int")
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @Enumerated(EnumType.ORDINAL)
    private AgeGroup ageGroup;
    
    @Column(name="`Date Of Birth`",columnDefinition="datetime")
    private Date dob;
    
    @Column(name="`Marital Status`",columnDefinition="int")
    private MaritalStatus maritalStatus;

    @Column(name="`Blood Group`",columnDefinition="varchar(30)")
    private BloodGroup bloodGroup;

    @Column(name="`Weight`", columnDefinition="decimal(38, 20)")
	private double weight;
	
	@Column(name="`Height`", columnDefinition="decimal(38, 20)")
	private double height;
	
	@Column(name="`Religion`", columnDefinition="varchar(50)")
	private String religion;
	
	@Column(name="`Citizenship`", columnDefinition="varchar(30)")
	private String nationality;

	@Column(name="`Payments By`", columnDefinition="varchar(20)")
	private String paymentsBy;
	
	@Column(name="`Student Type`",columnDefinition="varchar(20)")
	@Enumerated(EnumType.STRING)
	private StudentType type;
	
	@Column(name="`ID No`", columnDefinition="varchar(30)")
	private String idNo;
	
	@Column(name="`Birth Cert`", columnDefinition="varchar(30)")
	private String birthCert;
	
	 @ElementCollection(fetch=FetchType.LAZY)
    @CollectionTable(name="user_salutation", 
    	joinColumns=@JoinColumn(name="userid",referencedColumnName="id", nullable=false))
    @Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
    @Column(name="salutation")
    private Set<String> salutation = new HashSet<>(); //Mr, Miss, Dr etc
    
    private String title;// Not sure - will see
	
    @XmlTransient
    @OneToOne
    @JoinColumn(name="userid")
    private User user;
    
    @XmlTransient
    @OneToOne
    @JoinColumn(name="memberid")
    private Member member;
    private Long delegateId; //Event delegate id
    
    private String residence;
    private boolean isOverseas;
    private String county;

    public BioData() {
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public Set<String> getSalutation() {
		return salutation;
	}


	public void setSalutation(Set<String> salutation) {
		this.salutation = salutation;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public Gender getGender() {
		return gender;
	}


	public void setGender(Gender gender) {
		this.gender = gender;
	}


	public AgeGroup getAgeGroup() {
		return ageGroup;
	}


	public void setAgeGroup(AgeGroup ageGroup) {
		this.ageGroup = ageGroup;
	}


	public Date getDob() {
		return dob;
	}


	public void setDob(Date dob) {
		this.dob = dob;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public Member getMember() {
		return member;
	}


	public void setMember(Member member) {
		this.member = member;
	}


	public Long getDelegateId() {
		return delegateId;
	}


	public void setDelegateId(Long delegateId) {
		this.delegateId = delegateId;
	}


	public String getNationality() {
		return nationality;
	}


	public void setNationality(String nationality) {
		this.nationality = nationality;
	}


	public String getResidence() {
		return residence;
	}


	public void setResidence(String residence) {
		this.residence = residence;
	}


	public boolean isOverseas() {
		return isOverseas;
	}


	public void setOverseas(boolean isOverseas) {
		this.isOverseas = isOverseas;
	}


	public String getCounty() {
		return county;
	}


	public void setCounty(String county) {
		this.county = county;
	}

	public BioData clone(String ... opts){
		BioData data = new BioData();
		data.copy(this);
		
		return data;
	}

	public void copy(BioData userData) {
		setAgeGroup(userData.ageGroup);
		setCounty(userData.county);
		setDelegateId(userData.delegateId);
		setDob(userData.dob);
		setFirstName(userData.firstName);
		setGender(userData.gender);
		setLastName(userData.lastName);
		setNationality(userData.nationality);
		setOverseas(userData.isOverseas);
		setResidence(userData.residence);
		setSalutation(userData.salutation);
		setTitle(userData.title);
	}


	public String getFullNames() {
		
		return lastName+" "+firstName;
	}
	
}
