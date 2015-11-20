package com.workpoint.icpak.client.ui.component;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;

public class PagingTable extends TableView {

	PagingPanel panel = null;

	public PagingTable() {
		super();
		panel = new PagingPanel();
		panelPaging.clear();
		panelPaging.add(panel);
	}

	public PagingPanel getPagingPanel() {
		return panel;
	}

	public String getSelectedTownName() {
		return listTowns.getValue().getDisplayName();
	}

}
