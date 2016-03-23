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
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
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
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.Country;
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
	HTMLPanel panelPayment;
	@UiField
	Element divHeaderTopics;
	@UiField
	HTMLPanel divContent;
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
	DivElement divHeader;
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
	ActionLink aAddMember;
	@UiField
	ActionLink aAddNonMember;
	@UiField
	ProformaInvoice proformaInv;
	@UiField
	ActionLink aDownloadProforma;
	@UiField
	DivElement divContainer;
	@UiField
	ActionLink aBackToDelegates;
	@UiField
	SpanElement spnSpinner;
	@UiField
	Anchor aBrowseOthers;

	int counter = 0;
	boolean isValid = true;
	boolean isDelegateValid = true;

	private List<LIElement> liElements = new ArrayList<LIElement>();
	private List<PageElement> pageElements = new ArrayList<PageElement>();

	Map<String, DelegateDto> finalDelRefIds = new HashMap<String, DelegateDto>();

	public interface Binder extends UiBinder<Widget, EventBookingView> {
	}

	ColumnConfig memberColumn = new ColumnConfig("memberNo", "Member No",
			DataType.SELECTAUTOCOMPLETE, "", "form-control");

	ColumnConfig accommodationConfig = new ColumnConfig("accommodation",
			"Accommodation", DataType.SELECTBASIC);

	ColumnConfig fullNameConfig = new ColumnConfig("fullName", "FullNames",
			DataType.STRING, "Delegate FullNames", "form-control", false);

	ColumnConfig titleConfig = new ColumnConfig("title", "Title",
			DataType.SELECTBASIC, "", "form-control");

	@Inject
	public EventBookingView(final Binder binder) {
		widget = binder.createAndBindUi(this);

		tblDelegates.setAutoNumber(true);
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		configs.add(memberColumn);

		titleConfig.setDropDownItems(Arrays.asList(Title.values()));
		configs.add(titleConfig);
		configs.add(fullNameConfig);
		configs.add(accommodationConfig);

		tblDelegates.setColumnConfigs(configs);

		accommodationConfig.setHeaderStyleName("accomodation-header");
		
		accommodationConfig
				.addValueChangeHandler(new ValueChangeHandler<Listable>() {
					@Override
					public void onValueChange(ValueChangeEvent<Listable> event) {
						if (event.getValue() == null) {
							return;
						}

						Widget source = (Widget) event.getSource();
						DropDownList<AccommodationDto> field = (DropDownList) source;
						AggregationGridRow row = field.getParentRow();
						DelegateDto delegate = mapper.getData(row.getData());
						DelegateDto saved = finalDelRefIds.get(delegate
								.getRefId());

						AccommodationDto dto = (AccommodationDto) event
								.getValue();
						if (saved != null && saved.getAccommodation() != null
								&& saved.getAccommodation().equals(dto)) {
							return;
						}

						if (dto.getTotalBooking() > dto.getSpaces()) {
							Window.alert("No spaces available in '"
									+ dto.getHotel() + "'");
							field.setValue(null);
							return;
						}

						List<DelegateDto> delegates = getDelegates();
						int count = 0;
						for (DelegateDto d : delegates) {
							if (d.getAccommodation() != null
									&& d.getAccommodation().equals(dto)) {
								++count;
							}
						}

						if ((dto.getTotalBooking() + count) > dto.getSpaces()) {
							Window.alert("No more spaces available in '"
									+ dto.getHotel() + "'");
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
					if (dto != null) {
						delegate.setMemberNo(dto.getMemberNo());
						delegate.setMemberRefId(dto.getRefId());
						delegate.setMemberQrCode(dto.getMemberQrCode());
					}

					delegate.setFullName(dto.getFullName());
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
				fullNameConfig.setEnabled(false);
				titleConfig.setEnabled(false);
				accommodationConfig.setEnabled(true);
				tblDelegates.addRowData(new DataModel());
			}
		});

		aAddNonMember.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				memberColumn.setEnabled(false);
				fullNameConfig.setEnabled(true);
				titleConfig.setEnabled(true);
				accommodationConfig.setEnabled(true);
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
		pageElements.add(new PageElement(divCategories, "Submit", "Back",
				liTab2));
		pageElements
				.add(new PageElement(divProforma, "Proceed to Pay", liTab3));
		pageElements.add(new PageElement(divPayment, "Go to My Bookings",
				"Back", liTab4));

		setActive(liElements.get(counter), pageElements.get(counter));

		aBack.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				counter = counter - 1;
				setActivePage(counter);
			}
		});
		initAdminAspects();
	}

	private void initAdminAspects() {
		if (AppContext.isCurrentUserEventEdit()) {
			aBackToDelegates.removeStyleName("hide");
			aBackToDelegates.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					History.back();
				}
			});
		}
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

	public boolean isValid() {
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
				issuesPanel.addError("Contact Person details is required");
			}

			if (isNullOrEmpty(txtContactEmail.getValue())) {
				isValid = false;
				issuesPanel.addError("E-Mail is required");
			}
		}

		// show/hide isValid Panel
		if (isValid && counter == 0) {
			issuesPanel.addStyleName("hide");
		} else {
			issuesPanel.removeStyleName("hide");
		}
		return isValid;
	}

	public boolean isDelegateValid() {
		if (getDelegates().size() == 0) {
			issuesPanelDelegate
					.addError("You must enter at least 1 delegate for this event");
			isDelegateValid = false;
		} else {
			isDelegateValid = true;
		}
		if (isDelegateValid) {
			issuesPanelDelegate.addStyleName("hide");
		} else {
			issuesPanelDelegate.removeStyleName("hide");
		}

		return isDelegateValid;
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
		int topicsHeight = divHeader.getOffsetHeight();
		int headerTopics = divHeaderTopics.getOffsetHeight();
		int middleHeight = totalHeight - topHeight - topicsHeight
				- headerTopics - 53;

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

			Date startDate = DateUtils.parse(event.getStartDate(),
					DateUtils.FULLTIMESTAMP);
			spnStartDate.setInnerText(DateUtils.DATEFORMAT.format(startDate));
			if (event.getEndDate() != null) {
				Date endDate = DateUtils.parse(event.getEndDate(),
						DateUtils.FULLTIMESTAMP);
				spnDuration.setInnerText(DateUtils.getTimeDifference(startDate,
						endDate));
			}

			spnDays2Go.setInnerText(DateUtils.getTimeDifference(new Date(),
					startDate));
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

			List<DataModel> models = mapper.getDataModels(booking
					.getDelegates());
			tblDelegates.setData(models);
		}

	}

	@Override
	public void bindInvoice(InvoiceDto invoice) {
		proformaInv.clearRows();
		proformaInv.setInvoice(invoice);

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

	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if (slot == EventBookingPresenter.PAYMENTS_SLOT) {
			panelPayment.clear();
			if (content != null) {
				panelPayment.add(content);
			}
			super.setInSlot(slot, content);
		}
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
				member.setMemberNo(dto.getMemberNo());
				member.setFullName(dto.getFullName());
				member.setMemberQrCode(dto.getMemberQrCode());
			}

			DataModel model = new DataModel();
			model.setId(dto.getRefId());
			model.set("memberNo", member);
			model.set(
					"title",
					dto.getTitle() == null ? null : Title.valueOf(dto
							.getTitle()));
			model.set("fullName", dto.getFullName());
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

			MemberDto memberDto = model.get("memberNo") == null ? null
					: ((MemberDto) model.get("memberNo"));
			dto.setRefId(model.getId() == null ? null : model.getId()
					.toString());
			dto.setMemberNo(memberDto == null ? null : memberDto.getMemberNo());
			dto.setMemberRefId(memberDto == null ? null : memberDto.getRefId());
			dto.setMemberQrCode(memberDto == null ? null : memberDto
					.getMemberQrCode());

			dto.setTitle(model.get("title") == null ? null : model.get("title")
					.toString());
			dto.setFullName(model.get("fullName") == null ? null : model.get(
					"fullName").toString());
			dto.setEmail(model.get("email") == null ? null : model.get("email")
					.toString());
			dto.setAccommodation(model.get("accommodation") == null ? null
					: (AccommodationDto) model.get("accommodation"));

			return dto;
		}
	};

	@Override
	protected void onAttach() {
		super.onAttach();
	}

	public HasClickHandlers getaDownloadProforma() {
		return aDownloadProforma;
	}

	@Override
	public TextField getEmailTextBox() {
		return txtContactEmail;
	}

	@Override
	public void setEmailValid(boolean isValid, String message) {
		this.isValid = isValid;
		issuesPanel.clear();
		if (isValid) {
			issuesPanel.addStyleName("hide");
		} else {
			issuesPanel.addError(message);
			issuesPanel.removeStyleName("hide");
		}
	}

	public void showEmailValidating(boolean show) {
		if (show) {
			spnSpinner.removeClassName("hide");
			aNext.addStyleName("hide");
		} else {
			spnSpinner.addClassName("hide");
			aNext.removeStyleName("hide");
		}
	}

	public Anchor browseOthersEventsButton() {
		return aBrowseOthers;
	}

	@Override
	public void scrollToPaymentsTop() {
		panelPayment.getElement().scrollIntoView();
	}
}