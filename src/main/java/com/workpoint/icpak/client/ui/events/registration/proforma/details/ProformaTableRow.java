package com.workpoint.icpak.client.ui.events.registration.proforma.details;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.RowWidget;

public class ProformaTableRow extends RowWidget {

	private static ActivitiesTableRowUiBinder uiBinder = GWT
			.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends
			UiBinder<Widget, ProformaTableRow> {
	}

	public ProformaTableRow() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
