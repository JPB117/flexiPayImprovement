package com.icpak.rest.models.membership;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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

@ApiModel(value="Member Model", description="ICPAK Member Model")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonSerialize(include=Inclusion.NON_NULL)

@Entity
@Table
public class Member extends PO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userRefId;
	private String memberNo;
	
	@OneToOne
	@JoinColumn(name="userId")
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
}
