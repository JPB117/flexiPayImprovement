package com.workpoint.icpak.client.ui.events.registration;

import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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
import com.workpoint.icpak.client.ui.component.AutoCompleteField;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.ui.events.registration.proforma.ProformaInvoice;
import com.workpoint.icpak.client.ui.grid.AggregationGrid;
import com.workpoint.icpak.client.ui.grid.AggregationGridRow;
import com.workpoint.icpak.client.ui.grid.ColumnConfig;
import com.workpoint.icpak.client.ui.grid.DataMapper;
import com.workpoint.icpak.client.ui.grid.DataModel;
import com.workpoint.icpak.client.ui.membership.PageElement;
import com.workpoint.icpak.client.ui.payment.PaymentWidget;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.shared.model.Country;
import com.workpoint.icpak.shared.model.CreditCardDto;
import com.workpoint.icpak.shared.model.CreditCardResponse;
import com.workpoint.icpak.shared.model.DataType;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.Listable;
import com.workpoint.icpak.shared.model.MemberDto;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.Title;
import com.workpoint.icpak.shared.model.events.AccommodationDto;
import com.workpoint.icpak.shared.model.events.BookingDto;
import com.workpoint.icpak.shared.model.events.ContactDto;
import com.workpoint.icpak.shared.model.events.DelegateDto;
import com.workpoint.icpak.shared.model.events.EventDto;

public class EventBookingView extends ViewImpl implements EventBookingPresenter.MyView {

	private final Widget widget;

	@UiField
	HTMLPanel divMainContainer;

	@UiField
	HTMLPanel divHeaderContainer;

	@UiField
	HTMLPanel divFooter;

	@UiField
	PaymentWidget panelPayment;

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
	ActionLink aAddMember;

	@UiField
	ActionLink aAddNonMember;

	@UiField
	ProformaInvoice proformaInv;
	@UiField
	DivElement divContainer;

	private List<LIElement> liElements = new ArrayList<LIElement>();
	private List<PageElement> pageElements = new ArrayList<PageElement>();

	int counter = 0;
	Map<String, DelegateDto> finalDelRefIds = new HashMap<String, DelegateDto>();

	public interface Binder extends UiBinder<Widget, EventBookingView> {
	}

	ColumnConfig memberColumn = new ColumnConfig("memberNo", "Member No", DataType.SELECTAUTOCOMPLETE, "",
			"form-control");

	ColumnConfig accommodationConfig = new ColumnConfig("accommodation", "Accommodation", DataType.SELECTBASIC);

