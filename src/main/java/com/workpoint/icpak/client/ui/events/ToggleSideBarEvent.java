package com.workpoint.icpak.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ToggleSideBarEvent extends
		GwtEvent<ToggleSideBarEvent.ToggleSideBarHandler> {
	private static Type<ToggleSideBarHandler> TYPE = new Type<ToggleSideBarHandler>();
	private boolean show;

	public interface ToggleSideBarHandler extends EventHandler {
		void onToggleSideBar(ToggleSideBarEvent event);
	}

	public ToggleSideBarEvent() {
		this(true);
	}

	public ToggleSideBarEvent(boolean show) {
		this.show = show;
	}

	public static Type<ToggleSideBarHandler> getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final ToggleSideBarHandler handler) {
		handler.onToggleSideBar(this);
	}

	@Override
	public Type<ToggleSideBarHandler> getAssociatedType() {
		return TYPE;
	}

	public boolean getisShown() {
		return show;
	}

}
