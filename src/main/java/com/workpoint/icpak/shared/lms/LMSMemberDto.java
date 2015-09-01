package com.workpoint.icpak.shared.lms;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * Target Url - http://e-learning.datamedu.com:8084/mLearning/api/Account/Register
 * 
 * @author duggan
 *
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class LMSMemberDto {

	private int title; //Mr : 38, Miss : 39 , Mrs : 40 Dr : 42

	private String firstName;
	private String lastName;
	private String mobileNo;
	private String userName;
	private String password;
	private Date dob;
	private int gender; //Male : 22, Female : 23
	private String timeZone = "";
	
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

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
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
}
