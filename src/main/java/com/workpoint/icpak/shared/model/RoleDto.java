package com.workpoint.icpak.shared.model;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class RoleDto extends SerializableObj implements Listable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private String fullName;
	private Long UserTypeID;
	private Long RoleId;
	
	public RoleDto(){
	}

	public RoleDto(String name){
		this.name=name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof RoleDto)){
			return false;
		}
		
		RoleDto other = (RoleDto)obj;
		
		return other.name.equals(name);
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public String toString() {
		
		return name;
	}

	@JsonIgnore
	public String getDisplayName() {
		
		return fullName;
	}

	public Long getUserTypeID() {
		return UserTypeID;
	}

	public void setUserTypeID(Long userTypeID) {
		UserTypeID = userTypeID;
	}

	public Long getRoleId() {
		return RoleId;
	}

	public void setRoleId(Long roleId) {
		RoleId = roleId;
	}
	
}
