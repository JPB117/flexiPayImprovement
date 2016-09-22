package com.icpak.rest.models.auth;

import javax.persistence.Entity;

import com.icpak.rest.models.base.PO;

//@Index
@Entity
public class UserSession extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userRefId;
	private String cookie;

	public UserSession() {
	}

	public UserSession(String userRefId, String cookie) {
		super();

		this.userRefId = userRefId;
		this.cookie = cookie;
	}

	public String getCookie() {
		return cookie;
	}

	public String getUserRefId() {
		return userRefId;
	}

}