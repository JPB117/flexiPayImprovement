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
import com.workpoint.icpak.shared.model.CPDStatus;

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

	@UiField
	ActionLink aMember;

	public CPDTableRow() {
		initWidget(uiBinder.createAndBindUi(this));
		String url = "#cpd;cpdId=254";
		aMember.setHref(url);
	}

	public CPDTableRow(CPDDto dto) {
		this();

		if (dto.getCreated() != null)
			divDate.add(new InlineLabel(DateUtils.DATEFORMAT.format(dto
					.getCreated())));

		divCourseName.add(new InlineLabel(dto.getTitle()));
		divOrganiser.add(new InlineLabel(dto.getOrganizer()));

		if (dto.getCategory() != null)
			divCategory
					.add(new InlineLabel(dto.getCategory().getDisplayName()));

		divCPD.add(new InlineLabel(dto.getCpdHours() + " hrs"));

		CPDStatus status = dto.getStatus() == null ? CPDStatus.UNCONFIRMED
				: dto.getStatus();

		spnStatus.setInnerText(status.name());
		if (status.equals(CPDStatus.UNCONFIRMED)) {
			spnStatus.removeClassName("label-success");
			spnStatus.addClassName("label-warning");
		}
	}

}
