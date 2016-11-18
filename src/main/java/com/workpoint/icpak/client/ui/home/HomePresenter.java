package com.workpoint.icpak.client.ui.home;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
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
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest.Builder;
import com.workpoint.icpak.client.place.NameTokens;
import com.workpoint.icpak.client.service.AbstractAsyncCallback;
import com.workpoint.icpak.client.ui.MainPagePresenter;
import com.workpoint.icpak.client.ui.events.ContextLoadedEvent;
import com.workpoint.icpak.client.ui.events.FullScreenEvent;
import com.workpoint.icpak.client.ui.events.FullScreenEvent.FullScreenHandler;
import com.workpoint.icpak.client.ui.events.LogoutEvent;
import com.workpoint.icpak.client.ui.events.LogoutEvent.LogoutHandler;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent.ProcessingCompletedHandler;
import com.workpoint.icpak.client.ui.events.ProcessingEvent;
import com.workpoint.icpak.client.ui.events.ProcessingEvent.ProcessingHandler;
import com.workpoint.icpak.client.ui.events.ToggleSideBarEvent;
import com.workpoint.icpak.client.ui.events.ToggleSideBarEvent.ToggleSideBarHandler;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.api.SessionResource;
import com.workpoint.icpak.shared.model.Version;

public class HomePresenter extends TabContainerPresenter<HomePresenter.IHomeView, HomePresenter.MyProxy> implements
		ProcessingHandler, ProcessingCompletedHandler, LogoutHandler, FullScreenHandler, ToggleSideBarHandler {

	public interface IHomeView extends TabView {
		void refreshTabs();

		void changeTab(Tab tab, TabData tabData, String historyToken);

		void showmask(boolean b);

		void showDocsList();

		void setMiddleHeight();

		void showFullScreen(String message);

		void showSideBar(boolean b);

		void showCacheMessage(boolean show);
	}

	@ProxyStandard
	public interface MyProxy extends Proxy<HomePresenter> {
	}

	/**
	 * This will be the event sent to our "unknown" child presenters, in order
	 * for them to register their tabs.
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

	@Inject
	PlaceManager placeManager;

	private ResourceDelegate<SessionResource> sessionResource;

	public static final String VERSION = "v1.0.0-5-g6a189fd";

	@Inject
	public HomePresenter(final EventBus eventBus, final IHomeView view, final MyProxy proxy,
			ResourceDelegate<SessionResource> sessionResource) {

		super(eventBus, view, proxy, SLOT_SetTabContent, SLOT_RequestTabs, SLOT_ChangeTab,
				MainPagePresenter.CONTENT_SLOT);
		this.sessionResource = sessionResource;
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(ProcessingEvent.TYPE, this);
		addRegisteredHandler(ProcessingCompletedEvent.TYPE, this);
		addRegisteredHandler(LogoutEvent.TYPE, this);
		addRegisteredHandler(FullScreenEvent.getType(), this);
		addRegisteredHandler(ToggleSideBarEvent.getType(), this);
	}

	@Override
	protected void onReset() {
		super.onReset();
		getView().setMiddleHeight();
		getView().refreshTabs();
	}

	public void onProcessingCompleted(ProcessingCompletedEvent event) {
		getView().showmask(false);
	}

	@Override
	public void onProcessing(ProcessingEvent event) {
		getView().showmask(true);
	}

	@ProxyEvent
	public void onContextLoaded(ContextLoadedEvent event) {
		getView().refreshTabs();
	}

	@Override
	public void onLogout(LogoutEvent event) {
		fireEvent(new ProcessingEvent());
		sessionResource.withCallback(new AbstractAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				AppContext.clear();
				PlaceRequest placeRequest = new Builder().nameToken(NameTokens.login).build();
				placeManager.revealPlace(placeRequest);
				fireEvent(new ProcessingCompletedEvent());
			}
		}).logout();
	}

	@Override
	public void onFullScreen(FullScreenEvent event) {
		getView().showFullScreen(event.getMessage());
	}

	@Override
	public void onToggleSideBar(ToggleSideBarEvent event) {
		getView().showSideBar(event.getisShown());
	}

}
