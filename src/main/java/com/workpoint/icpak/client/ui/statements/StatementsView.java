package com.workpoint.icpak.client.ui.statements;

import java.util.List;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.component.Grid;
import com.workpoint.icpak.shared.model.statement.StatementDto;
import static com.workpoint.icpak.client.ui.util.DateUtils.*;
import static com.workpoint.icpak.client.ui.util.NumberUtils.*;

public class StatementsView extends ViewImpl implements
		StatementsPresenter.IStatementsView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, StatementsView> {
	}

	@UiField Grid<StatementDto> grid;
	
	@Inject
	public StatementsView(final Binder binder) {
		widget = binder.createAndBindUi(this);

		TextColumn<StatementDto> entryNo = new TextColumn<StatementDto>() {
			@Override
			public String getValue(StatementDto arg0) {
				return arg0.getEntryNo();
			}
		}; 
		
		TextColumn<StatementDto> ledgerNo = new TextColumn<StatementDto>() {
			@Override
			public String getValue(StatementDto arg0) {
				return arg0.getCustLedgerEntryNo();
			}
		}; 
		
		TextColumn<StatementDto> date = new TextColumn<StatementDto>() {
			@Override
			public String getValue(StatementDto arg0) {
				return (arg0.getPostingDate()==null? "" : DATEFORMAT.format(arg0.getPostingDate()));
			}
		}; 
		
		TextColumn<StatementDto> type = new TextColumn<StatementDto>() {
			@Override
			public String getValue(StatementDto arg0) {
				return arg0.getDocumentType();
			}
		}; 
		
		TextColumn<StatementDto> docNo = new TextColumn<StatementDto>() {
			@Override
			public String getValue(StatementDto arg0) {
				return arg0.getDocumentNo();
			}
		}; 
		
		TextColumn<StatementDto> amount = new TextColumn<StatementDto>() {
			@Override
			public String getValue(StatementDto arg0) {
				
				return arg0.getAmount()==null? "": 
					arg0.getAmount()<0? "("+CURRENCYFORMAT.format(-arg0.getAmount())+")":
						CURRENCYFORMAT.format(arg0.getAmount());
			}
		}; 
		
		TextColumn<StatementDto> description = new TextColumn<StatementDto>() {
			@Override
			public String getValue(StatementDto arg0) {
				return arg0.getDescription();
			}
		}; 
		
		TextColumn<StatementDto> memberNo = new TextColumn<StatementDto>() {
			@Override
			public String getValue(StatementDto arg0) {
				return arg0.getCustomerNo();
			}
		}; 
		
		TextColumn<StatementDto> dueDate = new TextColumn<StatementDto>() {
			@Override
			public String getValue(StatementDto arg0) {
				
				return (arg0.getDueDate()==null? "" : 
					DATEFORMAT.format(arg0.getDueDate()));
			}
		}; 
		
		grid.addColumn(entryNo, "Entry No");
		grid.addColumn(ledgerNo, "Ledger No");
		grid.addColumn(date, "Date");
		grid.addColumn(type, "Doc Type");
		grid.addColumn(docNo, "Doc No", "120px");
		grid.addColumn(amount, "Amount");
		grid.addColumn(description, "Description", "150px");
		grid.addColumn(memberNo, "Member No");
		grid.addColumn(dueDate, "Due Date");
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setData(List<StatementDto> data, int totalCount) {
		grid.setData(data,totalCount);		
	}

	@Override
	public Grid<StatementDto> getGrid() {
		
		return grid;
	}
}
