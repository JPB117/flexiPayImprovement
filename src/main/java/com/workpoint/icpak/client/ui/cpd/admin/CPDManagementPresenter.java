package com.workpoint.icpak.client.ui.cpd.admin;

//import com.workpoint.icpak.shared.requests.CheckPasswordRequest;
//import com.workpoint.icpak.shared.requests.GetUserRequest;
//import com.workpoint.icpak.shared.requests.SaveUserRequest;
//import com.workpoint.icpak.shared.requests.UpdatePasswordRequest;
//import com.workpoint.icpak.shared.responses.CheckPasswordRequestResult;
//import com.workpoint.icpak.shared.responses.GetUserRequestResult;
//import com.workpoint.icpak.shared.responses.SaveUserResponse;
//import com.workpoint.icpak.shared.responses.UpdatePasswordResponse;
import java.util.List;

import com.google.gwt.event.dom.client.HasClickHandlers;
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
import com.workpoint.icpak.client.ui.events.EditModelEvent;
import com.workpoint.icpak.client.ui.events.EditModelEvent.EditModelHandler;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent;
import com.workpoint.icpak.client.ui.events.ProcessingEvent;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.popup.GenericPopupPresenter;
import com.workpoint.icpak.client.ui.security.AdminGateKeeper;
import com.workpoint.icpak.client.ui.security.LoginGateKeeper;
import com.workpoint.icpak.shared.api.MemberResource;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.shared.model.CPDSummaryDto;

public class CPDManagementPresenter
		extends
		Presenter<CPDManagementPresenter.ICPDManagementView, CPDManagementPresenter.ICPDManagementProxy>
		implements EditModelHandler {

	public interface ICPDManagementView extends View {
		HasClickHandlers getRecordButton();

		void bindResults(List<CPDDto> result);

		void showDetailedView();

		PagingPanel getPagingPanel();

		void bindSummary(CPDSummaryDto summary);

	}

	@ProxyCodeSplit
	@NameToken(NameTokens.cpdmgt)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface ICPDManagementProxy extends
			TabContentProxyPlace<CPDManagementPresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(AdminGateKeeper adminGatekeeper) {
		TabDataExt data = new TabDataExt("C.P.D Management",
				"fa fa-graduation-cap", 5, adminGatekeeper, true);
		return data;
	}

	protected final ResourceDelegate<MemberResource> memberDelegate;
	protected final CurrentUser currentUser;

	@Inject
	public CPDManagementPresenter(final EventBus eventBus,
			final ICPDManagementView view, final ICPDManagementProxy proxy,
			final ResourceDelegate<MemberResource> memberDelegate,
			final CurrentUser currentUser) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
		this.memberDelegate = memberDelegate;
		this.currentUser = currentUser;
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(EditModelEvent.TYPE, this);

		getView().getPagingPanel().setLoader(new PagingLoader() {
			@Override
			public void onLoad(int offset, int limit) {
				onLoad(offset, limit);
			}
		});

	}

	@Inject
	GenericPopupPresenter popup;

	protected void saveRecord(CPDDto dto) {
		if (dto.getRefId() != null) {
			// Update
			memberDelegate.withCallback(new AbstractAsyncCallback<CPDDto>() {
				@Override
				public void onSuccess(CPDDto result) {
					loadData();
				}
			}).cpd(dto.getMemberRefId()).update(dto.getRefId(), dto);

		}

	}

	@Override
	protected void onReveal() {
		super.onReveal();
		loadData();
	}

	protected void loadData() {
		fireEvent(new ProcessingEvent());

		memberDelegate.withCallback(new AbstractAsyncCallback<CPDSummaryDto>() {
			@Override
			public void onSuccess(CPDSummaryDto summary) {
				getView().bindSummary(summary);
			}
		}).cpd("ALL").getCPDSummary();

		memberDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
			@Override
			public void onSuccess(Integer aCount) {
				fireEvent(new ProcessingCompletedEvent());
				PagingPanel panel = getView().getPagingPanel();
				panel.setTotal(aCount);
				PagingConfig config = panel.getConfig();
				loadCPD(config.getOffset(), config.getLimit());
			}
		}).cpd("ALL").getCount();

	}

	protected void loadCPD(int offset, int limit) {
		fireEvent(new ProcessingEvent());
		memberDelegate.withCallback(new AbstractAsyncCallback<List<CPDDto>>() {
			@Override
			public void onSuccess(List<CPDDto> result) {
				fireEvent(new ProcessingCompletedEvent());
				getView().bindResults(result);
			}
		}).cpd("ALL").getAll(offset, limit);
	}

	String getApplicationRefId() {
		String applicationRefId = currentUser.getUser() == null ? null
				: currentUser.getUser().getApplicationRefId();

		return applicationRefId;
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		getView().showDetailedView();
	}

	@Override
	public void onEditModel(EditModelEvent event) {

	}

}
