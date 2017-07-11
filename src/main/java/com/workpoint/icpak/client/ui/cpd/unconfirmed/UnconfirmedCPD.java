package com.workpoint.icpak.client.ui.cpd.unconfirmed;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.ActionLink;

public class UnconfirmedCPD extends Composite {

	private static UnconfirmedCPDUiBinder uiBinder = GWT
			.create(UnconfirmedCPDUiBinder.class);

	interface UnconfirmedCPDUiBinder extends UiBinder<Widget, UnconfirmedCPD> {
	}

	@UiField
	HTMLPanel panelNew;

	@UiField
	HTMLPanel panelListing;
	
	@UiField 
	ActionLink aCreate;

	public UnconfirmedCPD() {
		initWidget(uiBinder.createAndBindUi(this));
		showRecordPanel(false);
		
		aCreate.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showRecordPanel(true);
			}
		});
	}

	private void showRecordPanel(boolean show) {
		if (show) {
			panelListing.setVisible(false);
			panelNew.setVisible(true);
		} else {
			panelListing.setVisible(true);
			panelNew.setVisible(false);
		}
	}

}
