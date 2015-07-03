package com.workpoint.icpak.client.ui.cpd.unconfirmed;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class UnConfirmedCPD extends Composite {

	private static UnConfirmedCPDUiBinder uiBinder = GWT
			.create(UnConfirmedCPDUiBinder.class);

	interface UnConfirmedCPDUiBinder extends UiBinder<Widget, UnConfirmedCPD> {
	}

	public UnConfirmedCPD() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
