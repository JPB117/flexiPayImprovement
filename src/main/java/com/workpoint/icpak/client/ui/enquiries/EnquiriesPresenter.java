package com.workpoint.icpak.client.ui.enquiries;

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

public class EnquiriesPresenter extends
		Presenter<EnquiriesPresenter.IEnquiriesView, EnquiriesPresenter.IEnquiriesProxy>{

	public interface IEnquiriesView extends View {
		
	}
	
	@ProxyCodeSplit
	@NameToken(NameTokens.enquiries)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface IEnquiriesProxy extends TabContentProxyPlace<EnquiriesPresenter> {
	}
	
	@TabInfo(container = HomePresenter.class)
    static TabData getTabLabel(LoginGateKeeper adminGatekeeper) {
		TabDataExt data = new TabDataExt("Make an Enquiry","fa fa-bullhorn",5,adminGatekeeper, true);
        return data;
    }

	@Inject
	public EnquiriesPresenter(final EventBus eventBus, final IEnquiriesView view,final IEnquiriesProxy proxy) {
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
