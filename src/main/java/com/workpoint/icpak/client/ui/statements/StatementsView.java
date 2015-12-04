package com.workpoint.icpak.client.ui.statements;

import static com.workpoint.icpak.client.ui.util.DateUtils.DATEFORMAT;
import static com.workpoint.icpak.client.ui.util.NumberUtils.CURRENCYFORMAT;

import java.util.Date;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.model.UploadContext;
import com.workpoint.icpak.client.model.UploadContext.UPLOADACTION;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.DateField;
import com.workpoint.icpak.client.ui.component.Grid;
import com.workpoint.icpak.client.ui.statement.header.StatementHeader;
import com.workpoint.icpak.client.ui.util.DateRange;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.client.ui.util.NumberUtils;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.statement.SearchDto;
import com.workpoint.icpak.shared.model.statement.StatementDto;
import com.workpoint.icpak.shared.model.statement.StatementSummaryDto;

public class StatementsView extends ViewImpl implements
		StatementsPresenter.IStatementsView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, StatementsView> {
	}

	@UiField
	Grid<StatementDto> grid;

	@UiField
	DateField dtStartDate;
	@UiField
	DateField dtEndDate;
	@UiField
	ActionLink aRefresh;

	@UiField
	ActionLink downloadPdf;

	@UiField
	StatementHeader panelHeader;

	Double totalBalance = 0.00;
	Double totalCredit = 0.00;
	Double totalDebit = 0.00;

	@Inject
	public StatementsView(final Binder binder) {
		widget = binder.createAndBindUi(this);

		dtStartDate.getElement().getStyle().setMarginRight(25, Unit.PX);

		downloadPdf.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				UploadContext ctx = new UploadContext("getreport");
				if (dtStartDate.getValueDate() != null)
					ctx.setContext("startdate", dtStartDate.getValueDate()
							.getTime() + "");
				if (dtEndDate.getValueDate() != null)
					ctx.setContext("enddate", dtEndDate.getValueDate()
							.getTime() + "");
				ctx.setContext("memberRefId", AppContext.getCurrentUser()
						.getUser().getMemberRefId());
				ctx.setAction(UPLOADACTION.GETSTATEMENT);

				// ctx.setContext(key, value)
				Window.open(ctx.toUrl(), "", null);
			}
		});

		grid.getElement().getFirstChildElement().getStyle().clearHeight();

		dtEndDate.addStyleName("end-date");

		TextColumn<StatementDto> date = new TextColumn<StatementDto>() {
			@Override
			public String getValue(StatementDto arg0) {
				return (arg0.getPostingDate() == null ? "" : DATEFORMAT
						.format(arg0.getPostingDate()));
			}
		};

		TextColumn<StatementDto> type = new TextColumn<StatementDto>() {
			@Override
			public String getValue(StatementDto arg0) {
				return arg0.getDocumentType().equals("null") ? "" : arg0
						.getDocumentType();
			}
		};

		TextColumn<StatementDto> docNo = new TextColumn<StatementDto>() {
			@Override
			public String getValue(StatementDto arg0) {
				return arg0.getDocumentNo();
			}
		};

		TextColumn<StatementDto> debitAmount = new TextColumn<StatementDto>() {
			@Override
			public String getValue(StatementDto arg0) {
				if (arg0.getAmount() < 0) {
					StatementsView.this.totalDebit = totalDebit
							+ (-arg0.getAmount());
					// Window.alert(totalDebit + ">Debit");
				}
				return arg0.getAmount() == null ? ""
						: arg0.getAmount() < 0 ? "("
								+ CURRENCYFORMAT.format(-arg0.getAmount())
								+ ")" : CURRENCYFORMAT.format(0.00);
			}
		};
		TextColumn<StatementDto> creditAmount = new TextColumn<StatementDto>() {
			@Override
			public String getValue(StatementDto arg0) {
				if (arg0.getAmount() > 0) {
					StatementsView.this.totalCredit = totalCredit
							+ arg0.getAmount();
					// Window.alert(totalCredit + ">Credit");
				}

				return arg0.getAmount() == null ? ""
						: arg0.getAmount() < 0 ? CURRENCYFORMAT.format(0.0)
								: CURRENCYFORMAT.format(arg0.getAmount());
			}
		};

		TextColumn<StatementDto> balance = new TextColumn<StatementDto>() {
			@Override
			public String getValue(StatementDto arg0) {
				StatementsView.this.totalBalance = totalBalance
						+ arg0.getAmount();
				return CURRENCYFORMAT.format(totalBalance);
			}
		};

		TextColumn<StatementDto> description = new TextColumn<StatementDto>() {
			@Override
			public String getValue(StatementDto arg0) {
				return arg0.getDescription();
			}
		};

		grid.addColumn(date, "Date");
		grid.addColumn((type), "Doc Type");
		grid.addColumn(docNo, "Doc No");
		grid.addColumn(description, "Description", "200px");
		grid.addColumn(debitAmount, "Debit Amount");
		grid.addColumn(creditAmount, "Credit Amount");
		grid.addColumn(balance, "Balance");

	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setData(List<StatementDto> data, int totalCount) {
		clearSummaryAmounts();
		grid.setData(data, totalCount);
	}

	private void clearSummaryAmounts() {
		totalBalance = 0.0;
		totalDebit = 0.0;
		totalCredit = 0.0;
	}

	public void setSummary(StatementSummaryDto summary) {
		panelHeader.setSummary(
				NumberUtils.NUMBERFORMAT.format(-summary.getTotalDebit()),
				NumberUtils.NUMBERFORMAT.format(summary.getTotalCredit()),
				NumberUtils.NUMBERFORMAT.format(summary.getTotalBalance()));
	}

	@Override
	public Grid<StatementDto> getGrid() {
		return grid;
	}

	@Override
	public SearchDto getSearchCriteria() {
		SearchDto dto = new SearchDto();
		dto.setEndDate(dtEndDate.getValueDate());
		dto.setStartDate(dtStartDate.getValueDate());
		return dto;
	}

	@Override
	public HasClickHandlers getRefresh() {
		return aRefresh;
	}

	@Override
	public HasValueChangeHandlers<String> getStartDate() {
		return dtStartDate.getDateInput();
	}

	@Override
	public HasValueChangeHandlers<String> getEndDate() {
		return dtEndDate.getDateInput();
	}

	@Override
	public Date getStartDateValue() {
		return dtStartDate.getValueDate();
	}

	@Override
	public Date getEndDateValue() {
		return dtEndDate.getValueDate();
	}

	@Override
	public void setInitialDates(DateRange startDate, Date endDate) {
		dtStartDate.setValue(DateUtils.getDateByRange(startDate, false));
		dtEndDate.setValue(endDate);
	}

}
