package com.workpoint.icpak.client.ui.profile.specialization.form;

import java.util.ArrayList;
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
import com.workpoint.icpak.shared.model.ApplicationFormEmploymentDto;
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

	@UiField
	CheckBox aPractice;
	@UiField
	CheckBox aPublicSector;
	@UiField
	CheckBox aPrivateSector;
	@UiField
	CheckBox aBankingFinance;
	@UiField
	CheckBox aManufacturing2;
	@UiField
	CheckBox aHotel2;
	@UiField
	CheckBox aNonprofitSector;
	@UiField
	CheckBox aEducationTraining;
	@UiField
	CheckBox aCommerceRetail;
	@UiField
	CheckBox aOtherService;

	List<String> allIssues = new ArrayList<>();

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

		aPractice.addValueChangeHandler(checkHandler);
		aPublicSector.addValueChangeHandler(checkHandler);
		aPrivateSector.addValueChangeHandler(checkHandler);
		aBankingFinance.addValueChangeHandler(checkHandler);
		aManufacturing2.addValueChangeHandler(checkHandler);
		aHotel2.addValueChangeHandler(checkHandler);
		aNonprofitSector.addValueChangeHandler(checkHandler);
		aEducationTraining.addValueChangeHandler(checkHandler);
		aCommerceRetail.addValueChangeHandler(checkHandler);

		setEditMode(false);
	}

	protected void addRemove(CheckBox source, boolean value) {
		// Specialization Areas
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

		// /Employment Sector
		else if (source.equals(aPractice)) {
			addRemoveEmployment(Specializations.PRACTICE, value);
		} else if (source.equals(aManufacturing2)) {
			addRemoveEmployment(Specializations.MANUFACTURING, value);
		} else if (source.equals(aPublicSector)) {
			addRemoveEmployment(Specializations.PUBLICSECTOR, value);
		} else if (source.equals(aPrivateSector)) {
			addRemoveEmployment(Specializations.PRIVATESECTOR, value);
		} else if (source.equals(aBankingFinance)) {
			addRemoveEmployment(Specializations.BANKINGANDFINANCE, value);
		} else if (source.equals(aHotel2)) {
			addRemoveEmployment(Specializations.HOTEL, value);
		} else if (source.equals(aNonprofitSector)) {
			addRemoveEmployment(Specializations.NONPROFIT, value);
		} else if (source.equals(aEducationTraining)) {
			addRemoveEmployment(Specializations.EDUCATIONANDTRAINING, value);
		} else if (source.equals(aCommerceRetail)) {
			addRemoveEmployment(Specializations.COMMERCE, value);
		} else if (source.equals(aOtherService)) {
			addRemoveEmployment(Specializations.OTHER, value);
		}

	}

	private void addRemoveEmployment(Specializations specialization,
			boolean isSave) {
		switch (specialization) {
		case PRACTICE:
			AppContext.fireEvent(new EditModelEvent(
					new ApplicationFormEmploymentDto(Specializations.PRACTICE),
					!isSave));
			break;
		case PUBLICSECTOR:
			AppContext.fireEvent(new EditModelEvent(
					new ApplicationFormEmploymentDto(
							Specializations.PUBLICSECTOR), !isSave));
			break;
		case COMMERCE:
			AppContext.fireEvent(new EditModelEvent(
					new ApplicationFormEmploymentDto(Specializations.COMMERCE),
					!isSave));
			break;
		case PRIVATESECTOR:
			AppContext.fireEvent(new EditModelEvent(
					new ApplicationFormEmploymentDto(
							Specializations.PRIVATESECTOR), !isSave));
			break;
		case NONPROFIT:
			AppContext
					.fireEvent(new EditModelEvent(
							new ApplicationFormEmploymentDto(
									Specializations.NONPROFIT), !isSave));
		case BANKINGANDFINANCE:
			AppContext.fireEvent(new EditModelEvent(
					new ApplicationFormEmploymentDto(
							Specializations.BANKINGANDFINANCE), !isSave));
			break;
		case EDUCATIONANDTRAINING:
			AppContext.fireEvent(new EditModelEvent(
					new ApplicationFormEmploymentDto(
							Specializations.EDUCATIONANDTRAINING), !isSave));
			break;
		case MANUFACTURING:
			AppContext.fireEvent(new EditModelEvent(
					new ApplicationFormEmploymentDto(
							Specializations.MANUFACTURING), !isSave));
			break;
		case HOTEL:
			AppContext.fireEvent(new EditModelEvent(
					new ApplicationFormEmploymentDto(Specializations.HOTEL),
					!isSave));
			break;
		case OTHER:
			AppContext.fireEvent(new EditModelEvent(
					new ApplicationFormEmploymentDto(Specializations.OTHER),
					!isSave));
			break;
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
		// Window.alert(">>" + specializationDto.getSpecialization());
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

	public void bindEmploymentDetail(ApplicationFormEmploymentDto employment) {
		switch (employment.getSpecialization()) {
		case PRACTICE:
			aPractice.setValue(true);
			break;
		case PUBLICSECTOR:
			aPublicSector.setValue(true);
			break;
		case COMMERCE:
			aCommerceRetail.setValue(true);
			break;
		case PRIVATESECTOR:
			aPrivateSector.setValue(true);
			break;
		case NONPROFIT:
			aNonprofitSector.setValue(true);
		case BANKINGANDFINANCE:
			aBankingFinance.setValue(true);
			break;
		case EDUCATIONANDTRAINING:
			aEducationTraining.setValue(true);
			break;
		case HOTEL:
			aHotel2.setValue(true);
			break;
		case MANUFACTURING:
			aManufacturing2.setValue(true);
			break;
		case OTHER:
			aOtherService.setValue(true);
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
			// divHeader.removeClassName("hide");
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

			aPractice.setEnabled(true);
			aPublicSector.setEnabled(true);
			aPrivateSector.setEnabled(true);
			aBankingFinance.setEnabled(true);
			aManufacturing2.setEnabled(true);
			aHotel2.setEnabled(true);
			aNonprofitSector.setEnabled(true);
			aEducationTraining.setEnabled(true);
			aCommerceRetail.setEnabled(true);
			aOtherService.setEnabled(true);
		} else {
			// divHeader.addClassName("hide");
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

			aPractice.setEnabled(false);
			aPublicSector.setEnabled(false);
			aPrivateSector.setEnabled(false);
			aBankingFinance.setEnabled(false);
			aManufacturing2.setEnabled(false);
			aHotel2.setEnabled(false);
			aNonprofitSector.setEnabled(false);
			aEducationTraining.setEnabled(false);
			aCommerceRetail.setEnabled(false);
			aOtherService.setEnabled(false);
		}
	}

	public void clear() {
		aAudit.setValue(false);
		aTaxation.setValue(false);
		aFinancial.setValue(false);
		aHRConsultancy.setValue(false);
		aBanking.setValue(false);
		aFinance.setValue(false);
		aInsurance.setValue(false);
		aManufacturing.setValue(false);
		aHotel.setValue(false);
		aOther.setValue(false);
		aCentral.setValue(false);
		aLocal.setValue(false);
		aState.setValue(false);
		aCooperative.setValue(false);
		aEducation.setValue(false);
		aNGOs.setValue(false);

		aPractice.setValue(false);
		aPublicSector.setValue(false);
		aPrivateSector.setValue(false);
		aBankingFinance.setValue(false);
		aManufacturing2.setValue(false);
		aHotel2.setValue(false);
		aNonprofitSector.setValue(false);
		aEducationTraining.setValue(false);
		aCommerceRetail.setValue(false);
		aOtherService.setValue(false);
	}

	public void bindDetails(List<ApplicationFormSpecializationDto> result) {
		// clear();
		allIssues.clear();
		if (result.size() == 0) {
			allIssues
					.add("Please provide at least one area of specialization under the specialization tab");
		}
		for (ApplicationFormSpecializationDto dto : result) {
			bindDetail(dto);
		}
	}

	public void bindEmployment(List<ApplicationFormEmploymentDto> result) {
		// clear();
		allIssues.clear();
		if (result.size() == 0) {
			allIssues
					.add("Please provide your current employment sector under  the specialization tab");
		}
		for (ApplicationFormEmploymentDto dto : result) {
			bindEmploymentDetail(dto);
		}
	}

	public List<String> getAllIssues() {
		return allIssues;
	}
}
