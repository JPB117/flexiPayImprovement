package com.workpoint.icpak.client.ui.profile.accountancy.form;

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
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.ui.upload.custom.Uploader;
import com.workpoint.icpak.shared.model.ApplicationFormAccountancyDto;
import com.workpoint.icpak.shared.model.ApplicationFormTrainingDto;
import com.workpoint.icpak.shared.model.AttachmentDto;

public class AccountancyRegistrationForm extends Composite {

	private static EducationRegistrationFormUiBinder uiBinder = GWT
			.create(EducationRegistrationFormUiBinder.class);

	@UiField
	TextField txtExaminationBody;
	@UiField
	TextField txtRegistrationNo;
	@UiField
	TextField txtSectionPassed;
	@UiField
	DateField dtDatePassed;
	@UiField
	IssuesPanel issues;
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

	private ApplicationFormAccountancyDto accountancyDto;

	interface EducationRegistrationFormUiBinder extends
			UiBinder<Widget, AccountancyRegistrationForm> {
	}

	public AccountancyRegistrationForm() {
		initWidget(uiBinder.createAndBindUi(this));
		showUploadPanel(false);
	}

	public AccountancyRegistrationForm(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public boolean isValid() {
		boolean isValid = true;
		issues.clear();

		if (isNullOrEmpty(txtExaminationBody.getValue())) {
			isValid = false;
			issues.addError("Organization is mandatory");
		}
		if (isNullOrEmpty(txtRegistrationNo.getValue())) {
			isValid = false;
			issues.addError("Position is mandatory");
		}
		if (isNullOrEmpty(txtSectionPassed.getValue())) {
			isValid = false;
			issues.addError("Section Passed is mandatory");
		}
		if (dtDatePassed.getValueDate() == null) {
			isValid = false;
			issues.addError("Date Passed is mandatory");
		}
		if (isValid != true) {
			issues.removeStyleName("hide");
		} else {
			issues.addStyleName("hide");
		}
		return isValid;
	}

	public void clear() {
		clear(false);
	}

	public void clear(boolean clearDto) {
		if (clearDto) {
			accountancyDto = new ApplicationFormAccountancyDto();
		}
		issues.clear();
		txtExaminationBody.setValue("");
		txtRegistrationNo.setValue("");
		txtSectionPassed.setValue("");
		dtDatePassed.setValue(null);
		panelPreviousAttachments.clear();
		showUploadPanel(false);
		uploader.clear();
		uploader.clearImages();
	}

	public ApplicationFormAccountancyDto getAccountancyDto() {
		ApplicationFormAccountancyDto dto = new ApplicationFormAccountancyDto();
		if (accountancyDto != null) {
			dto = accountancyDto;
		}
		dto.setExaminingBody(txtExaminationBody.getText());
		dto.setRegistrationNo(txtRegistrationNo.getText());
		dto.setSectionPassed(txtSectionPassed.getText());
		dto.setDatePassed(dtDatePassed.getValueDate());
		return dto;
	}

	public void bindDetail(ApplicationFormAccountancyDto accountancyDto) {
		this.accountancyDto = accountancyDto;
		clear();
		txtExaminationBody.setValue(accountancyDto.getExaminingBody());
		txtRegistrationNo.setValue(accountancyDto.getRegistrationNo());
		txtSectionPassed.setValue(accountancyDto.getSectionPassed());
		dtDatePassed.setValue(accountancyDto.getDatePassed());
		if (accountancyDto.getAttachments() != null) {
			for (final AttachmentDto attachment : accountancyDto
					.getAttachments()) {
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
		uploader.clear();
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
		context.setContext("accountancyRefId", getAccountancyDto().getRefId());
		context.setAction(UPLOADACTION.UPLOADTRAININGATTATCHMENTS);
		context.setAccept(Arrays.asList("doc", "pdf", "jpg", "jpeg", "png",
				"docx"));
		uploader.setContext(context);
	}

	public HasClickHandlers getStartUploadButton() {
		return aStartUpload;
	}
}
