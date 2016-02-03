package com.workpoint.icpak.client.ui.membership.form;

import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.model.UploadContext;
import com.workpoint.icpak.client.model.UploadContext.UPLOADACTION;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.DateField;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.ui.upload.custom.Uploader;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationType;
import com.workpoint.icpak.shared.model.AttachmentDto;
import com.workpoint.icpak.shared.model.Branch;
import com.workpoint.icpak.shared.model.Country;
import com.workpoint.icpak.shared.model.Gender;

public class MemberRegistrationForm extends Composite {

	@UiField
	IssuesPanel issuesPanel;
	@UiField
	TextField txtSurname;
	@UiField
	TextField txtOtherNames;
	@UiField
	TextField txtEmailAddress;
	@UiField
	TextField txtPhone;
	@UiField
	TextField txtEmployer;
	@UiField
	TextField txtCity;
	@UiField
	TextField txtAddress;
	@UiField
	TextField txtPostalCode;
	@UiField
	TextField txtResidence;
	@UiField
	TextField txtIdNo;
	@UiField
	TextField txtContactName;
	@UiField
	TextField txtContactResidence;
	@UiField
	TextField txtContactTelephone;
	@UiField
	TextField txtContactEmail;

	@UiField
	TextField txtOffence;
	@UiField
	TextField txtDateAndPlace;
	@UiField
	TextField txtSentence;

	@UiField
	DateField dtDOB;
	@UiField
	Uploader uploaderIdCopy;
	@UiField
	DivElement divIdCopy;
	@UiField
	DropDownList<Gender> lstGender;
	@UiField
	HTMLPanel panelPreviousAttachments;

	@UiField
	DropDownList<Country> lstCountry;

	@UiField
	DropDownList<Branch> lstBranch;

	private boolean isEmailValid = true;

	private static MemberRegistrationFormUiBinder uiBinder = GWT
			.create(MemberRegistrationFormUiBinder.class);
	private int counter;
	private ApplicationType type;
	private ApplicationFormHeaderDto application;

	interface MemberRegistrationFormUiBinder extends
			UiBinder<Widget, MemberRegistrationForm> {
	}

	public MemberRegistrationForm() {
		initWidget(uiBinder.createAndBindUi(this));
		lstGender.setItems(Arrays.asList(Gender.values()));
		lstBranch.setItems(Arrays.asList(Branch.values()));
	}

	public boolean isValid() {
		boolean isValid = true;
		issuesPanel.clear();
		if (counter == 0) {
			if (!isEmailValid) {
				issuesPanel.addError("e-Mail " + txtEmailAddress.getValue()
						+ " is already registered");
			}
			if (isNullOrEmpty(txtSurname.getValue())) {
				isValid = false;
				issuesPanel.addError("Surname is required");
			}
			if (isNullOrEmpty(txtOtherNames.getValue())) {
				isValid = false;
				issuesPanel.addError("Other Names is required");
			}
			if (isNullOrEmpty(txtEmailAddress.getValue())) {
				isValid = false;
				issuesPanel.addError("Email is required");
			}

			if (isNullOrEmpty(txtPhone.getValue())) {
				isValid = false;
				issuesPanel.addError("Phone Number is required");
			}

			if (isNullOrEmpty(txtEmployer.getValue())) {
				isValid = false;
				issuesPanel.addError("Employer is required");
			}

			if (isNullOrEmpty(txtCity.getValue())) {
				isValid = false;
				issuesPanel.addError("City is required");
			}

			if (isNullOrEmpty(txtAddress.getValue())) {
				isValid = false;
				issuesPanel.addError("Address is required");
			}

			if (isNullOrEmpty(txtIdNo.getValue())) {
				isValid = false;
				issuesPanel.addError("IdNumber is required.");
			}

			if (isNullOrEmpty(txtContactName.getValue())) {
				isValid = false;
				issuesPanel.addError("Contact Name is required.");
			}

			if (isNullOrEmpty(txtContactResidence.getValue())) {
				isValid = false;
				issuesPanel.addError("Contact Residence is required.");
			}

			if (isNullOrEmpty(txtContactTelephone.getValue())) {
				isValid = false;
				issuesPanel.addError("Contact Telephone is required.");
			}

			if (isNullOrEmpty(txtContactEmail.getValue())) {
				isValid = false;
				issuesPanel.addError("Contact Email is required.");
			}
			if (lstGender.getValue() == null) {
				isValid = false;
				issuesPanel.addError("Gender is required");
			}

			if (!isEmailValid) {
				isValid = false;
			}
		}

		if (!isValid) {
			issuesPanel.removeStyleName("hide");
		} else {
			issuesPanel.addStyleName("hide");
		}

		return isValid;
	}

