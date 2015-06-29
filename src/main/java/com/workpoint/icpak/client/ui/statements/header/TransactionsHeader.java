package com.workpoint.icpak.client.ui.statements.header;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class TransactionsHeader extends Composite {

	private static ActivityHeaderUiBinder uiBinder = GWT
			.create(ActivityHeaderUiBinder.class);

	interface ActivityHeaderUiBinder extends
			UiBinder<Widget, TransactionsHeader> {
	}

	@UiField
	SpanElement spnTransactions;

	@UiField
	SpanElement spnAmount;

	@UiField
	SpanElement spnBalance;

	public TransactionsHeader() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
