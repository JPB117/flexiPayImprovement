package com.workpoint.icpak.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.workpoint.icpak.shared.model.UserDto;

public class EditModelEvent extends GwtEvent<EditModelEvent.EditModelHandler> {

	public static Type<EditModelHandler> TYPE = new Type<EditModelHandler>();
	private Object model;
	private boolean isDelete;

	public interface EditModelHandler extends EventHandler {
		void onEditModel(EditModelEvent event);
	}

	public EditModelEvent(Object model) {
		this.model = model;
	}

	public EditModelEvent(Object model, boolean isDelete) {
		this.model = model;
		this.isDelete = isDelete;
	}

	@Override
	protected void dispatch(EditModelHandler handler) {
		handler.onEditModel(this);
	}

	@Override
	public Type<EditModelHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<EditModelHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, UserDto user) {
		source.fireEvent(new EditUserEvent(user));
	}


	public Object getModel() {
		return model;
	}


	public void setModel(Object model) {
		this.model = model;
	}

	public boolean isDelete() {
		return isDelete;
	}
}