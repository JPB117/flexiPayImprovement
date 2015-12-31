package com.workpoint.icpak.client.ui.cpd;

import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.model.UploadContext;
import com.workpoint.icpak.client.model.UploadContext.UPLOADACTION;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.component.tabs.TabPanel;
import com.workpoint.icpak.client.ui.cpd.header.CPDHeader;
import com.workpoint.icpak.client.ui.cpd.member.table.CPDMemberTable;
import com.workpoint.icpak.client.ui.cpd.member.table.row.CPDMemberTableRow;
import com.workpoint.icpak.client.ui.util.DateRange;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.shared.model.CPDSummaryDto;

public class CPDMemberView extends ViewImpl implements
		CPDMemberPresenter.ICPDView {

	private final Widget widget;

	@UiField
	TabPanel divTabs;
	@UiField
	HTMLPanel container;
	@UiField
	ActionLink aShowFilter;
	@UiField
	CPDMemberTable tblView;
	@UiField
	CPDHeader headerContainer;
	@UiField
	ActionLink aCreate;

	public interface Binder extends UiBinder<Widget, CPDMemberView> {
	}

	@Inject
	public CPDMemberView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		tblView.getDownloadButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				UploadContext ctx = new UploadContext("getreport");
				if (tblView.getStartDate() != null)
					ctx.setContext("startdate", tblView.getStartDate()
							.getTime() + "");
				if (tblView.getEndDate() != null)
					ctx.setContext("enddate", tblView.getEndDate().getTime()
							+ "");
				ctx.setContext("memberRefId", AppContext.getCurrentUser()
						.getUser().getMemberRefId());
				ctx.setAction(UPLOADACTION.GETCPDSTATEMENT);
				Window.open(ctx.toUrl(), "", null);
			}
		});

		tblView.getFilterButton().setText("View CPD");

	}

	public HasClickHandlers getRecordButton() {
		return aCreate;
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void bindResults(List<CPDDto> result) {
		tblView.clearRows();
		tblView.setNoRecords(result.size());
		for (CPDDto dto : result) {
			tblView.createRow(new CPDMemberTableRow(dto));
		}
	}

	@Override
	public void showDetailedView() {

	}

	@Override
	public PagingPanel getPagingPanel() {
		return tblView.getPagingPanel();
	}

	@Override
	public void bindSummary(CPDSummaryDto summary) {
		headerContainer.setValues(
				summary.getUnconfirmedCPD() + summary.getConfirmedCPD(),
				summary.getConfirmedCPD(), summary.getUnconfirmedCPD());
	}

	@Override
	public void setInitialDates(DateRange thisYear, Date endDate) {
		tblView.setInitialDates(DateUtils.getDateByRange(thisYear, true),
				endDate);
	}

	@Override
	public HasClickHandlers getFilterButton() {
		return tblView.getFilterButton();
	}

	@Override
	public Date getEndDate() {
		return tblView.getEndDate();
	}

	@Override
	public Date getStartDate() {
		return tblView.getStartDate();
	}
}
