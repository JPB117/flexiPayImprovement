package com.icpak.rest.models.membership;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationType;
import com.workpoint.icpak.shared.model.Gender;
import com.workpoint.icpak.shared.model.Title;

@Entity
@Table(name = "`allicpakmembers_5`")
public class MemberImport {
	@Id
	private Short id;
	@Column(name = "`No_`")
	private String memberNo;
	private String Name;
	@Column(name = "`E-mail`")
	private String email;
	private Short status;
	@Column(name = "`Customer Type`")
	private String customerType;
	@Column(name = "`Customer Posting Group`")
	private String customerPostingGroup;
	private String practisingNo;
	@Column(name = "`Gender`")
	private Short gender;
	private Short paidUp;

	private String Address;
	private String Address2;
	private String City;
	private String phoneNo_;
	private String PostCode;
	private String County;
	private Date DateOfBirth;
	private String IDNo;
	private String Position;
	@Column(name = "`Practicing Cert Date`")
	private Date PracticingCertDate;
	@Column(name = "`Alternate Phone No_`")
	private String AlternatePhoneNo_;
	@Column(name = "`Mobile No_`")
	private String MobileNo_;

	/*
	 * Address City Phone No_ Customer Posting Group Post Code County E-Mail
	 * HomePage VAT Bus_ Posting Group Practising No_ Gender Date Of Birth ID No
	 * Date Registered Customer Type Status District Position Practicing Cert
	 * Date Alternate Phone No_ 1 Mobile No_
	 */
	public Short getID() {
		return id;
	}

	public void setID(Short iD) {
		id = iD;
	}

	public String getNO() {
		return memberNo;
	}

	public void setNO(String nO) {
		memberNo = nO;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		email = email;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public String getCustomerType() {
		return customerType;
	}

	public Short getId() {
		return id;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public String getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getAddress2() {
		return Address2;
	}

	public void setAddress2(String address2) {
		Address2 = address2;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public String getPhoneNo_() {
		return phoneNo_;
	}

	public void setPhoneNo_(String phoneNo_) {
		this.phoneNo_ = phoneNo_;
	}

	public String getPostCode() {
		return PostCode;
	}

	public void setPostCode(String postCode) {
		PostCode = postCode;
	}

	public String getCounty() {
		return County;
	}

	public void setCounty(String county) {
		County = county;
	}

	public Date getDateOfBirth() {
		return DateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		DateOfBirth = dateOfBirth;
	}

	public String getIDNo() {
		return IDNo;
	}

	public void setIDNo(String iDNo) {
		IDNo = iDNo;
	}

	public String getPosition() {
		return Position;
	}

	public void setPosition(String position) {
		Position = position;
	}

	public Date getPracticingCertDate() {
		return PracticingCertDate;
	}

	public void setPracticingCertDate(Date practicingCertDate) {
		PracticingCertDate = practicingCertDate;
	}

	public String getAlternatePhoneNo_() {
		return AlternatePhoneNo_;
	}

	public void setAlternatePhoneNo_(String alternatePhoneNo_) {
		AlternatePhoneNo_ = alternatePhoneNo_;
	}

	public String getMobileNo_() {
		return MobileNo_;
	}

	public void setMobileNo_(String mobileNo_) {
		MobileNo_ = mobileNo_;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getCustomerPostingGroup() {
		return customerPostingGroup;
	}

	public void setCustomerPostingGroup(String customerPostingGroup) {
		this.customerPostingGroup = customerPostingGroup;
	}

	public String getPractisingNo() {
		return practisingNo;
	}

	public void setPractisingNo(String practisingNo) {
		this.practisingNo = practisingNo;
	}

	public Short getGender() {
		return gender;
	}

	public void setGender(Short gender) {
		this.gender = gender;
	}

	public Short getPaidUp() {
		return paidUp;
	}

	public void setPaidUp(Short paidUp) {
		this.paidUp = paidUp;
	}

	public String getSno() {
		return sno;
	}

	public void setSno(String sno) {
		this.sno = sno;
	}

	private String sno;

	public ApplicationFormHeaderDto toDTO() {
		ApplicationFormHeaderDto dto = new ApplicationFormHeaderDto();
		String[] allNames = Name.split(" ");
		System.err.println("Length>>" + allNames.length);
		if (allNames.length >= 1) {
			if (allNames[0].equals("CPA")) {
				dto.setTitle(Title.CPA);
			} else if (allNames[0].equals("FCPA")) {
				dto.setTitle(Title.FCPA);
			}
		}
		if (allNames.length >= 2) {
			dto.setSurname(allNames[1]);
		}
		if (allNames.length >= 3) {
			dto.setOtherNames(allNames[2]);
		}
		dto.setMemberNo(memberNo);
		dto.setUserId(memberNo);
		if (email == null || email.isEmpty()) {
			dto.setEmail(memberNo);
		} else {
			dto.setEmail(email);
		}
		if (Address != null) {
			dto.setAddress1(Address);
		}
		if (Address2 != null) {
			dto.setAddress1(Address2);
		}
		if (City != null) {
			dto.setCity1(City);
		}
		if (phoneNo_ != null) {
			dto.setContactTelephone(phoneNo_);
		}
		if (PostCode != null) {
			dto.setAddress1(PostCode);
		}
		if (County != null) {
			dto.setAddress1(County);
		}
		if (DateOfBirth != null) {
			dto.setDob(DateOfBirth);
		}
		if (IDNo != null) {
			dto.setIdNumber(IDNo);
		}
		if (Position != null) {
			dto.setPosition(Position);
		}
		if (PracticingCertDate != null) {
			dto.setPracticingCertDate(PracticingCertDate);
		}
		if (AlternatePhoneNo_ != null) {
			dto.setTelephone1(AlternatePhoneNo_);
		}
		if (MobileNo_ != null) {
			dto.setMobileNumber(MobileNo_);
		}
		if (customerType.equals("MEMBER")) {
			dto.setApplicationType(ApplicationType.NON_PRACTISING);
		} else if (customerType.equals("PRACTICING RT")) {
			dto.setApplicationType(ApplicationType.PRACTISING_RT);
		} else if (customerType.equals("PRAC MEMBER")) {
			dto.setApplicationType(ApplicationType.PRACTISING);
		} else if (customerType.equals("FOREIGN")) {
			dto.setApplicationType(ApplicationType.OVERSEAS);
		} else if (customerType.equals("RETIRED")) {
			dto.setApplicationType(ApplicationType.RETIRED);
		} else {
			dto.setApplicationType(ApplicationType.NON_PRACTISING);
		}
		// Gender
		if (gender.equals(0)) {
			dto.setGender(Gender.MALE);
		} else {
			dto.setGender(Gender.FEMALE);
		}

		return dto;

	}

}
