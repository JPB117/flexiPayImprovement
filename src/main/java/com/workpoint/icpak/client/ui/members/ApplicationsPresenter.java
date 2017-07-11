package com.workpoint.icpak.client.ui.members;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.workpoint.icpak.client.place.NameTokens;
import com.workpoint.icpak.client.security.CurrentUser;
import com.workpoint.icpak.client.service.AbstractAsyncCallback;
import com.workpoint.icpak.client.ui.AppManager;
import com.workpoint.icpak.client.ui.OnOptionSelected;
import com.workpoint.icpak.client.ui.admin.TabDataExt;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.PagingConfig;
import com.workpoint.icpak.client.ui.component.PagingLoader;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.events.AfterSaveEvent;
import com.workpoint.icpak.client.ui.events.CheckboxSelectionEvent;
import com.workpoint.icpak.client.ui.events.CheckboxSelectionEvent.CheckboxSelectionHandler;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent;
import com.workpoint.icpak.client.ui.events.ProcessingEvent;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.members.appcategory.ApplicationCategoryForm;
import com.workpoint.icpak.client.ui.members.applicationSettings.ApplicationSettingsForm;
import com.workpoint.icpak.client.ui.members.management.ApplicationsActions;
import com.workpoint.icpak.client.ui.profile.widget.ProfileWidget;
import com.workpoint.icpak.client.ui.security.ApplicationsGateKeeper;
import com.workpoint.icpak.shared.api.ApplicationFormResource;
import com.workpoint.icpak.shared.api.CategoriesResource;
import com.workpoint.icpak.shared.api.SettingResource;
import com.workpoint.icpak.shared.api.UsersResource;
import com.workpoint.icpak.shared.model.ApplicationCategoryDto;
import com.workpoint.icpak.shared.model.ApplicationFormAccountancyDto;
import com.workpoint.icpak.shared.model.ApplicationFormEducationalDto;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationFormSpecializationDto;
import com.workpoint.icpak.shared.model.ApplicationFormTrainingDto;
import com.workpoint.icpak.shared.model.ApplicationSummaryDto;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.auth.ApplicationStatus;
import com.workpoint.icpak.shared.model.settings.SettingDto;

