package com.workpoint.icpak.client.ui.eventsandseminars.row;

import static com.workpoint.icpak.client.ui.util.DateUtils.DATEFORMAT;
import static com.workpoint.icpak.client.ui.util.NumberUtils.NUMBERFORMAT;
import static com.workpoint.icpak.client.ui.util.StringUtils.*;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.RowWidget;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.shared.model.EventType;
import com.workpoint.icpak.shared.model.events.EventDto;

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
	HTMLPanel divEventType;
	@UiField
	HTMLPanel divPaidAmount;
	@UiField
	HTMLPanel divUnPaidAmount;
	@UiField
	HTMLPanel divCPDHours;
	@UiField
	Anchor aEventName;
	@UiField
	Label lDays;

	public EventsTableRow() {
		initWidget(uiBinder.createAndBindUi(this));

		String url = "#events;eventId=254";
		aEventName.setHref(url);
	}

	public EventsTableRow(EventDto event) {
		this();
		Date startDate = isNullOrEmpty(event.getStartDate()) ? null : DateUtils
				.parse(event.getStartDate(), DateUtils.FULLTIMESTAMP);
		Date endDate = isNullOrEmpty(event.getEndDate()) ? null : DateUtils
				.parse(event.getEndDate(), DateUtils.FULLTIMESTAMP);

		Date today = new Date();

		String dates = startDate == null ? "" : DATEFORMAT.format(startDate)
				+ "-" + endDate == null ? "" : DATEFORMAT.format(endDate);
		if (today.getTime() < endDate.getTime()) {
			divDate.addStyleName("event-active");
			lDays.setText("[ " + DateUtils.getTimeDifferenceAsString(endDate)
					+ "]");
			divEventType.addStyleName("event-active");
			divDelegates.addStyleName("event-active");
			divEventLocation.addStyleName("event-active");
			divPaidAmount.addStyleName("event-active");
			divUnPaidAmount.addStyleName("event-active");
			divCPDHours.addStyleName("event-active");
			divEventName.addStyleName("event-active");
		} else {
			divDate.addStyleName("event-inactive");
			lDays.setText("[ " + DateUtils.getTimeDifference(endDate)
					+ " a go]");
			divEventType.addStyleName("event-inactive");
			divDelegates.addStyleName("event-inactive");
			divEventLocation.addStyleName("event-inactive");
			divPaidAmount.addStyleName("event-inactive");
			divUnPaidAmount.addStyleName("event-inactive");
			divCPDHours.addStyleName("event-inactive");
			divEventName.addStyleName("event-inactive");
		}

		divDate.add(new InlineLabel(dates));
		setEventType(event.getType());
		divEventType.add(new InlineLabel(event.getType().getDisplayName()));
		divDelegates.add(new InlineLabel(event.getDelegateCount() + ""));
		divEventLocation.add(new InlineLabel(event.getVenue()));
		divPaidAmount.add(new InlineLabel(NUMBERFORMAT.format(event
				.getPaidCount()) + ""));
		divUnPaidAmount.add(new InlineLabel(NUMBERFORMAT.format(event
				.getUnPaidCount()) + ""));
		divCPDHours.add(new InlineLabel(event.getCpdHours() + ""));
		String courseId = ((event.getCourseId() != null) ? "(Course Id:"
				+ event.getCourseId() + ")" : "");
		aEventName.setText(event.getName() + courseId);
		aEventName.setHref("#events;eventId=" + event.getRefId());
	}

	private void setEventType(EventType eventType) {
		InlineLabel label = new InlineLabel(eventType.getDisplayName());
		if (eventType == EventType.COURSE) {
			label.setStyleName("label label-fill label-default");
		} else {
			label.setStyleName("label label-fill label-success");
		}
	}
}
