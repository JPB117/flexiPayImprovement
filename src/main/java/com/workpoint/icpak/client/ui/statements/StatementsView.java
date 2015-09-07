package com.workpoint.icpak.client.ui.statements;

import java.util.List;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.statements.header.TransactionsHeader;
import com.workpoint.icpak.client.ui.statements.row.StatementTableRow;
import com.workpoint.icpak.client.ui.statements.table.TransactionTable;
import com.workpoint.icpak.shared.model.InvoiceDto;

public class StatementsView extends ViewImpl implements
		StatementsPresenter.IStatementsView {

	private final Widget widget;

	@UiField
	TransactionTable tblView;

	@UiField
	TransactionsHeader headerContainer;

	public interface Binder extends UiBinder<Widget, StatementsView> {
	}

	@Inject
	public StatementsView(final Binder binder) {
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

		double totalPaid = 0.0;
		double totalUnpaid = 0.0;
		for (InvoiceDto invoice : invoices) {
			tblView.createRow(new StatementTableRow(invoice));
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

}
