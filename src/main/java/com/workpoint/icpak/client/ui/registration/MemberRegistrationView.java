package com.workpoint.icpak.client.ui.registration;

import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationType;
import com.workpoint.icpak.shared.model.ApplicationCategoryDto;

public class MemberRegistrationView extends ViewImpl implements
		MemberRegistrationPresenter.MyView {

	private final Widget widget;

	@UiField
	Frame framePayment;

	@UiField
	Anchor aNext;
	@UiField
	Anchor aBack;

	@UiField
	Anchor aAccount;

	@UiField
	DivElement divPackage;

	@UiField
	DivElement divCategories;

	@UiField
	DivElement divPayment;
	@UiField
	DivElement divProforma;

	@UiField
	Anchor aAssociate;
	@UiField
	Anchor aOverseas;
	@UiField
	Anchor aNonPractising;

//	@UiField
//	Anchor aPractising;

	@UiField
	DivElement divNonPracticing;

	// @UiField
	// DivElement divPractising;

	@UiField
	DivElement divAssociate;
	@UiField
	DivElement divOverseas;

	@UiField
	LIElement liTab1;
	@UiField
	LIElement liTab2;
	@UiField
	LIElement liTab3;
	@UiField
	LIElement liTab4;

	@UiField
	IssuesPanel issuesPanel;

	@UiField
	TextField txtSurname;
	@UiField
	TextField txtOtherNames;
	@UiField
	TextField txtEmailAddress;
	@UiField
	TextField txtPhone;
	// @UiField DropDownList<ApplicationType> lstMemberCategory;
	@UiField
	TextField txtEmployer;
	@UiField
	TextField txtCity;
	@UiField
	TextField txtAddress;

	@UiField
	IssuesPanel issuesPanelCategory;
	@UiField
	TextField txtPostalCode;

	private String selectedName;

	@UiField
	SpanElement spnNonPracticingFee;
	@UiField
	SpanElement spnNonPracticingSubscription;
	@UiField
	SpanElement spnNonPracticingCondition;
	@UiField
	SpanElement spnPracticingFee;
	@UiField
	SpanElement spnPracticingSubscription;
	@UiField
	SpanElement spnPracticingCondition;
	@UiField
	SpanElement spnOverseasFee;
	@UiField
	SpanElement spnOverseasSubscription;
	@UiField
	SpanElement spnOverseasCondition;
	@UiField
	SpanElement spnAssociateFee;
	@UiField
	SpanElement spnAssociateSubscription;
	@UiField
	SpanElement spnAssociateCondition;

	@UiField
	SpanElement spnSelected;

	@UiField
	SpanElement spnNames;

	private List<LIElement> liElements = new ArrayList<LIElement>();
	private List<PageElement> pageElements = new ArrayList<PageElement>();

	int counter = 2;

	public interface Binder extends UiBinder<Widget, MemberRegistrationView> {
	}

	ApplicationType type = null;

	ClickHandler selectHandler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			Anchor selected = (Anchor) event.getSource();
			selectedName = selected.getName();

			if (selectedName.equals("NonPractising")) {
				removeActiveSelection(selected);
				spnSelected.setInnerText("You have selected: "
						+ "Non Practising Member");
				divNonPracticing.addClassName("active");
				type = ApplicationType.NON_PRACTISING;
			} else if (selectedName.equals("Practising")) {
				spnSelected.setInnerText("You have selected: "
						+ "Practising Member");
				removeActiveSelection(selected);
//				divPractising.addClassName("active");
				type = ApplicationType.PRACTISING;
			} else if (selectedName.equals("Overseas")) {
				spnSelected.setInnerText("You have selected: "
						+ "Overseas Member");
				removeActiveSelection(selected);
				divOverseas.addClassName("active");
				type = ApplicationType.OVERSEAS;
			} else if (selectedName.equals("Associate")) {
				spnSelected.setInnerText("You have selected: "
						+ "Associate Member");
				removeActiveSelection(selected);
				divAssociate.addClassName("active");
				type = ApplicationType.ASSOCIATE;
			}
		}
	};

	private boolean isEmailValid = true;

	@Inject
	public MemberRegistrationView(final Binder binder) {
		widget = binder.createAndBindUi(this);

		String url = "http://197.248.4.221:8080/ewallet/#websiteClient";
		framePayment.setUrl(url);

		// Li Elements
		liElements.add(liTab1);
		liElements.add(liTab2);
		liElements.add(liTab3);
		liElements.add(liTab4);

		aNonPractising.addClickHandler(selectHandler);
//		aPractising.addClickHandler(selectHandler);
		aOverseas.addClickHandler(selectHandler);
		aAssociate.addClickHandler(selectHandler);

		pageElements.add(new PageElement(divPackage, "Proceed"));
		pageElements.add(new PageElement(divCategories, "Submit", "Back"));
		pageElements.add(new PageElement(divProforma, "Proceed to Pay"));
		pageElements.add(new PageElement(divPayment, "Finish", "Back"));

		setActive(liElements.get(counter), pageElements.get(counter));

		aBack.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				counter = counter - 1;
				showMyAccountLink(counter);
				removeActive(liElements.get(counter), pageElements.get(counter));
				setActive(liElements.get(counter), pageElements.get(counter));
			}
		});

		// lstMemberCategory.setItems(types);
	}

	protected void showMyAccountLink(int counter) {
		if (counter == 2) {
			aAccount.removeStyleName("hide");
		} else {
			aAccount.addStyleName("hide");
		}
	}

	protected void removeActiveSelection(Anchor selected) {
		divNonPracticing.removeClassName("active");
		// divPractising.removeClassName("active");
		divOverseas.removeClassName("active");
		divAssociate.removeClassName("active");
	}

	private void removeActive(LIElement liElement, PageElement page) {
		setButtons(page);
		liElement.removeClassName("active");
		page.getElement().removeClassName("active");
	}

	private void setButtons(PageElement page) {
		if (page.getNextText() != null) {
			aNext.getElement().removeClassName("hide");
			aNext.setText(page.getNextText());
		} else {
			aNext.getElement().addClassName("hide");
		}

		if (page.getPreviousText() != null) {
			aBack.getElement().removeClassName("hide");
			aBack.setText(page.getPreviousText());
		} else {
			aBack.getElement().addClassName("hide");
		}
	}

	private void setActive(LIElement liElement, PageElement page) {
		clearAll();
		setButtons(page);
		liElement.addClassName("active");
		page.getElement().addClassName("active");
	}

	public ApplicationFormHeaderDto getApplicationForm() {
		ApplicationFormHeaderDto dto = new ApplicationFormHeaderDto();
		// dto.setRefId(getRefId());
		dto.setSurname(txtSurname.getValue());
		dto.setOtherNames(txtOtherNames.getValue());
		dto.setEmail(txtEmailAddress.getValue());
		dto.setEmployer(txtEmployer.getValue());
		dto.setCity1(txtCity.getValue());
		dto.setAddress1(txtAddress.getValue());
		dto.setPostCode(txtPostalCode.getValue());
		dto.setApplicationType(type);

		return dto;
	}

	private void clearAll() {
		liTab1.removeClassName("active");
		liTab2.removeClassName("active");
		liTab3.removeClassName("active");
		liTab4.removeClassName("active");

		divPackage.removeClassName("active");
		divPayment.removeClassName("active");
		divCategories.removeClassName("active");
		divProforma.removeClassName("active");
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public HasClickHandlers getANext() {

		return aNext;
	}

	@Override
	public HasClickHandlers getABack() {
		return aBack;
	}

	@Override
	public TextField getEmail() {
		return txtEmailAddress;
	}

	public boolean isValid() {
		boolean isValid = true;
		issuesPanel.clear();

		if (counter == 0) {
			if (!isEmailValid) {
				issuesPanel.addError("e-Mail " + txtEmailAddress.getValue()
						+ " is already registered");
			}

			if (isNullOrEmpty(txtSurname.getValue())) {
				isValid = false;
				issuesPanel.addError("Surname is required");
			}

			if (isNullOrEmpty(txtOtherNames.getValue())) {
				isValid = false;
				issuesPanel.addError("Other Names is required");
			}

			if (isNullOrEmpty(txtEmailAddress.getValue())) {
				isValid = false;
				issuesPanel.addError("Email is required");
			}

			if (isNullOrEmpty(txtPhone.getValue())) {
				isValid = false;
				issuesPanel.addError("Phone Number is required");
			}

			if (isNullOrEmpty(txtEmployer.getValue())) {
				isValid = false;
				issuesPanel.addError("Employer is required");
			}

			if (isNullOrEmpty(txtCity.getValue())) {
				isValid = false;
				issuesPanel.addError("City is required");
			}

			if (isNullOrEmpty(txtAddress.getValue())) {
				isValid = false;
				issuesPanel.addError("Address is required");
			}
		} else if (counter == 1) {
			if (type == null) {
				isValid = false;
			}
		}

		if (!isValid && isEmailValid) {
			issuesPanel.removeStyleName("hide");
		} else {
			issuesPanel.addStyleName("hide");
		}

		return isValid && isEmailValid;
	}

	public void setCategories(List<ApplicationCategoryDto> dtos) {
		for (ApplicationCategoryDto dto : dtos) {
			switch (dto.getType()) {
			case NON_PRACTISING:
				spnNonPracticingFee.setInnerText(dto.getApplicationAmount()
						+ "");
				spnNonPracticingSubscription.setInnerText(dto
						.getRenewalAmount() + "");
				spnNonPracticingCondition.setInnerText(dto.getDescription());
				break;
			case PRACTISING:

				spnPracticingFee.setInnerText(dto.getApplicationAmount() + "");
				spnPracticingSubscription.setInnerText(dto.getRenewalAmount()
						+ "");
				spnPracticingCondition.setInnerText(dto.getDescription());
				break;
			case OVERSEAS:

				spnOverseasFee.setInnerText(dto.getApplicationAmount() + "");
				spnOverseasSubscription.setInnerText(dto.getRenewalAmount()
						+ "");
				spnOverseasCondition.setInnerText(dto.getDescription());
				break;
			case ASSOCIATE:

				spnAssociateFee.setInnerText(dto.getApplicationAmount() + "");
				spnAssociateSubscription.setInnerText(dto.getRenewalAmount()
						+ "");
				spnAssociateCondition.setInnerText(dto.getDescription());
				break;
			}
		}
	}

	@Override
	public void setEmailValid(boolean isEmailValid) {
		issuesPanel.clear();
		this.isEmailValid = isEmailValid;
		if (!isEmailValid) {
			issuesPanel.addError("e-Mail " + txtEmailAddress.getValue()
					+ " is already registered");
		}
	}

	@Override
	public void bindForm(ApplicationFormHeaderDto result) {
		spnNames.setInnerText(result.getSurname() + " "
				+ result.getOtherNames());
	}

	@Override
	public void next() {
		counter = counter + 1;
		showMyAccountLink(counter);
		setActive(liElements.get(counter), pageElements.get(counter));
	}

	@Override
	public int getCounter() {
		return counter;
	}

	@Override
	public void setLoadingState(ActionLink anchor, boolean isLoading) {
		anchor.setLoadingState(anchor, isLoading);
	}

	@Override
	public void showError(String error) {
		issuesPanelCategory.clear();
		issuesPanelCategory.addError(error);
	}

}