	public TextField getEmail() {
		return txtEmailAddress;
	}

	private void setIDUploadContext(String applicationRefId) {
		UploadContext context = new UploadContext();
		context.setContext("applicationRefId", applicationRefId);
		context.setAction(UPLOADACTION.UPLOADIDPHOTOCOPY);
		context.setAccept(Arrays.asList("doc", "pdf", "jpg", "jpeg", "png",
				"docx"));
		uploaderIdCopy.setContext(context);
	}

	public ApplicationFormHeaderDto getApplicationForm() {
		ApplicationFormHeaderDto dto = new ApplicationFormHeaderDto();
		dto.setSurname(txtSurname.getValue());
		dto.setOtherNames(txtOtherNames.getValue());
		dto.setEmail(txtEmailAddress.getValue());
		dto.setEmployer(txtEmployer.getValue());
		dto.setCity1(txtCity.getValue());
		dto.setCountry(lstCountry.getValue() == null ? "" : lstCountry
				.getValue().getDisplayName());
		dto.setAddress1(txtAddress.getValue());
		dto.setTelephone1(txtPhone.getValue());
		dto.setPostCode(txtPostalCode.getValue());
		dto.setApplicationType(type);
		dto.setDob(dtDOB.getValueDate());
		dto.setGender(lstGender.getValue());
		dto.setResidence(txtResidence.getValue());
		dto.setIdNumber(txtIdNo.getValue());
		dto.setBranch(lstBranch.getValue().getName());
		dto.setContactPerson(txtContactName.getValue());
		dto.setContactAddress(txtContactResidence.getValue());
		dto.setContactTelephone(txtContactTelephone.getValue());
		dto.setContactEmail(txtContactEmail.getValue());
		dto.setOffence(txtOffence.getValue());
		dto.setConvictionDateAndPlace(txtDateAndPlace.getValue());
		dto.setSentence(txtSentence.getValue());
		return dto;
	}

	public void setEmailValid(boolean isEmailValid) {
		issuesPanel.clear();
		this.isEmailValid = isEmailValid;
		if (!isEmailValid) {
			issuesPanel.addError("e-Mail " + txtEmailAddress.getValue()
					+ " is already registered");
			issuesPanel.removeStyleName("hide");
		} else {
			issuesPanel.addStyleName("hide");
		}
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public void setType(ApplicationType type) {
		this.type = type;
	}

	public void bind(ApplicationFormHeaderDto application) {
		this.application = application;
		txtSurname.setValue(application.getSurname());
		txtOtherNames.setValue(application.getOtherNames());
		txtEmailAddress.setValue(application.getEmail());
		txtEmployer.setValue(application.getEmployer());
		txtCity.setValue(application.getCity1());
		dtDOB.setValue(application.getDob());
		lstGender.setValue(application.getGender());
		txtPhone.setValue(application.getTelephone1());
		txtAddress.setValue(application.getAddress1());
		txtPostalCode.setValue(application.getPostCode());
		txtResidence.setValue(application.getResidence());
		txtIdNo.setValue(application.getIdNumber());
		if (application.getBranch() != null) {
			lstBranch.setValue(Branch.valueOf(application.getBranch()));
		}

		txtContactName.setValue(application.getContactPerson());
		txtContactResidence.setValue(application.getContactAddress());
		txtContactTelephone.setValue(application.getContactTelephone());
		txtContactEmail.setValue(application.getContactEmail());
		txtOffence.setValue(application.getOffence());
		txtDateAndPlace.setValue(application.getConvictionDateAndPlace());
		txtSentence.setValue(application.getSentence());

		if (application.getAttachments() != null) {
			panelPreviousAttachments.clear();
			for (final AttachmentDto attachment : application.getAttachments()) {
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
				panelPreviousAttachments.add(link);
				panelPreviousAttachments.add(new HTML("<br/>"));
			}
		}
		type = application.getApplicationType();
		if (application.getRefId() != null) {
			divIdCopy.removeClassName("hide");
			setIDUploadContext(application.getRefId());
		} else {
			divIdCopy.addClassName("hide");
		}
	}

	public void clear() {
		txtSurname.setValue(null);
		txtOtherNames.setValue(null);
		txtEmailAddress.setValue(null);
		txtEmployer.setValue(null);
		txtCity.setValue(null);
		txtPhone.setValue(null);
		txtAddress.setValue(null);
		txtPostalCode.setValue(null);
		type = null;
	}

	public void setCountries(List<Country> countries) {
		lstCountry.setItems(countries);
		for (Country c : countries) {
			if (c.getName().equals("KE")) {
				lstCountry.setValue(c);
				break;
			}
		}
	}

	public void addError(String message) {
		issuesPanel.addError(message);
	}

}