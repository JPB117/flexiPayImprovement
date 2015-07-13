package com.workpoint.icpak.client.ui.eventsandseminars.delegates.row;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.RowWidget;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.events.AttendanceStatus;
import com.workpoint.icpak.shared.model.events.BookingDto;
import com.workpoint.icpak.shared.model.events.DelegateDto;

public class DelegateTableRow extends RowWidget {

	private static ActivitiesTableRowUiBinder uiBinder = GWT
			.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends
			UiBinder<Widget, DelegateTableRow> {
	}

	@UiField
	HTMLPanel row;

	@UiField
	HTMLPanel divMemberNo;
	@UiField
	HTMLPanel divTitle;
	@UiField
	HTMLPanel divSurName;
	@UiField
	HTMLPanel divOtherNames;
	@UiField
	HTMLPanel divEmail;
	@UiField
	HTMLPanel divAccomodation;
	@UiField
	HTMLPanel divAttendance;
	@UiField
	HTMLPanel divPaymentStatus;
	
	@UiField HTMLPanel divAmount;
	@UiField SpanElement spnPaymentStatus;
	@UiField SpanElement spnAttendance;

	ActionLink aRemove;

	private Integer rowId;

	public DelegateTableRow() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public DelegateTableRow(String memberNo, String title, String surName,
			String otherNames, String email, Integer rowId) {
		this();
		divMemberNo.getElement().setInnerHTML(memberNo);
		divTitle.getElement().setInnerHTML(title);
		divSurName.getElement().setInnerHTML(surName);
		divOtherNames.getElement().setInnerHTML(otherNames);
		divEmail.getElement().setInnerHTML(email);
		this.rowId = rowId;

	}

	public DelegateTableRow(BookingDto dto, DelegateDto delegate) {
		this(delegate.getMemberRegistrationNo(), delegate.getTitle(),
				delegate.getSurname(), delegate.getOtherNames(), delegate.getEmail(),null);
		
		if(delegate.getAccommodation()!=null){
			divAccomodation.add(new InlineLabel(delegate.getAccommodation().getHotel()+""));
		}
		
		if(dto.getPaymentStatus()!=null){
			spnPaymentStatus.setInnerText(dto.getPaymentStatus().name());
			if(dto.getPaymentStatus()==PaymentStatus.NOTPAID){
				spnPaymentStatus.removeClassName("label-success");
				spnPaymentStatus.addClassName("label-danger");
			}
		}
		
		if(delegate.getAttendance()!=null){
			spnAttendance.setInnerText(delegate.getAttendance().getDisplayName());
			if(delegate.getAttendance()==AttendanceStatus.NOTATTENDED){
				spnAttendance.removeClassName("label-success");
				spnAttendance.addClassName("label-danger");
			}
		}
	}

	public void InsertParameters(TextField memberNo, TextField title,
			TextField surName, TextField otherNames, TextField email,
			Integer rowId) {
		divMemberNo.add(memberNo);
		divTitle.add(title);
		divSurName.add(surName);
		divOtherNames.add(otherNames);
		divEmail.add(email);
		this.rowId = rowId;
	}

	public void showAdvancedDetails(boolean show) {
		if (show) {
			divPaymentStatus.setVisible(true);
			divAttendance.setVisible(true);
		} else {
			divPaymentStatus.setVisible(false);
			divAttendance.setVisible(false);
		}
	}
}
