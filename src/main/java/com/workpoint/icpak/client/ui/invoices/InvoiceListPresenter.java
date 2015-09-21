package com.workpoint.icpak.client.ui.invoices;

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
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.api.InvoiceResource;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.InvoiceSummary;

public class InvoiceListPresenter
		extends
		Presenter<InvoiceListPresenter.IInvoiceView, InvoiceListPresenter.IInvoiceProxy> {

	public interface IInvoiceView extends View {

		void bindInvoices(List<InvoiceDto> invoices);

		void setCount(Integer aCount);

		PagingPanel getPagingPanel();

		void bindSummary(InvoiceSummary result);

	}

	@ProxyCodeSplit
	@NameToken(NameTokens.invoices)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface IInvoiceProxy extends
			TabContentProxyPlace<InvoiceListPresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(LoginGateKeeper adminGatekeeper) {
		TabDataExt data = new TabDataExt("Invoicing Summary", "fa fa-briefcase",
				7, adminGatekeeper, true);
		return data;
	}

	@Inject
	CurrentUser user;
	private ResourceDelegate<InvoiceResource> invoiceDelegate;

	@Inject
	public InvoiceListPresenter(final EventBus eventBus,
			final IInvoiceView view, final IInvoiceProxy proxy,
			final ResourceDelegate<InvoiceResource> invoiceDelegate) {
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
		String memberId = (AppContext.isCurrentUserAdmin() ? "ALL" : AppContext
				.getContextUser().getMemberRefId());
		
		invoiceDelegate.withCallback(new AbstractAsyncCallback<InvoiceSummary>() {
			@Override
			public void onSuccess(InvoiceSummary result) {
				getView().bindSummary(result);
			}
		}).getSummary(memberId);
		
		invoiceDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
			@Override
			public void onSuccess(Integer aCount) {
				getView().setCount(aCount);
				PagingConfig config = getView().getPagingPanel().getConfig();
				loadInvoices(config.getOffset(), config.getLimit());
			}
		}).getCount(memberId);

	}

	protected void loadInvoices(int offset, int limit) {
		String memberId = (AppContext.isCurrentUserAdmin() ? "ALL" : AppContext
				.getContextUser().getMemberRefId());
		invoiceDelegate.withCallback(
				new AbstractAsyncCallback<List<InvoiceDto>>() {
					@Override
					public void onSuccess(List<InvoiceDto> result) {
						getView().bindInvoices(result);
					}
				}).getInvoices(memberId, offset, limit);
	}

}
