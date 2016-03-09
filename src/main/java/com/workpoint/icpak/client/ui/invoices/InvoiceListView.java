package com.workpoint.icpak.client.ui.invoices;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.invoices.header.TransactionsHeader;
import com.workpoint.icpak.client.ui.invoices.table.TransactionTable;
import com.workpoint.icpak.shared.model.InvoiceSummary;

public class InvoiceListView extends ViewImpl implements
		InvoiceListPresenter.IInvoiceView {

	private final Widget widget;

	@UiField
	TransactionTable tblView;
	@UiField
	TransactionsHeader headerContainer;

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

	// @Override
	// public void bindInvoices(List<TransactionDto> trxs) {
	// tblView.clearRows();
	// tblView.setNoRecords(trxs.size());
	// for (TransactionDto transaction : trxs) {
	// tblView.createRow(new InvoiceTableRow(transaction));
	// }
	// }

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

}
