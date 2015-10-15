package com.workpoint.icpak.client.ui.cpd.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
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
	ActionLink aShowFilter;

	@UiField
	CPDTable tblView;

	@UiField
	CPDHeader headerContainer;

	@UiField
	ActionLink aCreate;

	List<Year> allYears = new ArrayList<Year>();
	private int totalYears = 20;

	public interface Binder extends UiBinder<Widget, CPDManagementView> {
	}

	@Inject
	public CPDManagementView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		headerContainer.setTitles("Total CPD Requests", "Total Processed",
				"Total Pending");

		divTabs.setPosition(TabPosition.PILLS);

		Date endYear = new Date();
		allYears.add(new Year(endYear));

		String display = "";
		String display2 = "";
		for (int i = 1; i <= totalYears; i++) {
			CalendarUtil.addMonthsToDate(endYear, -12);
			allYears.add(new Year(endYear));
			display = display + "," + allYears.get(i).getDisplayName();
		}
		// spnDebug.setInnerText("Size>>" + allYears.size());

		for (int i2 = 0; i2 <= totalYears; i2++) {
			display2 = display2 + "," + allYears.get(i2).getDisplayName();
		}

		// spnDebug.setInnerText(">>>" + display + ">>>" + display2);

		lstFrom.setItems(allYears);
		lstTo.setItems(allYears);

		aShowFilter.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (isDateFilterShown) {
					showDateFilter(false);
				} else {
					showDateFilter(true);
				}

			}
		});
		showDateFilter(false);
		if (AppContext.isCurrentUserAdmin()) {
			aCreate.setVisible(false);
		}

		createRow(new CPDTableRow());
	}

	public HasClickHandlers getRecordButton() {
		return aCreate;
	}

	private void createRow(CPDTableRow cpdTableRow) {
		tblView.createRow(cpdTableRow);
	}

	public void showDateFilter(boolean showFilter) {
		if (showFilter) {
			panelDates.setVisible(true);
			isDateFilterShown = true;
			aShowFilter.setStyleName("fa fa-caret-up");
		} else {
			panelDates.setVisible(false);
			isDateFilterShown = false;
			aShowFilter.setStyleName("fa fa-caret-down");
		}
	}

	boolean isDateFilterShown = false;

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
}
