package com.icpak.rest.models.base;

import javax.persistence.Transient;

public class ResourceModel {

	@Transient
	private String uri;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
}
