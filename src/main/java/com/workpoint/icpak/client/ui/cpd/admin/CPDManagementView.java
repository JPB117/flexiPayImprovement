package com.workpoint.icpak.client.ui.cpd.admin;

import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
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
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.component.tabs.TabPanel;
import com.workpoint.icpak.client.ui.component.tabs.TabPanel.TabPosition;
import com.workpoint.icpak.client.ui.cpd.header.CPDHeader;
import com.workpoint.icpak.client.ui.cpd.table.CPDTable;
import com.workpoint.icpak.client.ui.cpd.table.row.CPDTableRow;
import com.workpoint.icpak.client.ui.util.DateRange;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.shared.model.CPDSummaryDto;
import com.workpoint.icpak.shared.model.Listable;

public class CPDManagementView extends ViewImpl implements
		CPDManagementPresenter.ICPDManagementView {

	private final Widget widget;
	@UiField
	TabPanel divTabs;
	@UiField
	HTMLPanel container;
	@UiField
	DropDownList<Year> lstFrom;

	@UiField
	HTMLPanel panelDates;

	@UiField
	DropDownList<Year> lstTo;

	@UiField
	CPDTable tblView;

	@UiField
	CPDHeader headerContainer;

	@UiField
	ActionLink aCreate;

	public interface Binder extends UiBinder<Widget, CPDManagementView> {
	}

	@Inject
	public CPDManagementView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		headerContainer.setTitles("Total CPD Requests", "Total Processed",
				"Total Pending");

		divTabs.setPosition(TabPosition.PILLS);
		aCreate.setVisible(false);

		/*tblView.getDownloadButton().addClickHandler(new ClickHandler() {
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
		});*/

	}

	public HasClickHandlers getRecordButton() {
		return aCreate;
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public class Year implements Listable {
		private Date pickDate;

		public Year(Date passedDate) {
			pickDate = passedDate;
		}

		@Override
		public String getName() {
			return DateUtils.YEARFORMAT.format(pickDate);
		}

		@Override
		public String getDisplayName() {
			return DateUtils.YEARFORMAT.format(pickDate);
		}

	}

	@Override
	public void bindResults(List<CPDDto> result) {
		tblView.clearRows();
		for (CPDDto dto : result) {
			tblView.createRow(new CPDTableRow(dto));
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
				summary.getProcessedCount() + summary.getPendingCount(),
				summary.getProcessedCount(), summary.getPendingCount());
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

	@Override
	public HasValueChangeHandlers<String> getSearchValueChangeHander() {
		return tblView.getSearchField();
	}

	@Override
	public String getSearchValue() {
		return tblView.getSearchValue();
	}

}
