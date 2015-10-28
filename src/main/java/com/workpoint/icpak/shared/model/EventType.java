package com.workpoint.icpak.shared.model;

import java.io.Serializable;

public enum EventType implements Serializable{

	EVENT("Event"),
	SEMINAR("Seminar"),
	WEBINAR("Webinar"),
	COURSE("Course"),
	CONFERENCE("Conference");

	private String displayName;

	private EventType(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		
		return displayName;
	}
}
