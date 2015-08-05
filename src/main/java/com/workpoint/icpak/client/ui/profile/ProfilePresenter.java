package com.workpoint.icpak.client.ui.profile;

import java.util.ArrayList;
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
import com.workpoint.icpak.client.ui.profile.education.form.EducationRegistrationForm;
import com.workpoint.icpak.client.ui.profile.specialization.form.SpecializationRegistrationForm;
import com.workpoint.icpak.client.ui.profile.training.form.TrainingRegistrationForm;
import com.workpoint.icpak.client.ui.profile.widget.ProfileWidget;
import com.workpoint.icpak.client.ui.security.LoginGateKeeper;
import com.workpoint.icpak.shared.api.ApplicationFormResource;
import com.workpoint.icpak.shared.model.ApplicationFormEducationalDto;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationFormTrainingDto;

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

		ApplicationFormHeaderDto getBasicDetails();

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
	}

	private final CurrentUser currentUser;

	@ProxyCodeSplit
	@NameToken(NameTokens.profile)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface IProfileProxy extends
			TabContentProxyPlace<ProfilePresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(LoginGateKeeper adminGatekeeper) {
		TabDataExt data = new TabDataExt("My Profile", "icon-user", 8,
				adminGatekeeper, true);
		return data;
	}

	private ResourceDelegate<ApplicationFormResource> applicationDelegate;

	@Inject
	public ProfilePresenter(final EventBus eventBus, final IProfileView view,
			final IProfileProxy proxy,
			ResourceDelegate<ApplicationFormResource> applicationDelegate,
			final CurrentUser currentUser) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
		this.applicationDelegate = applicationDelegate;
		this.currentUser = currentUser;
	}

	final MemberRegistrationForm memberForm = new MemberRegistrationForm();
	EducationRegistrationForm educationForm = new EducationRegistrationForm();
	TrainingRegistrationForm trainingForm = new TrainingRegistrationForm();
	SpecializationRegistrationForm specializationForm = new SpecializationRegistrationForm();

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
								}
							}
						}, "Save");
			}
		});

		getView().getEducationAddButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				educationForm.clear();
				showPopUp(educationForm);
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
						showPopUp(specializationForm);
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
							}
						}
					}
				}, "Save");
	}

	protected boolean saveSpecializationInformation() {
		if (specializationForm.isValid()) {
			// Save Specialization Here
			return true;
		} else {
			return false;
		}
	}

	protected boolean saveTrainingInformation() {
		if (trainingForm.isValid()) {
			// Save Training Here
			fireEvent(new ProcessingEvent());
			ApplicationFormTrainingDto dto = trainingForm.getTrainingDto();

			return true;
		} else {
			return false;
		}
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
										getView().setEditMode(false);
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
										getView().setEditMode(false);
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
			ApplicationFormHeaderDto applicationForm = getView()
					.getBasicDetails();
			applicationDelegate.withCallback(
					new AbstractAsyncCallback<ApplicationFormHeaderDto>() {
						@Override
						public void onSuccess(ApplicationFormHeaderDto result) {
							// result;
							getView().bindBasicDetails(result);
							getView().setEditMode(false);
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
		getView().bindCurrentUser(currentUser);
		String applicationRefId = getApplicationRefId();

		loadData(applicationRefId);
		getView().setApplicationId(applicationRefId);
		
	}

	private void loadData(String applicationRefId) {

		getView().clear();

		if (applicationRefId != null) {
			applicationDelegate.withCallback(
					new AbstractAsyncCallback<ApplicationFormHeaderDto>() {
						@Override
						public void onSuccess(ApplicationFormHeaderDto result) {
							getView().bindBasicDetails(result);
							memberForm.bind(result);
							// Window.alert("Binded Basic data");
						}
					}).getById(applicationRefId);

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

			// bind Training details
			getView().bindTrainingDetails(
					new ArrayList<ApplicationFormTrainingDto>());

			// bind Specialization details

		} else {
			Window.alert("User refId not sent in this request!");
		}

	}

	String getApplicationRefId() {
		String applicationRefId = currentUser.getUser() == null ? null
				: currentUser.getUser().getApplicationRefId();
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
		} else if (event.getModel() instanceof ApplicationFormHeaderDto) {
			ApplicationFormHeaderDto headerDto = (ApplicationFormHeaderDto) event
					.getModel();

			loadData(headerDto.getUserRefId());
			AppManager.showPopUp("View Profile Info", new ProfileWidget(),
					null, "Done");
		}

	}

	private void delete(ApplicationFormEducationalDto dto) {
		applicationDelegate.withCallback(new AbstractAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				loadData();
			}
		}).education(getApplicationRefId()).delete(dto.getRefId());
	}

}
