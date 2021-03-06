package com.workpoint.icpak.client.ui.home;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.events.LogoutEvent;
import com.workpoint.icpak.client.util.AppContext;

public class HomeView extends ViewImpl implements HomePresenter.IHomeView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, HomeView> {
	}

	@UiField
	HomeTabPanel tabPanel;
	@UiField
	HTMLPanel panelHome;
	@UiField
	HTMLPanel tabContent;
	@UiField
	Element panelContent;
	@UiField
	Element elSideBar;
	@UiField
	ActionLink aRemoveMenu;
	@UiField
	ActionLink aLogout;
	@UiField
	ActionLink aClearCache;
	@UiField
	DivElement divAlert;

	@Inject
	public HomeView(final Binder binder) {
		widget = binder.createAndBindUi(this);

		aRemoveMenu.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showSideBar(false);
			}
		});

		aLogout.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AppContext.fireEvent(new LogoutEvent());
				showSideBar(false);
			}
		});
		
		aClearCache.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				reloadAndClearCache();
			}
		});
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
		int middleHeight = totalHeight - topHeight;
		if (middleHeight > 0) {
			panelContent.getStyle().setHeight(middleHeight, Unit.PX);
		}
	}

	@Override
	public void showFullScreen(String message) {
		if (message.equals("show")) {
			elSideBar.addClassName("hide");
			panelContent.addClassName("ml0");
		} else {
			elSideBar.removeClassName("hide");
			panelContent.removeClassName("ml0");
		}
	}

	@Override
	public void showSideBar(boolean show) {
		if (show) {
			panelHome.addStyleName("enable-mobile-menu");
		} else {
			panelHome.removeStyleName("enable-mobile-menu");
		}
	}
	
	@Override
	public void showCacheMessage(boolean show){
		if (show) {
			divAlert.removeClassName("hide");
		} else {
			divAlert.addClassName("hide");
		}
	}
	
	
	public static native void reloadAndClearCache() /*-{
	  	$wnd.location.reload(true);
	}-*/;
}
