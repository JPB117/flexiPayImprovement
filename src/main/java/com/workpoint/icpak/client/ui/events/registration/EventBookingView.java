package com.workpoint.icpak.client.ui.events.registration;

import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.ui.events.registration.proforma.ProformaInvoice;
import com.workpoint.icpak.client.ui.grid.AggregationGrid;
import com.workpoint.icpak.client.ui.grid.ColumnConfig;
import com.workpoint.icpak.client.ui.grid.DataMapper;
import com.workpoint.icpak.client.ui.grid.DataModel;
import com.workpoint.icpak.client.ui.registration.PageElement;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.shared.model.Country;
import com.workpoint.icpak.shared.model.DataType;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.Listable;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.events.AccommodationDto;
import com.workpoint.icpak.shared.model.events.BookingDto;
import com.workpoint.icpak.shared.model.events.ContactDto;
import com.workpoint.icpak.shared.model.events.DelegateDto;
import com.workpoint.icpak.shared.model.events.EventDto;

public class EventBookingView extends ViewImpl implements
		EventBookingPresenter.MyView {

	private final Widget widget;

	@UiField
	HTMLPanel divMainContainer;

	@UiField
	HTMLPanel divHeaderContainer;

	@UiField
	HTMLPanel divFooter;

	@UiField
	Element divHeaderTopics;

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
	LIElement liTab1;
	@UiField
	LIElement liTab2;
	@UiField
	LIElement liTab3;
	@UiField
	LIElement liTab4;

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

	@UiField
	DropDownList<Country> lstCountry;

	@UiField
	IssuesPanel issuesPanel;

	@UiField
	IssuesPanel issuesPanelDelegate;

	@UiField
	AggregationGrid tblDelegates;

	@UiField
	SpanElement spnNames;

	@UiField
	ActionLink aAddRow;

	@UiField
	SpanElement spnEventTitle;
	
	@UiField
	ProformaInvoice proformaInv;

	private List<LIElement> liElements = new ArrayList<LIElement>();
	private List<PageElement> pageElements = new ArrayList<PageElement>();

	DataMapper mapper = new DataMapper() {

		@Override
		public List<DataModel> getDataModels(List objs) {
			List<DataModel> models = new ArrayList<DataModel>();
			for (Object o : objs) {
				DelegateDto dto = (DelegateDto) o;

				DataModel model = new DataModel();
				model.set("memberNo", dto.getMemberRegistrationNo());
				model.set("title", dto.getTitle());
				model.set("surname", dto.getSurname());
				model.set("otherNames", dto.getOtherNames());
				model.set("email", dto.getEmail());
				model.set("accomodation", dto.getAccommodation());
				models.add(model);
			}
			return models;
		}

		@Override
		public DelegateDto getData(DataModel model) {
			DelegateDto dto = new DelegateDto();

			if (model.isEmpty()) {
				return null;
			}

			dto.setMemberRegistrationNo(model.get("memberNo") == null ? null
					: model.get("memberNo").toString());
			dto.setTitle(model.get("title") == null ? null : model.get("title")
					.toString());
			dto.setSurname(model.get("surname") == null ? null : model.get(
					"surname").toString());
			dto.setOtherNames(model.get("otherNames") == null ? null : model
					.get("otherNames").toString());
			dto.setEmail(model.get("email") == null ? null : model.get("email")
					.toString());
			dto.setAccommodation(model.get("accommodation")==null ? null:
				(AccommodationDto)model.get("accommodation"));

			return dto;
		}
	};
	
	int counter = 0;

	public interface Binder extends UiBinder<Widget, EventBookingView> {
	}

	ColumnConfig accommodationConfig = new ColumnConfig("accommodation", "Accommodation",
			DataType.SELECTBASIC);
	@Inject
	public EventBookingView(final Binder binder) {
		widget = binder.createAndBindUi(this);

		tblDelegates.setAutoNumber(false);
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		ColumnConfig config = new ColumnConfig("memberNo", "Member No",
				DataType.STRING,"","input-small");
		configs.add(config);
		config = new ColumnConfig("title", "Title", DataType.STRING);
		configs.add(config);
		config = new ColumnConfig("surname", "Surname", DataType.STRING);
		configs.add(config);
		config = new ColumnConfig("otherNames", "Other Names", DataType.STRING);
		configs.add(config);
		config = new ColumnConfig("email", "e-Mail", DataType.STRING);
		configs.add(config);
		configs.add(accommodationConfig);
		
		configs.add(config);
		
		tblDelegates.setColumnConfigs(configs);

		aAddRow.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				tblDelegates.addRowData(new DataModel());
			}
		});

		// 197.248.4.221
		String url = "http://197.248.4.221:8080/ewallet/#websiteClient";
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
		// System.err.println("Added:" + counter);
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

	public boolean isValid() {
		boolean isValid = true;
		issuesPanel.clear();
		issuesPanelDelegate.clear();

		if (counter == 0) {
			if (isNullOrEmpty(txtCompanyName.getValue())) {
				isValid = false;
				issuesPanel.addError("Company name is required");
			}

			if (isNullOrEmpty(txtPhone.getValue())) {
				isValid = false;
				issuesPanel.addError("Telephone is required");
			}

			if (isNullOrEmpty(txtPostalCode.getValue())) {
				isValid = false;
				issuesPanel.addError("Postal Code is required");
			}

			if (isNullOrEmpty(txtAddress.getValue())) {
				isValid = false;
				issuesPanel.addError("Address is required");
			}

			if (lstCountry.getValue() == null) {
				isValid = false;
				issuesPanel.addError("Country is required");
			}

			if (isNullOrEmpty(txtCity.getValue())) {
				isValid = false;
				issuesPanel.addError("City is required");
			}

			if (isNullOrEmpty(txtContactPerson.getValue())) {
				isValid = false;
				issuesPanel.addError("Contact is required");
			}

			if (isNullOrEmpty(txtContactEmail.getValue())) {
				isValid = false;
				issuesPanel.addError("e-Mail is required");
			}
		} else if (counter == 1) {
			if (getDelegates().size() == 0) {
				isValid = false;
			}
		}

		// show/hide isValid Panel
		if (isValid) {
			issuesPanel.addStyleName("hide");
		} else {
			issuesPanel.removeStyleName("hide");
		}
		// return isValid;
		return true;

	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setCountries(List<Country> countries) {
		lstCountry.setItems(countries);
		for (Country c : countries) {
			if (c.getName().equals("KE")) {
				lstCountry.setValue(c);
				break;
			}
		}
	}

	public Anchor getANext() {
		return aNext;
	}

	@Override
	public int getCounter() {
		return counter;
	}

	@Override
	public void next() {
		counter = counter + 1;
		showMyAccountLink(counter);
		setActive(liElements.get(counter), pageElements.get(counter));
	}

	@Override
	public void addError(String error) {
		issuesPanelDelegate.removeStyleName("hide");
		issuesPanelDelegate.clear();
		issuesPanelDelegate.addError(error);
	}

	public BookingDto getBooking() {
		BookingDto dto = new BookingDto();
		// dto.setAmountDue(amountDue);
		// dto.setAmountPaid(amountPaid);
		// dto.setPaymentDate(paymentDate);
		// dto.setPaymentDate(paymentDate);
		// dto.setPaymentMode(paymentMode);
		// dto.setPaymentRef(paymentRef);
		dto.setStatus("");
		dto.setPaymentStatus(PaymentStatus.NOTPAID);
		dto.setBookingDate(new Date().getTime());
		dto.setContact(getContact());
		// dto.setCurrency(currency);
		dto.setDelegates(getDelegates());

		return dto;

	}

	private List<DelegateDto> getDelegates() {
		return tblDelegates.getData(mapper);
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

	private ContactDto getContact() {

		ContactDto contact = new ContactDto();
		contact.setAddress(txtAddress.getValue());
		contact.setCompany(txtCompanyName.getValue());
		contact.setCity(txtCity.getValue());
		contact.setCompany(txtCompanyName.getValue());
		contact.setContactName(txtContactPerson.getValue());
		contact.setCountry(lstCountry.getValue().getDisplayName());
		contact.setEmail(txtContactEmail.getValue());
		contact.setPostCode(txtPostalCode.getValue());
		contact.setTelephoneNumbers(txtPhone.getValue());

		return contact;
	}

	@Override
	public void setEvent(EventDto event) {

		spnEventName.setInnerText(event.getName());

		if (event.getStartDate() != null) {
			
			Date startDate = new Date(event.getStartDate());
			spnStartDate.setInnerText(DateUtils.DATEFORMAT.format(startDate));
			if (event.getEndDate() != null) {
				Date endDate = new Date(event.getEndDate());
				spnDuration.setInnerText(DateUtils.getTimeDifference(
						startDate, endDate));
			}

			spnDays2Go.setInnerText(DateUtils.getTimeDifference(new Date(),
					startDate));
		}
		
		List<Listable> accommodations = new ArrayList<Listable>();
		if(accommodations!=null){
			for(AccommodationDto acc: event.getAccommodation()){
				accommodations.add(acc);
			}
			accommodationConfig.setDropDownItems(accommodations);
		}
	}

	@Override
	public void bindBooking(BookingDto booking) {
		spnNames.setInnerText(booking.getContact().getContactName());
		if (booking.getContact() != null) {
			txtAddress.setValue(booking.getContact().getAddress());
			txtCompanyName.setValue(booking.getContact().getCompany());
			txtCity.setValue(booking.getContact().getCity());
			txtContactPerson.setValue(booking.getContact().getContactName());
			txtContactEmail.setValue(booking.getContact().getEmail());
			txtPostalCode.setValue(booking.getContact().getPostCode());
			txtPhone.setValue(booking.getContact().getTelephoneNumbers());
		}

		if (booking.getDelegates() != null) {
			tblDelegates.setData(mapper.getDataModels(booking.getDelegates()));
		}

	}

	@Override
	public void bindInvoice(InvoiceDto invoice) {
		proformaInv.clearRows();
		proformaInv.setInvoice(invoice);
	}
}
