package com.workpoint.icpak.client.ui.registration;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.LIElement;
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
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationType;

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
	DivElement divPackage;

	@UiField
	DivElement divCategories;

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

	// @UiField
	// DropDownList<> lstMemberCategory;

	@UiField TextField txtSurname;
	@UiField TextField txtOtherNames;
	@UiField TextField txtEmailAddress;
	//@UiField DropDownList<ApplicationType> lstMemberCategory;
	@UiField TextField txtEmployer;
	@UiField TextField txtCity;
	@UiField TextField txtAddress;
	@UiField TextField txtPostalCode;

	private List<LIElement> liElements = new ArrayList<LIElement>();
	private List<PageElement> pageElements = new ArrayList<PageElement>();

	int counter = 0;
	
	public interface Binder extends UiBinder<Widget, MemberRegistrationView> {
	}

	@Inject
	public MemberRegistrationView(final Binder binder) {
		widget = binder.createAndBindUi(this);

		String url = "http://197.248.2.44:8080/ewallet-beta/#websiteClient";
		framePayment.setUrl(url);

		// Li Elements
		liElements.add(liTab1);
		liElements.add(liTab2);
		liElements.add(liTab3);
		liElements.add(liTab4);

		// Div Elements
		pageElements.add(new PageElement(divPackage, "Next", "Back"));
		pageElements.add(new PageElement(divCategories, "Submit"));
		pageElements.add(new PageElement(divProforma, "Proceed to Pay",
				"Proceed To My Account"));
		pageElements.add(new PageElement(divPayment, "Finish"));

		setActive(liElements.get(counter), pageElements.get(counter));

		aBack.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				counter = counter - 1;
				removeActive(liElements.get(counter), pageElements.get(counter));
				setActive(liElements.get(counter), pageElements.get(counter));
			}
		});

		aNext.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				counter = counter + 1;
				setActive(liElements.get(counter), pageElements.get(counter));
			}
		});
		
		List<ApplicationType> types= new ArrayList<ApplicationType>();
		for(ApplicationType t: ApplicationType.values()){
			types.add(t);
		}

		//lstMemberCategory.setItems(types);
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
		//System.err.println("Added:" + counter);
	}
	
	public ApplicationFormHeaderDto getApplicationForm(){
		ApplicationFormHeaderDto dto = new ApplicationFormHeaderDto();
		//dto.setRefId(getRefId());
		dto.setSurname(txtSurname.getValue());
		dto.setOtherNames(txtOtherNames.getValue());
		dto.setEmail(txtEmailAddress.getValue());
//		if(lstMemberCategory.getValue()!=null){
//			dto.setApplicantType(lstMemberCategory.getValue().ordinal());
//		}
		dto.setEmployer(txtEmployer.getValue());
		dto.setCity1(txtCity.getValue());
		dto.setAddress1(txtAddress.getValue());
		dto.setPostCode(txtPostalCode.getValue());
		
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

}
