package com.workpoint.icpak.client.ui.cpd.member.table.footer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.RowWidget;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.shared.model.CPDFooterDto;
import com.workpoint.icpak.shared.model.CPDStatus;

public class CPDMemberFooterRow extends RowWidget {

	private static ActivitiesTableRowUiBinder uiBinder = GWT
			.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends
			UiBinder<Widget, CPDMemberFooterRow> {
	}

	@UiField
	HTMLPanel row;
	@UiField
	HTMLPanel divCourseName;
	@UiField
	HTMLPanel divCPD;
	@UiField
	HTMLPanel divStartDate;
	@UiField
	HTMLPanel divDesc;
	@UiField
	HTMLPanel divAction;
	@UiField
	HTMLPanel divCount;

	public CPDMemberFooterRow(CPDFooterDto footer) {
		initWidget(uiBinder.createAndBindUi(this));
		divDesc.add(new HTML("<Strong>" + footer.getDescription() + "</Strong>"));
		divCPD.add(new InlineLabel(footer.getCpdUnits() + ""));
	}

}
