package com.workpoint.icpak.client.ui.trx;

import java.util.Date;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.icpak.rest.models.SearchFilter;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.MyHTMLPanel;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.ui.trx.header.TransactionsHeader;
import com.workpoint.icpak.client.ui.trx.row.TransactionTableRow;
import com.workpoint.icpak.client.ui.trx.table.TransactionTable;

public class TransactionsView extends ViewImpl implements
		TransactionsPresenter.ITransactionView {

	private final Widget widget;

	@UiField
	HTMLPanel divMainContainer;

	@UiField
	HTMLPanel divMiddleContent;

	@UiField
	HTMLPanel divSearchBox;

	@UiField
	HTMLPanel divContentTop;

	@UiField
	TransactionTable tblView;

//	@UiField
//	DateBoxDropDown boxDateBox;

	@UiField
	ActionLink aRefresh;

	@UiField
	MyHTMLPanel divProgramsTable;

	@UiField
	TransactionsHeader headerContainer;

	@UiField
	TextField txtSearchBox;

	Long lastUpdatedId;

	public interface Binder extends UiBinder<Widget, TransactionsView> {
	}

	protected boolean isNotDisplayed;

	private SearchFilter filter = new SearchFilter();

	@Inject
	public TransactionsView(final Binder binder) {
		widget = binder.createAndBindUi(this);

//		boxDateBox.getDoneButton().addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				Date startDate = boxDateBox.getStartDateSelected();
//				Date endDate = boxDateBox.getEndDateSelected();
//				setDateRange(null, startDate, endDate, true);
//			}
//		});
//
//		boxDateBox.getPeriodDropDown().addChangeHandler(new ChangeHandler() {
//			@Override
//			public void onChange(ChangeEvent event) {
//				if (boxDateBox.getCallChangeEvent()) {
//					setDateRange(boxDateBox.getSelected(), null, null, true);
//				}
//			}
//		});

		showData();
	}

	private void showData() {
		for (int i = 0; i < 10; i++) {
			TransactionTableRow row = new TransactionTableRow();
			tblView.createRow(row);
		}

	}

	public void setDateRange(String displayName, Date passedStart,
			Date passedEnd, boolean fireValueChangedEvent) {
		Date startDate = null;
		Date endDate = null;

//		if (displayName != null) {
//			startDate = boxDateBox.getDateFromName(displayName, true);
//			endDate = boxDateBox.getDateFromName(displayName, false);
//			boxDateBox.setDateString(displayName);
//		} else {
//			startDate = passedStart;
//			endDate = passedEnd;
//		}
//
//		boxDateBox.setStartDate(startDate);
//		boxDateBox.setEndDate(endDate);
//
//		filter.setStartDate(startDate);
//		filter.setEndDate(endDate);
	}

	public HasClickHandlers getRefreshLink() {
		return aRefresh;
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void clear() {
		tblView.clearRows();
	}

	@Override
	public HasKeyDownHandlers getSearchBox() {
		return txtSearchBox;
	}
}