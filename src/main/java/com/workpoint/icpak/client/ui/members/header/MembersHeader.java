package com.workpoint.icpak.client.ui.members.header;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class MembersHeader extends Composite {

	private static ActivityHeaderUiBinder uiBinder = GWT
			.create(ActivityHeaderUiBinder.class);

	interface ActivityHeaderUiBinder extends UiBinder<Widget, MembersHeader> {
	}

	@UiField
	SpanElement spnTotalApplication;

	@UiField
	SpanElement spnTotalProcessed;

	@UiField
	SpanElement spnTotalPending;

	public MembersHeader() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setValues(int totalApplications, int totalProcessed, int totalPending){
		spnTotalApplication.setInnerText(totalApplications+"");
		spnTotalProcessed.setInnerText(totalProcessed+"");
		spnTotalPending.setInnerText(totalPending+"");
	}

}
