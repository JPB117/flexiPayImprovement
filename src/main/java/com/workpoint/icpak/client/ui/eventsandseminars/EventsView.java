package com.workpoint.icpak.client.ui.eventsandseminars;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class EventsView extends ViewImpl implements EventsPresenter.IEventsView {

	private final Widget widget;

	@UiField
	HTMLPanel container;

	@UiField
	HTMLPanel panelEvents;

	@UiField
	HTMLPanel panelEventDrillDown;

	public interface Binder extends UiBinder<Widget, EventsView> {
		
	}

	@Inject
	public EventsView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		showAdvancedView(false);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void showAdvancedView(boolean show) {
		if (show) {
			panelEventDrillDown.setVisible(true);
			panelEvents.setVisible(false);
		} else {
			panelEventDrillDown.setVisible(false);
			panelEvents.setVisible(true);
		}
	}

}
