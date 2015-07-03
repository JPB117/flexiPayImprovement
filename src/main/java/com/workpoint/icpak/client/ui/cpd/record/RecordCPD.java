package com.workpoint.icpak.client.ui.cpd.record;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.ActionLink;

public class RecordCPD extends Composite {

	private static UnconfirmedCPDUiBinder uiBinder = GWT
			.create(UnconfirmedCPDUiBinder.class);

	interface UnconfirmedCPDUiBinder extends UiBinder<Widget, RecordCPD> {
	}

	@UiField
	HTMLPanel panelForm;

	@UiField
	HTMLPanel panelCategories;

	@UiField
	ActionLink aCategoryD;

	public RecordCPD() {
		initWidget(uiBinder.createAndBindUi(this));
		
		aCategoryD.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showForm(true);
			}
		});
		
		showForm(false);
	}

	protected void showForm(boolean show) {
		if (show) {
			panelForm.setVisible(true);
			panelCategories.setVisible(false);
		} else {
			panelForm.setVisible(false);
			panelCategories.setVisible(true);
		}

	}

}
