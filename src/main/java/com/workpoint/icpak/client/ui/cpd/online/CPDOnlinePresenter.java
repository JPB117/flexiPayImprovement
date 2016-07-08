package com.workpoint.icpak.client.ui.cpd.online;

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
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.workpoint.icpak.client.place.NameTokens;
import com.workpoint.icpak.client.security.CurrentUser;
import com.workpoint.icpak.client.ui.admin.TabDataExt;
import com.workpoint.icpak.client.ui.events.ToggleSideBarEvent;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.security.TestingGateKeeper;
import com.workpoint.icpak.shared.api.MemberResource;

public class CPDOnlinePresenter extends
		Presenter<CPDOnlinePresenter.ICPDView, CPDOnlinePresenter.ICPDProxy> {

	public interface ICPDView extends View {
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.cpdOnline)
	@UseGatekeeper(TestingGateKeeper.class)
	public interface ICPDProxy extends TabContentProxyPlace<CPDOnlinePresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(TestingGateKeeper adminGatekeeper) {
		TabDataExt data = new TabDataExt("CPD Online", "fa fa-book", 6,
				adminGatekeeper, true);
		return data;
	}

	protected final ResourceDelegate<MemberResource> memberDelegate;
	protected final CurrentUser currentUser;

	@Inject
	public CPDOnlinePresenter(final EventBus eventBus, final ICPDView view,
			final ICPDProxy proxy,
			final ResourceDelegate<MemberResource> memberDelegate,
			final CurrentUser currentUser) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
		this.memberDelegate = memberDelegate;
		this.currentUser = currentUser;
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		fireEvent(new ToggleSideBarEvent(false));
	}

}
