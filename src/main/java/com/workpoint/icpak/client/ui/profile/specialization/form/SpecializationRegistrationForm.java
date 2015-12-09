package com.workpoint.icpak.client.ui.profile.specialization.form;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
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

	@UiField
	CheckBox aHRConsultancy;
	@UiField
	CheckBox aBanking;
	@UiField
	CheckBox aFinance;
	@UiField
	CheckBox aInsurance;
	@UiField
	CheckBox aManufacturing;
	@UiField
	CheckBox aHotel;
	@UiField
	CheckBox aOther;
	@UiField
	CheckBox aCentral;
	@UiField
	CheckBox aLocal;
	@UiField
	CheckBox aState;
	@UiField
	CheckBox aCooperative;
	@UiField
	CheckBox aEducation;
	@UiField
	CheckBox aNGOs;

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
		aHRConsultancy.addValueChangeHandler(checkHandler);
		aBanking.addValueChangeHandler(checkHandler);
		aFinance.addValueChangeHandler(checkHandler);
		aInsurance.addValueChangeHandler(checkHandler);
		aManufacturing.addValueChangeHandler(checkHandler);
		aHotel.addValueChangeHandler(checkHandler);
		aOther.addValueChangeHandler(checkHandler);
		aCentral.addValueChangeHandler(checkHandler);
		aLocal.addValueChangeHandler(checkHandler);
		aState.addValueChangeHandler(checkHandler);
		aCooperative.addValueChangeHandler(checkHandler);
		aEducation.addValueChangeHandler(checkHandler);
		aNGOs.addValueChangeHandler(checkHandler);

		setEditMode(false);
	}

	protected void addRemove(CheckBox source, boolean value) {
		if (source.equals(aAudit)) {
			addRemove(Specializations.AUDIT, value);
		} else if (source.equals(aTaxation)) {
			addRemove(Specializations.TAXATION, value);
		} else if (source.equals(aFinancial)) {
			addRemove(Specializations.FINANCIAL, value);
		} else if (source.equals(aHRConsultancy)) {
			addRemove(Specializations.HRCONSULTANCY, value);
		} else if (source.equals(aBanking)) {
			addRemove(Specializations.BANKING, value);
		} else if (source.equals(aFinance)) {
			addRemove(Specializations.FINANCE, value);
		} else if (source.equals(aInsurance)) {
			addRemove(Specializations.INSURANCE, value);
		} else if (source.equals(aManufacturing)) {
			addRemove(Specializations.MANUFACTURING, value);
		} else if (source.equals(aHotel)) {
			addRemove(Specializations.HOTEL, value);
		} else if (source.equals(aOther)) {
			addRemove(Specializations.OTHER, value);
		} else if (source.equals(aOther)) {
			addRemove(Specializations.CENTRALGVT, value);
		} else if (source.equals(aLocal)) {
			addRemove(Specializations.LOCALGVT, value);
		} else if (source.equals(aState)) {
			addRemove(Specializations.STATECOOP, value);
		} else if (source.equals(aCooperative)) {
			addRemove(Specializations.COOP, value);
		} else if (source.equals(aEducation)) {
			addRemove(Specializations.EDUCATION, value);
		} else if (source.equals(aNGOs)) {
			addRemove(Specializations.NGO, value);
		}
	}

	private void addRemove(Specializations specialization, boolean isSave) {
		switch (specialization) {
		case AUDIT:
			AppContext
					.fireEvent(new EditModelEvent(
							new ApplicationFormSpecializationDto(
									Specializations.AUDIT), !isSave));
			break;
		case FINANCIAL:
			AppContext.fireEvent(new EditModelEvent(
					new ApplicationFormSpecializationDto(
							Specializations.FINANCIAL), !isSave));
			break;
		case TAXATION:
			AppContext.fireEvent(new EditModelEvent(
					new ApplicationFormSpecializationDto(
							Specializations.TAXATION), !isSave));
			break;
		case HRCONSULTANCY:
			AppContext.fireEvent(new EditModelEvent(
					new ApplicationFormSpecializationDto(
							Specializations.HRCONSULTANCY), !isSave));
			break;
		case BANKING:
			AppContext.fireEvent(new EditModelEvent(
					new ApplicationFormSpecializationDto(
							Specializations.BANKING), !isSave));
			break;
		case FINANCE:
			AppContext.fireEvent(new EditModelEvent(
					new ApplicationFormSpecializationDto(
							Specializations.FINANCE), !isSave));
			break;
		case INSURANCE:
			AppContext.fireEvent(new EditModelEvent(
					new ApplicationFormSpecializationDto(
							Specializations.INSURANCE), !isSave));
			break;
		case MANUFACTURING:
			AppContext.fireEvent(new EditModelEvent(
					new ApplicationFormSpecializationDto(
							Specializations.MANUFACTURING), !isSave));
			break;
		case HOTEL:
			AppContext
					.fireEvent(new EditModelEvent(
							new ApplicationFormSpecializationDto(
									Specializations.HOTEL), !isSave));
			break;
		case OTHER:
			AppContext
					.fireEvent(new EditModelEvent(
							new ApplicationFormSpecializationDto(
									Specializations.OTHER), !isSave));
			break;

		case CENTRALGVT:
			AppContext.fireEvent(new EditModelEvent(
					new ApplicationFormSpecializationDto(
							Specializations.CENTRALGVT), !isSave));
			break;
		case LOCALGVT:
			AppContext.fireEvent(new EditModelEvent(
					new ApplicationFormSpecializationDto(
							Specializations.LOCALGVT), !isSave));
			break;

		case STATECOOP:
			AppContext.fireEvent(new EditModelEvent(
					new ApplicationFormSpecializationDto(
							Specializations.STATECOOP), !isSave));
			break;

		case COOP:
			AppContext.fireEvent(new EditModelEvent(
					new ApplicationFormSpecializationDto(Specializations.COOP),
					!isSave));
			break;

		case EDUCATION:
			AppContext.fireEvent(new EditModelEvent(
					new ApplicationFormSpecializationDto(
							Specializations.EDUCATION), !isSave));
			break;

		case NGO:
			AppContext.fireEvent(new EditModelEvent(
					new ApplicationFormSpecializationDto(Specializations.NGO),
					!isSave));
			break;
		}
	}

	public void bindDetail(ApplicationFormSpecializationDto specializationDto) {
		switch (specializationDto.getSpecialization()) {
		case AUDIT:
			aAudit.setValue(true);
			break;
		case TAXATION:
			aTaxation.setValue(true);
			break;
		case FINANCIAL:
			aFinancial.setValue(true);
			break;
		case HRCONSULTANCY:
			aHRConsultancy.setValue(true);
			break;
		case BANKING:
			aBanking.setValue(true);
			break;
		case FINANCE:
			aFinance.setValue(true);
			break;
		case INSURANCE:
			aInsurance.setValue(true);
			break;
		case MANUFACTURING:
			aManufacturing.setValue(true);
			break;
		case HOTEL:
			aHotel.setValue(true);
			break;
		case OTHER:
			aOther.setValue(true);
			break;
		case CENTRALGVT:
			aCentral.setValue(true);
			break;
		case LOCALGVT:
			aLocal.setValue(true);
			break;
		case STATECOOP:
			aState.setValue(true);
			break;
		case COOP:
			aCooperative.setValue(true);
			break;
		case EDUCATION:
			aEducation.setValue(true);
			break;
		case NGO:
			aNGOs.setValue(true);
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
			aAudit.setEnabled(true);
			aTaxation.setEnabled(true);
			aFinancial.setEnabled(true);
			aHRConsultancy.setEnabled(true);
			aBanking.setEnabled(true);
			aFinance.setEnabled(true);
			aInsurance.setEnabled(true);
			aManufacturing.setEnabled(true);
			aHotel.setEnabled(true);
			aOther.setEnabled(true);
			aCentral.setEnabled(true);
			aLocal.setEnabled(true);
			aState.setEnabled(true);
			aCooperative.setEnabled(true);
			aEducation.setEnabled(true);
			aNGOs.setEnabled(true);
		} else {
			divHeader.addClassName("hide");
			aAudit.setEnabled(false);
			aTaxation.setEnabled(false);
			aFinancial.setEnabled(false);
			aHRConsultancy.setEnabled(false);
			aBanking.setEnabled(false);
			aFinance.setEnabled(false);
			aInsurance.setEnabled(false);
			aManufacturing.setEnabled(false);
			aHotel.setEnabled(false);
			aOther.setEnabled(false);
			aCentral.setEnabled(false);
			aLocal.setEnabled(false);
			aState.setEnabled(false);
			aCooperative.setEnabled(false);
			aEducation.setEnabled(false);
			aNGOs.setEnabled(false);
		}
	}

	public void clear() {
		aAudit.setValue(false);
		aFinancial.setValue(false);
		aTaxation.setValue(false);
	}

	public void bindDetails(List<ApplicationFormSpecializationDto> result) {
		clear();
		for (ApplicationFormSpecializationDto dto : result) {
			bindDetail(dto);
		}
	}

}
