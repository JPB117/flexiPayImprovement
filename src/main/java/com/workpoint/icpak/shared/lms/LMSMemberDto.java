package com.workpoint.icpak.shared.lms;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Target Url -
 * http://e-learning.datamedu.com:8084/mLearning/api/Account/Register
 * 
 * @author duggan
 *
 */

@XmlRootElement
public class LMSMemberDto {
	private int title; // Mr : 38, Miss : 39 , Mrs : 40 Dr : 42
	private String firstName;
	private String lastName;
	private String mobileNo;
	private String userName;
	private String password;
	private String emailID;
	private String DOB;
	private int gender; // Male : 22, Female : 23
	private String timeZone = "";
	private String membershipID;
	private String refID;

	public LMSMemberDto() {

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

	public void setDOB(String dob) {
		this.DOB = dob;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public int getTitle() {
		return title;
	}

	public void setTitle(int title) {
		this.title = title;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getMembershipID() {
		return membershipID;
	}

	public void setMembershipID(String membershipId) {
		this.membershipID = membershipId;
	}

	public String getRefID() {
		return refID;
	}

	public void setRefID(String refId) {
		this.refID = refId;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
}
