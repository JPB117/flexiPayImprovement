package com.workpoint.icpak.client.ui.accomodation;

//import com.workpoint.icpak.shared.requests.CheckPasswordRequest;
//import com.workpoint.icpak.shared.requests.GetUserRequest;
//import com.workpoint.icpak.shared.requests.SaveUserRequest;
//import com.workpoint.icpak.shared.requests.UpdatePasswordRequest;
//import com.workpoint.icpak.shared.responses.CheckPasswordRequestResult;
//import com.workpoint.icpak.shared.responses.GetUserRequestResult;
//import com.workpoint.icpak.shared.responses.SaveUserResponse;
//import com.workpoint.icpak.shared.responses.UpdatePasswordResponse;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
import com.workpoint.icpak.client.place.NameTokens;
import com.workpoint.icpak.client.ui.admin.TabDataExt;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.security.LoginGateKeeper;

public class AccomodationPresenter
		extends
		Presenter<AccomodationPresenter.IAccomodationView, AccomodationPresenter.IAccomodationProxy> {

	public interface IAccomodationView extends View {

	}

	@ProxyCodeSplit
	@NameToken(NameTokens.accomodation)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface IAccomodationProxy extends
			TabContentProxyPlace<AccomodationPresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(LoginGateKeeper adminGatekeeper) {
		TabDataExt data = new TabDataExt("Accomodation", "fa fa-bed", 3,
				adminGatekeeper, true);
		return data;
	}

	@Inject
	public AccomodationPresenter(final EventBus eventBus,
			final IAccomodationView view, final IAccomodationProxy proxy) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
	}

	@Override
	protected void onBind() {
		super.onBind();

	}

	protected void save() {
	}

	@Override
	protected void onReset() {
		super.onReset();
	}

}
