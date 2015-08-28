package com.workpoint.icpak.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.workpoint.icpak.client.ui.cpd.table.row.CPDTableRow.TableActionType;
import com.workpoint.icpak.shared.model.UserDto;
import com.workpoint.icpak.shared.model.auth.ActionType;

public class TableActionEvent extends
		GwtEvent<TableActionEvent.TableActionHandler> {

	public static Type<TableActionHandler> TYPE = new Type<TableActionHandler>();
	private Object model;
	private TableActionType action;
	private boolean isDelete;

	public interface TableActionHandler extends EventHandler {
		void onTableAction(TableActionEvent event);
	}

	public TableActionEvent(Object model, TableActionType action) {
		this.model = model;
		this.action = action;
	}

	public TableActionEvent(Object model, boolean isDelete) {
		this.model = model;
		this.isDelete = isDelete;
	}

	@Override
	protected void dispatch(TableActionHandler handler) {
		handler.onTableAction(this);
	}

	@Override
	public Type<TableActionHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<TableActionHandler> getType() {
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

	public TableActionType getAction() {
		return action;
	}

	public void setAction(TableActionType action) {
		this.action = action;
	}
}