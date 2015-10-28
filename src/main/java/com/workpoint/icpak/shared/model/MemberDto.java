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
	private String practisingNo;
	private String customerType;
	private Date practisingCertDate;


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
		return (lastName == null ? "" : lastName) + " "
				+ (firstName == null ? "" : firstName);
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

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public Date getPractisingCertDate() {
		return practisingCertDate;
	}

	public void setPractisingCertDate(Date practisingCertDate) {
		this.practisingCertDate = practisingCertDate;
	}


}
