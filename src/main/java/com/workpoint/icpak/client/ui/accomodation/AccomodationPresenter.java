package com.workpoint.icpak.client.ui.accomodation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Timer;
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
import com.workpoint.icpak.client.ui.OnOptionSelected;
import com.workpoint.icpak.client.ui.OptionControl;
import com.workpoint.icpak.client.ui.accomodation.form.CreateAccomodation;
import com.workpoint.icpak.client.ui.admin.TabDataExt;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent;
import com.workpoint.icpak.client.ui.events.ProcessingEvent;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.security.LoginGateKeeper;
import com.workpoint.icpak.shared.model.AccomodationDTO;

public class AccomodationPresenter
		extends
		Presenter<AccomodationPresenter.IAccomodationView, AccomodationPresenter.IAccomodationProxy> {

	public interface IAccomodationView extends View {
		HasClickHandlers getCreateButton();
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.accomodation)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface IAccomodationProxy extends
			TabContentProxyPlace<AccomodationPresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(LoginGateKeeper adminGatekeeper) {
		TabDataExt data = new TabDataExt("Accomodation", "fa fa-bed", 3,
				adminGatekeeper, true);
		return data;
	}

	private AccomodationDTO accomodation;
	final CreateAccomodation newAccomodation = new CreateAccomodation();

	@Inject
	public AccomodationPresenter(final EventBus eventBus,
			final IAccomodationView view, final IAccomodationProxy proxy) {
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

		/*
		 * getEditButton().addClickHandler(new ClickHandler() {
		 * 
		 * @Override public void onClick(ClickEvent event) { showPopUp(false); }
		 * });
		 */
		
	}

	protected void showPopUp(boolean edit) {
		if (edit) {
			newAccomodation.setAccomodationDetails(accomodation);
		}

		AppManager.showPopUp("Create Accommodation",
				newAccomodation.asWidget(), new OptionControl() {
					@Override
					public void onSelect(String name) {
						if (name.equals("Save")) {
							if (newAccomodation.isValid()) {
								fireEvent(new ProcessingEvent());
								save();
							}
						}
					}
				}, "Save");
	}

	protected void save() {
		Timer t = new Timer() {
			@Override
			public void run() {
				//fireEvent(new ProcessingCompletedEvent());
			}
		};
		// Schedule the timer to run once in 5 seconds.
		t.schedule(5000);
	}

	@Override
	protected void onReset() {
		super.onReset();
	}

}
