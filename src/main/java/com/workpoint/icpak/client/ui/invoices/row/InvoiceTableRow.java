package com.workpoint.icpak.client.ui.invoices.row;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.model.UploadContext;
import com.workpoint.icpak.client.model.UploadContext.UPLOADACTION;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.RowWidget;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.client.ui.util.NumberUtils;
import com.workpoint.icpak.shared.model.AttachmentDto;
import com.workpoint.icpak.shared.model.PaymentMode;
import com.workpoint.icpak.shared.model.PaymentType;
import com.workpoint.icpak.shared.model.TransactionDto;

public class InvoiceTableRow extends RowWidget {

	private static ActivitiesTableRowUiBinder uiBinder = GWT
			.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends
			UiBinder<Widget, InvoiceTableRow> {
	}

	@UiField
	HTMLPanel row;
	@UiField
	HTMLPanel divDate;
	@UiField
	HTMLPanel divPaymentMode;
	@UiField
	HTMLPanel divDescription;
	@UiField
	HTMLPanel divAmount;
	@UiField
	HTMLPanel divChargeAbleAmount;
	@UiField
	HTMLPanel divTotalPaid;
	@UiField
	HTMLPanel divBalance;
	@UiField
	SpanElement spnStatus;
	@UiField
	ActionLink aProforma;

	public InvoiceTableRow() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public InvoiceTableRow(final TransactionDto trx) {
		this();
		if (trx.getCreatedDate() != null) {
			divDate.add(new InlineLabel(DateUtils.READABLETIMESTAMP.format(trx
					.getCreatedDate())));
		}
		if (trx.getPaymentMode() != null) {
			if (trx.getPaymentMode() == PaymentMode.MPESA) {
				divPaymentMode.add(new HTML(
						"<img src='img/Mpesa_Logo.png'></img>"));
			} else if (trx.getPaymentMode() == PaymentMode.CARDS) {
				divPaymentMode.add(new HTML(
						"<img src='img/Visa_Logo.png'></img>"));
			} else if (trx.getPaymentMode() == PaymentMode.BANKTRANSFER) {
				divPaymentMode
						.add(new HTML(
								"<span class='label label-fill label-info'>BANK TRANSFER</span>"));
			} else if (trx.getPaymentMode() == PaymentMode.DIRECTBANKING) {
				divPaymentMode
						.add(new HTML(
								"<span class='label label-fill label-default'>DIRECT BANKING</span>"));
			}
		}
		HTMLPanel panelAttachments = new HTMLPanel("");
		if (trx.getAllAttachment() != null) {
			for (final AttachmentDto attachment : trx.getAllAttachment()) {
				final UploadContext ctx = new UploadContext("getreport");
				ctx.setAction(UPLOADACTION.GETATTACHMENT);
				ctx.setContext("refId", attachment.getRefId());

				ActionLink link = new ActionLink(attachment.getAttachmentName());
				link.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						Window.open(ctx.toUrl(),
								attachment.getAttachmentName(), "");
					}
				});
				panelAttachments.add(link);

				if (trx.getAllAttachment().size() > 1) {
					panelAttachments.add(new InlineLabel(" | "));
				}
			}
		}
		if (trx.getDescription() != null) {
			String desc = "";
			if (trx.getPaymentMode() != PaymentMode.CARDS) {
				desc = trx.getDescription() + "(" + trx.getTrxNumber() + ")";
				desc = desc + "<br>";
			} else {
				desc = trx.getDescription();
			}
			HTMLPanel combinedPanel = new HTMLPanel("");
			combinedPanel.add(new HTML(desc));
			combinedPanel.add(panelAttachments);
			divDescription.add(combinedPanel);
		}
		if (trx.getAccountNo() != null) {
			aProforma.setText(trx.getAccountNo());

			if (trx.getPaymentType() != null) {
				if (trx.getPaymentType() == PaymentType.BOOKING
						|| trx.getPaymentType() == PaymentType.REGISTRATION) {
					aProforma.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							UploadContext ctx = new UploadContext("getreport");
							if (trx.getPaymentType() == PaymentType.BOOKING) {
								ctx.setContext("bookingRefId",
										trx.getInvoiceRef());
							} else if ((trx.getPaymentType() == PaymentType.REGISTRATION)) {
								ctx.setContext("applicationRefId",
										trx.getInvoiceRef());
							}
							ctx.setAction(UPLOADACTION.GETPROFORMA);
							Window.open(ctx.toUrl(), "Get Proforma", null);
						}
					});
				}
			}
		}

		if (trx.getAmountPaid() != null) {
			divAmount.add(new InlineLabel(NumberUtils.CURRENCYFORMAT.format(trx
					.getAmountPaid())));
		}

		if (trx.getChargableAmnt() != null) {
			divChargeAbleAmount.add(new InlineLabel(NumberUtils.CURRENCYFORMAT
					.format(trx.getChargableAmnt())));
		} else {
			divChargeAbleAmount.add(new InlineLabel("N/A"));
		}

		if (trx.getTotalPreviousPayments() != null) {
			divTotalPaid.add(new InlineLabel(NumberUtils.CURRENCYFORMAT
					.format(trx.getTotalPreviousPayments())));
		} else {
			divTotalPaid.add(new InlineLabel("N/A"));
		}

		if (trx.getTotalBalance() != null) {
			divBalance.add(new InlineLabel(NumberUtils.CURRENCYFORMAT
					.format(trx.getTotalBalance())));
		} else {
			divBalance.add(new InlineLabel("N/A"));
		}

	}
}
