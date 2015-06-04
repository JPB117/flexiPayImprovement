package com.workpoint.icpak.shared.model;

import java.io.Serializable;

public enum NotificationCategory implements Listable,Serializable{
	
	EMAILNOTIFICATION("Email Nofication"),
	ACTIVITYFEED("Activity Feed");
	
	
	private String displayName;
	
	private NotificationCategory() {
	}

	private NotificationCategory(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String getName() {
		return name();
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	
}