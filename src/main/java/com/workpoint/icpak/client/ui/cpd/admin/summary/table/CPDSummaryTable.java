package com.workpoint.icpak.client.ui.cpd.admin.summary.table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.model.UploadContext;
import com.workpoint.icpak.client.model.UploadContext.UPLOADACTION;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.component.PagingTable;
import com.workpoint.icpak.client.ui.component.TableHeader;
import com.workpoint.icpak.client.ui.cpd.admin.summary.table.row.CPDSummaryTableRow;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.client.util.AppContext;

public class CPDSummaryTable extends Composite {

	private static TransactionTableUiBinder uiBinder = GWT
			.create(TransactionTableUiBinder.class);

	interface TransactionTableUiBinder extends
			UiBinder<Widget, CPDSummaryTable> {
	}

	@UiField
	PagingTable tblView;

	public CPDSummaryTable() {
		initWidget(uiBinder.createAndBindUi(this));
		tblView.setAutoNumber(false);
		tblView.setSearchSectionVisible(true);
		tblView.getDownloadPdf().setVisible(false);

		if (AppContext.isCurrentUserAdmin()) {
			Date startDate = DateUtils.DATEFORMAT_SYS.parse("2011-01-01");
			tblView.setInitialDates(startDate, new Date());
			setDownloadHandlers();
			tblView.setDatesVisible(true, false);
			tblView.getAllTranscriptsButton().removeStyleName("hide");
		} else {
			tblView.setDatesVisible(false);
			tblView.getAllTranscriptsButton().addStyleName("hide");
		}
		createHeader();
	}

	private void setDownloadHandlers() {
		tblView.getAllTranscriptsButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				UploadContext ctx = new UploadContext("getreport");
				if (tblView.getStartDate() != null)
					ctx.setContext("startdate", tblView.getStartDate()
							.getTime() + "");
				if (tblView.getEndDate() != null)
					ctx.setContext("enddate", tblView.getEndDate().getTime()
							+ "");
				ctx.setContext("memberRefId", "ALLMEMBERS");
				ctx.setAction(UPLOADACTION.GETCPDSTATEMENT);
				Window.open(ctx.toUrl(), "", null);
			}
		});
	}

	public void createHeader() {
		List<TableHeader> th = new ArrayList<TableHeader>();
		th.add(new TableHeader("Reg No:"));
		th.add(new TableHeader("Member Name:"));
		th.add(new TableHeader("Category:"));
		// th.add(new TableHeader("Status:"));
		th.add(new TableHeader("2016:"));
		th.add(new TableHeader("2015:"));
		th.add(new TableHeader("2014:"));
		th.add(new TableHeader("2013:"));
		th.add(new TableHeader("2012:"));
		th.add(new TableHeader("2011:"));
		tblView.setSearchFieldVisible(true);
		tblView.setTableHeaders(th);
	}

	public void createRow(CPDSummaryTableRow row) {
		tblView.addRow(row);
	}

	public void clearRows() {
		tblView.clearRows();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}

	public void setAutoNumber(boolean autoNumber) {
		tblView.setAutoNumber(false);
	}

	public PagingPanel getPagingPanel() {
		return tblView.getPagingPanel();
	}

	public HasClickHandlers getDownloadButton() {
		return tblView.getDownloadPdf();
	}

	public Date getStartDate() {
		return tblView.getStartDate();
	}

	public Date getEndDate() {
		return tblView.getEndDate();
	}

	public void setNoRecords(int size) {
		tblView.setNoRecords(size);
	}

	public void setInitialDates(Date startDate, Date endDate) {
		tblView.setInitialDates(startDate, endDate);
	}

	public HasClickHandlers getFilterButton() {
		return tblView.getFilterButton();
	}

	public HasValueChangeHandlers<String> getSearchField() {
		return tblView.getSearchValueChangeHander();
	}

	public String getSearchValue() {
		return tblView.getSearchValue();
	}

	public HasKeyDownHandlers getSearchKeyDownHandler() {
		return tblView.getSearchKeyDownHandler();
	}
}
