package com.workpoint.icpak.client.ui.invoices.advanced;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.DateField;

public class AdvancedFilter extends Composite {

	private static AdvancedFilterUiBinder uiBinder = GWT
			.create(AdvancedFilterUiBinder.class);

	@UiField
	DateField dtStartDate;
	@UiField
	DateField dtEndDate;

	interface AdvancedFilterUiBinder extends UiBinder<Widget, AdvancedFilter> {
	}

	public AdvancedFilter() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public Date getStartDate() {
		return dtStartDate.getValueDate();
	}

	public Date getEndDate() {
		return dtEndDate.getValueDate();
	}

}
