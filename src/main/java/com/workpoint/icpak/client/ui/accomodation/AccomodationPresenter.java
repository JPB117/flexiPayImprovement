package com.workpoint.icpak.client.ui.accomodation;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
import com.workpoint.icpak.client.service.AbstractAsyncCallback;
import com.workpoint.icpak.client.ui.AppManager;
import com.workpoint.icpak.client.ui.OnOptionSelected;
import com.workpoint.icpak.client.ui.OptionControl;
import com.workpoint.icpak.client.ui.accomodation.form.CreateAccomodation;
import com.workpoint.icpak.client.ui.admin.TabDataExt;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.events.EditModelEvent;
import com.workpoint.icpak.client.ui.events.EditModelEvent.EditModelHandler;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent;
import com.workpoint.icpak.client.ui.events.ProcessingEvent;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.security.AdminGateKeeper;
import com.workpoint.icpak.client.ui.security.EventsGateKeeper;
import com.workpoint.icpak.shared.api.EventsResource;
import com.workpoint.icpak.shared.model.events.AccommodationDto;
import com.workpoint.icpak.shared.model.events.EventDto;

public class AccomodationPresenter
		extends
		Presenter<AccomodationPresenter.IAccomodationView, AccomodationPresenter.IAccomodationProxy>
		implements EditModelHandler {

	private ResourceDelegate<EventsResource> eventResource;

	public interface IAccomodationView extends View {
		HasClickHandlers getCreateButton();

		DropDownList<EventDto> getEventList();

		void bindAccommodations(List<AccommodationDto> accommodations);

		void setEvents(List<EventDto> events);

		void showCreate(boolean b);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.accomodation)
	@UseGatekeeper(EventsGateKeeper.class)
	public interface IAccomodationProxy extends
			TabContentProxyPlace<AccomodationPresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(EventsGateKeeper adminGatekeeper) {
		TabDataExt data = new TabDataExt("Accomodation", "fa fa-bed", 3,
				adminGatekeeper, true);
		return data;
	}

	List<EventDto> events = null;

	@Inject
	public AccomodationPresenter(final EventBus eventBus,
			final IAccomodationView view, final IAccomodationProxy proxy,
			final ResourceDelegate<EventsResource> eventResource) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
		this.eventResource = eventResource;
	}

	@Override
	protected void onBind() {
		super.onBind();

		addRegisteredHandler(EditModelEvent.TYPE, this);
		getView().getCreateButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showPopUp(null);
			}
		});

		getView().getEventList().addValueChangeHandler(
				new ValueChangeHandler<EventDto>() {

					@Override
					public void onValueChange(ValueChangeEvent<EventDto> event) {
						if (event.getValue() == null) {
							getView().bindAccommodations(
									new ArrayList<AccommodationDto>());
							return;
						}
						loadAccommodations(event.getValue().getRefId());
					}
				});

		/*
		 * getEditButton().addClickHandler(new ClickHandler() {
		 * 
		 * @Override public void onClick(ClickEvent event) { showPopUp(false); }
		 * });
		 */

	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		loadEvents();
	}

	private void loadAccommodations(String eventId) {
		eventResource
				.withCallback(
						new AbstractAsyncCallback<List<AccommodationDto>>() {
							@Override
							public void onSuccess(
									List<AccommodationDto> accommodations) {
								getView().bindAccommodations(accommodations);
								fireEvent(new ProcessingCompletedEvent());
							}
						}).accommodations(eventId).getAll(0, 100);

	}

	private void loadEvents() {
		fireEvent(new ProcessingEvent());
		eventResource.withCallback(new AbstractAsyncCallback<List<EventDto>>() {
			@Override
			public void onSuccess(List<EventDto> events) {
				getView().setEvents(events);
				if (events.size() > 0) {
					getView().getEventList().setValue(events.get(0));
					loadAccommodations(events.get(0).getRefId());
					getView().showCreate(true);
				}
			}
		}).getAll(0, 100, "");
	}

	protected void showPopUp(AccommodationDto dto) {
		final CreateAccomodation newAccomodation = new CreateAccomodation();
		newAccomodation.setEvent(getView().getEventList().getValue());
		newAccomodation.setAccomodationDetails(dto);

		AppManager.showPopUp("Create Accommodation",
				newAccomodation.asWidget(), new OptionControl() {
					@Override
					public void onSelect(String name) {
						if (name.equals("Save")) {
							if (newAccomodation.isValid()) {
								AccommodationDto accommodation = newAccomodation
										.getAccomodationDetails();
								saveAccommodation(accommodation.getEvent()
										.getRefId(), accommodation);
								hide();
							}
						}
					}
				}, "Save");
	}

	protected void saveAccommodation(final String eventId,
			final AccommodationDto accomodation) {

		fireEvent(new ProcessingEvent());
		if (accomodation.getRefId() != null) {

			eventResource
					.withCallback(
							new AbstractAsyncCallback<AccommodationDto>() {
								@Override
								public void onSuccess(AccommodationDto result) {
									fireEvent(new ProcessingCompletedEvent());
									loadAccommodations(eventId);
								}
							}).accommodations(eventId)
					.update(accomodation.getRefId(), accomodation);

		} else {

			eventResource
					.withCallback(
							new AbstractAsyncCallback<AccommodationDto>() {
								@Override
								public void onSuccess(AccommodationDto result) {
									// fireEvent(new
									// ProcessingCompletedEvent());
									loadAccommodations(eventId);
								}

							}).accommodations(eventId).create(accomodation);
		}

	}

	@Override
	public void onEditModel(EditModelEvent event) {
		if (event.getModel() instanceof AccommodationDto) {
			final AccommodationDto dto = (AccommodationDto) event.getModel();
			if (event.isDelete() && dto.getRefId() != null) {
				AppManager.showPopUp("Delete Accommodation",
						"Delete Accommodation " + dto.getHotel() + "?",
						new OnOptionSelected() {

							@Override
							public void onSelect(String name) {
								if (name.equals("Yes")) {
									deleteAccommodation(dto);
								}
							}
						}, "Yes", "Cancel");
			} else {
				showPopUp((AccommodationDto) (event.getModel()));
			}
		}
	}

	protected void deleteAccommodation(final AccommodationDto dto) {
		eventResource.withCallback(new AbstractAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void arg0) {
				loadAccommodations(dto.getEvent().getRefId());
			}
		}).accommodations(dto.getEvent().getRefId()).delete(dto.getRefId());

	}
}
