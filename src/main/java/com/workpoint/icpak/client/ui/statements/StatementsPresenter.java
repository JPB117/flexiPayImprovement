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
import com.workpoint.icpak.client.ui.component.Grid;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent;
import com.workpoint.icpak.client.ui.events.ProcessingEvent;
import com.workpoint.icpak.client.ui.events.ToggleSideBarEvent;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.security.MemberGateKeeper;
import com.workpoint.icpak.client.ui.util.DateRange;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.api.MemberResource;
import com.workpoint.icpak.shared.model.statement.SearchDto;
import com.workpoint.icpak.shared.model.statement.StatementDto;
import com.workpoint.icpak.shared.model.statement.StatementSummaryDto;

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

		void setSummary(StatementSummaryDto summary);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.statements)
	@UseGatekeeper(MemberGateKeeper.class)
	public interface IStatementsProxy extends
			TabContentProxyPlace<StatementsPresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(MemberGateKeeper gateKeeper) {
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

		getView().getRefresh().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				final Range range = getView().getGrid().getVisibleRange();
				loadStatements(range.getStart(), range.getLength());
			}
		});
	}

	protected String getMemberId() {

		String memberId = (AppContext.isCurrentUserAdmin() ? "ALL" : AppContext
				.getContextUser().getMemberRefId());
		return memberId;
	}

	protected void loadStatements(final int offset, final int limit) {
		final Date startDate = getView().getSearchCriteria().getStartDate();
		final Date endDate = getView().getSearchCriteria().getEndDate();
		fireEvent(new ProcessingEvent());
		loadStatementSummary(startDate, endDate);

		memberDelegate
				.withCallback(new AbstractAsyncCallback<Integer>() {
					@Override
					public void onSuccess(Integer aCount) {
						totalCount = aCount;
						// Load Actual Statements
						memberDelegate
								.withCallback(
										new AbstractAsyncCallback<List<StatementDto>>() {
											@Override
											public void onSuccess(
													List<StatementDto> result) {
												getView().setData(result,
														totalCount);
												fireEvent(new ProcessingCompletedEvent());
											}
										})
								.statements(getMemberId())
								.getAll(startDate == null ? null : startDate
										.getTime(),
										endDate == null ? null : endDate
												.getTime(), offset, limit);
					}
				})
				.statements(getMemberId())
				.getCount(startDate == null ? null : startDate.getTime(),
						endDate == null ? null : endDate.getTime());
	}

	protected void loadStatementSummary(Date startDate, Date endDate) {
		memberDelegate
				.withCallback(new AbstractAsyncCallback<StatementSummaryDto>() {
					@Override
					public void onSuccess(StatementSummaryDto result) {
						getView().setSummary(result);
						fireEvent(new ProcessingCompletedEvent());
					}
				})
				.statements(getMemberId())
				.getSummary(startDate == null ? null : startDate.getTime(),
						endDate == null ? null : endDate.getTime());
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		fireEvent(new ToggleSideBarEvent(false));
		getView().setInitialDates(DateRange.THISYEAR, null);
		getView().getGrid().setDataProvider(
				new AsyncDataProvider<StatementDto>() {
					@Override
					protected void onRangeChanged(
							final HasData<StatementDto> display) {
						final Range range = getView().getGrid()
								.getVisibleRange();
						loadStatements(range.getStart(), range.getLength());
					}
				});
	}

}
