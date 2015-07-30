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
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.ui.events.registration.proforma.ProformaInvoice;
import com.workpoint.icpak.client.ui.membership.form.MemberRegistrationForm;
import com.workpoint.icpak.shared.model.ApplicationCategoryDto;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationType;
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
	HTMLPanel divContent;

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
	@UiField
	DivElement divNonPracticing;

	@UiField
	DivElement divContainer;

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
	IssuesPanel issuesPanelCategory;
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
				// divPractising.addClassName("active");
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

			memberRegistrationForm.setType(type);

		}
	};

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

	protected void showMyAccountLink(int counter) {
		if (counter == 2) {
			aAccount.removeStyleName("hide");
		} else {
			aAccount.addStyleName("hide");
		}
	}

	protected void removeActiveSelection(Anchor selected) {
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
	public HasClickHandlers getANext() {
		return aNext;
	}

	@Override
	public void setMiddleHeight() {
		int totalHeight = Window.getClientHeight();
		int topHeight = divHeaderContainer.getOffsetHeight();
		// int footerHeight = divFooter.getOffsetHeight();
		// int topicsHeight = divHeaderContainer.getOffsetHeight();
		int middleHeight = totalHeight - topHeight;

		// Window.alert("\nTotalHeight:" + totalHeight + "MiddleHeight>>"
		// + middleHeight + "TopHeight" + topHeight);

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
		return memberRegistrationForm.isValid();
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
		memberRegistrationForm.setEmailValid(isEmailValid);
	}

	@Override
	public void bindForm(ApplicationFormHeaderDto result) {
		spnNames.setInnerText(result.getSurname() + " "
				+ result.getOtherNames());
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
		issuesPanelCategory.clear();
		issuesPanelCategory.addError(error);
	}

	@Override
	public void bindInvoice(InvoiceDto invoice) {
		proformaInv.clearRows();
		proformaInv.setInvoice(invoice);
	}

	public Anchor getActivateAccLink() {
		return aAccount;
	}

}
