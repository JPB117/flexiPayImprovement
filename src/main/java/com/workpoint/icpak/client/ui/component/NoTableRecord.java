package com.workpoint.icpak.client.ui.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class NoTableRecord extends Composite {

	private static NoTableRecordUiBinder uiBinder = GWT
			.create(NoTableRecordUiBinder.class);

	interface NoTableRecordUiBinder extends UiBinder<Widget, NoTableRecord> {
	}

	public NoTableRecord() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
