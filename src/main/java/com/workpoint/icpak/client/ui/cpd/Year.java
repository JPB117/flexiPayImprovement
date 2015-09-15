package com.workpoint.icpak.client.ui.cpd;

import java.util.Date;

import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.shared.model.Listable;

public class Year implements Listable {
	private Date pickDate;

	public Year(Date passedDate) {
		pickDate = passedDate;
	}

	@Override
	public String getName() {
		return DateUtils.YEARFORMAT.format(pickDate);
	}

	@Override
	public String getDisplayName() {
		return DateUtils.YEARFORMAT.format(pickDate);
	}

}