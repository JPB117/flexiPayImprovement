package com.workpoint.icpak.client.ui.invoices;

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
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent;
import com.workpoint.icpak.client.ui.events.ProcessingEvent;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.security.FinanceGateKeeper;
import com.workpoint.icpak.shared.api.InvoiceResource;
import com.workpoint.icpak.shared.model.InvoiceSummary;
import com.workpoint.icpak.shared.model.TransactionDto;

public class InvoiceListPresenter
		extends
		Presenter<InvoiceListPresenter.IInvoiceView, InvoiceListPresenter.InvoiceListProxy> {

	public interface IInvoiceView extends View {
		 void bindInvoices(List<TransactionDto> trxs);

		void setCount(Integer aCount);

		PagingPanel getPagingPanel();

		void bindSummary(InvoiceSummary result);
	}

	private int pageLimit = 20;

	@ProxyCodeSplit
	@NameToken(NameTokens.invoices)
	@UseGatekeeper(FinanceGateKeeper.class)
	public interface InvoiceListProxy extends
			TabContentProxyPlace<InvoiceListPresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(FinanceGateKeeper adminGatekeeper) {
		TabDataExt data = new TabDataExt("Financial Summary",
				"fa fa-briefcase", 7, adminGatekeeper, true);
		return data;
	}

	@Inject
	CurrentUser user;

	private final ResourceDelegate<InvoiceResource> invoiceDelegate;

	@Inject
	public InvoiceListPresenter(final EventBus eventBus,
			final IInvoiceView view, final InvoiceListProxy proxy,
			final ResourceDelegate<InvoiceResource> invoiceDelegate) {

		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
		this.invoiceDelegate = invoiceDelegate;
	}

	@Override
	protected void onBind() {
		super.onBind();

		getView().getPagingPanel().setLoader(new PagingLoader() {
			@Override
			public void onLoad(int offset, int limit) {
				loadInvoices(offset, pageLimit);
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
		fireEvent(new ProcessingEvent());

		invoiceDelegate.withCallback(
				new AbstractAsyncCallback<InvoiceSummary>() {
					@Override
					public void onSuccess(InvoiceSummary result) {
						getView().bindSummary(result);
					}
				}).getSummary("ALL");
		invoiceDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
			@Override
			public void onSuccess(Integer aCount) {
				getView().setCount(aCount);
				PagingConfig config = getView().getPagingPanel().getConfig();
				loadInvoices(config.getOffset(), pageLimit);
			}
		}).getCount("ALL");

	}

	protected void loadInvoices(int offset, int limit) {
		fireEvent(new ProcessingEvent());

		invoiceDelegate.withCallback(
				new AbstractAsyncCallback<List<TransactionDto>>() {
					public void onSuccess(List<TransactionDto> result) { //
						getView().bindInvoices(result);
						fireEvent(new ProcessingCompletedEvent());
					}
				}).getInvoices("ALL", offset, pageLimit);

	}

}
