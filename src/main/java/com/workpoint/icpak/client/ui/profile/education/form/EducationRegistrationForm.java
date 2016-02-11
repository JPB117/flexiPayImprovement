package com.workpoint.icpak.client.ui.profile.education.form;

import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
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
import com.workpoint.icpak.shared.model.ApplicationFormEducationalDto;
import com.workpoint.icpak.shared.model.AttachmentDto;
import com.workpoint.icpak.shared.model.CertificateAwarded;

public class EducationRegistrationForm extends Composite {

	private static EducationRegistrationFormUiBinder uiBinder = GWT
			.create(EducationRegistrationFormUiBinder.class);

	@UiField
	TextField txtInstitution;
	@UiField
	TextField txtExaminingBody;
	@UiField
	DateField dtStartDate;
	@UiField
	DateField dtDateCompleted;
	@UiField
	TextField txtSectionsPassed;
	@UiField
	TextField txtRegistrationNo;
	@UiField
	TextField txtClassOrDivision;
	@UiField
	TextField txtEduType;
	@UiField
	Uploader uploader;

	@UiField
	ActionLink aStartUpload;

	@UiField
	HTMLPanel panelUploader;

	@UiField
	HTMLPanel panelUpload;

	@UiField
	HTMLPanel panelPreviousAttachments;

	@UiField
	IssuesPanel issues;

	private ApplicationFormEducationalDto educationDto;

	@UiField
	DropDownList<CertificateAwarded> lstCertificateAwarded;

	interface EducationRegistrationFormUiBinder extends
			UiBinder<Widget, EducationRegistrationForm> {
	}

	public EducationRegistrationForm() {
		initWidget(uiBinder.createAndBindUi(this));
		showUploadPanel(false);
		lstCertificateAwarded.setItems(Arrays.asList(CertificateAwarded
				.values()));
	}

	public EducationRegistrationForm(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public boolean isValid() {
		boolean isValid = true;
		issues.clear();

		if (isNullOrEmpty(txtInstitution.getValue())) {
			isValid = false;
			issues.addError("Institution is mandatory");
		}
		if (isNullOrEmpty(txtExaminingBody.getValue())) {
			isValid = false;
			issues.addError("Examining Body is mandatory");
		}
		if (dtStartDate.getValueDate() == null) {
			isValid = false;
			issues.addError("Start date is mandatory");
		}
		if (dtDateCompleted.getValueDate() == null) {
			isValid = false;
			issues.addError("Date Completed is mandatory");
		}
		if (isNullOrEmpty(txtClassOrDivision.getValue())) {
			isValid = false;
			issues.addError("Class/Division is mandatory");
		}
		if (isNullOrEmpty(lstCertificateAwarded.getValue())) {
			isValid = false;
			issues.addError("Certficate Awarded is mandatory");
		}

		return isValid;
	}

	public void clear() {
		clear(false);
	}

	public void clear(boolean clearDto) {
		if (clearDto) {
			educationDto = new ApplicationFormEducationalDto();
			uploader = new Uploader();
		}
		txtInstitution.setValue("");
		txtExaminingBody.setValue("");
		dtStartDate.clear();
		dtDateCompleted.clear();
		txtSectionsPassed.setValue("");
		txtRegistrationNo.setValue("");
		txtClassOrDivision.setValue("");
		lstCertificateAwarded.setValue(null);
		panelPreviousAttachments.clear();
		uploader.clear();
		showUploadPanel(false);
	}

	public ApplicationFormEducationalDto getEducationDto() {
		ApplicationFormEducationalDto dto = new ApplicationFormEducationalDto();
		if (educationDto != null) {
			dto = educationDto;
		}
		dto.setWhereObtained(txtInstitution.getValue());
		dto.setExaminingBody(txtExaminingBody.getValue());
		dto.setFromDate(dtStartDate.getValueDate());
		dto.setToDate(dtDateCompleted.getValueDate());
		dto.setSections(txtSectionsPassed.getValue());
		dto.setRegNo(txtRegistrationNo.getValue());
		dto.setClassDivisionAttained(txtClassOrDivision.getValue());
		dto.setCertificateAwarded(lstCertificateAwarded.getValue());
		return dto;
	}

	public void bindDetail(ApplicationFormEducationalDto educationDto) {
		this.educationDto = educationDto;
		clear();
		txtInstitution.setValue(educationDto.getWhereObtained());
		txtExaminingBody.setValue(educationDto.getExaminingBody());
		dtStartDate.setValue(educationDto.getFromDate());
		dtDateCompleted.setValue(educationDto.getToDate());
		txtSectionsPassed.setValue(educationDto.getSections());
		txtRegistrationNo.setValue(educationDto.getRegNo());
		txtClassOrDivision.setValue(educationDto.getClassDivisionAttained());
		lstCertificateAwarded.setValue(educationDto.getCertificateAwarded());

		if (educationDto.getAttachments() != null) {
			for (final AttachmentDto attachment : educationDto.getAttachments()) {
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
			}
		}
		setUploadContext();
		showUploadPanel(true);
	}

	public void showUploadPanel(boolean showForm) {
		aStartUpload.setVisible(!showForm);
		if (showForm) {
			panelUploader.removeStyleName("hide");
			setUploadContext();
		} else {
			panelUploader.addStyleName("hide");
		}
	}

	private void setUploadContext() {
		UploadContext context = new UploadContext();
		context.setContext("educationRefId", getEducationDto().getRefId());
		context.setAction(UPLOADACTION.UPLOADEDUCATIONATTATCHMENTS);
		context.setAccept(Arrays.asList("doc", "pdf", "jpg", "jpeg", "png",
				"docx"));
		uploader.setContext(context);
		Window.alert(context.getContextValuesAsURLParams());
	}

	public HasClickHandlers getStartUploadButton() {
		return aStartUpload;
	}

}
