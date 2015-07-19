package com.workpoint.icpak.client.ui.statements;

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
import com.workpoint.icpak.client.ui.security.LoginGateKeeper;
import com.workpoint.icpak.shared.api.InvoiceResource;
import com.workpoint.icpak.shared.model.InvoiceDto;

public class StatementsPresenter extends
		Presenter<StatementsPresenter.IStatementsView, StatementsPresenter.IStatementsProxy>{

	public interface IStatementsView extends View {

		void bindInvoices(List<InvoiceDto> invoices);
		
	}
	
	@ProxyCodeSplit
	@NameToken(NameTokens.statements)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface IStatementsProxy extends TabContentProxyPlace<StatementsPresenter> {
	}
	
	@TabInfo(container = HomePresenter.class)
    static TabData getTabLabel(LoginGateKeeper adminGatekeeper) {
		TabDataExt data = new TabDataExt("Statement","fa fa-briefcase",7,adminGatekeeper, true);
        return data;
    }

	@Inject CurrentUser user;
	private ResourceDelegate<InvoiceResource> invoiceDelegate;
	
	@Inject
	public StatementsPresenter(final EventBus eventBus, final IStatementsView view,
			final IStatementsProxy proxy, final ResourceDelegate<InvoiceResource> invoiceDelegate) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
		this.invoiceDelegate = invoiceDelegate;
	}

	@Override
	protected void onBind() {
		super.onBind();
		
	}
	
	protected void save() {
	}


	@Override
	protected void onReveal() {
		super.onReveal();
		loadData();
	}

	private void loadData() {
		
		invoiceDelegate.withCallback(new AbstractAsyncCallback<List<InvoiceDto>>() {
			@Override
			public void onSuccess(List<InvoiceDto> result) {
				getView().bindInvoices(result);
			}
		}).getInvoices();
	}

}
