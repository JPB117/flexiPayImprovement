package com.workpoint.icpak.client.ui.profile.basic;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.model.UploadContext;
import com.workpoint.icpak.client.model.UploadContext.UPLOADACTION;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.membership.form.MemberRegistrationForm;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.AttachmentDto;
import com.workpoint.icpak.shared.model.Branch;
import com.workpoint.icpak.shared.model.Country;

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
	Element elBranch;
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
	Element elIdNo;
	@UiField
	HTMLPanel panelPassportCopy;

	@UiField
	Anchor aSave;
	@UiField
	Anchor aCancel;
	@UiField
	ActionLink aEdit;

	@UiField
	MemberRegistrationForm panelRegistration;

	List<String> allIssues = new ArrayList<>();

	public BasicDetails() {
		initWidget(uiBinder.createAndBindUi(this));

		setEditMode(true);
	}

	public void setEditMode(boolean editMode) {
		if (editMode) {
			aEdit.setVisible(true);
		} else {
			aEdit.setVisible(false);
		}
	}

	public void bindDetails(ApplicationFormHeaderDto result) {
		if (result.getTelephone1() != null) {
			elPhone.setInnerText(result.getTelephone1());
		} else {
			allIssues.add("No phone Number registered");
		}
		if (result.getEmail() != null) {
			elEmail.setInnerText(result.getEmail());
		} else {
			allIssues.add("No Email Address registered");
		}

		if (result.getBranch() != null) {
			elBranch.setInnerText(Branch.valueOf(result.getBranch())
					.getDisplayName());
		}

		if (result.getEmployer() != null) {
			elEmployer.setInnerText(result.getEmployer());
		}
		if (result.getIdNumber() != null) {
			elIdNo.setInnerText(result.getIdNumber());
		}
		if (result.getResidence() != null) {
			elResidence.setInnerText(result.getResidence());
		}
		if (result.getAddress1() != null) {
			elAddress.setInnerText(result.getAddress1());
		}
		if (result.getPostCode() != null) {
			elPostalCode.setInnerText(result.getPostCode());
		}
		if (result.getCountry() != null) {
			elCountry.setInnerText(result.getCountry());
		}
		if (result.getCity1() != null) {
			elCity.setInnerText(result.getCity1());
		}
		if (result.getGender() != null) {
			elSex.setInnerText(result.getGender().name());
		}

		if (result.getDob() != null) {
			elDob.setInnerText(DateUtils.DATEFORMAT.format(result.getDob()));
		}

		panelPassportCopy.clear();
		if (result.getAttachments() != null
				&& !result.getAttachments().isEmpty()) {
			for (final AttachmentDto attachment : result.getAttachments()) {
				final UploadContext ctx = new UploadContext("getreport");
				ctx.setAction(UPLOADACTION.GETATTACHMENT);
				ctx.setContext("refId", attachment.getRefId());

				ActionLink link = new ActionLink(attachment.getAttachmentName());
				link.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						Window.open(ctx.toUrl(),
								attachment.getAttachmentName(), "");
					}
				});
				panelPassportCopy.add(link);
				panelPassportCopy.add(new HTML("<br/>"));
			}
		} else {

			allIssues.add("Your ID/Passport Copy is required.");
		}
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

	public void clear() {
		elPhone.setInnerText(null);
		elEmail.setInnerText(null);
		elDob.setInnerText(null);
		elSex.setInnerText(null);
		elEmployer.setInnerText(null);
		elResidence.setInnerText(null);
		elAddress.setInnerText(null);
		elPostalCode.setInnerText(null);
		elCountry.setInnerText(null);
		elCity.setInnerText(null);
		panelRegistration.clear();
	}

	public void setCountries(List<Country> countries) {
		panelRegistration.setCountries(countries);
	}

	public List<String> getBasicDetailIssues() {
		return allIssues;
	}
}
