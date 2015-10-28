package com.workpoint.icpak.client.ui.accomodation.form;

import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.ui.util.DateUtils;
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
	TextField txtSpace;
	@UiField
	TextField txtPrice;

	@UiField
	InlineLabel spnEvent;

	private AccommodationDto accomodation;

	private EventDto event;

	public CreateAccomodation() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public boolean isValid() {
		boolean isValid = true;
		issues.clear();
		if (isNullOrEmpty(txtHotelName.getValue())) {
			isValid = false;
			issues.addError("Hotel name is mandatory");
		}
		if (isNullOrEmpty(txtNights.getValue())) {
			isValid = false;
			issues.addError("Nights Information is mandatory");
		}
		if (isNullOrEmpty(txtPrice.getValue())) {
			isValid = false;
			issues.addError("Price Information is mandatory");
		}
		if (isNullOrEmpty(txtSpace.getValue())) {
			isValid = false;
			issues.addError("Space information is mandatory");
		}

		return isValid;
	}

	public void setAccomodationDetails(AccommodationDto accomodation) {
		this.accomodation = accomodation;
		if (this.accomodation != null) {
			txtHotelName.setValue(accomodation.getHotel());
			txtNights.setValue(accomodation.getNights() + "");
			txtPrice.setValue(accomodation.getFee() + "");
			txtSpace.setValue(accomodation.getSpaces() + "");
			setEvent(accomodation.getEvent());
		}
	}

	public AccommodationDto getAccomodationDetails() {
		AccommodationDto accommodation = new AccommodationDto();
		if (this.accomodation != null) {
			accommodation = this.accomodation;
		}

		accommodation.setEvent(event);
		accommodation.setFee(new Double(txtPrice.getValue()));
		accommodation.setHotel(txtHotelName.getValue());
		accommodation.setNights(new Integer(txtNights.getValue()));
		accommodation.setSpaces(new Integer(txtSpace.getValue()));
		return accommodation;
	}

	public void setEvent(EventDto event) {
		this.event = event;

		Date startDate = DateUtils.parse(event.getStartDate(),
				DateUtils.FULLTIMESTAMP);
		Date endDate = DateUtils.parse(event.getEndDate(),
				DateUtils.FULLTIMESTAMP);
		spnEvent.setText(event.getName() + " ("
				+ DateUtils.format(startDate, DateUtils.DATEFORMAT) + " - "
				+ DateUtils.format(endDate, DateUtils.DATEFORMAT) + ")");
	}
}
