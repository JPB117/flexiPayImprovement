package com.workpoint.icpak.client.ui.invoices.advanced;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.DateField;
import com.workpoint.icpak.client.ui.component.DateTimeField;
import com.workpoint.icpak.client.ui.util.DateRange;
import com.workpoint.icpak.client.ui.util.DateUtils;

public class AdvancedFilter extends Composite {

	private static AdvancedFilterUiBinder uiBinder = GWT
			.create(AdvancedFilterUiBinder.class);

	@UiField
	DateTimeField dtStartTime;
	@UiField
	DateTimeField dtEndTime;

	@UiField
	DateField dtEndDate;
	@UiField
	DateField dtStartDate;

	interface AdvancedFilterUiBinder extends UiBinder<Widget, AdvancedFilter> {
	}

	public AdvancedFilter() {
		initWidget(uiBinder.createAndBindUi(this));

		// dtStartDate.setValue(DateUtils.getDateByRange(DateRange.THISMONTH,
		// false));
		// dtEndDate.setValue(DateUtils.getDateByRange(DateRange.NOW, true));

		dtStartTime.setValue(DateUtils.TIMEFORMAT12HR.parse("6:00 AM"));
		dtEndTime.setValue(DateUtils.TIMEFORMAT12HR.parse("11:59 PM"));
	}

	public Date getStartDate() {
		return dtStartDate.getValueDate();
	}

	public Date getEndDate() {
		return dtEndDate.getValueDate();
	}

	public String getStartTime() {
		return dtStartTime.getSelectedTime();
	}

	public String getEndTime() {
		return dtEndTime.getSelectedTime();
	}

}
