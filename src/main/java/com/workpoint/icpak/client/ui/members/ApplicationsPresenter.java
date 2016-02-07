package com.workpoint.icpak.client.ui.members;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
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
import com.workpoint.icpak.client.ui.AppManager;
import com.workpoint.icpak.client.ui.OptionControl;
import com.workpoint.icpak.client.ui.admin.TabDataExt;
import com.workpoint.icpak.client.ui.component.PagingConfig;
import com.workpoint.icpak.client.ui.component.PagingLoader;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.events.EditModelEvent;
import com.workpoint.icpak.client.ui.events.EditModelEvent.EditModelHandler;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent;
import com.workpoint.icpak.client.ui.events.ProcessingEvent;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.membership.form.MemberRegistrationForm;
import com.workpoint.icpak.client.ui.profile.accountancy.form.AccountancyRegistrationForm;
import com.workpoint.icpak.client.ui.profile.education.form.EducationRegistrationForm;
import com.workpoint.icpak.client.ui.profile.specialization.form.SpecializationRegistrationForm;
import com.workpoint.icpak.client.ui.profile.training.form.TrainingRegistrationForm;
import com.workpoint.icpak.client.ui.profile.widget.ProfileWidget;
import com.workpoint.icpak.client.ui.security.AdminGateKeeper;
import com.workpoint.icpak.shared.api.ApplicationFormResource;
import com.workpoint.icpak.shared.model.ApplicationFormAccountancyDto;
import com.workpoint.icpak.shared.model.ApplicationFormEducationalDto;
import com.workpoint.icpak.shared.model.ApplicationFormEmploymentDto;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationFormSpecializationDto;
import com.workpoint.icpak.shared.model.ApplicationFormTrainingDto;
import com.workpoint.icpak.shared.model.ApplicationSummaryDto;
import com.workpoint.icpak.shared.model.auth.ApplicationStatus;

