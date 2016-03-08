package com.workpoint.icpak.client.ui.payment.widget;

import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.workpoint.icpak.client.model.UploadContext;
import com.workpoint.icpak.client.model.UploadContext.UPLOADACTION;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.ui.cpd.Year;
import com.workpoint.icpak.client.ui.upload.custom.Uploader;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.client.ui.util.NumberUtils;
import com.workpoint.icpak.shared.model.CreditCardDto;
import com.workpoint.icpak.shared.model.CreditCardResponse;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.Listable;
import com.workpoint.icpak.shared.model.PaymentStatus;

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
	TextField txtAmount;
	@UiField
	ActionLink aPay;
	@UiField
	SpanElement elCardAmount;
	@UiField
	IssuesPanel issuesPanel;
	@UiField
	SpanElement spnAccountNo;
	@UiField
	SpanElement spnAmount;
	@UiField
	SpanElement spnMessage;
	@UiField
	TextField txtAddress1;
	@UiField
	ActionLink aCompleteMpesa;
	@UiField
	HTMLPanel PanelPayment;
	@UiField
	HTMLPanel panelSuccess;

	@UiField
	CheckBox aDepositSlip;
	@UiField
	CheckBox aCheque;
	@UiField
	Uploader uploaderAttachment;

	private int totalYears = 10;
	private int totalMonths = 12;
	String trxAmount;

	interface PaymentWidgetUiBinder extends UiBinder<Widget, PaymentWidget> {
	}

	public PaymentWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		setDate();
		setAmount("10");
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

	List<Year> allYears = new ArrayList<Year>();

	private CreditCardDto creditCard = new CreditCardDto();

	private String paymentRefId;

	private void setDate() {
		Date startDate = new Date();
		int thisYear = 1900 + startDate.getYear();
		for (int i = 1; i <= totalYears; i++) {
			Year year = new Year(thisYear);
			thisYear++;
			allYears.add(year);
		}

		lstYears.setItems(allYears, "Year");

		Date startMonth = new Date();
		CalendarUtil.addMonthsToDate(startMonth, -(startMonth.getMonth() + 1));
		List<Month> allMonths = new ArrayList<Month>();

		for (int i = 1; i <= totalMonths; i++) {
			CalendarUtil.addMonthsToDate(startMonth, 1);
			allMonths.add(new Month(startMonth));
		}
		lstMonths.setItems(allMonths, "Month");
	}

	public CreditCardDto getCardDetails() {
		String expiry = lstMonths.getValue().getName()
				+ lstYears.getValue().getName();
		creditCard = new CreditCardDto();
		creditCard.setCard_holder_name("This can be anything");
		creditCard.setCard_number(txtCardNumber.getValue());
		creditCard.setAmount(trxAmount);
		creditCard.setExpiry(expiry);
		creditCard.setSecurity_code(txtCvv.getValue());
		creditCard.setCountry("KE");
		creditCard.setCurrency("KES");
		creditCard.setState("Nairobi");
		creditCard.setZip(trxAmount);
		creditCard.setAddress1("This Can be anything!");
		creditCard.setPaymentRefId(paymentRefId);
		return creditCard;
	}

	public void setAmount(String amount1) {
		this.trxAmount = amount1;
		txtAmount.setText(NumberUtils.CURRENCYFORMAT.format(Double
				.parseDouble(amount1)));
		elCardAmount.setInnerText(NumberUtils.CURRENCYFORMAT.format(Double
				.parseDouble(amount1)));
		spnAmount.setInnerText(NumberUtils.CURRENCYFORMAT.format(Double
				.parseDouble(amount1)));

	}

	public boolean isValid() {
		boolean isValid = true;
		issuesPanel.clear();
		// Window.alert("Counter:"+counter);
		if (isNullOrEmpty(txtAmount.getValue())) {
			isValid = false;
			issuesPanel.addError("Amount is required!");
		}
		// if (isNullOrEmpty(txtCardHolderName.getValue())) {
		// isValid = false;
		// issuesPanel.addError("Card Holder name is required");
		// }

		if (isNullOrEmpty(txtCardNumber.getValue())) {
			isValid = false;
			issuesPanel.addError("Card number is required");
		}

		if (isNullOrEmpty(txtCvv.getValue())) {
			isValid = false;
			issuesPanel.addError("Security Code is required");
		}

		if (lstMonths.getValue() == null) {
			isValid = false;
			issuesPanel.addError("Provide month");
		}

		if (lstYears.getValue() == null) {
			isValid = false;
			issuesPanel.addError("Provide year");
		}

		// if (txtAddress1.getValue() == null) {
		// isValid = false;
		// issuesPanel.addError("Address is Mandatory");
		// }

		if (isValid) {
			issuesPanel.addStyleName("hide");
		} else {
			issuesPanel.removeStyleName("hide");
		}
		return isValid;

	}

	public HasClickHandlers getCardPayButton() {
		return aPay;
	}

	public HasClickHandlers getMpesaCompleteButton() {
		return aCompleteMpesa;
	}

	public void bindTransaction(InvoiceDto invoice) {
		this.paymentRefId = invoice.getDocumentNo();
		spnAccountNo.setInnerText(invoice.getDocumentNo());// MPESA Payment
		spnAmount.setInnerText(NumberUtils.CURRENCYFORMAT.format(invoice
				.getInvoiceAmount()));
		elCardAmount.setInnerText(NumberUtils.CURRENCYFORMAT.format(invoice
				.getInvoiceAmount()));
	}

	public void setCardResponse(CreditCardResponse response) {
		if (response.getStatusCode().equals("0000")) {
			panelSuccess.removeStyleName("hide");
			PanelPayment.addStyleName("hide");
		} else {
			issuesPanel.clear();
			String errorDesc = response.getInvalidParams();
			issuesPanel.addError(errorDesc);
			issuesPanel.removeStyleName("hide");
		}

	}

	public void setInvoiceResult(InvoiceDto invoice) {
		if (invoice.getStatus() == PaymentStatus.PAID) {
			panelSuccess.removeStyleName("hide");
			PanelPayment.addStyleName("hide");
		} else {
			spnMessage
					.setInnerText("Transaction not received. Please wait until you receive a message from ICPAK then try again");
			aCompleteMpesa.setText("Retry");
		}
	}

	public void setAttachmentUploadContext(String applicationRefId, String type) {
		UploadContext context = new UploadContext();
		if (type.equals("application")) {
			context.setContext("applicationRefId", applicationRefId);
		} else if (type.equals("booking")) {
			context.setContext("bookingRefId", applicationRefId);
		}
		context.setAction(UPLOADACTION.UPLOADIDPHOTOCOPY);
		context.setAccept(Arrays.asList("doc", "pdf", "jpg", "jpeg", "png",
				"docx"));
		uploaderAttachment.setContext(context);
	}
}
