package com.workpoint.icpak.client.ui.members;

import java.util.List;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.members.row.MembersTableRow;
import com.workpoint.icpak.client.ui.members.table.MembersTable;
import com.workpoint.icpak.client.ui.statements.row.StatementTableRow;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;

public class MembersView extends ViewImpl implements
		MembersPresenter.IMembersView {

	private final Widget widget;
	@UiField
	HTMLPanel container;

	@UiField
	MembersTable tblView;

	public interface Binder extends UiBinder<Widget, MembersView> {
	}

	@Inject
	public MembersView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		showData();
	}

	private void showData() {
		for (int i = 0; i < 20; i++) {
			MembersTableRow row = new MembersTableRow();
			tblView.createRow(row);
		}
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void bindApplications(List<ApplicationFormHeaderDto> result) {
		tblView.bindApplications(result);
	}

}
