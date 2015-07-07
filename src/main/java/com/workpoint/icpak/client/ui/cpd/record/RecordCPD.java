package com.workpoint.icpak.client.ui.cpd.record;

import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.OptionControl;
import com.workpoint.icpak.client.ui.component.DateField;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.ui.upload.custom.Uploader;
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
	IssuesPanel issues;

	@UiField
	TextField txtTitle;

	@UiField
	TextField txtOrganizer;

	@UiField
	DateField txtStartDate;

	@UiField
	DateField txtEndDate;

	@UiField
	Uploader uploader;

	@UiField
	DropDownList<Category> lstCategory;

	public RecordCPD() {
		initWidget(uiBinder.createAndBindUi(this));
		showForm(false);

		lstCategory.setItems(Arrays.asList(new Category("Category A", "A"),
				new Category("Category B", "B"),
				new Category("Category C", "C"),
				new Category("Category D", "D")));
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
		} else if (isNullOrEmpty(txtStartDate.getValue())) {
			isValid = false;
			issues.addError("Organizer is mandatory");
		} else if (isNullOrEmpty(lstCategory.getValue())) {
			isValid = false;
			issues.addError("Organizer is mandatory");
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

	public class Category implements Listable {
		String name;
		String value;

		public Category(String name, String value) {
			this.name = name;
			this.value = value;
		}
		@Override
		public String getName() {
			return value;
		}
		@Override
		public String getDisplayName() {
			return name;
		}
	}

}
