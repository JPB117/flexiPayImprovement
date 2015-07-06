package com.workpoint.icpak.client.ui.members;

import java.util.List;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.members.header.MembersHeader;
import com.workpoint.icpak.client.ui.members.row.MembersTableRow;
import com.workpoint.icpak.client.ui.members.table.MembersTable;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;

public class MembersView extends ViewImpl implements
		MembersPresenter.IMembersView {

	private final Widget widget;
	@UiField
	HTMLPanel container;

	@UiField
	MembersTable tblView;
	
	@UiField
	MembersHeader headerContainer;

	public interface Binder extends UiBinder<Widget, MembersView> {
	}

	@Inject
	public MembersView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void bindApplications(List<ApplicationFormHeaderDto> list) {
		headerContainer.setValues(list.size(), 0, list.size());
		
		tblView.clearRows();
		for(ApplicationFormHeaderDto dto: list){
			MembersTableRow row = new MembersTableRow(dto);
			tblView.createRow(row);
		}
	}

}
