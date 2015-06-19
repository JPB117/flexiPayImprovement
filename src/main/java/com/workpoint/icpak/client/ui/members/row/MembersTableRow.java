package com.workpoint.icpak.client.ui.members.row;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.RowWidget;

public class MembersTableRow extends RowWidget {

	private static ActivitiesTableRowUiBinder uiBinder = GWT
			.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends
			UiBinder<Widget, MembersTableRow> {
	}

	@UiField
	HTMLPanel row;
	@UiField
	HTMLPanel divDate;
	@UiField
	HTMLPanel divMemberNo;
	@UiField
	HTMLPanel divMemberName;
	@UiField
	HTMLPanel divStatus;
	@UiField
	HTMLPanel divAction;

	public MembersTableRow() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
