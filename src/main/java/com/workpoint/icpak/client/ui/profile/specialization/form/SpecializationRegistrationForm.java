package com.workpoint.icpak.client.ui.profile.specialization.form;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.client.ui.events.EditModelEvent;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.ApplicationFormSpecializationDto;
import com.workpoint.icpak.shared.model.Specializations;

public class SpecializationRegistrationForm extends Composite {

	private static EducationRegistrationFormUiBinder uiBinder = GWT
			.create(EducationRegistrationFormUiBinder.class);

	@UiField
	IssuesPanel issues;

	@UiField
	DivElement divHeader;

	@UiField
	CheckBox aAudit;
	@UiField
	CheckBox aTaxation;
	@UiField
	CheckBox aFinancial;
	
	interface EducationRegistrationFormUiBinder extends
			UiBinder<Widget, SpecializationRegistrationForm> {
	}

	ValueChangeHandler<Boolean> checkHandler = new ValueChangeHandler<Boolean>() {
		@Override
		public void onValueChange(ValueChangeEvent<Boolean> event) {
			boolean value = event.getValue();
			addRemove((CheckBox) event.getSource(), value);
		}
	};

	public SpecializationRegistrationForm() {
		initWidget(uiBinder.createAndBindUi(this));
		aAudit.addValueChangeHandler(checkHandler);
		aTaxation.addValueChangeHandler(checkHandler);
		aFinancial.addValueChangeHandler(checkHandler);
		setEditMode(false);
	}

	protected void addRemove(CheckBox source, boolean value) {
		if (source.equals(aAudit)) {
			addRemove(Specializations.AUDIT, value);
		}else if(source.equals(aTaxation)){
			addRemove(Specializations.TAXATION, value);
		}else if(source.equals(aFinancial)){
			addRemove(Specializations.FINANCIAL, value);
		}
	}

	private void addRemove(Specializations specialization, boolean isSave) {
		
		switch (specialization) {
		case AUDIT:
			AppContext.fireEvent(new EditModelEvent(new ApplicationFormSpecializationDto(Specializations.AUDIT),!isSave));
			break;
		case FINANCIAL:
			AppContext.fireEvent(new EditModelEvent(new ApplicationFormSpecializationDto(Specializations.FINANCIAL), !isSave));
			break;
		case TAXATION:
			AppContext.fireEvent(new EditModelEvent(new ApplicationFormSpecializationDto(Specializations.TAXATION),!isSave));
			break;
		}
	}
	
	public void bindDetail(ApplicationFormSpecializationDto specializationDto) {
		switch (specializationDto.getSpecialization()) {
		case AUDIT:
			aAudit.setValue(true);
			break;
		case FINANCIAL:
			aFinancial.setValue(true);
			break;
		case TAXATION:
			aTaxation.setValue(true);
			break;
		}
	}


	public SpecializationRegistrationForm(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public boolean isValid() {
		boolean isValid = true;
		issues.clear();
		return isValid;
	}

	public void setEditMode(boolean editMode) {
		if (editMode) {
			divHeader.removeClassName("hide");
		} else {
			divHeader.addClassName("hide");
		}
	}

	public void clear() {
		aAudit.setValue(false);
		aFinancial.setValue(false);
		aTaxation.setValue(false);
	}

	public void bindDetails(List<ApplicationFormSpecializationDto> result) {
		clear();
		for(ApplicationFormSpecializationDto dto: result){
			bindDetail(dto);
		}
	}

}
