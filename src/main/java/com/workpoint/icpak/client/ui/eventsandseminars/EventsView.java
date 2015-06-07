package com.workpoint.icpak.client.ui.eventsandseminars;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class EventsView extends ViewImpl implements EventsPresenter.IEventsView{

	private final Widget widget;
	
	@UiField HTMLPanel container; 
	public interface Binder extends UiBinder<Widget, EventsView> {
	}

	@Inject
	public EventsView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

}
