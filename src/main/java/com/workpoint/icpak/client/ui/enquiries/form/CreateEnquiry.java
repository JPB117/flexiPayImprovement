package com.workpoint.icpak.client.ui.enquiries.form;

import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.shared.model.AccomodationDTO;

public class CreateEnquiry extends Composite {

	private static RecordCPDUiBinder uiBinder = GWT
			.create(RecordCPDUiBinder.class);

	interface RecordCPDUiBinder extends UiBinder<Widget, CreateEnquiry> {
	}

	@UiField
	IssuesPanel issues;
	@UiField
	TextField txtFullNames;
	@UiField
	TextField txtMemberNo;
	@UiField
	TextField txtEmail;
	@UiField
	TextField txtMobile;
	@UiField
	TextField txtSubject;
	@UiField
	DropDownList lstCategory;
	@UiField
	TextArea txtMessage;

	public CreateEnquiry() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public boolean isValid() {
		boolean isValid = true;
		issues.clear();
		if (isNullOrEmpty(txtFullNames.getValue())) {
			isValid = false;
			issues.addError("Names is Mandatory");
		}
		if (isNullOrEmpty(txtMemberNo.getValue())) {
			isValid = false;
			issues.addError("Member No. is mandatory");
		}
		if (isNullOrEmpty(txtEmail.getValue())) {
			isValid = false;
			issues.addError("Email is mandatory");
		}
		if (isNullOrEmpty(txtMobile.getValue())) {
			isValid = false;
			issues.addError("Mobile is mandatory");
		}
		if (isNullOrEmpty(txtSubject.getValue())) {
			isValid = false;
			issues.addError("Subject is mandatory");
		}
		if (isNullOrEmpty(txtMessage.getValue())) {
			isValid = false;
			issues.addError("Message is mandatory");
		}
		if (lstCategory.getValue() == null) {
			isValid = false;
			issues.addError("Category is mandatory");
		}
		return isValid;
	}

	public void setEnquiryDetail(AccomodationDTO accomodation) {

	}

	public AccomodationDTO getAccomodationDetails() {
		return null;
	}
}