	@Inject
	public EventBookingView(final Binder binder) {
		widget = binder.createAndBindUi(this);

		tblDelegates.setAutoNumber(true);
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		configs.add(memberColumn);

		ColumnConfig config = new ColumnConfig("title", "Title", DataType.SELECTBASIC, "", "form-control");
		config.setDropDownItems(Arrays.asList(Title.values()));

		configs.add(config);
		config = new ColumnConfig("surname", "First Name:", DataType.STRING, "", "form-control");
		configs.add(config);
		config = new ColumnConfig("otherNames", "Other Names:", DataType.STRING, "", "form-control");
		configs.add(config);

		// config = new ColumnConfig("email", "Email Address", DataType.STRING,
		// "", "form-control");
		// configs.add(config);

		configs.add(accommodationConfig);

		tblDelegates.setColumnConfigs(configs);

		accommodationConfig.addValueChangeHandler(new ValueChangeHandler<Listable>() {
			@Override
			public void onValueChange(ValueChangeEvent<Listable> event) {
				if (event.getValue() == null) {
					return;
				}

				// Window.alert("Accommodations selection change");
				Widget source = (Widget) event.getSource();
				DropDownList<AccommodationDto> field = (DropDownList) source;
				AggregationGridRow row = field.getParentRow();
				DelegateDto delegate = mapper.getData(row.getData());
				DelegateDto saved = finalDelRefIds.get(delegate.getRefId());
				
				AccommodationDto dto = (AccommodationDto) event.getValue();
				if(saved!=null && saved.getAccommodation()!=null 
						&& saved.getAccommodation().equals(dto)){
					return;
				}
				
				if (dto.getTotalBooking() > dto.getSpaces()) {
					Window.alert("No spaces available in '" + dto.getHotel() + "'");
					field.setValue(null);
					return;
				}

				List<DelegateDto> delegates = getDelegates();
				int count = 0;
				for (DelegateDto d : delegates) {
					if (d.getAccommodation() != null && d.getAccommodation().equals(dto)) {
						++count;
					}
				}

				if ((dto.getTotalBooking() + count) > dto.getSpaces()) {
					Window.alert("No more spaces available in '" + dto.getHotel() + "'");
					field.setValue(null);
					return;
				}
			}
		});

		memberColumn.addValueChangeHandler(new ValueChangeHandler<Listable>() {
			@Override
			public void onValueChange(ValueChangeEvent<Listable> event) {
				Widget source = (Widget) event.getSource();
				AutoCompleteField<MemberDto> field = (AutoCompleteField) source;
				AggregationGridRow row = field.getParentRow();

				if (event.getValue() != null) {
					MemberDto dto = (MemberDto) event.getValue();
					DelegateDto delegate = mapper.getData(row.getData());
					// delegate.setMember(dto);
					if (dto != null) {
						delegate.setMemberId(dto.getMemberNo());
						delegate.setMemberRefId(dto.getRefId());
					}
					delegate.setSurname(dto.getLastName());
					delegate.setOtherNames(dto.getFirstName());
					delegate.setEmail(dto.getEmail());
					delegate.setTitle(dto.getTitle());

					DataModel model = mapper.getModel(delegate);
					row.setModel(model, true);
				}
			}
		});

		aAddMember.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				memberColumn.setEnabled(true);
				tblDelegates.addRowData(new DataModel());
			}
		});

		aAddNonMember.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				memberColumn.setEnabled(false);
				tblDelegates.addRowData(new DataModel());
			}
		});

		// Li Elements
		liElements.add(liTab1);
		liElements.add(liTab2);
		liElements.add(liTab3);
		liElements.add(liTab4);

		// Div Elements
		pageElements.add(new PageElement(divPackage, "Proceed", liTab1));
		pageElements.add(new PageElement(divCategories, "Submit", "Back", liTab2));
		pageElements.add(new PageElement(divProforma, "Proceed to Pay", liTab3));
		pageElements.add(new PageElement(divPayment, "Finish", "Back", liTab4));

		setActive(liElements.get(counter), pageElements.get(counter));

		aBack.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				counter = counter - 1;
				setActivePage(counter);

			}
		});
	}

	public void setActivePage(int index) {
		this.counter = index;
		showMyAccountLink(counter);
		removeActive(liElements.get(counter), pageElements.get(counter));
		setActive(liElements.get(counter), pageElements.get(counter));
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

		aBack.getElement().removeClassName("hide");
	}

	private void setActive(LIElement liElement, PageElement page) {
		clearAll();
		setButtons(page);
		liElement.addClassName("active");
		page.getElement().addClassName("active");
	}

	private void clearAll() {
		for (PageElement pageElement : pageElements) {
			if (pageElement.isComplete()) {
				pageElement.getLiElement().getFirstChildElement().getFirstChildElement().removeClassName("hide");
			} else {
				pageElement.getLiElement().removeClassName("active");
			}
		}

		divPackage.removeClassName("active");
		divPayment.removeClassName("active");
		divCategories.removeClassName("active");
		divProforma.removeClassName("active");
	}

	public boolean isValid() {
		boolean isValid = true;
		issuesPanel.clear();
		issuesPanelDelegate.clear();

		// Window.alert("Counter:"+counter);

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
				issuesPanel.addError("You must enter at least 1 delegate for this event");
				isValid = false;
			}

			// for(DelegateDto dto: getDelegates()){
			// if(dto.getMember()==null){
			// isValid = false;
			// issuesPanel.addError("Member cannot be null!!");
			// }
			// }
		}

		// show/hide isValid Panel
		if (isValid) {
			issuesPanel.addStyleName("hide");
		} else {
			issuesPanel.removeStyleName("hide");
		}
		return isValid;

	}

	public boolean isPaymentValid() {
		return panelPayment.isValid();
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
		pageElements.get(counter).setCompletion(true);
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
		dto.setStatus("");
		dto.setPaymentStatus(PaymentStatus.NOTPAID);
		dto.setBookingDate(new Date().getTime());
		dto.setContact(getContact());
		// dto.setCurrency(currency);
		dto.setDelegates(getDelegates());

		return dto;
	}

	private List<DelegateDto> getDelegates() {
		List<DelegateDto> list = tblDelegates.getData(mapper);
		return list;
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
				spnDuration.setInnerText(DateUtils.getTimeDifference(startDate, endDate));
			}

			spnDays2Go.setInnerText(DateUtils.getTimeDifference(new Date(), startDate));
		}

		// bindAccommodations(event.getAccommodation());
	}

	public void bindAccommodations(List<AccommodationDto> accommodation) {
		List<Listable> accommodations = new ArrayList<Listable>();
		if (accommodations != null) {
			for (AccommodationDto acc : accommodation) {
				accommodations.add(acc);
			}
			accommodationConfig.setDropDownItems(accommodations);
		}
	}

	@Override
	public void bindBooking(BookingDto booking) {

		if (booking.getContact() != null) {
			spnNames.setInnerText(booking.getContact().getContactName());
			txtAddress.setValue(booking.getContact().getAddress());
			txtCompanyName.setValue(booking.getContact().getCompany());
			txtCity.setValue(booking.getContact().getCity());
			txtContactPerson.setValue(booking.getContact().getContactName());
			txtContactEmail.setValue(booking.getContact().getEmail());
			txtPostalCode.setValue(booking.getContact().getPostCode());
			txtPhone.setValue(booking.getContact().getTelephoneNumbers());
		}

		if (booking.getDelegates() != null) {

			for (DelegateDto d : booking.getDelegates()) {
				finalDelRefIds.put(d.getRefId(), d);
			}

			List<DataModel> models = mapper.getDataModels(booking.getDelegates());
			tblDelegates.setData(models);
		}

	}

	@Override
	public void bindInvoice(InvoiceDto invoice) {
		proformaInv.clearRows();
		proformaInv.setInvoice(invoice);

		if (invoice.getInvoiceAmount() != null) {
			panelPayment.setAmount(invoice.getInvoiceAmount().toString());
		}

		if (invoice.getDocumentNo() != null) {
			panelPayment.bindTransaction(invoice);
		}
	}

	@Override
	public void setLoadingState(ActionLink anchor, boolean isLoading) {
		anchor.setLoadingState(anchor, isLoading);
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
	public ColumnConfig getMemberColumnConfig() {
		return memberColumn;
	}

	public HasClickHandlers getPayButton() {
		return panelPayment.getCardPayButton();
	}

	public CreditCardDto getCreditCardDetails() {
		return panelPayment.getCardDetails();
	}

	@Override
	public void setCardResponse(CreditCardResponse response) {
		panelPayment.setCardResponse(response);

	}

	@Override
	public HasClickHandlers getMpesaCompleteButton() {
		return panelPayment.getMpesaCompleteButton();
	}

	@Override
	public void setInvoiceResult(InvoiceDto invoice) {
		panelPayment.setInvoiceResult(invoice);
	}

	
	
	
	
	
	
	
	
	/**
	 * DELEGATE DATA MAPPER 
	 */
	DataMapper mapper = new DataMapper() {
		@Override
		public List<DataModel> getDataModels(List objs) {
			List<DataModel> models = new ArrayList<DataModel>();
			for (Object o : objs) {
				models.add(getModel(o));
			}
			return models;
		}

		@Override
		public DataModel getModel(Object obj) {
			DelegateDto dto = (DelegateDto) obj;
			MemberDto member = new MemberDto();
			if (dto.getMemberRefId() == null) {
				member = null;
			} else {
				member.setRefId(dto.getMemberRefId());
				member.setMemberNo(dto.getMemberId());
				member.setFirstName(dto.getOtherNames());
				member.setLastName(dto.getSurname());
			}

			DataModel model = new DataModel();
			model.setId(dto.getRefId());
			model.set("memberNo", member);
			model.set("title", dto.getTitle() == null ? null : Title.valueOf(dto.getTitle()));
			model.set("surname", dto.getSurname());
			model.set("otherNames", dto.getOtherNames());
			model.set("email", dto.getEmail());
			model.set("accommodation", dto.getAccommodation());
			return model;
		}

		@Override
		public DelegateDto getData(DataModel model) {
			DelegateDto dto = new DelegateDto();

			if (model.isEmpty()) {
				return null;
			}

			MemberDto memberDto = model.get("memberNo") == null ? null : ((MemberDto) model.get("memberNo"));
			dto.setRefId(model.getId() == null ? null : model.getId().toString());
			dto.setMemberId(memberDto == null ? null : memberDto.getMemberNo());
			dto.setMemberRefId(memberDto == null ? null : memberDto.getRefId());
			dto.setTitle(model.get("title") == null ? null : model.get("title").toString());
			dto.setSurname(model.get("surname") == null ? null : model.get("surname").toString());
			dto.setOtherNames(model.get("otherNames") == null ? null : model.get("otherNames").toString());
			dto.setEmail(model.get("email") == null ? null : model.get("email").toString());
			dto.setAccommodation(
					model.get("accommodation") == null ? null : (AccommodationDto) model.get("accommodation"));

			return dto;
		}
	};

}