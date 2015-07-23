package com.workpoint.icpak.client.ui.enquiries;

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
import com.workpoint.icpak.client.place.NameTokens;
import com.workpoint.icpak.client.ui.AppManager;
import com.workpoint.icpak.client.ui.OptionControl;
import com.workpoint.icpak.client.ui.admin.TabDataExt;
import com.workpoint.icpak.client.ui.enquiries.form.CreateEnquiry;
import com.workpoint.icpak.client.ui.events.ProcessingEvent;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.security.LoginGateKeeper;

public class EnquiriesPresenter
		extends
		Presenter<EnquiriesPresenter.IEnquiriesView, EnquiriesPresenter.IEnquiriesProxy> {

	public interface IEnquiriesView extends View {
		HasClickHandlers getCreateButton();
	}

	final CreateEnquiry newEnquiry = new CreateEnquiry();

	@ProxyCodeSplit
	@NameToken(NameTokens.enquiries)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface IEnquiriesProxy extends
			TabContentProxyPlace<EnquiriesPresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(LoginGateKeeper adminGatekeeper) {
		TabDataExt data = new TabDataExt("Enquiries", "fa fa-bullhorn", 8,
				adminGatekeeper, true);
		return data;
	}

	@Inject
	public EnquiriesPresenter(final EventBus eventBus,
			final IEnquiriesView view, final IEnquiriesProxy proxy) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
	}

	@Override
	protected void onBind() {
		super.onBind();

		getView().getCreateButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showPopUp(false);
			}
		});
	}

	protected void showPopUp(boolean edit) {
		if (edit) {
			// newEnquiry.setAccomodationDetails(newEnquiry);
		}

		AppManager.showPopUp("New Enquiry", newEnquiry.asWidget(),
				new OptionControl() {
					@Override
					public void onSelect(String name) {
						if (name.equals("Save")) {
							if (newEnquiry.isValid()) {
								fireEvent(new ProcessingEvent());
								save();
							}
						}
					}
				}, "Save");
	}

	protected void save() {
	}

	@Override
	protected void onReset() {
		super.onReset();
	}

}
