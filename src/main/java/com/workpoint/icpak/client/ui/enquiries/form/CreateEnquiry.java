package com.workpoint.icpak.client.ui.enquiries.form;

import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.EnquiriesCategory;
import com.workpoint.icpak.shared.model.EnquiriesDto;
import com.workpoint.icpak.shared.model.UserDto;

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
	DropDownList<EnquiriesCategory> lstCategory;
	@UiField
	TextArea txtMessage;

	public CreateEnquiry() {
		initWidget(uiBinder.createAndBindUi(this));
		List<EnquiriesCategory> cats  = new ArrayList<EnquiriesCategory>();
		for(EnquiriesCategory c: EnquiriesCategory.values()){
			cats.add(c);
		}
		
		lstCategory.setItems(cats);
		
		if(AppContext.isLoggedIn()){
			UserDto currentUser = AppContext.getContextUser();
			txtEmail.setValue(currentUser.getEmail());
			txtFullNames.setValue(currentUser.getFullName());
			txtMemberNo.setValue(currentUser.getMemberNo());
			txtMobile.setValue(currentUser.getPhoneNumber());
		}
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

	public EnquiriesDto getEnquiry() {
		
		EnquiriesDto dto = new EnquiriesDto();
		dto.setCategory(lstCategory.getValue());
		dto.setEmailAddress(txtEmail.getValue());
		dto.setFullNames(txtFullNames.getValue());
		dto.setMembershipNo(txtMemberNo.getValue());
		dto.setMessage(txtMemberNo.getValue());
		dto.setPhone(txtMobile.getValue());
		dto.setSubject(txtSubject.getValue());
		
		return dto;
	}
}
