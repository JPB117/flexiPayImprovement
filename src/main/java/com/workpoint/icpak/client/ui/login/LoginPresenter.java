package com.workpoint.icpak.client.ui.login;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.NewCookie;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.TextBox;
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
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.api.ApiParameters;
import com.workpoint.icpak.shared.api.SessionResource;
import com.workpoint.icpak.shared.api.UsersResource;
import com.workpoint.icpak.shared.model.auth.ActionType;
import com.workpoint.icpak.shared.model.auth.CurrentUserDto;
import com.workpoint.icpak.shared.model.auth.LogInAction;
import com.workpoint.icpak.shared.model.auth.LogInResult;

public class LoginPresenter extends
		Presenter<LoginPresenter.ILoginView, LoginPresenter.ILoginProxy> {

	public interface ILoginView extends View {
		String getUsername();

		String getPassword();

		Anchor getLoginBtn();

		TextBox getPasswordBox();

		boolean isValid();

		void setError(String err);

		void showLoginProgress();

		void clearLoginProgress();

		void clearViewItems(boolean status);

		TextBox getUserNameBox();

		void clearErrors();

		void setOrgName(String orgName);

		void setLoginButtonEnabled(boolean b);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.login)
	@NoGatekeeper
	public interface ILoginProxy extends ProxyPlace<LoginPresenter> {
	}

	private final CurrentUser currentUser;

	private static final int REMEMBER_ME_DAYS = 14;

	@Inject
	RestDispatch requestHelper;

	@Inject
	PlaceManager placeManager;

	private static final Logger LOGGER = Logger.getLogger(LoginPresenter.class
			.getName());
	private ResourceDelegate<UsersResource> usersDelegate;

	private ResourceDelegate<SessionResource> sessionResource;

	@Inject
	public LoginPresenter(final EventBus eventBus, final ILoginView view,
			final ILoginProxy proxy, final CurrentUser currentUser,
			ResourceDelegate<UsersResource> usersDelegate,
			ResourceDelegate<SessionResource> sessionResource) {
		super(eventBus, view, proxy);
		this.usersDelegate = usersDelegate;
		this.currentUser = currentUser;
		this.sessionResource = sessionResource;
	}

	@Override
	protected void revealInParent() {
		RevealRootLayoutContentEvent.fire(this, this);
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		if (AppContext.hasLoggedInCookie()) {
			tryLoggingInWithCookieFirst();
		}

	}

	@Override
	public boolean useManualReveal() {
		return false;
	}

	@Override
	protected void onBind() {
		super.onBind();

		getView().getLoginBtn().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (getView().isValid()) {
					callServerLoginAction(new LogInAction(getView()
							.getUsername(), getView().getPassword()));
				}
			}
		});

		KeyDownHandler keyHandler = new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					if (getView().isValid()) {
						callServerLoginAction(new LogInAction(getView()
								.getUsername(), getView().getPassword()));
					}
				}
			}
		};

		getView().getUserNameBox().addKeyDownHandler(keyHandler);
		getView().getPasswordBox().addKeyDownHandler(keyHandler);
	}

	protected void onReset() {
		getView().clearViewItems(true);
	};

	private void callServerLoginAction(final LogInAction logInAction) {
		getView().clearErrors();
		getView().showLoginProgress();

		usersDelegate.withCallback(
		// ManualRevealCallback.create(this,
				new AbstractAsyncCallback<LogInResult>() {
					@Override
					public void onSuccess(LogInResult result) {
						getView().clearLoginProgress();

						if (result.getCurrentUserDto().isLoggedIn()) {
							setLoggedInCookie(result.getLoggedInCookie());
						}

						if (result.getActionType() == ActionType.VIA_COOKIE) {
							onLoginCallSucceededForCookie(result
									.getCurrentUserDto());
						} else {
							onLoginCallSucceeded(result.getCurrentUserDto());
						}

					}

					@Override
					public void onFailure(Throwable caught) {
						super.onFailure(caught);
						getView().clearLoginProgress();
						LOGGER.log(Level.SEVERE,
								"Wrong username or password......");
						getView().setError("Wrong username or password");
						// getView().setError("Could authenticate user. Please report this to your administrator");
						// LOGGER.log(
						// Level.SEVERE,
						// "callServerLoginAction(): Server failed to process login call.",
						// caught);
					}
				}// )
				).execLogin(logInAction);

	}

	private void onLoginCallSucceededForCookie(CurrentUserDto currentUserDto) {
		getView().setLoginButtonEnabled(true);

		if (currentUserDto.isLoggedIn()) {
			onLoginCallSucceeded(currentUserDto);
		}
	}

	private void onLoginCallSucceeded(CurrentUserDto currentUserDto) {

		if (currentUserDto.isLoggedIn()) {
			currentUser.fromCurrentUserDto(currentUserDto);
			redirectToLoggedOnPage();
		} else {
			LOGGER.log(Level.SEVERE, "Wrong username or password......");
			getView().setError("Wrong username or password");
		}
	}

	private void tryLoggingInWithCookieFirst() {
		getView().setLoginButtonEnabled(false);

		String loggedInCookie = AppContext.getLoggedInCookie();
		LogInAction logInAction = new LogInAction(loggedInCookie);
		callServerLoginAction(logInAction);
	}

	public void redirectToLoggedOnPage() {
		String token = placeManager.getCurrentPlaceRequest().getParameter(
				ParameterTokens.REDIRECT, NameTokens.getOnLoginDefaultPage());
		PlaceRequest placeRequest = new Builder().nameToken(token).build();

		placeManager.revealPlace(placeRequest);
	}

	public void setLoggedInCookie(String value) {
		String path = "/";
		String domain = getDomain();
		int maxAge = REMEMBER_ME_DAYS * 24 * 60 * 60 * 1000;
		boolean secure = false;

		NewCookie newCookie = new NewCookie(ApiParameters.LOGIN_COOKIE, value,
				path, domain, "", maxAge, secure);
		sessionResource.withoutCallback().rememberMe(newCookie);

		// LOGGER.info("LoginPresenter.setLoggedInCookie() Set client cookie="
		// + value);
	}

	public static String getDomain() {
		String domain = GWT.getHostPageBaseURL().replaceAll(".*//", "")
				.replaceAll("/", "").replaceAll(":.*", "");

		return "localhost".equalsIgnoreCase(domain) ? null : domain;
	}

}