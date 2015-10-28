package com.workpoint.icpak.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.workpoint.icpak.shared.model.RoleDto;

public class EditGroupEvent extends GwtEvent<EditGroupEvent.EditGroupHandler> {

	public static Type<EditGroupHandler> TYPE = new Type<EditGroupHandler>();
	private RoleDto group;

	public interface EditGroupHandler extends EventHandler {
		void onEditGroup(EditGroupEvent event);
	}

	public EditGroupEvent(RoleDto group) {
		this.group = group;
	}

	public RoleDto getGroup() {
		return group;
	}

	@Override
	protected void dispatch(EditGroupHandler handler) {
		handler.onEditGroup(this);
	}

	@Override
	public Type<EditGroupHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<EditGroupHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, RoleDto group) {
		source.fireEvent(new EditGroupEvent(group));
	}
}
