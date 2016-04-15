package com.workpoint.icpak.shared.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MemberDto extends SerializableObj implements Listable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userId;
	private String memberNo;
	private String email;
	private String lastName;
	private String firstName;
	private String title;
	private String address;
	private String city;
	private String practisingNo;
	private String phoneNumber;
	private ApplicationType customerType;
	private String customerPostingGroup;
	private Date practisingCertDate;
	private MembershipStatus membershipStatus;
	private String fullName;
	private String member;
	private String memberQrCode;

	public MemberDto() {
	}

	public MemberDto(String memberNo) {
		this.memberNo = memberNo;
	}

	public MemberDto(String memberRefId, String memberNo) {
		setRefId(memberRefId);
		this.memberNo = memberNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(String memberId) {
		this.memberNo = memberId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@JsonIgnore
	public String getDisplayName() {
		return (memberNo == null ? "" : memberNo) + " - " + getName();
	}

	@JsonIgnore
	public String getName() {
		return fullName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MemberDto)) {
			return false;
		}

		MemberDto other = (MemberDto) obj;
		if (other.getRefId() == null || getRefId() == null) {
			return false;
		}

		return other.getRefId().equals(getRefId());
	}

	public String getPractisingNo() {
		return practisingNo;
	}

	public void setPractisingNo(String practisingNo) {
		this.practisingNo = practisingNo;
	}

	public Date getPractisingCertDate() {
		return practisingCertDate;
	}

	public void setPractisingCertDate(Date practisingCertDate) {
		this.practisingCertDate = practisingCertDate;
	}

	public MembershipStatus getMembershipStatus() {
		return membershipStatus;
	}

	public void setMembershipStatus(MembershipStatus membershipStatus) {
		this.membershipStatus = membershipStatus;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
	}

	public ApplicationType getCustomerType() {
		return customerType;
	}

	public void setCustomerType(ApplicationType customerType) {
		this.customerType = customerType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCustomerPostingGroup() {
		return customerPostingGroup;
	}

	public void setCustomerPostingGroup(String customerPostingGroup) {
		this.customerPostingGroup = customerPostingGroup;
	}

	public String getMemberQrCode() {
		return memberQrCode;
	}

	public void setMemberQrCode(String memberQrCode) {
		this.memberQrCode = memberQrCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
