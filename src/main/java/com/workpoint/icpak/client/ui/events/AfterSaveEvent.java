package com.workpoint.icpak.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class AfterSaveEvent extends GwtEvent<AfterSaveEvent.AfterSaveHandler> {

	public static Type<AfterSaveHandler> TYPE = new Type<AfterSaveHandler>();
	private String message;

	public interface AfterSaveHandler extends EventHandler {
		void onAfterSave(AfterSaveEvent event);
	}

	public AfterSaveEvent() {
	}

	public AfterSaveEvent(String message) {
		this.message = message;
	}

	@Override
	protected void dispatch(AfterSaveHandler handler) {
		handler.onAfterSave(this);
	}

	@Override
	public Type<AfterSaveHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<AfterSaveHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new AfterSaveEvent());
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
