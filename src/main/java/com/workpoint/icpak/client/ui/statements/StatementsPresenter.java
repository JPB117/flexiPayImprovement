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

import com.google.gwt.user.client.Window;
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
import com.workpoint.icpak.client.service.AbstractAsyncCallback;
import com.workpoint.icpak.client.ui.admin.TabDataExt;
import com.workpoint.icpak.client.ui.component.PagingConfig;
import com.workpoint.icpak.client.ui.component.PagingLoader;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.security.LoginGateKeeper;
import com.workpoint.icpak.shared.api.InvoiceResource;
import com.workpoint.icpak.shared.model.InvoiceDto;

public class StatementsPresenter extends
		Presenter<StatementsPresenter.IStatementsView, StatementsPresenter.IStatementsProxy>{

	public interface IStatementsView extends View {

		void bindInvoices(List<InvoiceDto> invoices);
		void setCount(Integer aCount);
		PagingPanel getPagingPanel();
		
	}
	
	@ProxyCodeSplit
	@NameToken(NameTokens.statements)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface IStatementsProxy extends TabContentProxyPlace<StatementsPresenter> {
	}
	
	@TabInfo(container = HomePresenter.class)
    static TabData getTabLabel(LoginGateKeeper adminGatekeeper) {
		TabDataExt data = new TabDataExt("Payment Summary","fa fa-briefcase",7,adminGatekeeper, true);
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
		
		getView().getPagingPanel().setLoader(new PagingLoader() {
			
			@Override
			public void load(int offset, int limit) {
				loadInvoices(offset, limit);
			}
		});
	}
	
	protected void save() {
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		loadData();
	}

	private void loadData() {
		
		invoiceDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
			@Override
			public void onSuccess(Integer aCount) {
				getView().setCount(aCount);
				PagingConfig config = getView().getPagingPanel().getConfig();
				loadInvoices(config.getOffset(),config.getLimit());
				
			}
		}).getCount();
		
	}

	protected void loadInvoices(int offset, int limit) {
		invoiceDelegate.withCallback(new AbstractAsyncCallback<List<InvoiceDto>>() {
			@Override
			public void onSuccess(List<InvoiceDto> result) {
				getView().bindInvoices(result);
			}
		}).getInvoices(offset, limit);
	}

}
