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

import com.amazonaws.services.simpleworkflow.flow.core.TryCatchFinally.State;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Window;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
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
import com.workpoint.icpak.client.ui.component.Grid;
import com.workpoint.icpak.client.ui.component.PagingConfig;
import com.workpoint.icpak.client.ui.component.PagingLoader;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.security.LoginGateKeeper;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.api.InvoiceResource;
import com.workpoint.icpak.shared.api.MemberResource;
import com.workpoint.icpak.shared.model.InvoiceSummary;
import com.workpoint.icpak.shared.model.statement.StatementDto;

public class StatementsPresenter
		extends
		Presenter<StatementsPresenter.IStatementsView, StatementsPresenter.IStatementsProxy> {

	public interface IStatementsView extends View {
		void setData(List<StatementDto> result,int totalCount);
		Grid<StatementDto> getGrid();

	}

	@ProxyCodeSplit
	@NameToken(NameTokens.statements)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface IStatementsProxy extends
			TabContentProxyPlace<StatementsPresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(LoginGateKeeper gateKeeper) {
		TabDataExt data = new TabDataExt("Statements", "fa fa-briefcase",
				8, gateKeeper, true);
		return data;
	}

	int totalCount=0;
	
	@Inject
	CurrentUser user;

	private ResourceDelegate<MemberResource> memberDelegate;

	@Inject
	public StatementsPresenter(final EventBus eventBus,
			final IStatementsView view, final IStatementsProxy proxy,
			final ResourceDelegate<MemberResource> memberResource) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
		this.memberDelegate = memberResource;
	}

	@Override
	protected void onBind() {
		super.onBind();

		getView().getGrid().setDataProvider(new AsyncDataProvider<StatementDto>() {
			@Override
			protected void onRangeChanged(final HasData<StatementDto> display) {
				final Range range = getView().getGrid().getVisibleRange();		
				
				if(totalCount==0){
					memberDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
						@Override
						public void onSuccess(Integer aCount) {
							totalCount = aCount;
							loadStatements(display,range.getStart(), range.getLength());
						}
					}).statements(getMemberId()).getCount();
				}else{
					loadStatements(display,range.getStart(), range.getLength());
				}
				
			}
		});
//		getView().getPagingPanel().setLoader(new PagingLoader() {
//
//			@Override
//			public void load(int offset, int limit) {
//				loadInvoices(offset, limit);
//			}
//		});
	}

	protected String getMemberId() {

		String memberId = (AppContext.isCurrentUserAdmin() ? "ALL" : AppContext
				.getContextUser().getMemberRefId());
		return memberId;
	}

	protected void save() {
	}

	protected void loadStatements(final HasData<StatementDto> arg0, int offset, int limit) {
		memberDelegate.withCallback(
				new AbstractAsyncCallback<List<StatementDto>>() {
					@Override
					public void onSuccess(List<StatementDto> result) {
						getView().setData(result,totalCount);
					}
				}).statements(getMemberId()).getAll(offset, limit);
				
	}

}
