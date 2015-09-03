/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.icpak.rest.models.auth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.icpak.rest.models.base.ExpandTokens;
import com.icpak.rest.models.base.PO;
import com.icpak.rest.models.membership.ApplicationFormHeader;
import com.icpak.rest.models.membership.Member;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.workpoint.icpak.shared.model.RoleDto;
import com.workpoint.icpak.shared.model.UserDto;
import com.workpoint.icpak.shared.model.auth.AccountStatus;

/**
 * Simple class that represents any User domain entity in any application.
 */

@ApiModel(value = "User Model", description = "A User represents any person who may have access to the system including "
		+ "members, administrators, icpak staff and stakeholders")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({ BioData.class })
@JsonSerialize(include = Inclusion.NON_NULL)
@Entity
@Table(name = "user", indexes = { @Index(columnList = "username", name = "idx_users_username")
// ,@Index(columnList = "email", name = "idx_users_email")
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "username", required = true)
	@Basic(optional = false)
	@Column(length = 100, unique = true)
	// @Index(name="idx_users_username")
	private String username;

	@ApiModelProperty(value = "user email")
	// @Basic(optional=false)
	// @Index(name="idx_users_email")
	private String email;

	@Basic(optional = false)
	@Column(length = 255)
	private String password;

	@JsonIgnore
	@XmlTransient
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "userid"), inverseJoinColumns = @JoinColumn(name = "roleid"))
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<Role> roles = new HashSet<Role>();

	@Embedded
	private BioData userData = null;

	@OneToOne(mappedBy = "user", cascade = { CascadeType.PERSIST,
			CascadeType.REMOVE })
	private Member member;

	private String memberNo;
	private String phoneNumber;
	private String residence;
	private String address;
	private String city;
	private String nationality;

	@Enumerated(EnumType.STRING)
	private AccountStatus status = AccountStatus.NEWACC;

	public User() {
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public void addRole(Role role) {
		roles.add(role);
	}

	public void removeRole(Role role) {
		roles.remove(role);
	}

	public User clone(String... expand) {

		User user = new User();
		user.setRefId(refId);
		// user.setUsername(getUsername());
		user.setEmail(email);
		user.setAddress(address);
		user.setCity(city);
		user.setNationality(nationality);
		user.setPhoneNumber(phoneNumber);
		user.setResidence(residence);

		if (roles != null) {
			Set<Role> cloneRoles = new HashSet<>();
			for (Role role : roles) {
				Role clone = role.clone();
				cloneRoles.add(clone);
			}
			user.setRoles(cloneRoles);
		}

		if (userData != null)
			user.setUserData(userData.clone());
		user.setCreated(getCreated());
		user.setUpdated(getUpdated());

		if (expand != null) {
			for (String token : expand) {
				// if(token.equals("bookings")){
				// for(Booking booking: bookings){
				// user.addBooking(booking.clone());
				// }
				// }

				if (token.toUpperCase().equals(ExpandTokens.DETAIL.name())) {
					// user.setLastName(getLastName());
					// user.setFirstName(getFirstName());
					user.setEmail(getEmail());
				}

				if (token.equals("member")) {
					// user.setMember(member);
				}

				if (token.equals("roles")) {
					for (Role role : roles) {
						user.addRole(role.clone());
					}
				}
			}
		}
		return user;
	}

	public BioData getUserData() {
		return userData;
	}

	public void setUserData(BioData userData) {
		this.userData = userData;
	}

	public void copyFrom(UserDto dto) {
		setEmail(dto.getEmail());
		setPhoneNumber(dto.getPhoneNumber());
		BioData bio = new BioData();
		bio.setFirstName(dto.getName());
		bio.setLastName(dto.getSurname());
		// bio.setGender(dto.get);
		setUserData(bio);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
		this.username = email;
	}

	public String getHashedPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(String memberId) {
		this.memberNo = memberId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getResidence() {
		return residence;
	}

	public void setResidence(String residence) {
		this.residence = residence;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public UserDto toDto() {
		UserDto dto = new UserDto();
		dto.setMemberNo(memberNo);
		dto.setEmail(email);
		dto.setName(userData.getFirstName());
		dto.setSurname(userData.getLastName());
		dto.setPhoneNumber(phoneNumber);
		dto.setRefId(refId);
		dto.setStatus(status);

		List<RoleDto> dtos = new ArrayList<>();
		if (getRoles() != null) {
			for (Role r : getRoles()) {
				dtos.add(r.toDto());
			}
		}
		dto.setGroups(dtos);

		return dto;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public void setUserData(UserDto dto) {
		BioData bio = new BioData();
		bio.setFirstName(dto.getName());
		bio.setLastName(dto.getSurname());
		this.userData = bio;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
		member.setUser(this);
	}

}
