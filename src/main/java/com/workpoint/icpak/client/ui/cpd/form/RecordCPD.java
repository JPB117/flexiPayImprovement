package com.workpoint.icpak.client.ui.cpd.form;

import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
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
import com.workpoint.icpak.shared.model.CPDCategory;
import com.workpoint.icpak.shared.model.CPDDto;

public class RecordCPD extends Composite {

	private static RecordCPDUiBinder uiBinder = GWT
			.create(RecordCPDUiBinder.class);

	interface RecordCPDUiBinder extends UiBinder<Widget, RecordCPD> {
	}

	@UiField
	HTMLPanel panelForm;

	@UiField
	HTMLPanel panelCategories;

	@UiField
	ActionLink aPreviousForm;

	@UiField
	IssuesPanel issues;

	@UiField
	TextField txtTitle;

	@UiField
	TextField txtOrganizer;

	@UiField
	DateField dtStartDate;

	@UiField
	DateField dtEndDate;

	@UiField
	Uploader uploader;

	@UiField
	ActionLink aStartUpload;

	@UiField
	HTMLPanel panelUploader;

	@UiField
	DropDownList<CPDCategory> lstCategory;

	private CPDDto dto;

	public RecordCPD() {
		initWidget(uiBinder.createAndBindUi(this));
		showForm(false);

		List<CPDCategory> categories = new ArrayList<CPDCategory>();
		for (CPDCategory cat : CPDCategory.values()) {
			categories.add(cat);
		}
		lstCategory.setItems(categories);
		//uploader.setAutoSubmit(false);
	}

	public void showUploadPanel(boolean showForm) {
		aStartUpload.setVisible(showForm);
		if (showForm) {
			panelUploader.removeStyleName("hide");
			setUploadContext();
		} else {
			panelUploader.addStyleName("hide");

		}
	}

	private void setUploadContext() {
		UploadContext context = new UploadContext();
		context.setContext("cpdRefId", getCPD().getRefId());
		context.setAction(UPLOADACTION.UPLOADCPD);
		context.setAccept(Arrays.asList("doc","pdf","jpg","jpeg","png","docx"));
		uploader.setContext(context);	
	}

	public boolean isValid() {
		boolean isValid = true;
		issues.clear();
		if (isNullOrEmpty(txtTitle.getValue())) {
			isValid = false;
			issues.addError("Title is mandatory");
		}
		if (isNullOrEmpty(txtOrganizer.getValue())) {
			isValid = false;
			issues.addError("Organizer is mandatory");
		}
		if (dtStartDate.getValueDate() == null) {
			isValid = false;
			issues.addError("Start date is mandatory");
		}
		if (dtEndDate.getValueDate() == null) {
			isValid = false;
			issues.addError("End date is mandatory");
		}
		if (lstCategory.getValue() == null) {
			isValid = false;
			issues.addError("Category is mandatory");
		}
		return isValid;
	}

	public void showForm(boolean show) {
		if (show) {
			panelForm.setVisible(true);
			panelCategories.setVisible(false);
		} else {
			panelForm.setVisible(false);
			panelCategories.setVisible(true);
		}
	}

	public CPDDto getCPD() {

		CPDDto dto = new CPDDto();
		if (this.dto != null) {
			dto = this.dto;
		}

		dto.setCategory(lstCategory.getValue());
		// dto.setCpdHours();
		dto.setEndDate(dtEndDate.getValueDate());
		// dto.setMemberId(memberId);
		dto.setOrganizer(txtOrganizer.getValue());
		dto.setStartDate(dtStartDate.getValueDate());
		// dto.setStatus();
		dto.setTitle(txtTitle.getValue());

		return dto;
	}

	public void setCPD(CPDDto dto) {
		this.dto = dto;
		if (dto == null) {
			return;
		}

		lstCategory.setValue(dto.getCategory());
		dtEndDate.setValue(dto.getEndDate());
		dtStartDate.setValue(dto.getStartDate());
		txtTitle.setValue(dto.getTitle());
		txtOrganizer.setValue(dto.getOrganizer());
	}

	public HasClickHandlers getStartUploadButton() {
		return aStartUpload;
	}

}
