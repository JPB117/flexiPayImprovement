package com.workpoint.icpak.shared.model.events;

import com.workpoint.icpak.shared.model.Listable;

public enum AttendanceStatus implements Listable {

	ATTENDED("ATTENDED"), NOTATTENDED("NOT-ATTENDED"), ENROLLED("ENROLLED"), NOTENROLLED(
			"ENROLLED");

	private String displayName;

	private AttendanceStatus(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	@Override
	public String getName() {
		return name();
	}
}
