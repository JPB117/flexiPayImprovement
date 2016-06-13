package com.workpoint.icpak.client.ui.header;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.workpoint.icpak.client.ui.events.AdminPageLoadEvent;
import com.workpoint.icpak.client.ui.events.AdminPageLoadEvent.AdminPageLoadHandler;
import com.workpoint.icpak.client.ui.events.AfterSaveEvent;
import com.workpoint.icpak.client.ui.events.AfterSaveEvent.AfterSaveHandler;
import com.workpoint.icpak.client.ui.events.ClientDisconnectionEvent;
import com.workpoint.icpak.client.ui.events.ClientDisconnectionEvent.ClientDisconnectionHandler;
import com.workpoint.icpak.client.ui.events.ContextLoadedEvent;
import com.workpoint.icpak.client.ui.events.ContextLoadedEvent.ContextLoadedHandler;
import com.workpoint.icpak.client.ui.events.FullScreenEvent;
import com.workpoint.icpak.client.ui.events.FullScreenEvent.FullScreenHandler;
import com.workpoint.icpak.client.ui.events.LoadAlertsEvent;
import com.workpoint.icpak.client.ui.events.LoadAlertsEvent.LoadAlertsHandler;
import com.workpoint.icpak.client.ui.events.LogoutEvent;
import com.workpoint.icpak.client.ui.notifications.NotificationsPresenter;
import com.workpoint.icpak.shared.model.UserDto;

public class HeaderPresenter extends
		PresenterWidget<HeaderPresenter.IHeaderView> implements
		AfterSaveHandler, AdminPageLoadHandler, ContextLoadedHandler,
		LoadAlertsHandler, FullScreenHandler, ClientDisconnectionHandler {

	public interface IHeaderView extends View {
		public HasClickHandlers getALogout();

		public void setLoggedInUser(UserDto currentUser);

		void showPopUpMessage(String message);

		public void showSmallLogo(String message);

		void showPopUpMessage(String message, boolean isSuccess);
	}

	@Inject
	PlaceManager placeManager;

	@Inject
	NotificationsPresenter notifications;

	boolean onFocus = true;

	@ContentSlot
	public static final Type<RevealContentHandler<?>> NOTIFICATIONS_SLOT = new Type<RevealContentHandler<?>>();

	@Inject
	public HeaderPresenter(final EventBus eventBus, final IHeaderView view) {

		super(eventBus, view);
		alertTimer.scheduleRepeating(alertReloadInterval);
	}

	static int alertReloadInterval = 60 * 1000 * 5; // 5 mins
	static long lastLoad = 0;
	private Timer alertTimer = new Timer() {
		@Override
		public void run() {
			loadAlertCount();
		}
	};

	private boolean isInternetConnectionLost = false;

	@Override
	protected void onBind() {
		super.onBind();
		this.addRegisteredHandler(AfterSaveEvent.TYPE, this);
		this.addRegisteredHandler(AdminPageLoadEvent.TYPE, this);
		this.addRegisteredHandler(ContextLoadedEvent.TYPE, this);
		this.addRegisteredHandler(LoadAlertsEvent.TYPE, this);
		this.addRegisteredHandler(FullScreenEvent.getType(), this);
		addRegisteredHandler(ClientDisconnectionEvent.TYPE, this);

		getView().getALogout().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new LogoutEvent());
			}
		});
	}

	/**
	 * Called too many times - reloading context/ alert counts from here slows
	 * the application down.
	 * 
	 * TODO: Find Out why
	 */
	@Override
	protected void onReset() {
		super.onReset();
		setInSlot(NOTIFICATIONS_SLOT, notifications);
	}

	protected void loadAlertCount() {
		alertTimer.cancel();

	}

	@Override
	public void onAfterSave(AfterSaveEvent event) {
		if (!isInternetConnectionLost) {
			getView().showPopUpMessage(event.getMessage(),
					event.isSuccessMessage());
		}
	}

	@Override
	public void onAdminPageLoad(AdminPageLoadEvent event) {
	}

	@Override
	public void onContextLoaded(ContextLoadedEvent event) {
		UserDto currentUser = event.getCurrentUser();
		getView().setLoggedInUser(currentUser);
	}

	@Override
	public void onLoadAlerts(LoadAlertsEvent event) {
		loadAlertCount();
	}

	@Override
	public void onFullScreen(FullScreenEvent event) {
		getView().showSmallLogo(event.getMessage());
	}

	@Override
	public void onClientDisconnection(ClientDisconnectionEvent event) {
		isInternetConnectionLost = true;
		getView()
				.showPopUpMessage(
						"Internet Connection Lost, Please check and try again..",
						false);
	}

}
