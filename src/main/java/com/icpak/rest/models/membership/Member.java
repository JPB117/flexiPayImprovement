package com.icpak.rest.models.membership;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.base.PO;
import com.wordnik.swagger.annotations.ApiModel;
import com.workpoint.icpak.shared.model.MemberDto;
import com.workpoint.icpak.shared.model.MembershipStatus;

@ApiModel(value = "Member Model", description = "ICPAK Member Model")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table
public class Member extends PO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userRefId;
	private String memberNo;
	@Column(nullable = false)
	private Date registrationDate;
	private Date lastUpdate;
	private String date;
	private String practisingNo;
	private String customerType;
	private Date practisingCertDate;
	private String setPracisingNo;
	private String customerPostingGroup;
	private String memberQrCode;
	private Double memberBalance;
	private boolean isInGoodStanding;

	@Column(nullable = false, columnDefinition = "int(1) not null default 0")
	private int memberDisplinaryCase = 0;

	/**
	 * TODO - Link this to the membership approval process<br/>
	 * - Also Link this to the membership annual renewal process.<br/>
	 * - Check also whether Admin can deactivate account for whatever reason <br/>
	 */
	@Enumerated(EnumType.STRING)
	private MembershipStatus memberShipStatus;

	@OneToMany(mappedBy = "member")
	private Set<GoodStandingCertificate> goodStandingCerts = new HashSet<>();

	@OneToOne
	@JoinColumn(name = "userId")
	private User user;

	public Member() {
	}

	public Member(String userId) {
		this.userRefId = userId;
	}

	public String getUserRefId() {
		return userRefId;
	}

	public void setUserRefId(String userRefId) {
		this.userRefId = userRefId;
	}

	public MemberDto toDto() {
		MemberDto dto = new MemberDto();
		dto.setRefId(getRefId());
		dto.setUserId(userRefId);
		dto.setMemberNo(memberNo);
		dto.setFullName(user.getFullName());
		return dto;
	}

	public void copyFrom(MemberDto memberDto) {
		setMemberNo(memberNo);
	}

	public String getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public MembershipStatus getMemberShipStatus() {
		return memberShipStatus;
	}

	public void setMemberShipStatus(MembershipStatus memberShipStatus) {
		this.memberShipStatus = memberShipStatus;
	}

	public boolean hasDisplinaryCase() {
		return memberDisplinaryCase == 1;
	}

	public void setMemberDisplinaryCase(boolean hasDisplinaryCase) {
		this.memberDisplinaryCase = hasDisplinaryCase ? 1 : 0;
	}

	public Set<GoodStandingCertificate> getGoodStandingCerts() {
		return goodStandingCerts;
	}

	public void setGoodStandingCerts(
			Set<GoodStandingCertificate> goodStandingCerts) {
		this.goodStandingCerts = goodStandingCerts;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public int getMemberDisplinaryCase() {
		return memberDisplinaryCase;
	}

	public void setMemberDisplinaryCase(int memberDisplinaryCase) {
		this.memberDisplinaryCase = memberDisplinaryCase;
	}

	public String getSetPracisingNo() {
		return setPracisingNo;
	}

	public void setSetPracisingNo(String setPracisingNo) {
		this.setPracisingNo = setPracisingNo;
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

	public Double getMemberBalance() {
		return memberBalance;
	}

	public void setMemberBalance(Double memberBalance) {
		this.memberBalance = memberBalance;
	}

	public boolean isInGoodStanding() {
		return isInGoodStanding;
	}

	public void setInGoodStanding(boolean isInGoodStanding) {
		this.isInGoodStanding = isInGoodStanding;
	}

}
