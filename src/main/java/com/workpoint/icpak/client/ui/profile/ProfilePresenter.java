package com.workpoint.icpak.client.ui.profile;

//import com.workpoint.icpak.shared.requests.CheckPasswordRequest;
//import com.workpoint.icpak.shared.requests.GetUserRequest;
//import com.workpoint.icpak.shared.requests.SaveUserRequest;
//import com.workpoint.icpak.shared.requests.UpdatePasswordRequest;
//import com.workpoint.icpak.shared.responses.CheckPasswordRequestResult;
//import com.workpoint.icpak.shared.responses.GetUserRequestResult;
//import com.workpoint.icpak.shared.responses.SaveUserResponse;
//import com.workpoint.icpak.shared.responses.UpdatePasswordResponse;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
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
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.security.LoginGateKeeper;
import com.workpoint.icpak.shared.api.ApplicationFormResource;
import com.workpoint.icpak.shared.model.ApplicationFormEducationalDto;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;

public class ProfilePresenter
		extends
		Presenter<ProfilePresenter.IProfileView, ProfilePresenter.IProfileProxy> {

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
		ApplicationFormEducationalDto getEducationDetails();
		void bindEducationDetails(List<ApplicationFormEducationalDto> result);
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

	@Override
	protected void onBind() {
		super.onBind();
		getView().getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int currentTab = getView().getActiveTab(); 
				if (currentTab == 0) {
					// Basic Details
					saveBasicDetails();
				}else if(currentTab==1){
					
					saveEducationInformation();
				}else if(currentTab==2){
					
				}
				
			}
		});
		
		getView().getSaveBasicDetailsButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				saveBasicDetails();
			}
		});
		
		getView().getCancelDetailButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				getView().setEditMode(false);
			}
		});
	}

	protected void saveEducationInformation() {
//		if(getView().isValid()){
//			applicationDelegate.withCallback(new AbstractAsyncCallback<ApplicationFormEducationalDto>() {
//				@Override
//				public void onSuccess(ApplicationFormEducationalDto result) {
//					
//				}
//			}).education(getApplicationRefId())
//			.create(getView().getEducationDetails());
//		}
	}

	protected void saveBasicDetails() {
		if (getView().isValid()) {
			ApplicationFormHeaderDto applicationForm = getView()
					.getBasicDetails();
			applicationDelegate.withCallback(
					new AbstractAsyncCallback<ApplicationFormHeaderDto>() {
						@Override
						public void onSuccess(ApplicationFormHeaderDto result) {
							// result;
							getView().setEditMode(false);
							getView().bindBasicDetails(result);
							
						}

						@Override
						public void onFailure(Throwable caught) {
							super.onFailure(caught);
						}
					}).update(getApplicationRefId(), applicationForm);
		}
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		loadData();
	}

	private void loadData() {
		getView().bindCurrentUser(currentUser);
		String applicationRefId = getApplicationRefId();
		
		//Window.alert("ApplicationID = "+applicationRefId+" >> User = "+currentUser.getUser());
		if (applicationRefId != null){
			applicationDelegate.withCallback(
					new AbstractAsyncCallback<ApplicationFormHeaderDto>() {
						@Override
						public void onSuccess(ApplicationFormHeaderDto result) {
							getView().bindBasicDetails(result);
						}
					}).getById(applicationRefId);
			
//			applicationDelegate.withCallback(new AbstractAsyncCallback<List<ApplicationFormEducationalDto>>() {
//				@Override
//				public void onSuccess(List<ApplicationFormEducationalDto> result) {
//					getView().bindEducationDetails(result);
//				}
//			}).education(applicationRefId)
//			.getAll(0, 100);
//			
		}
	}

	String getApplicationRefId() {
		String applicationRefId = currentUser.getUser() == null ? null
				: currentUser.getUser().getApplicationRefId();

		return applicationRefId;
	}

	protected void save() {

	}

}
