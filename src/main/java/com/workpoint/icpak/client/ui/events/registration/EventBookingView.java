package com.workpoint.icpak.client.ui.events.registration;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.ui.registration.PageElement;

public class EventBookingView extends ViewImpl implements
		EventBookingPresenter.MyView {

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
	HTMLPanel panelDelegates;

	@UiField
	DivElement divPayment;
	@UiField
	DivElement divProforma;

	@UiField
	LIElement liTab1;
	@UiField
	LIElement liTab2;
	@UiField
	LIElement liTab3;
	@UiField
	LIElement liTab4;

	private String selectedName;

	@UiField
	SpanElement spnEventName;
	@UiField
	SpanElement spnStartDate;
	@UiField
	SpanElement spnDays2Go;
	@UiField
	SpanElement spnDuration;

	@UiField
	TextField txtCompanyName;
	@UiField
	TextField txtPhone;
	@UiField
	TextField txtAddress;
	@UiField
	TextField txtPostalCode;
	@UiField
	TextField txtCity;
	@UiField
	TextField txtContactPerson;
	@UiField
	TextField txtContactEmail;
	// @UiField
	// DropDownList<> lstCountries;


	private List<LIElement> liElements = new ArrayList<LIElement>();
	private List<PageElement> pageElements = new ArrayList<PageElement>();

	int counter = 0;

	public interface Binder extends UiBinder<Widget, EventBookingView> {
	}

	@Inject
	public EventBookingView(final Binder binder) {
		widget = binder.createAndBindUi(this);

		String url = "http://197.248.2.44:8080/ewallet-beta/#websiteClient";
		framePayment.setUrl(url);

		// Li Elements
		liElements.add(liTab1);
		liElements.add(liTab2);
		liElements.add(liTab3);
		liElements.add(liTab4);


		// Div Elements
		pageElements.add(new PageElement(divPackage, "Proceed"));
		pageElements.add(new PageElement(divCategories, "Submit", "Back"));
		pageElements.add(new PageElement(divProforma, "Proceed to Pay"));
		pageElements.add(new PageElement(divPayment, "Finish", "Back"));

		setActive(liElements.get(counter+1), pageElements.get(counter+1));

		aBack.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				counter = counter - 1;
				showMyAccountLink(counter);
				removeActive(liElements.get(counter), pageElements.get(counter));
				setActive(liElements.get(counter), pageElements.get(counter));
			}
		});

		aNext.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				counter = counter + 1;
				showMyAccountLink(counter);
				setActive(liElements.get(counter), pageElements.get(counter));
			}
		});
	}

	protected void showMyAccountLink(int counter) {
		if (counter == 2) {
			aAccount.removeStyleName("hide");
		} else {
			aAccount.addStyleName("hide");
		}
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
		System.err.println("Added:" + counter);
	}

	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if (slot == EventBookingPresenter.DELEGATE_SLOT) {
			panelDelegates.clear();
			if (content != null) {
				panelDelegates.add(content);
			}
		}
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


}
