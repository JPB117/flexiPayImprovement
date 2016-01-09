package com.workpoint.icpak.client.ui.profile;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
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
import com.workpoint.icpak.client.place.NameTokens;
import com.workpoint.icpak.client.security.CurrentUser;
import com.workpoint.icpak.client.service.AbstractAsyncCallback;
import com.workpoint.icpak.client.ui.AppManager;
import com.workpoint.icpak.client.ui.OptionControl;
import com.workpoint.icpak.client.ui.admin.TabDataExt;
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
import com.workpoint.icpak.client.ui.security.BasicMemberGateKeeper;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.api.ApplicationFormResource;
import com.workpoint.icpak.shared.api.CountriesResource;
import com.workpoint.icpak.shared.api.MemberResource;
import com.workpoint.icpak.shared.model.ApplicationFormAccountancyDto;
import com.workpoint.icpak.shared.model.ApplicationFormEducationalDto;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationFormSpecializationDto;
import com.workpoint.icpak.shared.model.ApplicationFormTrainingDto;
import com.workpoint.icpak.shared.model.Country;
import com.workpoint.icpak.shared.model.MemberStanding;
import com.workpoint.icpak.shared.model.auth.ApplicationStatus;

public class ProfilePresenter
		extends
		Presenter<ProfilePresenter.IProfileView, ProfilePresenter.IProfileProxy>
		implements EditModelHandler {

	public interface IProfileView extends View {

		void bindBasicDetails(ApplicationFormHeaderDto result);

		void bindCurrentUser(CurrentUser user);

		HasClickHandlers getSaveButton();

		HasClickHandlers getCancelDetailButton();

		HasClickHandlers getSaveBasicDetailsButton();

		int getActiveTab();

		boolean isValid();

		void setEditMode(boolean editMode);

		void bindEducationDetails(List<ApplicationFormEducationalDto> result);

		HasClickHandlers getEducationDetailSaveButton();

		HasClickHandlers getProfileEditButton();

		HasClickHandlers getEducationAddButton();

		HasClickHandlers getTrainingAddButton();

		HasClickHandlers getSpecializationAddButton();

		void bindTrainingDetails(List<ApplicationFormTrainingDto> arrayList);

		void setApplicationId(String applicationRefId);

		void clear();

		void setCountries(List<Country> countries);

		void bindSpecializations(List<ApplicationFormSpecializationDto> result);

		void bindMemberStanding(MemberStanding standing);

		HasClickHandlers getErpRefreshButton();

		void setApplicationStaus(ApplicationStatus applicationStatus);

		void showBasicMember(boolean show);

		void bindAccountancyDetails(List<ApplicationFormAccountancyDto> result);

		HasClickHandlers getAccountancyAddButton();

		HasClickHandlers getSubmitButton();

		boolean validateBasicDetailIssues();

		void setLastUpdateToNow();
	}

	private final CurrentUser currentUser;

	@ProxyCodeSplit
	@NameToken(NameTokens.profile)
	@UseGatekeeper(BasicMemberGateKeeper.class)
	public interface IProfileProxy extends
			TabContentProxyPlace<ProfilePresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(BasicMemberGateKeeper basicGatekeeper) {
		TabDataExt data = new TabDataExt("My Profile", "icon-user", 9,
				basicGatekeeper, true);
		return data;
	}

	private ResourceDelegate<ApplicationFormResource> applicationDelegate;
	private ResourceDelegate<CountriesResource> countriesResource;
	private ResourceDelegate<MemberResource> memberDelegate;

	@Inject
	public ProfilePresenter(final EventBus eventBus, final IProfileView view,
			final IProfileProxy proxy,
			ResourceDelegate<CountriesResource> countriesResource,
			ResourceDelegate<ApplicationFormResource> applicationDelegate,
			ResourceDelegate<MemberResource> memberDelegate,
			final CurrentUser currentUser) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
		this.countriesResource = countriesResource;
		this.applicationDelegate = applicationDelegate;
		this.memberDelegate = memberDelegate;
		this.currentUser = currentUser;
	}

	final MemberRegistrationForm memberForm = new MemberRegistrationForm();
	EducationRegistrationForm educationForm = new EducationRegistrationForm();
	TrainingRegistrationForm trainingForm = new TrainingRegistrationForm();
	SpecializationRegistrationForm specializationForm = new SpecializationRegistrationForm();
	AccountancyRegistrationForm accountancyForm = new AccountancyRegistrationForm();

	protected ApplicationStatus applicationStatus;

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(EditModelEvent.TYPE, this);

		getView().getProfileEditButton().addClickHandler(new ClickHandler() {
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

		getView().getSubmitButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getView().validateBasicDetailIssues();
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

		getView().getEducationAddButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				educationForm.clear();
				showPopUp(educationForm);
			}
		});

		getView().getAccountancyAddButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				accountancyForm.clear();
				showPopUp(accountancyForm);
			}
		});

		getView().getTrainingAddButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				trainingForm.clear();
				showPopUp(trainingForm);
			}
		});

		getView().getSpecializationAddButton().addClickHandler(
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						specializationForm.clear();
						specializationForm.setEditMode(true);
						loadSpecializations();
						showPopUp(specializationForm);
					}
				});

		getView().getErpRefreshButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				loadDataFromErp(true);
			}
		});

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

	protected boolean saveAccountancyInformation() {
		if (accountancyForm.isValid()) {
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

	protected boolean saveTrainingInformation() {
		String applicationId = getApplicationRefId();

		if (applicationId == null) {
			Window.alert("Current user has no active application");
			return true;
		}

		if (trainingForm.isValid()) {
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

	protected void loadTrainings() {
		String applicationId = getApplicationRefId();
		if (applicationId == null) {
			return;
		}
		applicationDelegate
				.withCallback(
						new AbstractAsyncCallback<List<ApplicationFormTrainingDto>>() {
							@Override
							public void onSuccess(
									List<ApplicationFormTrainingDto> result) {
								// bind Training details
								getView().bindTrainingDetails(result);
							}
						}).training(applicationId).getAll(0, 50);

	}

	protected boolean saveEducationInformation() {
		if (educationForm.isValid()) {
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
										loadData();
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
										loadData();
									}
								}).education(getApplicationRefId()).create(dto);
			}

			return true;
		} else {
			return false;
		}
	}

	protected void saveBasicDetails() {
		if (memberForm.isValid()) {
			fireEvent(new ProcessingEvent());

			// TODO: Tom, you have two instances of MemberRegistrationForm,
			// one is ProfileView & the one in Profile Presenter - memberForm;
			// this is confusing
			// ApplicationFormHeaderDto applicationForm = getView()
			// .getBasicDetails();
			ApplicationFormHeaderDto applicationForm = memberForm
					.getApplicationForm();
			applicationDelegate.withCallback(
					new AbstractAsyncCallback<ApplicationFormHeaderDto>() {
						@Override
						public void onSuccess(ApplicationFormHeaderDto result) {
							// result;
							getView().setEditMode(true);
							loadMemberDetails();
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

	@Override
	protected void onReveal() {
		super.onReveal();
		loadData();
	}

	private void loadData() {
		if (AppContext.isCurrentUserMember()) {
			getView().showBasicMember(false);
			loadDataFromErp(false);
		} else {
			getView().showBasicMember(true);
		}

		String applicationRefId = getApplicationRefId();
		loadData(applicationRefId);
		getView().setApplicationId(applicationRefId);
	}

	/*
	 * forceRefresh - Set it to true if you want to Override
	 */
	private void loadDataFromErp(boolean forceRefesh) {
		fireEvent(new ProcessingEvent());

		/*
		 * memberDelegate.withCallback(new AbstractAsyncCallback<Boolean>() {
		 * 
		 * @Override public void onSuccess(Boolean hasLoaded) { fireEvent(new
		 * ProcessingCompletedEvent()); loadData(getApplicationRefId());
		 * getView().setLastUpdateToNow(); if (!hasLoaded) {
		 * Window.alert("There was a problem loading ERP Data"); } }
		 * }).getDataFromErp(getMemberId(), forceRefesh);
		 */
	}

	private void loadGoodStanding() {
		memberDelegate.withCallback(
				new AbstractAsyncCallback<MemberStanding>() {
					@Override
					public void onSuccess(MemberStanding standing) {
						getView().bindMemberStanding(standing);
					}
				}).getMemberStanding(getMemberId());
	}

	private void loadData(String applicationRefId) {
		getView().clear();
		getView().bindCurrentUser(currentUser);
		countriesResource.withCallback(
				new AbstractAsyncCallback<List<Country>>() {
					public void onSuccess(List<Country> countries) {
						Collections.sort(countries, new Comparator<Country>() {
							@Override
							public int compare(Country o1, Country o2) {
								return o1.getDisplayName().compareTo(
										o2.getDisplayName());
							}
						});
						memberForm.setCountries(countries);
					};
				}).getAll();

		if (applicationRefId != null) {
			loadMemberDetails();
			loadEducation();
			loadTrainings();
			loadSpecializations();
			loadGoodStanding();
			loadAccountancyExamination();
		} else {
			// Window.alert("User refId not sent in this request!");
		}

	}

	private void loadAccountancyExamination() {
		String applicationRefId = getApplicationRefId();
		if (applicationRefId == null) {
			return;
		}
		applicationDelegate
				.withCallback(
						new AbstractAsyncCallback<List<ApplicationFormAccountancyDto>>() {
							@Override
							public void onSuccess(
									List<ApplicationFormAccountancyDto> result) {
								getView().bindAccountancyDetails(result);
							}
						}).accountancy(applicationRefId).getAll(0, 100);
	}

	private void loadMemberDetails() {
		String applicationRefId = getApplicationRefId();
		if (applicationRefId == null) {
			return;
		}
		applicationDelegate.withCallback(
				new AbstractAsyncCallback<ApplicationFormHeaderDto>() {
					@Override
					public void onSuccess(ApplicationFormHeaderDto result) {
						memberForm.bind(result);
						getView().bindBasicDetails(result);
						// ProfilePresenter.this.applicationStatus = result
						// .getApplicationStatus();
						// getView().setApplicationStaus(applicationStatus);
					}
				}).getById(applicationRefId);
	}

	private void loadEducation() {
		String applicationRefId = getApplicationRefId();
		if (applicationRefId == null) {
			return;
		}
		applicationDelegate
				.withCallback(
						new AbstractAsyncCallback<List<ApplicationFormEducationalDto>>() {
							@Override
							public void onSuccess(
									List<ApplicationFormEducationalDto> result) {
								getView().bindEducationDetails(result);
								// Window.alert("Binded Education Form data");
							}
						}).education(applicationRefId).getAll(0, 100);
	}

	private void loadSpecializations() {
		String applicationId = getApplicationRefId();
		fireEvent(new ProcessingEvent());
		if (applicationId == null) {
			return;
		}
		applicationDelegate
				.withCallback(
						new AbstractAsyncCallback<List<ApplicationFormSpecializationDto>>() {
							@Override
							public void onSuccess(
									List<ApplicationFormSpecializationDto> result) {
								// bind Training details
								getView().bindSpecializations(result);
								specializationForm.bindDetails(result);
								fireEvent(new ProcessingCompletedEvent());
							}
						}).specialization(applicationId).getAll(0, 50);
	}

	String getApplicationRefId() {
		String applicationRefId = currentUser.getUser() == null ? null
				: currentUser.getUser().getApplicationRefId();
		return applicationRefId;
	}

	String getMemberId() {
		String applicationRefId = currentUser.getUser() == null ? null
				: currentUser.getUser().getMemberRefId();
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
