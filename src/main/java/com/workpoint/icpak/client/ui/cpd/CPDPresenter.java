package com.workpoint.icpak.client.ui.cpd;

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
import com.workpoint.icpak.client.ui.OnOptionSelected;
import com.workpoint.icpak.client.ui.OptionControl;
import com.workpoint.icpak.client.ui.admin.TabDataExt;
import com.workpoint.icpak.client.ui.cpd.record.RecordCPD;
import com.workpoint.icpak.client.ui.events.EditModelEvent;
import com.workpoint.icpak.client.ui.events.EditModelEvent.EditModelHandler;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.popup.GenericPopupPresenter;
import com.workpoint.icpak.client.ui.security.LoginGateKeeper;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.api.MemberResource;
import com.workpoint.icpak.shared.model.CPDDto;

public class CPDPresenter extends
		Presenter<CPDPresenter.ICPDView, CPDPresenter.ICPDProxy> implements
		EditModelHandler {

	public interface ICPDView extends View {
		HasClickHandlers getRecordButton();

		void bindResults(List<CPDDto> result);

		void showDetailedView();

	}

	@ProxyCodeSplit
	@NameToken(NameTokens.cpd)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface ICPDProxy extends TabContentProxyPlace<CPDPresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(LoginGateKeeper adminGatekeeper) {
		TabDataExt data = new TabDataExt("My C.P.D", "fa fa-graduation-cap", 5,
				adminGatekeeper, true);
		return data;
	}

	protected final ResourceDelegate<MemberResource> memberDelegate;
	protected final CurrentUser currentUser;

	@Inject
	public CPDPresenter(final EventBus eventBus, final ICPDView view,
			final ICPDProxy proxy,
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
		getView().getRecordButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showCreatePopup();
			}
		});

	}

	@Inject
	GenericPopupPresenter popup;

	protected void showCreatePopup() {
		showInstructions();
	}

	private void showInstructions(){
		showInstructions(null);
	}
	
	private void showInstructions(final CPDDto model) {
		final RecordCPD cpdRecord = new RecordCPD();
		cpdRecord.setCPD(model);
		cpdRecord.showForm(false);
		AppManager.showPopUp("Record CPD Wizard", cpdRecord.asWidget(),
				new OnOptionSelected() {
					@Override
					public void onSelect(String name) {
						if (name.equals("Next")) {
							showForm(model);
						}
					}
				}, "Next");
	}

	protected void showForm() {
		showForm(null);
	}

	protected void showForm(final CPDDto model) {
		final RecordCPD cpdRecord = new RecordCPD();
		cpdRecord.setCPD(model);
		cpdRecord.showForm(true);
		AppManager.showPopUp("Record CPD Wizard", cpdRecord.asWidget(),
				new OptionControl() {
					@Override
					public void onSelect(String name) {
						if (name.equals("Save")) {
							if (cpdRecord.isValid()) {
								saveRecord(cpdRecord.getCPD());
								hide();
							}
						} else {

							showInstructions(model);
						}
					}
				}, "Previous", "Save");
	}

	protected void saveRecord(CPDDto dto) {
		String memberId = currentUser.getUser().getRefId();

		if (dto.getRefId() != null) {
			// Update
			memberDelegate.withCallback(new AbstractAsyncCallback<CPDDto>() {
				@Override
				public void onSuccess(CPDDto result) {
					loadData();
				}
			}).cpd(memberId).update(dto.getRefId(), dto);
			
		} else {
			memberDelegate.withCallback(new AbstractAsyncCallback<CPDDto>() {
				@Override
				public void onSuccess(CPDDto result) {
					loadData();
				}
			}).cpd(memberId).create(dto);
		}

	}

	@Override
	protected void onReveal() {
		super.onReveal();
		loadData();
	}

	protected void loadData() {
		String memberId = currentUser.getUser().getRefId();

		memberDelegate.withCallback(new AbstractAsyncCallback<List<CPDDto>>() {
			@Override
			public void onSuccess(List<CPDDto> result) {
				getView().bindResults(result);
			}
		}).cpd(AppContext.isCurrentUserAdmin()? "ALL":memberId).getAll(0, 100);
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
		if (event.getModel() instanceof CPDDto) {
			if (event.isDelete()) {
				delete((CPDDto) event.getModel());
			} else {
				showForm((CPDDto) event.getModel());
			}
		}
	}

	private void delete(CPDDto model) {

	}

}
