package com.workpoint.icpak.client.ui.util;

import com.workpoint.icpak.shared.model.Listable;

public enum DateRange implements Listable {
	NOW("Now"), TODAY("Today"), YESTERDAY("Yesterday"), THISMONTH("This Month"), THISWEEK(
			"Last 7 Days"), LASTMONTH("Last Month"), THISQUARTER(
			"Last 3 Months"), HALFYEAR("Last 6 Months"), THISYEAR("This Year"), LASTYEAR(
			"This Year"), SPECIFIC("Specific Date"), INBETWEEN("DateRange");

	private String displayName;

	DateRange(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String getName() {
		return displayName;
	}

	public static DateRange getDateRange(String name) {
		for (DateRange type : DateRange.values()) {
			if (type.displayName.equals(name)) {
				return type;
			}
		}
		return null;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

}