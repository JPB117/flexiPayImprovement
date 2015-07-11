package com.workpoint.icpak.client.ui.accomodation.form;

import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.shared.model.AccomodationDTO;

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
	DropDownList lstEvent;

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
			issues.addError("Category is mandatory");
		}
		return isValid;
	}

	public void setAccomodationDetails(AccomodationDTO accomodation) {

	}

	public AccomodationDTO getAccomodationDetails() {
		return null;
	}
}
