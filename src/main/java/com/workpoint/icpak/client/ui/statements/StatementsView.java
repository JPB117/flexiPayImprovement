package com.workpoint.icpak.client.ui.statements;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.statements.row.StatementTableRow;
import com.workpoint.icpak.client.ui.statements.table.TransactionTable;

public class StatementsView extends ViewImpl implements
		StatementsPresenter.IStatementsView {

	private final Widget widget;

	@UiField
	TransactionTable tblView;

	public interface Binder extends UiBinder<Widget, StatementsView> {
	}

	@Inject
	public StatementsView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		tblView.setAutoNumber(false);

		showData();

	}

	private void showData() {
		for (int i = 0; i < 20; i++) {
			StatementTableRow row = new StatementTableRow();
			tblView.createRow(row);
		}
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

}
