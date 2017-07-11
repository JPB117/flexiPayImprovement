package com.workpoint.icpak.client.ui.accomodation;

import java.util.List;

import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.accomodation.row.AccomodationTableRow;
import com.workpoint.icpak.client.ui.accomodation.table.AccomodationTable;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.events.AccommodationDto;
import com.workpoint.icpak.shared.model.events.EventDto;

public class AccomodationView extends ViewImpl implements
		AccomodationPresenter.IAccomodationView {

	private final Widget widget;

	@UiField
	HTMLPanel container;
	@UiField
	ActionLink aCreate;
	@UiField
	SpanElement spnEventTitle;
	@UiField
	DropDownList<EventDto> lstEvents;
	@UiField
	AccomodationTable tblView;

	public interface Binder extends UiBinder<Widget, AccomodationView> {
	}

	@Inject
	public AccomodationView(final Binder binder) {
		widget = binder.createAndBindUi(this);

		if (AppContext.isCurrentUserEventEdit()) {
			lstEvents.addValueChangeHandler(new ValueChangeHandler<EventDto>() {
				@Override
				public void onValueChange(ValueChangeEvent<EventDto> arg0) {
					boolean show = arg0.getValue() != null;
					aCreate.removeStyleName("hide");
				}
			});
		} else {
			aCreate.addStyleName("hide");
		}
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public HasClickHandlers getCreateButton() {
		return aCreate;
	}

	@Override
	public void bindAccommodations(List<AccommodationDto> accommodations) {
		tblView.clearRows();
		if (accommodations.isEmpty()) {
			tblView.showEmptyText(true, "No records to display");
		} else {
			tblView.showEmptyText(false, null);
		}

		for (AccommodationDto accommodation : accommodations) {
			tblView.createRow(new AccomodationTableRow(accommodation));
		}
	}

	public DropDownList<EventDto> getEventList() {
		return lstEvents;
	}

	@Override
	public void setEvents(List<EventDto> events) {
		lstEvents.setItems(events);
	}

	@Override
	public void showCreate(boolean show) {
		aCreate.setVisible(show);
	}

}
