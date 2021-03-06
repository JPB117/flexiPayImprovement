package com.workpoint.icpak.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.workpoint.icpak.shared.model.auth.AccountStatus;

@XmlRootElement
public class UserDto extends SerializableObj implements Listable, Serializable {

	private static final long serialVersionUID = -5249516544970187459L;
	private String applicationRefId;
	private String name;
	private String userId;
	private String memberRefId;
	private String memberNo;
	private String email;
	private String surname;
	private String password;
	private String fullName;
	private String memberQrCode;
	private MembershipStatus membershipStatus;
	private int hasDisciplinaryCase;
	private ArrayList<RoleDto> groups = new ArrayList<RoleDto>();
	private AccountStatus status = AccountStatus.NEWACC;
	private String phoneNumber;
	private Date lastDateUpdateFromErp;
	private Long userLongId;
	private String gender;

	public UserDto() {
	}

	public UserDto(String id) {
		this.userId = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getFullName() {
		return fullName;
	}

	public String getName() {
		return name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getUserLongId() {
		return userLongId;
	}

	public void setUserLongId(Long userLongId) {
		this.userLongId = userLongId;
	}

	@Override
	public String toString() {
		return "{refId:" + getRefId() + ",userId:" + userId + ",fullNames:"
				+ getFullName() + "}";
	}

	public List<RoleDto> getGroups() {
		return groups;
	}

	public void setGroups(List<RoleDto> groups) {
		this.groups.addAll(groups);
	}

	@JsonIgnore
	public String getGroupsAsString() {
		StringBuffer out = new StringBuffer();
		if (groups != null) {
			for (RoleDto group : groups) {
				out.append(group.getName() + ",");
			}
		}

		if (out.length() > 0) {
			return out.substring(0, out.length() - 1);
		}

		return "";
	}

	public boolean hasGroup(String groupName) {
		if (groups != null)
			for (RoleDto group : groups) {
				if (group.getName().equalsIgnoreCase(groupName)) {
					return true;
				}
			}
		return false;
	}

	@JsonIgnore
	public boolean isAdmin() {
		return hasGroup("admin");
	}

	@JsonIgnore
	public boolean isEventEdit() {
		return hasGroup("event_edit");
	}

	@JsonIgnore
	public boolean isEventRead() {
		return hasGroup("Event_read");
	}

	@JsonIgnore
	public boolean isUsersEdit() {
		return hasGroup("users_edit");
	}

	public int getHasDisciplinaryCase() {
		return hasDisciplinaryCase;
	}

	public void setHasDisciplinaryCase(int hasDisciplinaryCase) {
		this.hasDisciplinaryCase = hasDisciplinaryCase;
	}

	@JsonIgnore
	public boolean isUsersRead() {
		return hasGroup("users_read");
	}

	@JsonIgnore
	public boolean isApplicationsEdit() {
		return hasGroup("applications_edit");
	}

	@JsonIgnore
	public boolean isApplicationsRead() {
		return hasGroup("applications_read");
	}

	@JsonIgnore
	public boolean isCPDEdit() {
		return hasGroup("cpd_edit");
	}

	@JsonIgnore
	public boolean isCPDRead() {
		return hasGroup("cpd_read");
	}

	@JsonIgnore
	public boolean isFinanceEdit() {
		return hasGroup("finance_edit");
	}

	@JsonIgnore
	public boolean isFinanceEditApplications() {
		return hasGroup("finance_edit_applications");
	}

	@JsonIgnore
	public boolean isFinanceEditEvents() {
		return hasGroup("finance_edit_events");
	}

	@JsonIgnore
	public boolean isFinanceRead() {
		return hasGroup("finance_read");
	}

	@JsonIgnore
	public boolean isBasicMember() {
		return hasGroup("basic_member");
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof UserDto)) {
			return false;
		}

		UserDto other = (UserDto) obj;

		if (name == null) {
			return false;
		}

		return name.equals(other.name);
	}

	@JsonIgnore
	public String getDisplayName() {
		return getFullName();
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public String getApplicationRefId() {
		return applicationRefId;
	}

	public void setApplicationRefId(String applicationRefId) {
		this.applicationRefId = applicationRefId;
	}

	public String getMemberRefId() {
		return memberRefId;
	}

	public void setMemberRefId(String memberId) {
		this.memberRefId = memberId;
	}

	public String getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setLastDateUpdateFromErp(Date lastDateUpdateFromErp) {
		this.lastDateUpdateFromErp = lastDateUpdateFromErp;
	}

	public Date getLastDateUpdateFromErp() {
		return lastDateUpdateFromErp;
	}

	public MembershipStatus getMembershipStatus() {
		return membershipStatus;
	}

	public void setMembershipStatus(MembershipStatus membershipStatus) {
		this.membershipStatus = membershipStatus;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMemberQrCode() {
		return memberQrCode;
	}

	public void setMemberQrCode(String memberQrCode) {
		this.memberQrCode = memberQrCode;
	}
	
}
