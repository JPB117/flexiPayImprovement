package com.workpoint.icpak.client.ui.login.createpassword;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
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
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealRootLayoutContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest.Builder;
import com.workpoint.icpak.client.place.NameTokens;
import com.workpoint.icpak.client.place.ParameterTokens;
import com.workpoint.icpak.client.security.CurrentUser;
import com.workpoint.icpak.client.service.AbstractAsyncCallback;
import com.workpoint.icpak.shared.api.SessionResource;
import com.workpoint.icpak.shared.api.UsersResource;
import com.workpoint.icpak.shared.model.UserDto;
import com.workpoint.icpak.shared.model.auth.ActionType;
import com.workpoint.icpak.shared.model.auth.CurrentUserDto;
import com.workpoint.icpak.shared.model.auth.LogInAction;
import com.workpoint.icpak.shared.model.auth.LogInResult;

public class ActivateAccountPresenter
		extends
		Presenter<ActivateAccountPresenter.IActivateAccountView, ActivateAccountPresenter.IActivateAccountProxy> {

	public interface IActivateAccountView extends View {

		void bindUser(UserDto user);

		boolean isValid();

		String getPassword();

		HasClickHandlers getSubmit();

		void setError(String string);

		void setLoginButtonEnabled(boolean b);

		void changeWidget(String reason);

		HasClickHandlers getResendButton();

		String getEmail();

		void showProcessing(boolean b);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.activateacc)
	@NoGatekeeper
	public interface IActivateAccountProxy extends
			ProxyPlace<ActivateAccountPresenter> {
	}

	@Inject
	RestDispatch requestHelper;

	@Inject
	PlaceManager placeManager;

	private final CurrentUser currentUser;
	private ResourceDelegate<UsersResource> usersDelegate;
	private UserDto user;
	private ResourceDelegate<SessionResource> sessionResource;
	private static final Logger LOGGER = Logger
			.getLogger(ActivateAccountPresenter.class.getName());

	@Inject
	public ActivateAccountPresenter(final EventBus eventBus,
			final IActivateAccountView view, final IActivateAccountProxy proxy,
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
		// RevealContentEvent.fire(this, MainPagePresenter.CONTENT_SLOT, this);
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		String userId = request.getParameter("uid", null);
		String reason = request.getParameter("r", null);

		if (reason != null) {
			getView().changeWidget(reason);
		} else if (userId == null) {
			// Error
			return;
		} else {
			getView().changeWidget("default");
			usersDelegate.withCallback(new AbstractAsyncCallback<UserDto>() {
				@Override
				public void onSuccess(UserDto user) {
					ActivateAccountPresenter.this.user = user;
					getView().bindUser(user);
				}
			}).getById(userId);
		}

	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().getSubmit().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (getView().isValid()) {
					if (user != null) {
						usersDelegate.withoutCallback().changePassword(
								user.getRefId(), getView().getPassword());
						Window.alert("Your Password has been saved successfully..,You will be now taken to Login Page.");
						postUserToLMS(user.getRefId());
					}
				}
			}
		});

		getView().getResendButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!getView().getEmail().isEmpty()) {
					getView().showProcessing(true);
					usersDelegate.withCallback(
							new AbstractAsyncCallback<UserDto>() {
								@Override
								public void onSuccess(UserDto result) {
									getView().showProcessing(false);
									sendResetEmail(result.getRefId());
								}
								
								public void onFailure(Throwable caught) {
									getView().showProcessing(false);
									Window.alert("Your Email Address was not found. Kindly contact ICPAK Support to update your Email");
								};
							}).getById(getView().getEmail());
				}
			}
		});
	}

	/*
	 * Integration with LMS;
	 */
	protected void postUserToLMS(String refId) {
		getView().showProcessing(true);
		usersDelegate.withCallback(new AbstractAsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				getView().showProcessing(false);
				// Window.alert(result);
				placeManager.revealPlace(new PlaceRequest.Builder().nameToken(
						NameTokens.login).build());
			}

			@Override
			public void onFailure(Throwable caught) {
				getView().showProcessing(false);
				super.onFailure(caught);
			}
		}).postUserLMS(refId);
	}

	private void sendResetEmail(String userRefId) {
		usersDelegate.withoutCallback().resetAccount(userRefId);
		Window.alert("Reset Password Instructions have been sent to your email");
		placeManager.revealPlace(new PlaceRequest.Builder().nameToken(
				NameTokens.login).build());
	}

	protected void executeLogin(String email, String password) {
		LogInAction logInAction = new LogInAction(email, password);
		usersDelegate.withCallback(new AbstractAsyncCallback<LogInResult>() {
			@Override
			public void onSuccess(LogInResult result) {
				// getView().clearLoginProgress();

				if (result.getCurrentUserDto().isLoggedIn()) {
					// setLoggedInCookie(result.getLoggedInCookie());
				}

				if (result.getActionType() == ActionType.VIA_COOKIE) {
					onLoginCallSucceededForCookie(result.getCurrentUserDto());
				} else {
					onLoginCallSucceeded(result.getCurrentUserDto());
				}

			}

			@Override
			public void onFailure(Throwable caught) {
				// getView().clearLoginProgress();
				super.onFailure(caught);
				getView()
						.setError(
								"Could authenticate user. Please report this to your administrator");
				LOGGER.log(
						Level.SEVERE,
						"callServerLoginAction(): Server failed to process login call.",
						caught);
			}
		}).execLogin(logInAction);
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
			getView().setError("Wrong username or password");
		}
	}

	void redirectToLoggedOnPage() {
		String token = placeManager.getCurrentPlaceRequest().getParameter(
				ParameterTokens.REDIRECT, NameTokens.getOnLoginDefaultPage());
		PlaceRequest placeRequest = new Builder().nameToken(token).build();

		placeManager.revealPlace(placeRequest);
	}

}