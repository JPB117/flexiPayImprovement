package com.workpoint.icpak.client.ui.membership;

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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.ui.events.registration.proforma.ProformaInvoice;
import com.workpoint.icpak.client.ui.membership.form.MemberRegistrationForm;
import com.workpoint.icpak.client.ui.util.NumberUtils;
import com.workpoint.icpak.shared.model.ApplicationCategoryDto;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationType;
import com.workpoint.icpak.shared.model.Country;
import com.workpoint.icpak.shared.model.InvoiceDto;

public class MemberRegistrationView extends ViewImpl implements
		MemberRegistrationPresenter.MyView {

	private final Widget widget;

	@UiField
	HTMLPanel divMainContainer;

	@UiField
	HTMLPanel divHeaderContainer;

	@UiField
	HTMLPanel divFooter;

	@UiField
	MemberRegistrationForm memberRegistrationForm;

	@UiField
	DivElement divPasswordConfiguration;

	@UiField
	HTMLPanel divContent;

	@UiField
	HTMLPanel panelPayment;

	@UiField
	ActionLink aNext;
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
	@UiField
	DivElement divNonPracticing;

	@UiField
	DivElement divContainer;

	@UiField
	DivElement divAssociate;
	@UiField
	DivElement divOverseas;
	@UiField
	IssuesPanel issuesPanel;

	@UiField
	LIElement liTab1;
	@UiField
	LIElement liTab2;
	@UiField
	LIElement liTab3;
	@UiField
	LIElement liTab4;

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
	ProformaInvoice proformaInv;

	// List of Items
	private List<LIElement> liElements = new ArrayList<LIElement>();
	private List<PageElement> pageElements = new ArrayList<PageElement>();
	private String selectedName;

	int counter = 0;

	public interface Binder extends UiBinder<Widget, MemberRegistrationView> {
	}

	ApplicationType type = null;

	ClickHandler selectHandler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			Anchor selected = (Anchor) event.getSource();
			selectedName = selected.getName();
			selectCategory(selected);
		}
	};

	@Inject
	public MemberRegistrationView(final Binder binder) {
		widget = binder.createAndBindUi(this);

		// Li Elements
		liElements.add(liTab1);
		liElements.add(liTab2);
		liElements.add(liTab3);
		liElements.add(liTab4);

		aNonPractising.addClickHandler(selectHandler);
		// aPractising.addClickHandler(selectHandler);
		aOverseas.addClickHandler(selectHandler);
		aAssociate.addClickHandler(selectHandler);

		pageElements.add(new PageElement(divPackage, "Proceed", liTab1));
		pageElements.add(new PageElement(divCategories, "Submit", "Back",
				liTab2));
		pageElements
				.add(new PageElement(divProforma, "Proceed to Pay", liTab3));
		pageElements.add(new PageElement(divPayment, "Finish", "Back", liTab4));

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

		memberRegistrationForm.setCounter(counter);
	}

	protected void selectCategory(Anchor selected) {
		if (selectedName.equals("NonPractising")) {
			removeActiveSelection();
			selectCategory(ApplicationType.NON_PRACTISING);
		} else if (selectedName.equals("Practising")) {
			removeActiveSelection();
			selectCategory(ApplicationType.PRACTISING);
		} else if (selectedName.equals("Overseas")) {
			removeActiveSelection();
			selectCategory(ApplicationType.OVERSEAS);
		} else if (selectedName.equals("Associate")) {
			removeActiveSelection();
			selectCategory(ApplicationType.ASSOCIATE);
		}

		memberRegistrationForm.setType(type);
	}

	private void selectCategory(ApplicationType type2) {
		type = type2;
		switch (type2) {
		case NON_PRACTISING:
			spnSelected.setInnerText("You have selected: "
					+ "Non Practising Member");
			divNonPracticing.addClassName("active");
			break;

		case PRACTISING:
			// Deactivated
			spnSelected.setInnerText("You have selected: "
					+ "Practising Member");
			// spnSelected.addClassName("active");
			break;

		case OVERSEAS:
			spnSelected.setInnerText("You have selected: " + "Overseas Member");
			divOverseas.addClassName("active");
			break;

		case ASSOCIATE:
			spnSelected
					.setInnerText("You have selected: " + "Associate Member");
			divAssociate.addClassName("active");
			break;
		default:
			break;
		}
	}

	protected void showMyAccountLink(int counter) {
		if (counter == 2) {
			aAccount.removeStyleName("hide");
		} else {
			aAccount.addStyleName("hide");
		}
	}

	protected void removeActiveSelection() {
		divNonPracticing.removeClassName("active");
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

		return memberRegistrationForm.getApplicationForm();
	}

	private void clearAll() {
		for (PageElement pageElement : pageElements) {
			if (pageElement.isComplete()) {
				pageElement.getLiElement().getFirstChildElement()
						.getFirstChildElement().removeClassName("hide");
			} else {
				pageElement.getLiElement().removeClassName("active");
			}
		}

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
	public ActionLink getANext() {
		return aNext;
	}

	@Override
	public void setMiddleHeight() {
		int totalHeight = Window.getClientHeight();
		int topHeight = divHeaderContainer.getOffsetHeight();
		int middleHeight = totalHeight - topHeight;
		if (middleHeight > 0) {
			divContent.setHeight(middleHeight + "px");
		}
	}

	@Override
	public HasClickHandlers getABack() {
		return aBack;
	}

	@Override
	public TextField getEmail() {
		return memberRegistrationForm.getEmail();
	}

	@Override
	public boolean isValid() {
		boolean isValid = true;
		issuesPanel.clear();
		if (counter == 0) {
			isValid = memberRegistrationForm.isValid();
		} else if (counter == 1) {
			if (type == null) {
				issuesPanel
						.addError("Kindly select your category before Submitting..");
				isValid = false;
			}
		}

		return isValid;
	}

	public void setCategories(List<ApplicationCategoryDto> dtos) {
		for (ApplicationCategoryDto dto : dtos) {
			switch (dto.getType()) {
			case NON_PRACTISING:
				spnNonPracticingFee.setInnerText(NumberUtils.CURRENCYFORMAT
						.format(dto.getApplicationAmount()) + "");
				spnNonPracticingSubscription
						.setInnerText(NumberUtils.CURRENCYFORMAT.format(dto
								.getRenewalAmount()) + "");
				spnNonPracticingCondition.setInnerText(dto.getDescription());
				break;
			case OVERSEAS:
				spnOverseasFee.setInnerText(NumberUtils.CURRENCYFORMAT
						.format(dto.getApplicationAmount()) + "");
				spnOverseasSubscription.setInnerText(NumberUtils.CURRENCYFORMAT
						.format(dto.getRenewalAmount()) + "");
				spnOverseasCondition.setInnerText(dto.getDescription());
				break;
			case ASSOCIATE:
				spnAssociateFee.setInnerText(NumberUtils.CURRENCYFORMAT
						.format(dto.getApplicationAmount()) + "");
				spnAssociateSubscription
						.setInnerText(NumberUtils.CURRENCYFORMAT.format(dto
								.getRenewalAmount()) + "");
				spnAssociateCondition.setInnerText(dto.getDescription());
				break;
			}
		}
	}

	@Override
	public void bindTransaction(InvoiceDto invoice) {
	}

	@Override
	public void setEmailValid(boolean isEmailValid) {
		memberRegistrationForm.setEmailValid(isEmailValid);
	}

	@Override
	public void bindForm(ApplicationFormHeaderDto result) {
		// spnNames.setInnerText(result.getSurname() + " "
		// + result.getOtherNames());
		memberRegistrationForm.bind(result);
		selectCategory(result.getApplicationType());
	}

	@Override
	public void next() {
		pageElements.get(counter).setCompletion(true);
		counter = counter + 1;
		showMyAccountLink(counter);
		setActive(liElements.get(counter), pageElements.get(counter));
	}

	@Override
	public int getCounter() {
		return counter;
	}

	@Override
	public void showmask(boolean processing) {
		if (processing) {
			divContainer.addClassName("whirl traditional");
		} else {
			divContainer.removeClassName("whirl traditional");
		}
	}

	@Override
	public void setLoadingState(ActionLink anchor, boolean isLoading) {
		anchor.setLoadingState(anchor, isLoading);
	}

	@Override
	public void showError(String error) {
		issuesPanel.clear();
		issuesPanel.addError(error);
		issuesPanel.removeStyleName("hide");
	}

	@Override
	public void bindInvoice(InvoiceDto invoice) {
		proformaInv.clearRows();
		proformaInv.setInvoice(invoice);
	}

	public Anchor getActivateAccLink() {
		return aAccount;
	}

	@Override
	public void setCounter(int counter) {
		this.counter = counter;

		for (int i = 0; i < counter; i++) {
			setActive(liElements.get(i), pageElements.get(i));
			pageElements.get(i).setCompletion(true);
		}
		setActive(liElements.get(counter), pageElements.get(counter));

	}

	@Override
	public void setCountries(List<Country> countries) {
		memberRegistrationForm.setCountries(countries);
	}

	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if (slot == MemberRegistrationPresenter.PAYMENTS_SLOT) {
			panelPayment.clear();
			if (content != null) {
				panelPayment.add(content);
			}
			super.setInSlot(slot, content);
		}
	}

	@Override
	public MemberRegistrationForm getRegistrationForm() {
		return memberRegistrationForm;
	}
}