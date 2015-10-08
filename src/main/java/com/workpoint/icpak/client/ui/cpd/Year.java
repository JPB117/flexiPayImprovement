package com.workpoint.icpak.client.ui.cpd;

import com.workpoint.icpak.shared.model.Listable;

public class Year implements Listable {
	private int pickDate;

	public Year(int passedDate) {
		pickDate = passedDate;
	}

	@Override
	public String getName() {
		return pickDate+"";
	}

	@Override
	public String getDisplayName() {
		return pickDate+"";
	}

}