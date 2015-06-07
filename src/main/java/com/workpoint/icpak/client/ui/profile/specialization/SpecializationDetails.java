package com.workpoint.icpak.client.ui.profile.specialization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class SpecializationDetails extends Composite {

	private static TillDetailsUiBinder uiBinder = GWT
			.create(TillDetailsUiBinder.class);


	interface TillDetailsUiBinder extends UiBinder<Widget, SpecializationDetails> {
	}

	public SpecializationDetails() {
		initWidget(uiBinder.createAndBindUi(this));
		
	}

}