public class ApplicationsPresenter
		extends
		Presenter<ApplicationsPresenter.IApplicationsView, ApplicationsPresenter.IApplicationsProxy>
		implements EditModelHandler {

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
	final MemberRegistrationForm memberForm = new MemberRegistrationForm();
	EducationRegistrationForm educationForm = new EducationRegistrationForm();
	TrainingRegistrationForm trainingForm = new TrainingRegistrationForm();
	SpecializationRegistrationForm specializationForm = new SpecializationRegistrationForm();
	AccountancyRegistrationForm accountancyForm = new AccountancyRegistrationForm();

	protected ApplicationFormHeaderDto selectedApplication;

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
		addRegisteredHandler(EditModelEvent.TYPE, this);

		getView().getTxtSearch().addKeyDownHandler(keyHandler);

		getView().getPagingPanel().setLoader(new PagingLoader() {
			@Override
			public void onLoad(int offset, int limit) {
				loadApplications(offset, limit, "");
			}
		});

		getView().getPanelProfile().getErpSync()
				.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						String applicationNo = getView().getPanelProfile()
								.getErpApplicationNumber();
						if (selectedApplication != null) {
							selectedApplication.setErpCode(applicationNo);
							selectedApplication
									.setApplicationStatus(ApplicationStatus.PROCESSING);
							updateApplication(selectedApplication);
						}
					}
				});

		getView().getPanelProfile().getProfileEditButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AppManager.showPopUp("Edit Basic Details", memberForm,
						new OptionControl() {
							@Override
							public void onSelect(String name) {
								if (name.equals("Save")) {
									if (memberForm.isValid()) {
										saveBasicDetails();
										hide();
									}
								} else {
									hide();
								}
							}
						}, "Save", "Cancel");
			}
		});

		educationForm.getStartUploadButton().addClickHandler(
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						saveEducationInformation();
					}
				});

		trainingForm.getStartUploadButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				saveTrainingInformation();
			}
		});

		accountancyForm.getStartUploadButton().addClickHandler(
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						saveAccountancyInformation();
					}
				});

		getView().getPanelProfile().getEducationAddButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				educationForm.clear();
				showPopUp(educationForm);
			}
		});

		getView().getPanelProfile().getAccountancyAddButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				accountancyForm.clear();
				showPopUp(accountancyForm);
			}
		});

		getView().getPanelProfile().getTrainingAddButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				trainingForm.clear();
				showPopUp(trainingForm);
			}
		});

		getView().getPanelProfile().getSpecializationAddButton().addClickHandler(
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						specializationForm.clear();
						specializationForm.setEditMode(true);
						loadspecializationDetails();
						showPopUp(specializationForm);
					}
				});

	}
	
	protected void saveBasicDetails() {
		if (memberForm.isValid()) {
//			getView().showApplicationIssues(false);
			fireEvent(new ProcessingEvent());
			ApplicationFormHeaderDto applicationForm = memberForm
					.getApplicationForm();
			applicationDelegate.withCallback(
					new AbstractAsyncCallback<ApplicationFormHeaderDto>() {
						@Override
						public void onSuccess(ApplicationFormHeaderDto result) {
							getView().getPanelProfile().setEditMode(true);
							reloadMemberDetails();
							fireEvent(new ProcessingCompletedEvent());
						}

						@Override
						public void onFailure(Throwable caught) {
							fireEvent(new ProcessingCompletedEvent());
							Window.alert("Oops an error occured while saving the data..");
							super.onFailure(caught);
						}
					}).update(getApplicationRefId(), applicationForm);
		}
	}
	
	public void showPopUp(final Widget passedForm) {
		AppManager.showPopUp("Create/Edit PopUp", passedForm,
				new OptionControl() {
					@Override
					public void onSelect(String name) {
						if (name.equals("Save")) {
							if (passedForm instanceof EducationRegistrationForm) {
								if (saveEducationInformation()) {
									hide();
								}
							} else if (passedForm instanceof TrainingRegistrationForm) {
								if (saveTrainingInformation()) {
									hide();
								}
							} else if (passedForm instanceof SpecializationRegistrationForm) {
								if (saveSpecializationInformation()) {
									hide();
								}
							} else if (passedForm instanceof AccountancyRegistrationForm) {
								if (saveAccountancyInformation()) {
									hide();
								}
							}

						}
					}
				}, "Save");
	}
	
	
	protected boolean saveEducationInformation() {
		if (educationForm.isValid()) {
			getView().showApplicationIssues(false);
			fireEvent(new ProcessingEvent());
			ApplicationFormEducationalDto dto = educationForm.getEducationDto();

			// Updating
			if (dto.getRefId() != null) {
				applicationDelegate
						.withCallback(
								new AbstractAsyncCallback<ApplicationFormEducationalDto>() {
									@Override
									public void onSuccess(
											ApplicationFormEducationalDto result) {
										fireEvent(new ProcessingCompletedEvent());
										educationForm.bindDetail(result);
										// getView().setEditMode(true);
										loadEducationDetails();
									}
								}).education(getApplicationRefId())
						.update(dto.getRefId(), dto);

			} else {
				applicationDelegate
						.withCallback(
								new AbstractAsyncCallback<ApplicationFormEducationalDto>() {
									@Override
									public void onSuccess(
											ApplicationFormEducationalDto result) {
										fireEvent(new ProcessingCompletedEvent());
										educationForm.bindDetail(result);
										educationForm.showUploadPanel(true);
										loadEducationDetails();
									}
								}).education(getApplicationRefId()).create(dto);
			}
			return true;
		} else {
			return false;
		}
	}
	
	protected boolean saveTrainingInformation() {
		String applicationId = getApplicationRefId();

		if (applicationId == null) {
			Window.alert("Current user has no active application");
			return true;
		}
		if (trainingForm.isValid()) {
			getView().showApplicationIssues(false);
			// Save Training Here
			fireEvent(new ProcessingEvent());
			ApplicationFormTrainingDto dto = trainingForm.getTrainingDto();

			if (dto.getRefId() == null) {
				applicationDelegate
						.withCallback(
								new AbstractAsyncCallback<ApplicationFormTrainingDto>() {
									@Override
									public void onSuccess(
											ApplicationFormTrainingDto result) {
										loadTrainings();
										trainingForm.bindDetail(result);
										trainingForm.showUploadPanel(true);
									}
								}).training(applicationId).create(dto);
			} else {
				applicationDelegate
						.withCallback(
								new AbstractAsyncCallback<ApplicationFormTrainingDto>() {
									@Override
									public void onSuccess(
											ApplicationFormTrainingDto trainingDto) {
										loadTrainings();
										trainingForm.bindDetail(trainingDto);
										trainingForm.showUploadPanel(true);
									}
								}).training(applicationId)
						.update(dto.getRefId(), dto);
			}

			return true;
		} else {
			return false;
		}
	}
	
	protected boolean saveAccountancyInformation() {
		if (accountancyForm.isValid()) {
			getView().showApplicationIssues(false);
			fireEvent(new ProcessingEvent());
			ApplicationFormAccountancyDto dto = accountancyForm
					.getAccountancyDto();

			// Updating
			if (dto.getRefId() != null) {
				applicationDelegate
						.withCallback(
								new AbstractAsyncCallback<ApplicationFormAccountancyDto>() {
									@Override
									public void onSuccess(
											ApplicationFormAccountancyDto result) {
										fireEvent(new ProcessingCompletedEvent());
										accountancyForm.bindDetail(result);
										loadAccountancyExamination();
									}
								}).accountancy(getApplicationRefId())
						.update(dto.getRefId(), dto);

			} else {
				applicationDelegate
						.withCallback(
								new AbstractAsyncCallback<ApplicationFormAccountancyDto>() {
									@Override
									public void onSuccess(
											ApplicationFormAccountancyDto result) {
										fireEvent(new ProcessingCompletedEvent());
										accountancyForm.bindDetail(result);
										accountancyForm.showUploadPanel(true);
										loadAccountancyExamination();
									}
								}).accountancy(getApplicationRefId())
						.create(dto);
			}

			return true;
		} else {
			return false;
		}
	}
	

	protected boolean saveSpecializationInformation() {
		if (specializationForm.isValid()) {
			loadSpecializations();
			return true;
		} else {
			return false;
		}
	}




	protected void updateApplication(
			ApplicationFormHeaderDto selectedApplication) {
		fireEvent(new ProcessingEvent());
		applicationDelegate.withCallback(
				new AbstractAsyncCallback<ApplicationFormHeaderDto>() {
					@Override
					public void onSuccess(ApplicationFormHeaderDto result) {
						fireEvent(new ProcessingCompletedEvent());
						reloadMemberDetails();
					}
				}).update(applicationRefId, selectedApplication);
	}

	protected void reloadMemberDetails() {
		applicationDelegate.withCallback(
				new AbstractAsyncCallback<ApplicationFormHeaderDto>() {
					@Override
					public void onSuccess(ApplicationFormHeaderDto result) {
						ApplicationsPresenter.this.selectedApplication = result;
						getView().getPanelProfile().bindBasicDetails(result);
						getView().getPanelProfile().initDisplay(
								result.getApplicationStatus(),
								result.getPaymentStatus());
						fireEvent(new ProcessingCompletedEvent());
					}
				}).getById(applicationRefId);
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

	private void loadProfileDetails(String applicationRefId,
			final String previousRefId, final String nextRefId,
			final int maxSize) {
		fireEvent(new ProcessingEvent());

		getView().getPanelProfile().setUserImage(applicationRefId);

		loadMemberDetails(applicationRefId,previousRefId,nextRefId,maxSize);

		loadEducationDetails();
		loadspecializationDetails();
		loadTrainingDetails();
		loadAccountancyDetails();
		
	}

	private void loadAccountancyDetails() {
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

	private void loadTrainingDetails() {
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
	}

	private void loadspecializationDetails() {
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
	}

	private void loadEducationDetails() {
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

	}

	private void loadMemberDetails(String applicationRefId, final String previousRefId, final String nextRefId, final int maxSize) {
		applicationDelegate.withCallback(
				new AbstractAsyncCallback<ApplicationFormHeaderDto>() {
					@Override
					public void onSuccess(ApplicationFormHeaderDto result) {
						ApplicationsPresenter.this.selectedApplication = result;
						getView().getPanelProfile().bindBasicDetails(result);
						getView().showSingleApplication(true, previousRefId,
								nextRefId, maxSize);
						getView().getPanelProfile().initDisplay(
								result.getApplicationStatus(),
								result.getPaymentStatus());
						fireEvent(new ProcessingCompletedEvent());
					}
				}).getById(applicationRefId);
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
		return applicationRefId;
	}

	@Override
	public void onEditModel(EditModelEvent event) {
		if ((event.getModel() instanceof ApplicationFormEducationalDto)) {
			ApplicationFormEducationalDto dto = (ApplicationFormEducationalDto) event
					.getModel();
			if (event.isDelete()) {
				delete(dto);
			} else {
				showPopUp(educationForm);
				educationForm.bindDetail(dto);
			}
		} else if ((event.getModel() instanceof ApplicationFormTrainingDto)) {
			ApplicationFormTrainingDto dto = (ApplicationFormTrainingDto) event
					.getModel();
			if (event.isDelete()) {
				delete(dto);
			} else {
				showPopUp(trainingForm);
				trainingForm.bindDetail(dto);
			}
		} else if ((event.getModel() instanceof ApplicationFormSpecializationDto)) {
			ApplicationFormSpecializationDto dto = (ApplicationFormSpecializationDto) event
					.getModel();
			if (event.isDelete()) {
				delete(dto);
			} else {
				saveSpecialization(dto);
			}
		} else if ((event.getModel() instanceof ApplicationFormEmploymentDto)) {
			ApplicationFormEmploymentDto dto = (ApplicationFormEmploymentDto) event
					.getModel();
			if (event.isDelete()) {
				delete(dto);
			} else {
				saveEmployment(dto);
			}
		}

		else if ((event.getModel() instanceof ApplicationFormAccountancyDto)) {
			ApplicationFormAccountancyDto dto = (ApplicationFormAccountancyDto) event
					.getModel();
			if (event.isDelete()) {
				delete(dto);
			} else {
				showPopUp(accountancyForm);
				accountancyForm.bindDetail(dto);
			}
		}

	}

	private void saveEmployment(ApplicationFormEmploymentDto dto) {
		applicationDelegate
				.withCallback(
						new AbstractAsyncCallback<ApplicationFormEmploymentDto>() {
							@Override
							public void onSuccess(
									ApplicationFormEmploymentDto result) {
							}
						}).employment(getApplicationRefId()).create(dto);
	}

	private void delete(ApplicationFormEmploymentDto dto) {
		applicationDelegate.withCallback(new AbstractAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
			}
		}).employment(getApplicationRefId())
				.delete(dto.getSpecialization().name());
	}

	private void saveSpecialization(ApplicationFormSpecializationDto dto) {
		applicationDelegate
				.withCallback(
						new AbstractAsyncCallback<ApplicationFormSpecializationDto>() {
							@Override
							public void onSuccess(
									ApplicationFormSpecializationDto result) {
							}
						}).specialization(getApplicationRefId()).create(dto);
	}

	private void delete(ApplicationFormSpecializationDto dto) {
		applicationDelegate.withCallback(new AbstractAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
			}
		}).specialization(getApplicationRefId())
				.delete(dto.getSpecialization().name());
	}

	private void delete(ApplicationFormEducationalDto dto) {
		applicationDelegate.withCallback(new AbstractAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				loadData();
			}
		}).education(getApplicationRefId()).delete(dto.getRefId());
	}

	private void delete(ApplicationFormTrainingDto dto) {
		applicationDelegate.withCallback(new AbstractAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				loadData();
			}
		}).training(getApplicationRefId()).delete(dto.getRefId());
	}

	private void delete(ApplicationFormAccountancyDto dto) {
		applicationDelegate.withCallback(new AbstractAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				loadData();
			}
		}).accountancy(getApplicationRefId()).delete(dto.getRefId());
	}

	}

}
