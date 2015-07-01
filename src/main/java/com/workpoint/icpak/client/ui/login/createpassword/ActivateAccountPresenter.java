package com.workpoint.icpak.client.ui.login.createpassword;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
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
import com.workpoint.icpak.client.ui.login.LoginPresenter;
import com.workpoint.icpak.client.ui.profile.password.PasswordWidget;
import com.workpoint.icpak.client.util.AppContext;
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

	}

	@ProxyCodeSplit
	@NameToken(NameTokens.activateacc)
	@NoGatekeeper
	public interface IActivateAccountProxy extends ProxyPlace<ActivateAccountPresenter> {
	}

	@Inject
	RestDispatch requestHelper;

	@Inject
	PlaceManager placeManager;

	private final CurrentUser currentUser;
	private ResourceDelegate<UsersResource> usersDelegate;
	private UserDto user;
	private ResourceDelegate<SessionResource> sessionResource;
	private static final Logger LOGGER = Logger.getLogger(ActivateAccountPresenter.class
			.getName());

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
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		String userId = request.getParameter("uid", null);
		if(userId==null){
			//Error
			return;
		}
		
		usersDelegate.withCallback(new AbstractAsyncCallback<UserDto>() {
			@Override
			public void onSuccess(UserDto user) {
				ActivateAccountPresenter.this.user = user;
				getView().bindUser(user);
				
			}
		}).getById(userId);
		
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().getSubmit().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(getView().isValid()){
					if(user!=null){
						usersDelegate.withoutCallback()
						.changePassword(user.getRefId(), getView().getPassword());
						
						executeLogin(user.getEmail(), getView().getPassword());
					}
					
				}
			}
		});
	}

	protected void executeLogin(String email, String password) {
		LogInAction logInAction = new LogInAction(email, password);
		usersDelegate.withCallback(
				new AbstractAsyncCallback<LogInResult>() {
					@Override
					public void onSuccess(LogInResult result) {
						//getView().clearLoginProgress();
						
						if (result.getCurrentUserDto().isLoggedIn()) {
							AppContext.setLoggedInCookie(result.getLoggedInCookie());
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
						//getView().clearLoginProgress();
						super.onFailure(caught);
						getView().setError("Could authenticate user. Please report this to your administrator");
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