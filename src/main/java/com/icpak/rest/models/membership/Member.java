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

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.base.PO;
import com.wordnik.swagger.annotations.ApiModel;
import com.workpoint.icpak.shared.model.MemberDto;
import com.workpoint.icpak.shared.model.MembershipStatus;

@ApiModel(value = "Member Model", description = "ICPAK Member Model")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonSerialize(include = Inclusion.NON_NULL)
@Entity
@Table
public class Member extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userRefId;
	private String memberNo;
	private Date registrationDate;

	@Column(nullable = false, columnDefinition = "int(1) not null default 0")
	private int memberDisplinaryCase = 0;

	/**
	 * TODO - Link this to the membership approval process<br/>
	 * - Also Link this to the membership annual renewal process.<br/>
	 * - Check also whether Admin can deactivate account for whatever reason<br/>
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
}
