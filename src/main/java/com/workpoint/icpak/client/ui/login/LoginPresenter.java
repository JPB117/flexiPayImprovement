package com.workpoint.icpak.client.ui.login;

//import com.workpoint.icpak.shared.model.Value;
//import com.workpoint.icpak.shared.model.settingss.SETTINGNAME;
//import com.workpoint.icpak.shared.model.settingss.Setting;
//import com.workpoint.icpak.shared.requests.GetSettingsRequest;
//import com.workpoint.icpak.shared.requests.LoginRequest;
//import com.workpoint.icpak.shared.responses.GetSettingsResponse;
//import com.workpoint.icpak.shared.responses.LoginRequestResult;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
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
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealRootLayoutContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.workpoint.icpak.client.place.NameTokens;
import com.workpoint.icpak.client.service.AbstractAsyncCallback;
import com.workpoint.icpak.client.ui.security.LoginGateKeeper;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.api.UsersResource;
import com.workpoint.icpak.shared.model.UserDto;

public class LoginPresenter extends
		Presenter<LoginPresenter.ILoginView, LoginPresenter.MyProxy> {

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
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.login)
	public interface MyProxy extends ProxyPlace<LoginPresenter> {
	}

	@Inject
	RestDispatch requestHelper;

	@Inject
	PlaceManager placeManager;

	@Inject
	LoginGateKeeper gateKeeper;

	String redirect = null;

	private ResourceDelegate<UsersResource> usersDelegate;

	@Inject
	public LoginPresenter(final EventBus eventBus, final ILoginView view,
			final MyProxy proxy, 
			ResourceDelegate<UsersResource> usersDelegate
			) {
		super(eventBus, view, proxy);
		this.usersDelegate = usersDelegate;
		
	}

	@Override
	protected void revealInParent() {
		RevealRootLayoutContentEvent.fire(this, this);
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
	}

	@Override
	protected void onBind() {
		super.onBind();

		getView().getLoginBtn().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				login();
			}
		});

		KeyDownHandler keyHandler = new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					login();
				}
			}
		};

		getView().getUserNameBox().addKeyDownHandler(keyHandler);
		getView().getPasswordBox().addKeyDownHandler(keyHandler);
	}

	protected void onReset() {
		getView().clearViewItems(true);
	};


	protected void login() {
		if (getView().isValid()) {
			getView().clearErrors();
			getView().showLoginProgress();
			
			usersDelegate.withCallback(new AbstractAsyncCallback<UserDto>() {
				@Override
				public void onSuccess(UserDto result) {
					AppContext.setSessionValues(result,"XXXXX");
					placeManager.revealPlace(new PlaceRequest.Builder().nameToken(NameTokens.profile).build());
				}
			}).login(getView().getUsername(),
					getView().getPassword());

			// requestHelper.execute(new LoginRequest(getView().getUsername(),
			// getView().getPassword()),
			// new TaskServiceCallback<LoginRequestResult>() {
			// @Override
			// public void processResult(LoginRequestResult result) {
			// boolean isValid = result.isValid();
			// if(isValid){
			// AppContext.setSessionValues(result.getUser(),
			// result.getSessionId());
			//
			// if(redirect!=null){
			// History.newItem(redirect);
			// }else{
			// placeManager.revealDefaultPlace();
			// }
			// AppContext.reloadContext();
			// fireEvent(new LoadAlertsEvent());
			//
			// }else{
			// getView().clearLoginProgress();
			// getView().getPasswordBox().setText("");
			// getView().setError("Wrong username or password");
			// }
			// }
			//
			// @Override
			// public void onFailure(Throwable caught) {
			// getView().clearLoginProgress();
			// super.onFailure(caught);
			// getView().setError("Could authenticate user. Please report this to your administrator");
			// }
			// });
		}
	}

}
