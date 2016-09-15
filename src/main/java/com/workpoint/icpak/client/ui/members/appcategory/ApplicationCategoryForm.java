package com.workpoint.icpak.client.ui.members.appcategory;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.shared.model.ApplicationCategoryDto;
import com.workpoint.icpak.shared.model.ApplicationType;

public class ApplicationCategoryForm extends Composite {

	private static ApplicationCategoryFormUiBinder uiBinder = GWT.create(ApplicationCategoryFormUiBinder.class);

	interface ApplicationCategoryFormUiBinder extends UiBinder<Widget, ApplicationCategoryForm> {
	}

	@UiField
	DropDownList<ApplicationType> lstApplicationCategories;

	public ApplicationCategoryForm() {
		initWidget(uiBinder.createAndBindUi(this));

		lstApplicationCategories.setItems(Arrays.asList(ApplicationType.values()));
	}
	
	public ApplicationCategoryDto getApplicationCategory(){
		return null;
	}
	
	public void setApplicationCategory(){
		
	}

}
