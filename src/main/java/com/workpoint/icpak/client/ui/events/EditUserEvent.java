package com.workpoint.icpak.client.ui.events;

import com.workpoint.icpak.shared.model.User;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class EditUserEvent extends GwtEvent<EditUserEvent.EditUserHandler> {

	public static Type<EditUserHandler> TYPE = new Type<EditUserHandler>();
	private User user;

	public interface EditUserHandler extends EventHandler {
		void onEditUser(EditUserEvent event);
	}

	public EditUserEvent(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	@Override
	protected void dispatch(EditUserHandler handler) {
		handler.onEditUser(this);
	}

	@Override
	public Type<EditUserHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<EditUserHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, User user) {
		source.fireEvent(new EditUserEvent(user));
	}
}
