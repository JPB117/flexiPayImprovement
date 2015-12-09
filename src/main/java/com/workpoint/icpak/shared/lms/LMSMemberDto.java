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
	/**
	 * { "title":38, "firstName":"Ogot", "lastName":"Wilson", "mobileNo":
	 * "041-2221510, 0733-623584", "userName":"ogotcpa@iconnect.co.ke",
	 * "password":"pass1", "DOB":"08-12-2015", "refID":"iypUNLFxbsEFMJLB",
	 * "gender":23, "timeZone":"E. Africa Standard Time", "membershipID":"1" }
	 * 
	 * { "lastName":"Wilson", "title":"38", "DOB":"",
	 * "refID":"iypUNLFxbsEFMJLB", "gender":"23",
	 * "userName":"ogotcpa@iconnect.co.ke", "timeZone":"E. Africa Standard Time"
	 * , "firstName":"Ogot", "password":"pass1", "membershipID":"1", "mobileNo":
	 * "041-2221510, 0733-623584" }
	 * 
	 * 
	 * { "title":"38", "firstName":"Ogot", "lastName":"Wilson",
	 * "mobileNo":"041-2221510, 0733-623584", "userName":"ogotcpa@iconnect.co.ke",
	 * "password":"pass1", "DOB":"", "gender":"23", "timeZone":
	 * "E. Africa StandardTime", "membershipID":"1", "refID":"iypUNLFxbsEFMJLB"
	 * }
	 */
	private String title; // Mr : 38, Miss : 39 , Mrs : 40 Dr : 42
	private String firstName;
	private String lastName;
	private String mobileNo;
	private String userName;
	private String password;
	private String DOB;
	private String gender; // Male : 22, Female : 23
	private String timeZone;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
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

}
