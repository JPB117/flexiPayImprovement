package com.workpoint.icpak.client.ui.home;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Tab;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.ViewImpl;

public class HomeView extends ViewImpl implements HomePresenter.IHomeView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, HomeView> {
	}

	@UiField
	HomeTabPanel tabPanel;
	@UiField
	HTMLPanel tabContent;

	@UiField
	Element panelContent;

	@Inject
	public HomeView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public Tab addTab(TabData tabData, String historyToken) {
		return tabPanel.addTab(tabData, historyToken);
	}

	@Override
	public void removeTab(Tab tab) {
		tabPanel.removeTab(tab);
	}

	@Override
	public void removeTabs() {
		tabPanel.removeTabs();
	}

	@Override
	public void setActiveTab(Tab tab) {
		tabPanel.setActiveTab(tab);
	}

	@Override
	public void changeTab(Tab tab, TabData tabData, String historyToken) {
		tabPanel.changeTab(tab, tabData, historyToken);
	}

	@Override
	public void refreshTabs() {
		tabPanel.refreshTabs();
	}

	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if (slot == HomePresenter.SLOT_SetTabContent) {
			tabContent.clear();
			if (content != null) {
				tabContent.add(content);
			}
		} else {
			super.setInSlot(slot, content);
		}
	}

	@Override
	public void showDocsList() {
	}

	@Override
	public void showmask(boolean processing) {
		if (processing) {
			tabContent.addStyleName("whirl traditional");
		} else {
			tabContent.removeStyleName("whirl traditional");
		}
	}

	@Override
	public void setMiddleHeight() {
		int totalHeight = Window.getClientHeight();
		int topHeight = 50;
		// int footerHeight = divFooter.getOffsetHeight();
		// int topicsHeight = divHeaderContainer.getOffsetHeight();
		int middleHeight = totalHeight - topHeight;

		// Window.alert("\nTotalHeight:" + totalHeight + "MiddleHeight>>"
		// + middleHeight + "TopHeight" + topHeight);

		if (middleHeight > 0) {
			panelContent.getStyle().setHeight(middleHeight, Unit.PX);
		}
	}

}
