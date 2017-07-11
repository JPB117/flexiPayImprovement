package com.workpoint.icpak.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.workpoint.icpak.shared.model.UserDto;

public class EditUserEvent extends GwtEvent<EditUserEvent.EditUserHandler> {

	public static Type<EditUserHandler> TYPE = new Type<EditUserHandler>();
	private UserDto user;

	public interface EditUserHandler extends EventHandler {
		void onEditUser(EditUserEvent event);
	}

	public EditUserEvent(UserDto user) {
		this.user = user;
	}

	public UserDto getUser() {
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

	public static void fire(HasHandlers source, UserDto user) {
		source.fireEvent(new EditUserEvent(user));
	}
}
