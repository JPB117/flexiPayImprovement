package com.workpoint.icpak.client.ui.eventsandseminars.delegates.updatepayment;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.shared.model.PaymentMode;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.events.DelegateDto;

public class UpdatePaymentWidget extends Composite {

	private static UpdatePaymentWidgetUiBinder uiBinder = GWT
			.create(UpdatePaymentWidgetUiBinder.class);

	@UiField
	DropDownList<PaymentStatus> lstPaymentStatus;
	@UiField
	DropDownList<PaymentMode> lstPaymentMode;
	@UiField
	TextField txtReceiptNo;
	@UiField
	TextField txtClearanceNo;
	@UiField
	CheckBox chckIsCredit;
	@UiField
	TextField txtLPO;

	private DelegateDto delegate;

	interface UpdatePaymentWidgetUiBinder extends
			UiBinder<Widget, UpdatePaymentWidget> {
	}

	public UpdatePaymentWidget(DelegateDto delegate) {
		this.delegate = delegate;
		initWidget(uiBinder.createAndBindUi(this));
	}

	public DelegateDto getDelegate() {
		DelegateDto delegate = new DelegateDto();
		// delegate.setPaymentStatus(lstPaymentStatus.getValue());
		return null;
	}

	public void setDelegateValues() {

	}

}
