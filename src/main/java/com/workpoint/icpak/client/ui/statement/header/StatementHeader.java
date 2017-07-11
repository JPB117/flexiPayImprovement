package com.workpoint.icpak.client.ui.statement.header;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class StatementHeader extends Composite {

	private static ActivityHeaderUiBinder uiBinder = GWT
			.create(ActivityHeaderUiBinder.class);

	interface ActivityHeaderUiBinder extends UiBinder<Widget, StatementHeader> {
	}

	@UiField
	SpanElement spnTotalDebit;
	@UiField
	SpanElement spnTotalCredit;
	@UiField
	SpanElement spnBalance;

	public StatementHeader() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setSummary(String totalDebit, String totalCredit,
			String totalBalance) {
		spnTotalDebit.setInnerText(totalDebit);
		spnTotalCredit.setInnerText(totalCredit);
		if (totalBalance.contains("-")) {
			spnBalance.addClassName("text-danger");
		} else {
			spnBalance.addClassName("text-success");
		}
		spnBalance.setInnerText(totalBalance);
	}

}
