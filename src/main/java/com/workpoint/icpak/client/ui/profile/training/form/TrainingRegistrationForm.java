package com.workpoint.icpak.client.ui.profile.training.form;

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
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.model.UploadContext;
import com.workpoint.icpak.client.model.UploadContext.UPLOADACTION;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.DateField;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.ui.upload.custom.Uploader;
import com.workpoint.icpak.shared.model.ApplicationFormTrainingDto;
import com.workpoint.icpak.shared.model.AttachmentDto;
import com.workpoint.icpak.shared.model.TrainingType;

public class TrainingRegistrationForm extends Composite {

	private static EducationRegistrationFormUiBinder uiBinder = GWT
			.create(EducationRegistrationFormUiBinder.class);

	@UiField
	TextField txtOrganization;
	@UiField
	TextField txtPosition;
	@UiField
	TextField txtTaskNature;
	@UiField
	DateField dtStartDate;
	@UiField
	DateField dtDateCompleted;
	@UiField
	TextArea txtResponsibility;
	@UiField
	DropDownList<TrainingType> lstTrainingType;
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

	private ApplicationFormTrainingDto trainingDto;

	interface EducationRegistrationFormUiBinder extends
			UiBinder<Widget, TrainingRegistrationForm> {
	}

	public TrainingRegistrationForm() {
		initWidget(uiBinder.createAndBindUi(this));
		lstTrainingType.setItems(Arrays.asList(TrainingType.values()));
		showUploadPanel(false);
	}

	public TrainingRegistrationForm(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public boolean isValid() {
		boolean isValid = true;
		issues.clear();

		if (isNullOrEmpty(txtOrganization.getValue())) {
			isValid = false;
			issues.addError("Organization is mandatory");
		}
		if (isNullOrEmpty(txtPosition.getValue())) {
			isValid = false;
			issues.addError("Position is mandatory");
		}
		if (dtStartDate.getValueDate() == null) {
			isValid = false;
			issues.addError("Start date is mandatory");
		}
		if (dtDateCompleted.getValueDate() == null) {
			isValid = false;
			issues.addError("Date Completed is mandatory");
		}
		if (isNullOrEmpty(txtTaskNature.getValue())) {
			isValid = false;
			issues.addError("Task Nature is mandatory");
		}
		if (isValid != true) {
			issues.removeStyleName("hide");
		} else {
			issues.addStyleName("hide");
		}
		return isValid;
	}

	public void clear() {
		issues.clear();
		txtOrganization.setValue("");
		txtPosition.setValue("");
		dtStartDate.clear();
		dtDateCompleted.clear();
		txtTaskNature.setValue("");
		txtResponsibility.setValue("");
		dtDatePassed.setValue(null);
		panelPreviousAttachments.clear();
		showUploadPanel(false);
		uploader.clear();
	}

	public ApplicationFormTrainingDto getTrainingDto() {
		ApplicationFormTrainingDto dto = new ApplicationFormTrainingDto();
		if (trainingDto != null) {
			dto = trainingDto;
		}
		dto.setPosition(txtPosition.getText());
		dto.setOrganisationName(txtOrganization.getText());
		dto.setFromDate(dtStartDate.getValueDate());
		dto.setToDate(dtDateCompleted.getValueDate());
		dto.setTaskNature(txtTaskNature.getText());
		dto.setResponsibility(txtResponsibility.getText());
		dto.setTrainingType(lstTrainingType.getValue());
		dto.setDatePassed(dtDatePassed.getValueDate());
		return dto;
	}

	public void bindDetail(ApplicationFormTrainingDto trainingDto) {
		this.trainingDto = trainingDto;
		clear();
		txtOrganization.setValue(trainingDto.getPosition());
		txtPosition.setValue(trainingDto.getPosition());
		dtStartDate.setValue(trainingDto.getFromDate());
		dtDateCompleted.setValue(trainingDto.getToDate());
		txtTaskNature.setValue(trainingDto.getTaskNature());
		txtResponsibility.setValue(trainingDto.getResponsibility());
		lstTrainingType.setValue(trainingDto.getTrainingType());
		dtDatePassed.setValue(trainingDto.getDatePassed());

		if (trainingDto.getAttachments() != null) {
			for (final AttachmentDto attachment : trainingDto.getAttachments()) {
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
		context.setContext("trainingRefId", getTrainingDto().getRefId());
		context.setAction(UPLOADACTION.UPLOADTRAININGATTATCHMENTS);
		context.setAccept(Arrays.asList("doc", "pdf", "jpg", "jpeg", "png",
				"docx"));
		uploader.setContext(context);
	}

	public HasClickHandlers getStartUploadButton() {
		return aStartUpload;
	}
}
