package com.workpoint.icpak.client.ui.cpd.record;

import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.OptionControl;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.DateField;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.ui.upload.custom.Uploader;
import com.workpoint.icpak.shared.model.CPDCategory;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.shared.model.Listable;

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
	DropDownList<CPDCategory> lstCategory;

	public RecordCPD() {
		initWidget(uiBinder.createAndBindUi(this));
		showForm(false);

		List<CPDCategory> categories = new ArrayList<CPDCategory>();
		for (CPDCategory cat : CPDCategory.values()) {
			categories.add(cat);
		}
		lstCategory.setItems(categories);
	}

	public boolean isValid() {
		boolean isValid = true;
		issues.clear();
		if (isNullOrEmpty(txtTitle.getValue())) {
			isValid = false;
			issues.addError("Title is mandatory");
		} else if (isNullOrEmpty(txtOrganizer.getValue())) {
			isValid = false;
			issues.addError("Organizer is mandatory");
		} else if (dtStartDate.getValueDate() == null) {
			isValid = false;
			issues.addError("Start date is mandatory");
		} else if (dtEndDate.getValueDate() == null) {
			isValid = false;
			issues.addError("End date is mandatory");
		} else if (lstCategory.getValue() == null) {
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

}
