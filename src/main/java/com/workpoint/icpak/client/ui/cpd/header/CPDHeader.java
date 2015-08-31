package com.workpoint.icpak.client.ui.cpd.header;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class CPDHeader extends Composite {

	private static ActivityHeaderUiBinder uiBinder = GWT
			.create(ActivityHeaderUiBinder.class);

	interface ActivityHeaderUiBinder extends UiBinder<Widget, CPDHeader> {
	}

	@UiField
	SpanElement spnTransactions;

	@UiField
	SpanElement spnAmount;

	@UiField
	SpanElement spnDebug;

	@UiField
	SpanElement spnBalance;
	
	@UiField DivElement title1;
	@UiField DivElement title2;
	@UiField DivElement title3;


	public CPDHeader() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setTitles(String text1, String text2, String text3){
		title1.setInnerText(text1);
		title2.setInnerText(text2);
		title3.setInnerText(text3);

	}

	public void setValues(int totalHrs, int confirmed, int unconfirmed) {
		spnTransactions.setInnerText(totalHrs+"");
		spnAmount.setInnerText(confirmed+"");
		spnBalance.setInnerText(unconfirmed+"");
	}

}
