package com.workpoint.icpak.client.ui.events;

import com.workpoint.icpak.shared.model.User;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class UserSelectedEvent extends
		GwtEvent<UserSelectedEvent.UserSelectedHandler> {

	public static Type<UserSelectedHandler> TYPE = new Type<UserSelectedHandler>();
	private User user;

	public interface UserSelectedHandler extends EventHandler {
		void onUserSelected(UserSelectedEvent event);
	}

	public UserSelectedEvent(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	@Override
	protected void dispatch(UserSelectedHandler handler) {
		handler.onUserSelected(this);
	}

	@Override
	public Type<UserSelectedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<UserSelectedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, User user) {
		source.fireEvent(new UserSelectedEvent(user));
	}
}
