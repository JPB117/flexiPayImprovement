package com.workpoint.icpak.client.ui.eventsandseminars.header;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class EventsHeader extends Composite {

	private static ActivityHeaderUiBinder uiBinder = GWT
			.create(ActivityHeaderUiBinder.class);

	interface ActivityHeaderUiBinder extends UiBinder<Widget, EventsHeader> {
	}

	@UiField
	SpanElement spnTotalApplication;

	@UiField
	SpanElement spnTotalProcessed;

	@UiField
	SpanElement spnTotalPending;

	public EventsHeader() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setCounts(int total, int done, int upcoming) {
		spnTotalApplication.setInnerText(total+"");
		spnTotalProcessed.setInnerText(done+"");
		spnTotalPending.setInnerText(upcoming+"");
	}

}
