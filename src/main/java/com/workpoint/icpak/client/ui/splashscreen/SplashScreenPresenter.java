package com.workpoint.icpak.client.ui.splashscreen;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.client.RestDispatch;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ManualRevealCallback;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealRootLayoutContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest.Builder;
import com.workpoint.icpak.client.place.NameTokens;
import com.workpoint.icpak.client.place.ParameterTokens;
import com.workpoint.icpak.client.security.CurrentUser;
import com.workpoint.icpak.client.service.AbstractAsyncCallback;
import com.workpoint.icpak.client.ui.events.ContextLoadedEvent;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.api.ApiParameters;
import com.workpoint.icpak.shared.api.SessionResource;
import com.workpoint.icpak.shared.api.UsersResource;
import com.workpoint.icpak.shared.model.auth.ActionType;
import com.workpoint.icpak.shared.model.auth.CurrentUserDto;
import com.workpoint.icpak.shared.model.auth.LogInAction;
import com.workpoint.icpak.shared.model.auth.LogInResult;

public class SplashScreenPresenter
		extends
		Presenter<SplashScreenPresenter.ILoginView, SplashScreenPresenter.ILoginProxy> {

	public interface ILoginView extends View {
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.splash)
	@NoGatekeeper
	public interface ILoginProxy extends ProxyPlace<SplashScreenPresenter> {
	}

	@Inject
	RestDispatch requestHelper;
	@Inject
	PlaceManager placeManager;
	private static final Logger LOGGER = Logger
			.getLogger(SplashScreenPresenter.class.getName());
	private ResourceDelegate<UsersResource> usersDelegate;
	private final CurrentUser currentUser;
	private ResourceDelegate<SessionResource> sessionResource;

	private static final int REMEMBER_ME_DAYS = 14;

	@Inject
	public SplashScreenPresenter(final EventBus eventBus,
			final ILoginView view, final ILoginProxy proxy,
			final CurrentUser currentUser,
			ResourceDelegate<UsersResource> usersDelegate,
			ResourceDelegate<SessionResource> sessionResource) {
		super(eventBus, view, proxy);
		this.currentUser = currentUser;
		this.usersDelegate = usersDelegate;
		this.sessionResource = sessionResource;
	}

	@Override
	protected void revealInParent() {
		RevealRootLayoutContentEvent.fire(this, this);
	}

	private void tryLoggingInWithCookieFirst() {
		String loggedInCookie = AppContext.getLoggedInCookie();
		LogInAction logInAction = new LogInAction(loggedInCookie);
		callServerLoginAction(logInAction);
	}

	private void callServerLoginAction(final LogInAction logInAction) {
		usersDelegate.withCallback(new AbstractAsyncCallback<LogInResult>() {
			@Override
			public void onSuccess(LogInResult result) {
				if (result.getCurrentUserDto().isLoggedIn()) {
					setLoggedInCookie(result.getLoggedInCookie());
				}
				onLoginCallSucceeded(result.getCurrentUserDto());
			}

			@Override
			public void onFailure(Throwable caught) {
				super.onFailure(caught);
				// Show Login Screen

			}
		}).execLogin(logInAction);

	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		tryLoggingInWithCookieFirst();
	}

	private void onLoginCallSucceeded(CurrentUserDto currentUserDto) {
		if (currentUserDto.isLoggedIn()) {
			currentUser.fromCurrentUserDto(currentUserDto);
			fireEvent(new ContextLoadedEvent(currentUser.getUser(), null));
			redirectToLoggedOnPage();
		} else {
			PlaceRequest placeRequest = new Builder().nameToken(
					NameTokens.login).build();
			placeManager.revealPlace(placeRequest);
			// Window.alert("To Login>>");
		}
	}

	public void redirectToLoggedOnPage() {
		String redirect = "";
		String redirectType = "";
		String resource = "";

		if (AppContext.isCurrentUserAdmin()) {
			redirect = NameTokens.getAdminDefaultPage();
		} else {
			redirect = NameTokens.getOnLoginDefaultPage();
		}

		Set<String> allStrings = placeManager.getCurrentPlaceRequest()
				.getParameterNames();
		Map<String, String> params = new HashMap<String, String>();

		for (Iterator<String> it = allStrings.iterator(); it.hasNext();) {
			String item = it.next();
			params.put(item, placeManager.getCurrentPlaceRequest()
					.getParameter(item, ""));
		}

		String token = placeManager.getCurrentPlaceRequest().getParameter(
				ParameterTokens.REDIRECT, redirect);
		String type = placeManager.getCurrentPlaceRequest().getParameter(
				ParameterTokens.REDIRECTTYPE, redirectType);
		String resourceValue = placeManager.getCurrentPlaceRequest()
				.getParameter(ParameterTokens.RESOURCE, resource);

		if (type.equals("website")) {
			Window.Location.replace("https://icpak.com/resource/"
					+ resourceValue);
		} else {
			PlaceRequest placeRequest = new Builder().nameToken(token)
					.with(params).build();
			placeManager.revealPlace(placeRequest);
		}

	}

	@Override
	protected void onBind() {
		super.onBind();
	}

	protected void onReset() {
	};

	@Override
	protected void onReveal() {
		super.onReveal();
	}

	public void setLoggedInCookie(String value) {
		String path = "/";
		String domain = getDomain();
		// Window.alert(domain);
		int maxAge = REMEMBER_ME_DAYS * 24 * 60 * 60 * 1000;
		Date expires = DateUtils.addDays(new Date(), REMEMBER_ME_DAYS);
		boolean secure = true;

		Cookies.setCookie(ApiParameters.LOGIN_COOKIE, value, expires, domain,
				path, secure);

		setCookie(ApiParameters.LOGIN_COOKIE, value);

		// NewCookie newCookie = new NewCookie(ApiParameters.LOGIN_COOKIE,
		// value,
		// path, domain, "", maxAge, secure);
		// sessionResource.withoutCallback().rememberMe(newCookie);

		// LOGGER.info("LoginPresenter.setLoggedInCookie() Set client cookie="
		// + value);
	}

	public static String getDomain() {
		String domain = GWT.getHostPageBaseURL().replaceAll(".*//", "")
				.replaceAll("/", "").replaceAll(":.*", "");

		return "localhost".equalsIgnoreCase(domain) ? null : domain;
	}

	public static native void setCookie(String cname, String cvalue) /*-{
																		var d = new Date();
																		d.setTime(d.getTime() + (10*24*60*60*1000));
																		var expires = "expires="+d.toUTCString();
																		$doc.cookie = cname + "=" + cvalue + "; " + expires+"; path=/";
																		}-*/;

}
