package com.workpoint.icpak.client.ui.component;

public class PagingTable extends TableView{

	PagingPanel panel = null;
	public PagingTable() {
		super();
		panel = new PagingPanel();
		overalContainer.add(panel);
	}
	
	public PagingPanel getPagingPanel(){
		return panel;
	}
}
