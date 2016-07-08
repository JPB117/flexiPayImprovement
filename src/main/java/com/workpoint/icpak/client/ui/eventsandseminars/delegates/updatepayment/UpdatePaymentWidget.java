package com.workpoint.icpak.client.ui.eventsandseminars.delegates.updatepayment;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.events.DelegateDto;

public class UpdatePaymentWidget extends Composite {

	private static UpdatePaymentWidgetUiBinder uiBinder = GWT
			.create(UpdatePaymentWidgetUiBinder.class);

	@UiField
	DropDownList<PaymentStatus> lstPaymentStatus;
	@UiField
	TextField txtReceiptNo;
	@UiField
	TextField txtClearanceNo;
	@UiField
	CheckBox chckIsCredit;
	@UiField
	TextField txtLPONo;
	@UiField
	SpanElement spnLastUpdated;

	private DelegateDto delegate;

	interface UpdatePaymentWidgetUiBinder extends
			UiBinder<Widget, UpdatePaymentWidget> {
	}

	public UpdatePaymentWidget(DelegateDto delegate) {
		this.delegate = delegate;
		initWidget(uiBinder.createAndBindUi(this));
		lstPaymentStatus.setItems(Arrays.asList(PaymentStatus.values()),
				"-Select Payment Status-");
		setDelegateValues(delegate);

		if (delegate.getUpdatedBy() != null) {
			spnLastUpdated.setInnerText(" by "
					+ delegate.getUpdatedBy()
					+ " on "
					+ DateUtils.READABLETIMESTAMP.format(delegate
							.getLastUpdateDate()));
		}
	}

	public DelegateDto getDelegate() {
		delegate.setReceiptNo(txtReceiptNo.getValue());
		delegate.setClearanceNo(txtClearanceNo.getValue());
		delegate.setLpoNo(txtLPONo.getValue());
		delegate.setDelegatePaymentStatus(lstPaymentStatus.getValue());

		String userFullNames = AppContext.getCurrentUser().getUser()
				.getSurname()
				+ " " + AppContext.getCurrentUser().getUser().getName();
		delegate.setUpdatedBy(userFullNames);
		if (chckIsCredit.getValue()) {
			delegate.setIsCredit(1);
		} else {
			delegate.setIsCredit(0);
		}

		return delegate;
	}

	public void setDelegateValues(DelegateDto delegate) {
		txtReceiptNo.setValue(delegate.getReceiptNo());
		txtClearanceNo.setValue(delegate.getClearanceNo());
		txtLPONo.setValue(delegate.getLpoNo());
		determinePaymentStatus(delegate.getBookingPaymentStatus(),
				delegate.getDelegatePaymentStatus());
	}

	private void determinePaymentStatus(PaymentStatus bookingPaymentStatus,
			PaymentStatus delegatePaymentStatus) {
		if (bookingPaymentStatus != null) {
			if (bookingPaymentStatus == PaymentStatus.PAID
					|| bookingPaymentStatus == PaymentStatus.Credit) {
				lstPaymentStatus.setValue(delegate.getBookingPaymentStatus());
			} else {
				lstPaymentStatus.setValue(delegate.getDelegatePaymentStatus());
			}
		}
	}

}
