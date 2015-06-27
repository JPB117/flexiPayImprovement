package com.workpoint.icpak.client.ui.eventsandseminars.row;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.RowWidget;

public class EventsTableRow extends RowWidget {

	private static ActivitiesTableRowUiBinder uiBinder = GWT
			.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends
			UiBinder<Widget, EventsTableRow> {
	}

	@UiField
	HTMLPanel row;
	@UiField
	HTMLPanel divDate;
	@UiField
	HTMLPanel divEventName;
	@UiField
	HTMLPanel divDelegates;
	@UiField
	HTMLPanel divEventLocation;
	@UiField
	HTMLPanel divPaidAmount;
	@UiField
	HTMLPanel divUnPaidAmount;
	@UiField
	Anchor aEventName;
	

	public EventsTableRow() {
		initWidget(uiBinder.createAndBindUi(this));
		
		String url = "#events;eventId=254";
		aEventName.setHref(url);
	}

}
