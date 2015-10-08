package com.workpoint.icpak.client.ui.statements;

//import com.workpoint.icpak.shared.requests.CheckPasswordRequest;
//import com.workpoint.icpak.shared.requests.GetUserRequest;
//import com.workpoint.icpak.shared.requests.SaveUserRequest;
//import com.workpoint.icpak.shared.requests.UpdatePasswordRequest;
//import com.workpoint.icpak.shared.responses.CheckPasswordRequestResult;
//import com.workpoint.icpak.shared.responses.GetUserRequestResult;
//import com.workpoint.icpak.shared.responses.SaveUserResponse;
//import com.workpoint.icpak.shared.responses.UpdatePasswordResponse;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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
import com.workpoint.icpak.client.place.NameTokens;
import com.workpoint.icpak.client.security.CurrentUser;
import com.workpoint.icpak.client.service.AbstractAsyncCallback;
import com.workpoint.icpak.client.ui.admin.TabDataExt;
import com.workpoint.icpak.client.ui.component.DateField;
import com.workpoint.icpak.client.ui.component.Grid;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.security.LoginGateKeeper;
import com.workpoint.icpak.client.ui.util.DateRange;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.api.MemberResource;
import com.workpoint.icpak.shared.model.statement.SearchDto;
import com.workpoint.icpak.shared.model.statement.StatementDto;

public class StatementsPresenter
		extends
		Presenter<StatementsPresenter.IStatementsView, StatementsPresenter.IStatementsProxy> {

	public interface IStatementsView extends View {
		void setData(List<StatementDto> result, int totalCount);

		Grid<StatementDto> getGrid();

		SearchDto getSearchCriteria();

		HasClickHandlers getRefresh();

		HasValueChangeHandlers<String> getStartDate();

		HasValueChangeHandlers<String> getEndDate();

		Date getEndDateValue();

		Date getStartDateValue();

		void setInitialDates(DateRange thisquarter, Date date);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.statements)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface IStatementsProxy extends
			TabContentProxyPlace<StatementsPresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(LoginGateKeeper gateKeeper) {
		TabDataExt data = new TabDataExt("Statements", "fa fa-briefcase", 8,
				gateKeeper, true);
		return data;
	}

	int totalCount = 0;

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

		getView().getGrid().setDataProvider(
				new AsyncDataProvider<StatementDto>() {
					@Override
					protected void onRangeChanged(
							final HasData<StatementDto> display) {
						final Range range = getView().getGrid()
								.getVisibleRange();

						if (totalCount == 0) {
							memberDelegate
									.withCallback(
											new AbstractAsyncCallback<Integer>() {
												@Override
												public void onSuccess(
														Integer aCount) {
													totalCount = aCount;
													loadStatements(
															range.getStart(),
															range.getLength());
												}
											}).statements(getMemberId())
									.getCount();
						} else {
							loadStatements(range.getStart(), range.getLength());
						}
					}
				});

		ValueChangeHandler<String> vch = new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> arg0) {

				Date startDate = getView().getStartDateValue();
				Date endDate = getView().getEndDateValue();

				memberDelegate
						.withCallback(new AbstractAsyncCallback<Integer>() {
							@Override
							public void onSuccess(Integer aCount) {
								// final Range range =
								// getView().getGrid().getVisibleRange();
								totalCount = aCount;
								// loadStatements(0, range.getLength());
							}
						})
						.statements(getMemberId())
						.getCount(
								startDate == null ? null : startDate.getTime(),
								endDate == null ? null : endDate.getTime());
			}
		};

		getView().getStartDate().addValueChangeHandler(vch);
		getView().getEndDate().addValueChangeHandler(vch);

		getView().getRefresh().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				// getView().getGrid().refresh();
				final Range range = getView().getGrid().getVisibleRange();
				loadStatements(range.getStart(), range.getLength());

			}
		});
		// getView().getPagingPanel().setLoader(new PagingLoader() {
		//
		// @Override
		// public void load(int offset, int limit) {
		// loadInvoices(offset, limit);
		// }
		// });
	}

	protected String getMemberId() {

		String memberId = (AppContext.isCurrentUserAdmin() ? "ALL" : AppContext
				.getContextUser().getMemberRefId());
		return memberId;
	}

	protected void save() {
	}

	protected void loadStatements(int offset, int limit) {
		Date startDate = getView().getSearchCriteria().getStartDate();
		Date endDate = getView().getSearchCriteria().getEndDate();

		memberDelegate
				.withCallback(new AbstractAsyncCallback<List<StatementDto>>() {
					@Override
					public void onSuccess(List<StatementDto> result) {
						getView().setData(result, totalCount);
					}
				})
				.statements(getMemberId())
				.getAll(startDate == null ? null : startDate.getTime(),
						endDate == null ? null : endDate.getTime(), offset,
						limit);

	}

	@Override
	protected void onReveal() {
		super.onReveal();
		final Range range = getView().getGrid().getVisibleRange();
		loadStatements(range.getStart(), range.getLength());

		getView().setInitialDates(DateRange.THISQUARTER, new Date());
	}

}
