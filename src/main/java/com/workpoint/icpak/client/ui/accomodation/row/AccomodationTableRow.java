package com.workpoint.icpak.client.ui.accomodation.row;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.RowWidget;

public class AccomodationTableRow extends RowWidget {

	private static ActivitiesTableRowUiBinder uiBinder = GWT
			.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends
			UiBinder<Widget, AccomodationTableRow> {
	}

	@UiField
	HTMLPanel row;
	@UiField
	HTMLPanel divHotelId;
	@UiField
	HTMLPanel divHotelName;
	@UiField
	HTMLPanel divNights;
	@UiField
	HTMLPanel divPrice;
	@UiField
	Anchor aHotelName;

	public AccomodationTableRow() {
		initWidget(uiBinder.createAndBindUi(this));

		String url = "#accomodation;eventId=254";
		aHotelName.setHref(url);
	}

}
