package com.workpoint.icpak.client.ui.statements;

import java.util.List;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.component.Grid;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.shared.model.InvoiceDto;

public class StatementsView extends ViewImpl implements
		StatementsPresenter.IStatementsView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, StatementsView> {
	}

	@UiField Grid<InvoiceDto> grid;
	
	@Inject
	public StatementsView(final Binder binder) {
		widget = binder.createAndBindUi(this);

		TextColumn<InvoiceDto> entryNo = new TextColumn<InvoiceDto>() {
			@Override
			public String getValue(InvoiceDto arg0) {
				return arg0.getCompanyName();
			}
		}; 
		
		TextColumn<InvoiceDto> ledgerNo = new TextColumn<InvoiceDto>() {
			@Override
			public String getValue(InvoiceDto arg0) {
				return arg0.getCompanyName();
			}
		}; 
		
		TextColumn<InvoiceDto> date = new TextColumn<InvoiceDto>() {
			@Override
			public String getValue(InvoiceDto arg0) {
				return arg0.getCompanyName();
			}
		}; 
		
		TextColumn<InvoiceDto> type = new TextColumn<InvoiceDto>() {
			@Override
			public String getValue(InvoiceDto arg0) {
				return arg0.getCompanyName();
			}
		}; 
		
		TextColumn<InvoiceDto> docNo = new TextColumn<InvoiceDto>() {
			@Override
			public String getValue(InvoiceDto arg0) {
				return arg0.getCompanyName();
			}
		}; 
		
		TextColumn<InvoiceDto> amount = new TextColumn<InvoiceDto>() {
			@Override
			public String getValue(InvoiceDto arg0) {
				return arg0.getCompanyName();
			}
		}; 
		
		TextColumn<InvoiceDto> description = new TextColumn<InvoiceDto>() {
			@Override
			public String getValue(InvoiceDto arg0) {
				return arg0.getCompanyName();
			}
		}; 
		
		TextColumn<InvoiceDto> memberNo = new TextColumn<InvoiceDto>() {
			@Override
			public String getValue(InvoiceDto arg0) {
				return arg0.getCompanyName();
			}
		}; 
		
		TextColumn<InvoiceDto> dueDate = new TextColumn<InvoiceDto>() {
			@Override
			public String getValue(InvoiceDto arg0) {
				return arg0.getCompanyName();
			}
		}; 
		
		grid.addColumn(entryNo, "Entry No");
		grid.addColumn(ledgerNo, "Ledger No");
		grid.addColumn(date, "Date");
		grid.addColumn(type, "Doc Type");
		grid.addColumn(docNo, "Doc No");
		grid.addColumn(amount, "Amount");
		grid.addColumn(description, "Description");
		grid.addColumn(memberNo, "Member No");
		grid.addColumn(dueDate, "Due Date");
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setCount(Integer aCount) {
		grid.getPagingPanel().setTotal(aCount);
	}

	@Override
	public PagingPanel getPagingPanel() {
		return grid.getPagingPanel();
	}

	@Override
	public void setData(List<InvoiceDto> data) {
		grid.setData(data);		
	}
}
