package com.icpak.rest.models.membership;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationType;
import com.workpoint.icpak.shared.model.Gender;

@Entity
@Table(name = "`allicpakmembers_5`")
public class MemberImport {

	@Id
	private Short ID;
	private String NO;
	private String Name;
	@Column(name = "`E-mail`")
	private String Email;
	private Short status;
	@Column(name = "`Customer Type`")
	private String customerType;
	@Column(name = "`Customer Posting Group`")
	private String customerPostingGroup;
	private String practisingNo;
	private Short gender;
	private Short paidUp;

	public Short getID() {
		return ID;
	}

	public void setID(Short iD) {
		ID = iD;
	}

	public String getNO() {
		return NO;
	}

	public void setNO(String nO) {
		NO = nO;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
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

		if (allNames.length > 1) {
			dto.setSurname(allNames[1]);
		}
		if (allNames.length > 2) {
			dto.setOtherNames(allNames[2]);
		}
		dto.setEmail(Email);

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
		}

		// Gender
		if (gender.equals(1)) {
			dto.setGender(Gender.MALE);
		} else {
			dto.setGender(Gender.FEMALE);
		}

		return dto;

	}

}
