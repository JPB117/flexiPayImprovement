package com.workpoint.icpak.shared.model.events;

import com.workpoint.icpak.shared.model.SerializableObj;

public class DelegateDto extends SerializableObj{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String memberRegistrationNo;
	private String title;
	private String surname;
	private String otherNames;
	private String email;
	private String hotel;
	
	public DelegateDto() {
		
	}

	public String getMemberRegistrationNo() {
		return memberRegistrationNo;
	}

	public void setMemberRegistrationNo(String memberRegistrationNo) {
		this.memberRegistrationNo = memberRegistrationNo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
