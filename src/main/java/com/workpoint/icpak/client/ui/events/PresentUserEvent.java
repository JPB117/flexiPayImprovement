package com.workpoint.icpak.client.ui.events;

import com.workpoint.icpak.shared.model.UserDto;
import com.workpoint.icpak.shared.model.UserGroup;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class PresentUserEvent extends
		GwtEvent<PresentUserEvent.PresentUserHandler> {

	public static Type<PresentUserHandler> TYPE = new Type<PresentUserHandler>();
	private UserDto user;
	private UserGroup group;
	
	public static Type<PresentUserHandler> getTYPE() {
		return TYPE;
	}

	public UserGroup getGroup() {
		return group;
	}

	public interface PresentUserHandler extends EventHandler {
		void onPresentUser(PresentUserEvent event);
	}

	public PresentUserEvent(UserDto user, UserGroup group) {
		this.user = user;
		this.group = group;
	}

	public UserDto getUser() {
		return user;
	}

	@Override
	protected void dispatch(PresentUserHandler handler) {
		handler.onPresentUser(this);
	}

	@Override
	public Type<PresentUserHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<PresentUserHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, UserDto user, UserGroup group) {
		source.fireEvent(new PresentUserEvent(user,group));
	}
}
