package com.workpoint.icpak.client.ui.profile.basic;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.AppManager;
import com.workpoint.icpak.client.ui.OnOptionSelected;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.membership.form.MemberRegistrationForm;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;

public class BasicDetails extends Composite {

	private static TillDetailsUiBinder uiBinder = GWT
			.create(TillDetailsUiBinder.class);

	interface TillDetailsUiBinder extends UiBinder<Widget, BasicDetails> {
	}

	@UiField
	HTMLPanel panelDisplay;
	@UiField
	HTMLPanel panelEditMode;

	@UiField
	Element elPhone;
	@UiField
	Element elEmail;
	@UiField
	Element elDob;
	@UiField
	Element elSex;
	@UiField
	Element elEmployer;
	@UiField
	Element elResidence;
	@UiField
	Element elAddress;
	@UiField
	Element elPostalCode;
	@UiField
	Element elCountry;
	@UiField
	Element elCity;
	@UiField
	Anchor aSave;
	@UiField
	Anchor aCancel;

	@UiField
	ActionLink aEdit;

	@UiField
	MemberRegistrationForm panelRegistration;

	public BasicDetails() {
		initWidget(uiBinder.createAndBindUi(this));

		setEditMode(false);

	}

	public void setEditMode(boolean editMode) {
		if (editMode) {
			aEdit.setVisible(true);
		} else {
			aEdit.setVisible(false);
		}
	}

	public void bindDetails(ApplicationFormHeaderDto result) {
		elPhone.setInnerText(result.getTelephone1());
		elEmail.setInnerText(result.getEmail());

		if (result.getDob() != null) {
			elDob.setInnerText(DateUtils.DATEFORMAT.format(result.getDate()));
		}

		if (result.getGender() != null) {
			elSex.setInnerText(result.getGender().name());
		}

		elEmployer.setInnerText(result.getEmployer());
		elResidence.setInnerText(result.getResidence());
		elAddress.setInnerText(result.getAddress1());
		elPostalCode.setInnerText(result.getPostCode());
		elCountry.setInnerText(result.getCountry());
		elCity.setInnerText(result.getCity1());

		panelRegistration.bind(result);
	}

	public ApplicationFormHeaderDto getApplicationForm() {
		return panelRegistration.getApplicationForm();
	}

	public HasClickHandlers getSaveButton() {
		return aSave;
	}

	public HasClickHandlers getEditButton() {
		return aEdit;
	}

	public boolean isValid() {
		return panelRegistration.isValid();
	}

	public HasClickHandlers getCancelButton() {
		return aCancel;
	}
}
