package com.workpoint.icpak.client.ui.enquiries.table.row;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.RowWidget;
import com.workpoint.icpak.shared.model.EnquiriesDto;

public class EnquiriesTableRow extends RowWidget {

	private static ActivitiesTableRowUiBinder uiBinder = GWT
			.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends
			UiBinder<Widget, EnquiriesTableRow> {
	}

	@UiField
	HTMLPanel row;
	@UiField
	HTMLPanel divDate;
	@UiField
	ActionLink aMemberNo;
	@UiField
	ActionLink aMember;
	@UiField
	HTMLPanel divEmail;
	@UiField
	HTMLPanel divPhone;
	@UiField
	ActionLink aSubject;
	@UiField
	HTMLPanel divCategory;
	@UiField
	HTMLPanel divEnquiryDetails;
	@UiField
	SpanElement spnStatus;
	@UiField
	ActionLink aReply;
	@UiField
	ActionLink aMarkComplete;

	public EnquiriesTableRow() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public EnquiriesTableRow(EnquiriesDto dto) {
		this();
		divDate.getElement().setInnerText(dto.getDate().toString());
		aMemberNo.setText(dto.getMembershipNo());
		aMember.setText(dto.getFullNames());
		divEmail.getElement().setInnerText(dto.getEmailAddress());
		divPhone.getElement().setInnerText(dto.getPhone());
		aSubject.setText(dto.getSubject());
		divCategory.getElement().setInnerText(dto.getCategory().getDisplayName());
		divEnquiryDetails.getElement().setInnerText(dto.getMessage());
		spnStatus.setInnerText(dto.getStatus()==null? "DRAFT" : dto.getStatus().name());
		//aReply.setText(dto.getReply());
		//aMarkComplete
	}

}
