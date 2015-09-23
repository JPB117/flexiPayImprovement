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
import com.workpoint.icpak.client.ui.AppManager;
import com.workpoint.icpak.client.ui.admin.TabDataExt;
import com.workpoint.icpak.client.ui.component.PagingConfig;
import com.workpoint.icpak.client.ui.component.PagingLoader;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.events.EditModelEvent;
import com.workpoint.icpak.client.ui.events.EditModelEvent.EditModelHandler;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.profile.widget.ProfileWidget;
import com.workpoint.icpak.client.ui.security.AdminGateKeeper;
import com.workpoint.icpak.shared.api.ApplicationFormResource;
import com.workpoint.icpak.shared.model.ApplicationFormEducationalDto;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationSummaryDto;

public class ApplicationsPresenter
		extends
		Presenter<ApplicationsPresenter.IApplicationsView, ApplicationsPresenter.IApplicationsProxy>
		implements EditModelHandler {

	public interface IApplicationsView extends View {

		void bindApplications(List<ApplicationFormHeaderDto> result);
		void setCount(Integer aCount);

		PagingPanel getPagingPanel();
		void bindSummary(ApplicationSummaryDto summary);

	}

	@ProxyCodeSplit
	@NameToken(NameTokens.members)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface IApplicationsProxy extends
			TabContentProxyPlace<ApplicationsPresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(AdminGateKeeper adminGatekeeper) {
		TabDataExt data = new TabDataExt("Applications", "icon-users", 4,
				adminGatekeeper, true);
		return data;
	}

	private final CurrentUser currentUser;
	private ResourceDelegate<ApplicationFormResource> applicationDelegate;

	@Inject
	public ApplicationsPresenter(final EventBus eventBus, final IApplicationsView view,
			final IApplicationsProxy proxy,
			ResourceDelegate<ApplicationFormResource> applicationDelegate,
			final CurrentUser currentUser) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
		this.applicationDelegate = applicationDelegate;
		this.currentUser = currentUser;
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(EditModelEvent.TYPE, this);

		getView().getPagingPanel().setLoader(new PagingLoader() {
			@Override
			public void onLoad(int offset, int limit) {
				loadApplications(offset, limit);
			}
		});

	}

	@Override
	protected void onReveal() {
		super.onReveal();
		loadData();
	}

	private void loadData() {
		
		applicationDelegate.withCallback(new AbstractAsyncCallback<ApplicationSummaryDto>() {
			@Override
			public void onSuccess(ApplicationSummaryDto result) {
				getView().bindSummary(result);
			}
		}).getSummary();
		applicationDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
			@Override
			public void onSuccess(Integer aCount) {
				getView().setCount(aCount);
				PagingConfig config = getView().getPagingPanel().getConfig();
				loadApplications(config.getOffset(), config.getLimit());
			}
		}).getCount();

	}

	ProfileWidget profileWidget = new ProfileWidget();

	private void loadProfileDetails(String applicationRefId) {
		profileWidget.setUserImage(applicationRefId);

		applicationDelegate.withCallback(
				new AbstractAsyncCallback<ApplicationFormHeaderDto>() {
					@Override
					public void onSuccess(ApplicationFormHeaderDto result) {
						profileWidget.bindBasicDetails(result);
					}
				}).getById(applicationRefId);

		applicationDelegate
				.withCallback(
						new AbstractAsyncCallback<List<ApplicationFormEducationalDto>>() {
							@Override
							public void onSuccess(
									List<ApplicationFormEducationalDto> result) {
								profileWidget.bindEducationDetails(result);
							}
						}).education(applicationRefId).getAll(0, 100);

	}

	protected void loadApplications(int offset, int limit) {
		applicationDelegate.withCallback(
				new AbstractAsyncCallback<List<ApplicationFormHeaderDto>>() {
					@Override
					public void onSuccess(List<ApplicationFormHeaderDto> result) {
						getView().bindApplications(result);
					}
				}).getAll(offset, limit);
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

	@Override
	public void onEditModel(EditModelEvent event) {
		if (event.getModel() instanceof ApplicationFormHeaderDto) {
			ApplicationFormHeaderDto headerDto = (ApplicationFormHeaderDto) event
					.getModel();

			loadProfileDetails(headerDto.getRefId());

			profileWidget.setEditMode(false);
			
			AppManager.showPopUp("View Profile Info", profileWidget, null,
					"Done");
		}
	}
}
