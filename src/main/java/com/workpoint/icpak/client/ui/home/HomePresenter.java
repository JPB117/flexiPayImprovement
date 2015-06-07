package com.workpoint.icpak.client.ui.home;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ChangeTabHandler;
import com.gwtplatform.mvp.client.RequestTabsHandler;
import com.gwtplatform.mvp.client.Tab;
import com.gwtplatform.mvp.client.TabContainerPresenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.TabView;
import com.gwtplatform.mvp.client.annotations.ChangeTab;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.annotations.RequestTabs;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.workpoint.icpak.client.ui.MainPagePresenter;
import com.workpoint.icpak.client.ui.events.ContextLoadedEvent;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent.ProcessingCompletedHandler;
import com.workpoint.icpak.client.ui.events.ProcessingEvent;
import com.workpoint.icpak.client.ui.events.ProcessingEvent.ProcessingHandler;

public class HomePresenter extends TabContainerPresenter<HomePresenter.IHomeView, HomePresenter.MyProxy> implements
ProcessingHandler, ProcessingCompletedHandler{

	public interface IHomeView extends TabView {
		//void bindAlerts(HashMap<TaskType, Integer> alerts);
		void refreshTabs();
		void changeTab(Tab tab, TabData tabData, String historyToken);
		void showmask(boolean b);
		//void bindAlerts(HashMap<TaskType, Integer> alerts);
		void showDocsList();
	}
	
	@ProxyStandard
	public interface MyProxy extends Proxy<HomePresenter> {
	}

	/**
     * This will be the event sent to our "unknown" child presenters, in order for them to register their tabs.
     */
    @RequestTabs
    public static final Type<RequestTabsHandler> SLOT_RequestTabs = new Type<RequestTabsHandler>();

    /**
     * Fired by child proxie's when their tab content is changed.
     */
    @ChangeTab
    public static final Type<ChangeTabHandler> SLOT_ChangeTab = new Type<ChangeTabHandler>();

    /**
     * Use this in leaf presenters, inside their {@link #revealInParent} method.
     */
    @ContentSlot
    public static final Type<RevealContentHandler<?>> SLOT_SetTabContent = new Type<RevealContentHandler<?>>();


	@ContentSlot
	public static final Type<RevealContentHandler<?>> DOCPOPUP_SLOT = new Type<RevealContentHandler<?>>();

	@ContentSlot
	public static final Type<RevealContentHandler<?>> DOCTREE_SLOT = new Type<RevealContentHandler<?>>();
		
	@Inject PlaceManager placeManager;
	
	@Inject
	public HomePresenter(final EventBus eventBus, final IHomeView view,
			final MyProxy proxy) {
		
		super(eventBus, view, proxy,SLOT_SetTabContent,SLOT_RequestTabs, SLOT_ChangeTab,MainPagePresenter.CONTENT_SLOT);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(ProcessingEvent.TYPE, this);
		addRegisteredHandler(ProcessingCompletedEvent.TYPE, this);
		
	}
	
	@Override
	protected void onReset() {
		super.onReset();
	}
	
	public void onProcessingCompleted(ProcessingCompletedEvent event) {
		getView().showmask(false);
	}
	

	@Override
	public void onProcessing(ProcessingEvent event) {		
		getView().showmask(true);
	}
	
	
	@ProxyEvent
	public void onContextLoaded(ContextLoadedEvent event){
		getView().refreshTabs();
	}
	
}
