package com.workpoint.icpak.client.ui.members;

import java.util.List;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.members.header.MembersHeader;
import com.workpoint.icpak.client.ui.members.row.MembersTableRow;
import com.workpoint.icpak.client.ui.members.table.MembersTable;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationSummaryDto;

public class ApplicationsView extends ViewImpl implements
		ApplicationsPresenter.IApplicationsView {

	private final Widget widget;
	@UiField
	HTMLPanel container;

	@UiField
	MembersTable tblView;

	@UiField
	MembersHeader headerContainer;

	public interface Binder extends UiBinder<Widget, ApplicationsView> {
	}

	@Inject
	public ApplicationsView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void bindApplications(List<ApplicationFormHeaderDto> list) {
		tblView.clearRows();
		for (ApplicationFormHeaderDto dto : list) {
			MembersTableRow row = new MembersTableRow(dto);
			tblView.createRow(row);
		}
	}

	@Override
	public void setCount(Integer aCount) {
		tblView.getPagingPanel().setTotal(aCount);
	}

	@Override
	public PagingPanel getPagingPanel() {
		return tblView.getPagingPanel();
	}

	@Override
	public void bindSummary(ApplicationSummaryDto summary) {
		headerContainer.setValues(summary.getPendingCount()+summary.getProcessedCount(), summary.getProcessedCount(),
				summary.getPendingCount());

	}

}
