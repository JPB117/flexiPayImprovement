package com.workpoint.icpak.client.ui.members.appcategory;

import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.shared.model.ApplicationCategoryDto;
import com.workpoint.icpak.shared.model.ApplicationType;

public class ApplicationCategoryForm extends Composite {

	private static ApplicationCategoryFormUiBinder uiBinder = GWT.create(ApplicationCategoryFormUiBinder.class);

	interface ApplicationCategoryFormUiBinder extends UiBinder<Widget, ApplicationCategoryForm> {
	}

	@UiField
	DropDownList<ApplicationType> lstApplicationCategories;
	@UiField
	TextField txtApplicationAmount;
	@UiField
	TextField txtRenewalAmount;
	@UiField
	TextArea txtDescription;
	@UiField
	IssuesPanel issuesPanel;

	ApplicationCategoryDto appCategory;

	public ApplicationCategoryForm() {
		initWidget(uiBinder.createAndBindUi(this));
		lstApplicationCategories.setItems(Arrays.asList(ApplicationType.values()));
	}

	public boolean isValid() {
		boolean isValid = true;
		issuesPanel.clear();

		if (isNullOrEmpty(lstApplicationCategories.getValue())) {
			isValid = false;
			issuesPanel.addError("Select an Application Category");
		}
		if (isNullOrEmpty(txtApplicationAmount.getValue())) {
			isValid = false;
			issuesPanel.addError("Application Amount is required");
		}
		if (isNullOrEmpty(txtRenewalAmount.getValue())) {
			isValid = false;
			issuesPanel.addError("Renewal Amount is required");
		}
		if (isNullOrEmpty(txtDescription.getValue())) {
			isValid = false;
			issuesPanel.addError("Description is required");
		}
		return isValid;
	}

	public ApplicationCategoryDto getApplicationCategory() {
		if (appCategory == null) {
			appCategory = new ApplicationCategoryDto();
		}
		if (isValid()) {
			appCategory.setType(lstApplicationCategories.getValue());
			appCategory.setApplicationAmount(Double.valueOf(txtApplicationAmount.getValue()));
			appCategory.setRenewalAmount(Double.valueOf(txtRenewalAmount.getValue()));
			appCategory.setDescription(txtDescription.getValue());
			return appCategory;
		} else {
			return null;
		}
	}

	public void setApplicationCategory(ApplicationCategoryDto appCategory) {
		this.appCategory = appCategory;
		if (appCategory != null) {
			lstApplicationCategories.setValue(appCategory.getType());
			txtApplicationAmount.setValue(String.valueOf(appCategory.getApplicationAmount()));
			txtRenewalAmount.setValue(String.valueOf(appCategory.getRenewalAmount()));
			txtDescription.setValue(appCategory.getDescription());
		}
	}

	public void clear() {
		lstApplicationCategories.setValue(null);
		txtApplicationAmount.setValue("");
		txtRenewalAmount.setValue("");
		txtDescription.setValue("");
	}

}
