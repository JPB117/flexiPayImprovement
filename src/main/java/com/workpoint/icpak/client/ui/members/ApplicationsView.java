package com.workpoint.icpak.client.ui.members;

import java.util.List;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.members.header.MembersHeader;
import com.workpoint.icpak.client.ui.members.row.MembersTableRow;
import com.workpoint.icpak.client.ui.members.table.MembersTable;
import com.workpoint.icpak.client.ui.profile.widget.ProfileWidget;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationSummaryDto;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.auth.ApplicationStatus;

public class ApplicationsView extends ViewImpl implements
		ApplicationsPresenter.IApplicationsView {

	private final Widget widget;
	@UiField
	HTMLPanel container;

	@UiField
	DivElement divSingleApplication;
	@UiField
	DivElement divAllApplication;
	@UiField
	MembersTable tblView;
	@UiField
	MembersHeader headerContainer;
	@UiField
	ProfileWidget panelProfile;

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
		int counter = 0;
		for (ApplicationFormHeaderDto dto : list) {
			MembersTableRow row = new MembersTableRow(dto, counter);
			tblView.createRow(row);
			counter++;
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
		headerContainer.setValues(
				summary.getPendingCount() + summary.getProcessedCount(),
				summary.getProcessedCount(), summary.getPendingCount());

	}

	public ProfileWidget getPanelProfile() {
		return panelProfile;
	}

	@Override
	public void showSingleApplication(boolean show) {
		showSingleApplication(show, "0", "0", 0);
	}

	@Override
	public String getSearchText() {
		return tblView.getSearchValue();
	}

	@Override
	public HasKeyDownHandlers getTxtSearch() {
		return tblView.getSearchKeyDownHandler();
	}

	@Override
	public void showSingleApplication(boolean show, String previousRefId,
			String nextRefId, int maxSize) {
		panelProfile.setNavigationLinks(previousRefId, nextRefId, maxSize);

		if (show) {
			divAllApplication.addClassName("hide");
			divSingleApplication.removeClassName("hide");
		} else {
			divAllApplication.removeClassName("hide");
			divSingleApplication.addClassName("hide");
		}
	}

	public DropDownList<ApplicationStatus> getLstApplicationStatus() {
		return tblView.getLstApplicationStatus();
	}

	public DropDownList<PaymentStatus> getLstPaymentStatus() {
		return tblView.getLstPaymentStatus();
	}

	public ActionLink getaSearch() {
		return tblView.getaSearch();
	}

}
