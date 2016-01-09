package com.workpoint.icpak.client.ui.users.item.statement;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.DateField;
import com.workpoint.icpak.client.ui.util.DateRange;
import com.workpoint.icpak.client.ui.util.DateUtils;

public class MemberStatementWidget extends Composite {

	private static StatementWidgetUiBinder uiBinder = GWT
			.create(StatementWidgetUiBinder.class);

	@UiField
	DateField dtStartDate;
	@UiField
	DateField dtEndDate;
	@UiField
	ActionLink aRefresh;
	@UiField
	SpanElement spnLastUpdated;

	@UiField
	SpanElement spnShowLoading;

	interface StatementWidgetUiBinder extends
			UiBinder<Widget, MemberStatementWidget> {
	}

	public MemberStatementWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		dtStartDate
				.setValue(DateUtils.getDateByRange(DateRange.THISYEAR, true));
	}

	public DateField getStartDate() {
		return dtStartDate;
	}

	public DateField getEndDate() {
		return dtEndDate;
	}

	public HasClickHandlers getRefreshButton() {
		return aRefresh;
	}

	public void setLastUpdated(String lastUpdated) {
		spnLastUpdated.setInnerText(lastUpdated);
	}

	public void showLoading(boolean show) {
		if (show) {
			spnLastUpdated.addClassName("hide");
			spnShowLoading.removeClassName("hide");
		} else {
			spnLastUpdated.removeClassName("hide");
			spnShowLoading.addClassName("hide");
		}
	}
}
