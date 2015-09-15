package com.workpoint.icpak.client.ui.payment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.ui.cpd.Year;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.shared.model.CreditCardDto;
import com.workpoint.icpak.shared.model.Listable;

public class PaymentWidget extends Composite {

	private static PaymentWidgetUiBinder uiBinder = GWT
			.create(PaymentWidgetUiBinder.class);

	@UiField
	TextField txtCardHolderName;
	@UiField
	TextField txtCardNumber;
	@UiField
	DropDownList<Month> lstMonths;
	@UiField
	DropDownList<Year> lstYears;
	@UiField
	TextField txtCvv;
	@UiField
	ActionLink aPay;

	private int totalYears = 10;

	private int totalMonths = 12;

	interface PaymentWidgetUiBinder extends UiBinder<Widget, PaymentWidget> {
	}

	public PaymentWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		setDate();
	}

	public class Month implements Listable {
		String displayName;
		String value;

		public Month(Date startMonth) {
			displayName = DateUtils.MONTHFORMAT.format(startMonth);
			value = DateUtils.MONTHONLYFORMAT.format(startMonth);
		}

		@Override
		public String getName() {
			return value;
		}

		@Override
		public String getDisplayName() {
			return displayName;
		}

	}

	private void setDate() {
		Date startDate = new Date();
		List<Year> allYears = new ArrayList<Year>();
		for (int i = 1; i <= totalYears; i++) {
			CalendarUtil.addMonthsToDate(startDate, 12);
			allYears.add(new Year(startDate));
		}
		lstYears.setItems(allYears);

		Date startMonth = new Date();
		CalendarUtil.addMonthsToDate(startMonth, -startMonth.getMonth());
		List<Month> allMonths = new ArrayList<Month>();

		for (int i = 1; i <= totalMonths; i++) {
			CalendarUtil.addMonthsToDate(startMonth, 1);
			allMonths.add(new Month(startMonth));
		}
		lstMonths.setItems(allMonths);
	}

	public CreditCardDto getCardDetails() {
		String expiry = lstMonths.getValue().getName()
				+ lstYears.getValue().getName();
		CreditCardDto creditCard = new CreditCardDto();
		creditCard.setCard_holder_name(txtCardHolderName.getValue());
		creditCard.setCard_number(txtCardNumber.getValue());
		creditCard.setExpiry(expiry);
		creditCard.setSecurity_code(txtCvv.getValue());
		return creditCard;
	}

	public HasClickHandlers getPayButton() {
		return aPay;
	}

}
