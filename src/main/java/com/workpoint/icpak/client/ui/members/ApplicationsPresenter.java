package com.workpoint.icpak.client.ui.members;

import java.util.List;

import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
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
import com.workpoint.icpak.client.ui.profile.widget.ProfileWidget;
import com.workpoint.icpak.client.ui.security.AdminGateKeeper;
import com.workpoint.icpak.shared.api.ApplicationFormResource;
import com.workpoint.icpak.shared.model.ApplicationFormAccountancyDto;
import com.workpoint.icpak.shared.model.ApplicationFormEducationalDto;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationFormSpecializationDto;
import com.workpoint.icpak.shared.model.ApplicationFormTrainingDto;
import com.workpoint.icpak.shared.model.ApplicationSummaryDto;

public class ApplicationsPresenter
		extends
		Presenter<ApplicationsPresenter.IApplicationsView, ApplicationsPresenter.IApplicationsProxy> {

	public interface IApplicationsView extends View {
		void bindApplications(List<ApplicationFormHeaderDto> result);

		void setCount(Integer aCount);

		PagingPanel getPagingPanel();

		void bindSummary(ApplicationSummaryDto summary);

		void showSingleApplication(boolean show);

		ProfileWidget getPanelProfile();

		String getSearchText();

		HasKeyDownHandlers getTxtSearch();

		void showSingleApplication(boolean b, String previousRefId,
				String nextRefId, int maxSize);
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

	private List<ApplicationFormHeaderDto> applications;

	KeyDownHandler keyHandler = new KeyDownHandler() {
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				loadData(getView().getSearchText());
			}
		}
	};

	private final CurrentUser currentUser;
	private ResourceDelegate<ApplicationFormResource> applicationDelegate;
	private String applicationRefId = "";

	@Inject
	public ApplicationsPresenter(final EventBus eventBus,
			final IApplicationsView view, final IApplicationsProxy proxy,
			ResourceDelegate<ApplicationFormResource> applicationDelegate,
			final CurrentUser currentUser) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
		this.applicationDelegate = applicationDelegate;
		this.currentUser = currentUser;
	}

	@Override
	protected void onBind() {
		super.onBind();

		getView().getTxtSearch().addKeyDownHandler(keyHandler);

		getView().getPagingPanel().setLoader(new PagingLoader() {
			@Override
			public void onLoad(int offset, int limit) {
				loadApplications(offset, limit, "");
			}
		});
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		loadData("");
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		applicationRefId = request.getParameter("applicationRefId", "");
		String counterString = request.getParameter("counter", "");
		int counter = 0;
		ApplicationFormHeaderDto application = null;

		if (!counterString.isEmpty()) {
			counter = Integer.parseInt(counterString);
			application = applications.get(counter);
			applicationRefId = application.getRefId();
		}

		if (applicationRefId == null || applicationRefId.isEmpty()) {
			getView().showSingleApplication(false);
			return;
		} else {
			loadProfileDetails(applicationRefId, Integer.toString(counter - 1),
					Integer.toString(counter + 1), applications.size());
		}
	}

	private void loadData(final String searchTerm) {
		fireEvent(new ProcessingEvent());
		getView().showSingleApplication(false);

		// loadSummary();

		applicationDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
			@Override
			public void onSuccess(Integer aCount) {
				getView().setCount(aCount);
				PagingConfig config = getView().getPagingPanel().getConfig();
				loadApplications(config.getOffset(), config.getLimit(),
						searchTerm);

			}
		}).getSearchCount(searchTerm);

	}

	private void loadSummary() {
		applicationDelegate.withCallback(
				new AbstractAsyncCallback<ApplicationSummaryDto>() {
					@Override
					public void onSuccess(ApplicationSummaryDto result) {
						getView().bindSummary(result);
					}
				}).getSummary();
	}

	private void loadProfileDetails(String applicationRefId,
			final String previousRefId, final String nextRefId, final int maxSize) {
		fireEvent(new ProcessingEvent());
		getView().getPanelProfile().setUserImage(applicationRefId);

		applicationDelegate.withCallback(
				new AbstractAsyncCallback<ApplicationFormHeaderDto>() {
					@Override
					public void onSuccess(ApplicationFormHeaderDto result) {
						getView().getPanelProfile().bindBasicDetails(result);
						getView().showSingleApplication(true, previousRefId,
								nextRefId, maxSize);
						getView().getPanelProfile().setEditMode(false);
						fireEvent(new ProcessingCompletedEvent());
					}
				}).getById(applicationRefId);

		applicationDelegate
				.withCallback(
						new AbstractAsyncCallback<List<ApplicationFormEducationalDto>>() {
							@Override
							public void onSuccess(
									List<ApplicationFormEducationalDto> result) {
								getView().getPanelProfile()
										.bindEducationDetails(result);
							}
						}).education(applicationRefId).getAll(0, 100);

		applicationDelegate
				.withCallback(
						new AbstractAsyncCallback<List<ApplicationFormSpecializationDto>>() {
							@Override
							public void onSuccess(
									List<ApplicationFormSpecializationDto> result) {
								getView().getPanelProfile()
										.bindSpecializations(result);
							}
						}).specialization(applicationRefId).getAll(0, 50);

		applicationDelegate
				.withCallback(
						new AbstractAsyncCallback<List<ApplicationFormTrainingDto>>() {
							@Override
							public void onSuccess(
									List<ApplicationFormTrainingDto> result) {
								getView().getPanelProfile()
										.bindTrainingDetails(result);
							}
						}).training(applicationRefId).getAll(0, 50);

		applicationDelegate
				.withCallback(
						new AbstractAsyncCallback<List<ApplicationFormAccountancyDto>>() {
							@Override
							public void onSuccess(
									List<ApplicationFormAccountancyDto> result) {
								getView().getPanelProfile()
										.bindAccountancyDetails(result);
							}
						}).accountancy(applicationRefId).getAll(0, 100);

	}

	protected void loadApplications(int offset, int limit, String searchTerm) {
		applicationDelegate.withCallback(
				new AbstractAsyncCallback<List<ApplicationFormHeaderDto>>() {
					@Override
					public void onSuccess(
							List<ApplicationFormHeaderDto> applications) {
						ApplicationsPresenter.this.applications = applications;
						getView().bindApplications(applications);
						fireEvent(new ProcessingCompletedEvent());
					}
				}).getAll(offset, limit, searchTerm);
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

}
