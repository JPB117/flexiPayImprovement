package com.workpoint.icpak.client.ui.invoices;

import java.util.List;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.invoices.header.TransactionsHeader;
import com.workpoint.icpak.client.ui.invoices.row.InvoiceTableRow;
import com.workpoint.icpak.client.ui.invoices.table.TransactionTable;
import com.workpoint.icpak.shared.model.InvoiceDto;
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

	@Override
	public void bindInvoices(List<InvoiceDto> invoices) {
		tblView.clearRows();
		tblView.setNoRecords(invoices.size());
		for (InvoiceDto invoice : invoices) {
			tblView.createRow(new InvoiceTableRow(invoice));
		}
		// headerContainer.setValues(invoices.size(), totalPaid, totalUnpaid);
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

}
