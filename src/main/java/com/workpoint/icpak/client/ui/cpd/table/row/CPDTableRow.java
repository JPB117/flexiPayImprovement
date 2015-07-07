package com.workpoint.icpak.client.ui.cpd.table.row;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.RowWidget;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.shared.model.CPDDto;

public class CPDTableRow extends RowWidget {

	private static ActivitiesTableRowUiBinder uiBinder = GWT
			.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends UiBinder<Widget, CPDTableRow> {
	}

	@UiField
	HTMLPanel row;

	@UiField
	HTMLPanel divDate;
	@UiField
	HTMLPanel divCourseName;
	@UiField
	HTMLPanel divOrganiser;
	@UiField
	HTMLPanel divCategory;
	@UiField
	HTMLPanel divCPD;
	@UiField
	SpanElement spnStatus;
	ActionLink aMember;

	public CPDTableRow() {
		initWidget(uiBinder.createAndBindUi(this));
		String url="#cpd;cpdId=254";
		aMember.setHref(url);
	}
	

	public CPDTableRow(CPDDto dto) {
		this();

		divDate.add(new InlineLabel(DateUtils.DATEFORMAT.format(dto
				.getCreated())));
		
		divCourseName.add(new InlineLabel(dto.getTitle()));
		divOrganiser.add(new InlineLabel(dto.getOrganizer()));

		if (dto.getCategory() != null)
			divCategory
					.add(new InlineLabel(dto.getCategory().getDisplayName()));
		
		divCPD.add(new InlineLabel(dto.getCpdHours() + " hrs"));

		if (dto.getStatus() != null)
			spnStatus.setInnerText(dto.getStatus().name());
	}

}
