package com.workpoint.icpak.client.ui.accomodation;

import java.util.List;

import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.accomodation.row.AccomodationTableRow;
import com.workpoint.icpak.client.ui.accomodation.table.AccomodationTable;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.shared.model.events.AccommodationDto;

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
	AccomodationTable tblView;

	public interface Binder extends UiBinder<Widget, AccomodationView> {
	}

	@Inject
	public AccomodationView(final Binder binder) {
		widget = binder.createAndBindUi(this);
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
		for(AccommodationDto accommodation: accommodations){
			tblView.createRow(new AccomodationTableRow(accommodation));
		}
	}

}
