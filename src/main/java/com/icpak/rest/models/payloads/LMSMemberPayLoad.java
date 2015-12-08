package com.icpak.rest.models.payloads;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.icpak.rest.models.base.PO;
import com.workpoint.icpak.shared.lms.LMSMemberDto;

@Entity
@Table(name = "lmsMemberPayLoad")
public class LMSMemberPayLoad extends PO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String title;
	private String firstName;
	private String lastName;
	private String mobileNo;
	private String userName;
	private String password;
	private String DOB;
	private String gender;
	private String timeZone;
	private String membershipID;
	private String meberRefID;
	private String Status;

	public LMSMemberPayLoad() {

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDOB() {
		return DOB;
	}

	public void setDOB(String dOB) {
		DOB = dOB;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getMembershipID() {
		return membershipID;
	}

	public void setMembershipID(String membershipID) {
		this.membershipID = membershipID;
	}

	public String getMeberRefID() {
		return meberRefID;
	}

	public void setMeberRefID(String meberRefID) {
		this.meberRefID = meberRefID;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public void copyFromDto(LMSMemberDto dto, String status) {
		setTitle(dto.getTitle());
		setFirstName(dto.getFirstName());
		setLastName(dto.getLastName());
		setMobileNo(dto.getMobileNo());
		setUserName(dto.getUserName());
		setPassword(dto.getPassword());
		setDOB(dto.getDOB());
		setGender(dto.getGender());
		setTimeZone(dto.getTimeZone());
		setMembershipID(dto.getMembershipID());
		setMeberRefID(dto.getRefID());
		setStatus(status);
	}

	public LMSMemberDto toDto() {
		LMSMemberDto dto = new LMSMemberDto();

		dto.setTitle(title);
		dto.setFirstName(firstName);
		dto.setLastName(lastName);
		dto.setMobileNo(mobileNo);
		dto.setUserName(userName);
		dto.setPassword(password);
		dto.setDOB(DOB);
		dto.setGender(gender);
		dto.setTimeZone(timeZone);
		dto.setMembershipID(membershipID);
		dto.setRefID(meberRefID);

		return dto;
	}

}
