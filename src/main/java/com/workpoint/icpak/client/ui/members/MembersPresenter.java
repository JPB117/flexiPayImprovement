package com.workpoint.icpak.client.ui.members;

//import com.workpoint.icpak.shared.requests.CheckPasswordRequest;
//import com.workpoint.icpak.shared.requests.GetUserRequest;
//import com.workpoint.icpak.shared.requests.SaveUserRequest;
//import com.workpoint.icpak.shared.requests.UpdatePasswordRequest;
//import com.workpoint.icpak.shared.responses.CheckPasswordRequestResult;
//import com.workpoint.icpak.shared.responses.GetUserRequestResult;
//import com.workpoint.icpak.shared.responses.SaveUserResponse;
//import com.workpoint.icpak.shared.responses.UpdatePasswordResponse;
import java.util.List;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
import com.workpoint.icpak.client.place.NameTokens;
import com.workpoint.icpak.client.security.CurrentUser;
import com.workpoint.icpak.client.service.AbstractAsyncCallback;
import com.workpoint.icpak.client.ui.admin.TabDataExt;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.security.AdminGateKeeper;
import com.workpoint.icpak.client.ui.security.LoginGateKeeper;
import com.workpoint.icpak.shared.api.ApplicationFormResource;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;

public class MembersPresenter
		extends
		Presenter<MembersPresenter.IMembersView, MembersPresenter.IMembersProxy> {

	public interface IMembersView extends View {

		void bindApplications(List<ApplicationFormHeaderDto> result);

	}

	@ProxyCodeSplit
	@NameToken(NameTokens.members)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface IMembersProxy extends
			TabContentProxyPlace<MembersPresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(AdminGateKeeper adminGatekeeper) {
		TabDataExt data = new TabDataExt("Membership", "icon-users", 4,
				adminGatekeeper, true);
		return data;
	}

	private final CurrentUser currentUser;
	private ResourceDelegate<ApplicationFormResource> applicationDelegate;
	
	@Inject
	public MembersPresenter(final EventBus eventBus, final IMembersView view,
			final IMembersProxy proxy,
			ResourceDelegate<ApplicationFormResource> applicationDelegate,
			final CurrentUser currentUser) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
		this.applicationDelegate = applicationDelegate;
		this.currentUser = currentUser;
	}

	@Override
	protected void onBind() {
		super.onBind();

	}
	
	@Override
	protected void onReveal() {
		super.onReveal();
		loadData();
	}

	private void loadData() {
		applicationDelegate.withCallback(new AbstractAsyncCallback<List<ApplicationFormHeaderDto>>() {
			@Override
			public void onSuccess(List<ApplicationFormHeaderDto> result) {
				getView().bindApplications(result);
			}
		}).getAll(0, 100);
	}

	protected void save() {
	}

	@Override
	protected void onReset() {
		super.onReset();
	}

	String getApplicationRefId() {
		String applicationRefId = currentUser.getUser() == null ? null
				: currentUser.getUser().getApplicationRefId();

		return applicationRefId;
	}
}
