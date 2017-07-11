package com.workpoint.icpak.client.ui.events.registration.proforma.last;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.RowWidget;

public class ProformaLastRow extends RowWidget {

	private static ActivitiesTableRowUiBinder uiBinder = GWT
			.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends
			UiBinder<Widget, ProformaLastRow> {
	}

	public ProformaLastRow() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
