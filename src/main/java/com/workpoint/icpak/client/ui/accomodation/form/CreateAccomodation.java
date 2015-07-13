package com.workpoint.icpak.client.ui.accomodation.form;

import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.shared.model.events.AccommodationDto;
import com.workpoint.icpak.shared.model.events.EventDto;

public class CreateAccomodation extends Composite {

	private static RecordCPDUiBinder uiBinder = GWT
			.create(RecordCPDUiBinder.class);

	interface RecordCPDUiBinder extends UiBinder<Widget, CreateAccomodation> {
	}

	@UiField
	IssuesPanel issues;

	@UiField
	TextField txtHotelName;
	@UiField
	TextField txtNights;
	@UiField
	TextField txtPrice;
	@UiField
	DropDownList<EventDto> lstEvent;

	private AccommodationDto accomodation;

	public CreateAccomodation() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public boolean isValid() {
		boolean isValid = true;
		issues.clear();
		if (isNullOrEmpty(txtHotelName.getValue())) {
			isValid = false;
			issues.addError("Title is mandatory");
		}
		if (isNullOrEmpty(txtNights.getValue())) {
			isValid = false;
			issues.addError("Nights Information is mandatory");
		}
		if (isNullOrEmpty(txtPrice.getValue())) {
			isValid = false;
			issues.addError("Price Information is mandatory");
		}
		if (lstEvent.getValue() == null) {
			isValid = false;
			issues.addError("Event is mandatory");
		}
		return isValid;
	}

	public void setAccomodationDetails(AccommodationDto accomodation) {
		this.accomodation = accomodation;
		if (this.accomodation != null) {
			txtHotelName.setValue(accomodation.getHotel());
			txtNights.setValue(accomodation.getNights() + "");
			txtPrice.setValue(accomodation.getFee() + "");
			if(accomodation.getEvent()!=null){
				lstEvent.setValue(accomodation.getEvent());
			}
		}
	}

	public AccommodationDto getAccomodationDetails() {
		AccommodationDto accommodation = new AccommodationDto();
		if(this.accomodation!=null){
			accommodation = this.accomodation;
		}
		
		accommodation.setEvent(lstEvent.getValue());
		accommodation.setFee(new Double(txtPrice.getValue()));
		accommodation.setHotel(txtHotelName.getValue());
		accommodation.setNights(new Integer(txtNights.getValue()));
		//accommodation.setDescription(txt);
		
		return accommodation;
	}

	public void setEvents(List<EventDto> events) {
		lstEvent.setItems(events);
	}
}
