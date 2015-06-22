package com.workpoint.icpak.client.ui.events.registration.proforma;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ProformaInvoice extends Composite {

	private static ProformaInvoiceUiBinder uiBinder = GWT
			.create(ProformaInvoiceUiBinder.class);

	interface ProformaInvoiceUiBinder extends UiBinder<Widget, ProformaInvoice> {
	}

	public ProformaInvoice() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
