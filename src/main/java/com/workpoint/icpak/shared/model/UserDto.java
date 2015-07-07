package com.workpoint.icpak.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.workpoint.icpak.shared.model.auth.AccountStatus;

@XmlRootElement
public class UserDto implements Listable,Serializable {

	private static final long serialVersionUID = -5249516544970187459L;
	private Long id;
	private String uri;
	private String refId;
	private String applicationRefId;
	private String name;
	private String userId;
	private String memberId;
	private String email;
	private String surname;
	private String password;
	private ArrayList<UserGroup> groups = new ArrayList<UserGroup>() ;
	private int participated;
	private int inbox;
	private AccountStatus status = AccountStatus.NEWACC;
	
	public UserDto() {
	}

	public UserDto(String id) {
		this.userId = id;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}

	public void setUserId(String id) {
		this.userId = id;
	}

	public String getName() {
		return name;
	}

	public String getUserId() {
		return userId;
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
	
	public String getFullName(){
		return surname+" "+name;
	}
	
	@Override
	public String toString() {
		return "{refId:"+refId+",userId:"+userId+",fullNames:"+getFullName()+"}";
	}

	public List<UserGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<UserGroup> groups) {
		this.groups.addAll(groups);
	}
	
	public String getGroupsAsString(){
		StringBuffer out = new StringBuffer();
		if(groups!=null){
			for(UserGroup group: groups){
				out.append(group.getName()+",");
			}
		}
		
		if(out.length()>0){
			return out.substring(0, out.length()-1);
		}
		
		return "";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean hasGroup(String groupName) {
		if(groups!=null)
		for(UserGroup group:groups){
			if(group.getName().equalsIgnoreCase(groupName)){
				return true;
			}
		}
		return false;
	}

	public boolean isAdmin() {

		return hasGroup("admin");
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null || !(obj instanceof UserDto)){
			return false;
		}
		
		UserDto other =  (UserDto)obj;
		
		if(name==null){
			return false;
		}
		
		return name.equals(other.name);
	}

	public int getInbox() {
		return inbox;
	}

	public void setInbox(int inbox) {
		this.inbox = inbox;
	}

	public int getParticipated() {
		return participated;
	}

	public void setParticipated(int participated) {
		this.participated = participated;
	}

	public int getTotal() {
		
		return participated+inbox;
	}

	@Override
	public String getDisplayName() {
		return getFullName();
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
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

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
}
