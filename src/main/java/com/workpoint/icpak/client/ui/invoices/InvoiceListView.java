package com.workpoint.icpak.client.ui.invoices;

import java.util.List;

import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.invoices.header.TransactionsHeader;
import com.workpoint.icpak.client.ui.invoices.row.InvoiceTableRow;
import com.workpoint.icpak.client.ui.invoices.table.TransactionTable;
import com.workpoint.icpak.shared.model.InvoiceSummary;
import com.workpoint.icpak.shared.model.PaymentMode;
import com.workpoint.icpak.shared.model.PaymentType;
import com.workpoint.icpak.shared.model.TransactionDto;

public class InvoiceListView extends ViewImpl implements
		InvoiceListPresenter.IInvoiceView {

	private final Widget widget;

	@UiField
	TransactionTable tblView;
	@UiField
	TransactionsHeader headerContainer;
	@UiField
	SpanElement spnDates;

	public interface Binder extends UiBinder<Widget, InvoiceListView> {
	}

	@Inject
	public InvoiceListView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		tblView.setAutoNumber(true);

	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void bindInvoices(List<TransactionDto> trxs) {
		tblView.clearRows();
		tblView.setNoRecords(trxs.size());
		for (TransactionDto transaction : trxs) {
			tblView.createRow(new InvoiceTableRow(transaction));
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
	public void bindSummary(InvoiceSummary result) {
		if (result.getPaid() == null) {
			headerContainer.setValues(0, 0, 0);
		} else {
			headerContainer.setValues(result.getPaid() + result.getUnpaid(),
					result.getPaid(), result.getUnpaid());
		}
	}

	@Override
	public String getSearchText() {
		return tblView.getSearchText();
	}

	@Override
	public HasClickHandlers getAdvancedFilterButton() {
		return tblView.getAdvancedFilterButton();
	}

	@Override
	public HasClickHandlers getSearchButton() {
		return tblView.getSearchButton();
	}

	@Override
	public HasKeyDownHandlers getTxtSearch() {
		return tblView.getTxtSearch();
	}

	@Override
	public DropDownList<PaymentMode> getLstPaymentMode() {
		return tblView.getLstPaymentMode();
	}

	@Override
	public DropDownList<PaymentType> getLstPaymentType() {
		return tblView.getLstPaymentType();
	}

	public void setTransactionDateString(String dateString) {
		spnDates.setInnerText(dateString);
	}

}
