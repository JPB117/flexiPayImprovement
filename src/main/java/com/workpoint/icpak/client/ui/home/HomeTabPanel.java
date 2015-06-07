package com.workpoint.icpak.client.ui.home;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.Tab;
import com.gwtplatform.mvp.client.TabData;
import com.workpoint.icpak.client.ui.admin.AbstractTabPanel;
import com.workpoint.icpak.client.ui.component.BulletListPanel;

public class HomeTabPanel extends AbstractTabPanel {

	private static HomeTabPanelUiBinder uiBinder = GWT
			.create(HomeTabPanelUiBinder.class);

	interface HomeTabPanelUiBinder extends UiBinder<Widget, HomeTabPanel> {
	}
	
//	/collapse="userBlockVisible" ng-controller="UserBlockController"
	//style="height: 0px;"
	
	@UiField HTMLPanel userDiv;
	@UiField BulletListPanel linksPanel;
	
	public HomeTabPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		userDiv.getElement().getStyle().setHeight(0.0, Unit.PX);
	}
	
	@Override
	public BulletListPanel getLinksPanel() {
		return linksPanel;
	}

	@Override
	protected Tab createNewTab(TabData tabData) {
		
		return new TabItem(tabData);
	}

	@Override
	public void setPanelContent(IsWidget panelContent) {
		
	}

}
