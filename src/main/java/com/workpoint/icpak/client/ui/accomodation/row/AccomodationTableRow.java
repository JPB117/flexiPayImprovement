package com.workpoint.icpak.client.ui.accomodation.row;

import static com.workpoint.icpak.client.ui.util.NumberUtils.NUMBERFORMAT;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.RowWidget;
import com.workpoint.icpak.client.ui.events.EditModelEvent;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.events.AccommodationDto;
import com.workpoint.icpak.shared.model.events.EventDto;

public class AccomodationTableRow extends RowWidget {

	private static ActivitiesTableRowUiBinder uiBinder = GWT
			.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends
			UiBinder<Widget, AccomodationTableRow> {
	}

	@UiField
	HTMLPanel row;
	@UiField
	HTMLPanel divHotelName;
	@UiField
	HTMLPanel divNights;
	@UiField
	Anchor aHotelName;
	@UiField
	HTMLPanel divEventName;
	@UiField	
	ActionLink aEdit;
	@UiField
	ActionLink aDelete;
	
	@UiField
	HTMLPanel divPrice;
	@UiField
	HTMLPanel divSpaces;
	@UiField
	HTMLPanel divOccupied;

	
	AccommodationDto accomodation;

	public AccomodationTableRow() {
		initWidget(uiBinder.createAndBindUi(this));
		
		aEdit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AppContext.fireEvent(new EditModelEvent(accomodation));
			}
		});
		
		aHotelName.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AppContext.fireEvent(new EditModelEvent(accomodation));
			}
		});
		
		aDelete.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				AppContext.fireEvent(new EditModelEvent(accomodation,true));
			}
		});
		
	}

	public AccomodationTableRow(AccommodationDto accommodation) {
		this();
		accomodation = accommodation;
		EventDto event = accommodation.getEvent();
		divEventName.add(new InlineLabel(event.getName()));
		aHotelName.setText(accommodation.getHotel());
		divNights.add(new InlineLabel(accommodation.getNights()+""));
		
		divOccupied.add(new InlineLabel((accommodation.getSpaces() - accommodation.getTotalBooking())+""));
//		divNights.add(new InlineLabel(DateUtils.getTimeDifference(
//				new Date(event.getStartDate()), new Date(event.getEndDate()))));
		divPrice.add(new InlineLabel(NUMBERFORMAT.format(accommodation.getFee())));
		
		boolean hasAvailableSpace = accomodation.getSpaces() > accommodation.getTotalBooking();
		
		HTML html = new HTML(+accommodation.getSpaces()+"");
		if(hasAvailableSpace){
			html.getElement().getStyle().setColor("green");
		}else{
			html.getElement().getStyle().setColor("red");
		}
		divSpaces.add(html);
	}
	
	

}