public class ApplicationsPresenter
		extends Presenter<ApplicationsPresenter.IApplicationsView, ApplicationsPresenter.IApplicationsProxy>
		implements CheckboxSelectionHandler {

	public interface IApplicationsView extends View {
		void bindApplications(List<ApplicationFormHeaderDto> result);

		void setCount(Integer aCount);

		PagingPanel getPagingPanel();

		void bindSummary(ApplicationSummaryDto summary);

		void showSingleApplication(boolean show);

		ProfileWidget getPanelProfile();

		String getSearchText();

		HasKeyDownHandlers getTxtSearch();

		void showSingleApplication(boolean b, String previousRefId, String nextRefId, int maxSize);

		DropDownList<ApplicationStatus> getLstApplicationStatus();

		DropDownList<PaymentStatus> getLstPaymentStatus();

		ActionLink getaSearch();

		void bindApplicationCategories(ArrayList<ApplicationCategoryDto> categories);

		void setActiveTab(String page);

		void setApplicationCategoryEdit(boolean value);

		HasClickHandlers getCategoryAddButton();

		HasClickHandlers getCategoryEditButton();

		HasClickHandlers getCategoryDeleteButton();

		HasClickHandlers getSyncApprovedButton();

		HasClickHandlers getRQAButton();
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.members)
	@UseGatekeeper(ApplicationsGateKeeper.class)
	public interface IApplicationsProxy extends TabContentProxyPlace<ApplicationsPresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(ApplicationsGateKeeper adminGatekeeper) {
		TabDataExt data = new TabDataExt("Applications", "icon-users", 4, adminGatekeeper, true);
		return data;
	}

	private List<ApplicationFormHeaderDto> applications;
	private static String searchTerm = "";
	private static String applicationStatus = "";
	private static String paymentStatus = "";
	private Object selectedModel;
	private ApplicationSettingsForm settingForm = new ApplicationSettingsForm();

	KeyDownHandler keyHandler = new KeyDownHandler() {
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				Map<String, String> params = new HashMap<String, String>();
				searchTerm = getView().getSearchText();
				params.put("appStatus", applicationStatus);
				params.put("searchTerm", searchTerm);
				params.put("paymentStatus", paymentStatus);
				PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(NameTokens.members).with(params)
						.build();
				placeManager.revealPlace(placeRequest);
			}
		}
	};

	ClickHandler searchClickHandler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			Map<String, String> params = new HashMap<String, String>();
			searchTerm = getView().getSearchText();
			params.put("appStatus", getView().getLstApplicationStatus().getValue().name());
			params.put("searchTerm", searchTerm);
			params.put("paymentStatus", getView().getLstPaymentStatus().getValue().name());
			PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(NameTokens.members).with(params).build();
			placeManager.revealPlace(placeRequest);
		}
	};

	ValueChangeHandler<ApplicationStatus> applicationStatusValueChangeHandler = new ValueChangeHandler<ApplicationStatus>() {
		@Override
		public void onValueChange(ValueChangeEvent<ApplicationStatus> event) {
			if (event.getValue() != null) {
				applicationStatus = event.getValue().getDisplayName();
			} else {
				applicationStatus = "";
			}
			Map<String, String> params = new HashMap<String, String>();
			params.put("appStatus", applicationStatus);
			params.put("searchTerm", searchTerm);
			params.put("paymentStatus", paymentStatus);
			PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(NameTokens.members).with(params).build();
			placeManager.revealPlace(placeRequest);
		}
	};

	ValueChangeHandler<PaymentStatus> paymentStatusValueChangeHandler = new ValueChangeHandler<PaymentStatus>() {
		@Override
		public void onValueChange(ValueChangeEvent<PaymentStatus> event) {
			if (event.getValue() != null) {
				paymentStatus = event.getValue().name();
			} else {
				paymentStatus = "";
			}
			Map<String, String> params = new HashMap<String, String>();
			params.put("appStatus", applicationStatus);
			params.put("searchTerm", searchTerm);
			params.put("paymentStatus", paymentStatus);
			PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(NameTokens.members).with(params).build();
			placeManager.revealPlace(placeRequest);
		}
	};

	private final CurrentUser currentUser;
	private ResourceDelegate<ApplicationFormResource> applicationDelegate;
	private ResourceDelegate<CategoriesResource> categoriesDelegate;
	private ResourceDelegate<SettingResource> settingDelegate;
	private String applicationRefId = "";

	protected ApplicationFormHeaderDto selectedApplication;
	private PlaceManager placeManager;
	protected boolean isSyncToERPCalled = false;
	private String page;

	@Inject
	public ApplicationsPresenter(final EventBus eventBus, final IApplicationsView view, final IApplicationsProxy proxy,
			ResourceDelegate<ApplicationFormResource> applicationDelegate, final CurrentUser currentUser,
			ResourceDelegate<UsersResource> usersDelegate, ResourceDelegate<CategoriesResource> categoriesDelegate,
			ResourceDelegate<SettingResource> settingDelegate, final PlaceManager placeManager) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
		this.applicationDelegate = applicationDelegate;
		this.categoriesDelegate = categoriesDelegate;
		this.currentUser = currentUser;
		this.settingDelegate = settingDelegate;
		this.placeManager = placeManager;
	}

	@Override
	protected void onBind() {
		super.onBind();

		addRegisteredHandler(CheckboxSelectionEvent.getType(), this);
		getView().getTxtSearch().addKeyDownHandler(keyHandler);
		getView().getaSearch().addClickHandler(searchClickHandler);
		getView().getLstApplicationStatus().addValueChangeHandler(applicationStatusValueChangeHandler);

		getView().getLstPaymentStatus().addValueChangeHandler(paymentStatusValueChangeHandler);

		getView().getPagingPanel().setLoader(new PagingLoader() {
			@Override
			public void onLoad(int offset, int limit) {
				loadApplications(offset, limit);
			}
		});
		/* Sync to ERP */
		getView().getPanelProfile().getErpSync().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String applicationNo = getView().getPanelProfile().getErpApplicationNumber();
				if (selectedApplication != null) {
					isSyncToERPCalled = true;
					selectedApplication.setErpCode(applicationNo);
					selectedApplication.setApplicationStatus(ApplicationStatus.PROCESSING);
					updateApplication(selectedApplication);
				}
			}
		});

		/* Management Actions PopUp */
		getView().getPanelProfile().getManagementActionsButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final ApplicationsActions applicationsActionsWidget = new ApplicationsActions();
				applicationsActionsWidget.bindApplicationActions(selectedApplication);
				AppManager.showPopUp("Management Action", applicationsActionsWidget, new OnOptionSelected() {
					@Override
					public void onSelect(String name) {
						if (name.equals("Save")) {
							updateApplication(applicationsActionsWidget.getApplicationAction());
						}
					}
				}, "Save", "Cancel");
			}
		});

		final ApplicationCategoryForm appCategoryForm = new ApplicationCategoryForm();
		getView().getCategoryAddButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				appCategoryForm.setApplicationCategory(null);
				AppManager.showPopUp("Create a New Category", appCategoryForm, new OnOptionSelected() {
					@Override
					public void onSelect(String name) {
						if (name.equals("Save")) {
							saveCategory(appCategoryForm.getApplicationCategory());
							appCategoryForm.clear();
						}
					}
				}, "Save", "Cancel");
			}
		});

		getView().getCategoryEditButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (selectedModel != null) {
					appCategoryForm.setApplicationCategory((ApplicationCategoryDto) selectedModel);
				}
				AppManager.showPopUp("Edit Category", appCategoryForm, new OnOptionSelected() {
					@Override
					public void onSelect(String name) {
						if (name.equals("Save")) {
							saveCategory(appCategoryForm.getApplicationCategory());
							appCategoryForm.clear();
						}
					}
				}, "Save", "Cancel");
			}
		});

		getView().getCategoryDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				AppManager.showPopUp("Confirm", "Are you sure you want to delete Category?", new OnOptionSelected() {
					@Override
					public void onSelect(String name) {
						if (name.equals("Yes")) {
							categoriesDelegate.withCallback(new AbstractAsyncCallback<List<ApplicationCategoryDto>>() {
								@Override
								public void onSuccess(List<ApplicationCategoryDto> categories) {
									fireEvent(new ProcessingCompletedEvent());
									fireEvent(new AfterSaveEvent("Category Record successfully deleted."));
								}
							}).delete(appCategoryForm.getApplicationCategory().getRefId());
						}
					}
				}, "Yes", "No");
			}
		});

		getView().getRQAButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new ProcessingEvent());
				settingDelegate.withCallback(new AbstractAsyncCallback<SettingDto>() {
					@Override
					public void onSuccess(SettingDto setting) {
						fireEvent(new ProcessingCompletedEvent());
						settingForm.setSetting(setting);
						showAppSetting();
					}
				}).getBySettingName("next_rqa_meeting");

			}
		});

		getView().getSyncApprovedButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

			}
		});
	}

	public void showAppSetting() {
		AppManager.showPopUp("Application Form Settings", settingForm, new OnOptionSelected() {
			@Override
			public void onSelect(String name) {
				if (name.equals("Save")) {
					fireEvent(new ProcessingEvent());
					SettingDto setting = settingForm.getConfiguredSetting();
					if (setting.getRefId() != null) {
						settingDelegate.withCallback(new AbstractAsyncCallback<SettingDto>() {
							@Override
							public void onSuccess(SettingDto setting) {
								fireEvent(new ProcessingCompletedEvent());
								fireEvent(new AfterSaveEvent("RQA Setting successfully updated."));
							}

						}).update(setting.getRefId(), setting);
					} else {
						settingDelegate.withCallback(new AbstractAsyncCallback<SettingDto>() {
							@Override
							public void onSuccess(SettingDto setting) {
								fireEvent(new ProcessingCompletedEvent());
								fireEvent(new AfterSaveEvent("RQA Setting successfully created."));
							}

						}).create(setting);
					}
				}
			}
		}, "Save", "Cancel");
	}

	public void saveCategory(ApplicationCategoryDto appCategory) {
		fireEvent(new ProcessingEvent());
		if (appCategory.getRefId() != null) {
			// Edit
			categoriesDelegate.withCallback(new AbstractAsyncCallback<ApplicationCategoryDto>() {
				@Override
				public void onSuccess(ApplicationCategoryDto categories) {
					fireEvent(new ProcessingCompletedEvent());
					fireEvent(new AfterSaveEvent("Category Record successfully updated."));
					loadData();
				}
			}).update(appCategory.getRefId(), appCategory);
		} else {
			// Create
			categoriesDelegate.withCallback(new AbstractAsyncCallback<ApplicationCategoryDto>() {
				@Override
				public void onSuccess(ApplicationCategoryDto categories) {
					fireEvent(new ProcessingCompletedEvent());
					fireEvent(new AfterSaveEvent("Category Record successfully created."));
					loadData();
				}
			}).create(appCategory);
		}
		getView().setApplicationCategoryEdit(false); // Hide action buttons
	}

	protected void updateApplication(ApplicationFormHeaderDto selectedApplication) {
		fireEvent(new ProcessingEvent());
		applicationDelegate.withCallback(new AbstractAsyncCallback<ApplicationFormHeaderDto>() {
			@Override
			public void onSuccess(ApplicationFormHeaderDto result) {
				fireEvent(new ProcessingCompletedEvent());
				if (isSyncToERPCalled) {
					if (result.getErpMessage().equals("success")) {
						fireEvent(new AfterSaveEvent("Application changes saved Successfully.!"));
					} else {
						AppManager.showPopUp("Error Updating!", result.getErpMessage(), new OnOptionSelected() {
							@Override
							public void onSelect(String name) {
							}
						}, "Cancel");
					}
					isSyncToERPCalled = false;
				} else {
					fireEvent(new AfterSaveEvent("Application changes saved Successfully.!"));
				}
				reloadMemberDetails();
			}

			@Override
			public void onFailure(Throwable caught) {
				super.onFailure(caught);
				fireEvent(new ProcessingCompletedEvent());
				AppManager.showPopUp("Error Updating!", caught.getStackTrace() + "", new OnOptionSelected() {
					@Override
					public void onSelect(String name) {
					}
				}, "Cancel");
			}

		}).update(applicationRefId, selectedApplication);
	}

	protected void reloadMemberDetails() {
		applicationDelegate.withCallback(new AbstractAsyncCallback<ApplicationFormHeaderDto>() {
			@Override
			public void onSuccess(ApplicationFormHeaderDto result) {
				ApplicationsPresenter.this.selectedApplication = result;
				getView().getPanelProfile().bindBasicDetails(result);
				getView().getPanelProfile().initDisplay(result.getApplicationStatus(), result.getPaymentStatus());
				fireEvent(new ProcessingCompletedEvent());
			}
		}).getById(applicationRefId);
	}

	@Override
	protected void onReveal() {
		isSyncToERPCalled = false;
		super.onReveal();
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		page = request.getParameter("p", "allApplications");
		applicationRefId = request.getParameter("applicationRefId", "");
		String counterString = request.getParameter("counter", "");
		paymentStatus = request.getParameter("paymentStatus", "");
		applicationStatus = request.getParameter("appStatus", "");
		searchTerm = request.getParameter("searchTerm", "");

		// Load Event Details to View
		getView().setActiveTab(page);

		if (page.equals("allApplications")) {
			int counter = 0;
			ApplicationFormHeaderDto application = null;

			if (!counterString.isEmpty()) {
				counter = Integer.parseInt(counterString);
				application = applications.get(counter);
				applicationRefId = application.getRefId();
			}

			if (applicationRefId != null && !applicationRefId.isEmpty()) {
				loadProfileDetails(applicationRefId, Integer.toString(counter - 1), Integer.toString(counter + 1),
						applications.size());
			} else {
				loadData();
			}
		} else if (page.equals("applicationCategories")) {
			loadData();
		}
	}

	private void loadData() {
		fireEvent(new ProcessingEvent());
		if (page.equals("allApplications")) {
			getView().showSingleApplication(false);

			applicationDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
				@Override
				public void onSuccess(Integer aCount) {
					getView().setCount(aCount);
					PagingConfig config = getView().getPagingPanel().getConfig();
					loadApplications(config.getOffset(), config.getLimit());
				}
			}).getSearchCount(searchTerm, paymentStatus, applicationStatus);
		} else if (page.equals("applicationCategories")) {
			// Get All Categories
			categoriesDelegate.withCallback(new AbstractAsyncCallback<List<ApplicationCategoryDto>>() {
				@Override
				public void onSuccess(List<ApplicationCategoryDto> categories) {
					getView().bindApplicationCategories(new ArrayList<>(categories));
					fireEvent(new ProcessingCompletedEvent());
				}
			}).getAll();

			// Get NextRQA Setting
			settingDelegate.withCallback(new AbstractAsyncCallback<SettingDto>() {
				@Override
				public void onSuccess(SettingDto settingDto) {
					settingForm.setSetting(settingDto);
					fireEvent(new ProcessingCompletedEvent());
				}
			}).getAll();
		}

	}

	private void loadProfileDetails(String applicationRefId, final String previousRefId, final String nextRefId,
			final int maxSize) {
		fireEvent(new ProcessingEvent());
		applicationDelegate.withCallback(new AbstractAsyncCallback<ApplicationFormHeaderDto>() {
			@Override
			public void onSuccess(ApplicationFormHeaderDto result) {
				ApplicationsPresenter.this.selectedApplication = result;
				getView().getPanelProfile().bindBasicDetails(result);
				getView().showSingleApplication(true, previousRefId, nextRefId, maxSize);
				getView().getPanelProfile().initDisplay(result.getApplicationStatus(), result.getPaymentStatus());
				getView().getPanelProfile().setUserImage(result.getUserRefId());
				fireEvent(new ProcessingCompletedEvent());
			}
		}).getById(applicationRefId);

		applicationDelegate.withCallback(new AbstractAsyncCallback<List<ApplicationFormEducationalDto>>() {
			@Override
			public void onSuccess(List<ApplicationFormEducationalDto> result) {
				getView().getPanelProfile().bindEducationDetails(result);
			}
		}).education(applicationRefId).getAll(0, 100);

		applicationDelegate.withCallback(new AbstractAsyncCallback<List<ApplicationFormSpecializationDto>>() {
			@Override
			public void onSuccess(List<ApplicationFormSpecializationDto> result) {
				getView().getPanelProfile().bindSpecializations(result);
			}
		}).specialization(applicationRefId).getAll(0, 50);

		applicationDelegate.withCallback(new AbstractAsyncCallback<List<ApplicationFormTrainingDto>>() {
			@Override
			public void onSuccess(List<ApplicationFormTrainingDto> result) {
				getView().getPanelProfile().bindTrainingDetails(result);
			}
		}).training(applicationRefId).getAll(0, 50);

		applicationDelegate.withCallback(new AbstractAsyncCallback<List<ApplicationFormAccountancyDto>>() {
			@Override
			public void onSuccess(List<ApplicationFormAccountancyDto> result) {
				getView().getPanelProfile().bindAccountancyDetails(result);
			}
		}).accountancy(applicationRefId).getAll(0, 100);

	}

	protected void loadApplications(int offset, int limit) {
		fireEvent(new ProcessingEvent());
		applicationDelegate.withCallback(new AbstractAsyncCallback<List<ApplicationFormHeaderDto>>() {
			@Override
			public void onSuccess(List<ApplicationFormHeaderDto> applications) {
				ApplicationsPresenter.this.applications = applications;
				getView().bindApplications(applications);
				fireEvent(new ProcessingCompletedEvent());
			}
		}).getAll(offset, limit, searchTerm, paymentStatus, applicationStatus);
	}

	protected void save() {
	}

	@Override
	protected void onReset() {
		super.onReset();
	}

	String getApplicationRefId() {
		String applicationRefId = currentUser.getUser() == null ? null : currentUser.getUser().getApplicationRefId();
		return applicationRefId;
	}

	@Override
	public void onCheckboxSelection(CheckboxSelectionEvent event) {
		selectedModel = event.getModel();
		selectItem(selectedModel, event.getValue());

		if (!event.getValue()) {
			selectedModel = null;
		}
	}

	private void selectItem(Object model, boolean value) {
		if (model instanceof ApplicationCategoryDto) {
			getView().setApplicationCategoryEdit(value);
		}
	}

}
