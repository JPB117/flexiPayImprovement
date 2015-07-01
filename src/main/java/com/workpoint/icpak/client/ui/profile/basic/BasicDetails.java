package com.workpoint.icpak.client.ui.profile.basic;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.ActionLink;

public class BasicDetails extends Composite {

	private static TillDetailsUiBinder uiBinder = GWT
			.create(TillDetailsUiBinder.class);


	interface TillDetailsUiBinder extends UiBinder<Widget, BasicDetails> {
	}

	public BasicDetails() {
		initWidget(uiBinder.createAndBindUi(this));
		
	}

}
