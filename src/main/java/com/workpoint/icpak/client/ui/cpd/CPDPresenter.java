package com.workpoint.icpak.client.ui.cpd;

//import com.workpoint.icpak.shared.requests.CheckPasswordRequest;
//import com.workpoint.icpak.shared.requests.GetUserRequest;
//import com.workpoint.icpak.shared.requests.SaveUserRequest;
//import com.workpoint.icpak.shared.requests.UpdatePasswordRequest;
//import com.workpoint.icpak.shared.responses.CheckPasswordRequestResult;
//import com.workpoint.icpak.shared.responses.GetUserRequestResult;
//import com.workpoint.icpak.shared.responses.SaveUserResponse;
//import com.workpoint.icpak.shared.responses.UpdatePasswordResponse;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
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
import com.workpoint.icpak.client.ui.AppManager;
import com.workpoint.icpak.client.ui.OnOptionSelected;
import com.workpoint.icpak.client.ui.OptionControl;
import com.workpoint.icpak.client.ui.admin.TabDataExt;
import com.workpoint.icpak.client.ui.cpd.record.RecordCPD;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.popup.GenericPopupPresenter;
import com.workpoint.icpak.client.ui.security.LoginGateKeeper;

public class CPDPresenter extends
		Presenter<CPDPresenter.ICPDView, CPDPresenter.ICPDProxy> {

	public interface ICPDView extends View {

		HasClickHandlers getRecordButton();

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

	@Inject
	public CPDPresenter(final EventBus eventBus, final ICPDView view,
			final ICPDProxy proxy) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
	}

	@Override
	protected void onBind() {
		super.onBind();

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

	private void showInstructions() {
		final RecordCPD cpdRecord = new RecordCPD();
		cpdRecord.showForm(false);
		AppManager.showPopUp("Record CPD Wizard", cpdRecord.asWidget(), new OnOptionSelected() {
			@Override
			public void onSelect(String name) {
				if (name.equals("Next")) {
					showForm();
				}
			}
		}, "Next");
	}

	protected void showForm() {
		final RecordCPD cpdRecord = new RecordCPD();
		cpdRecord.showForm(true);
		AppManager.showPopUp("Record CPD Wizard", cpdRecord.asWidget(), new OnOptionSelected() {
			@Override
			public void onSelect(String name) {
				if (name.equals("Save")) {
					saveRecord();
				}else{
					
					showInstructions();
				}
			}
		}, "Previous","Save");
	}

	protected void saveRecord() {
		
	}

	@Override
	protected void onReset() {
		super.onReset();
	}
	
	
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		
		getView().showDetailedView();
	}

}
