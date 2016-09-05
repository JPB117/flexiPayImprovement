package com.workpoint.icpak.client.ui.cpd.admin.summary.table.row;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.RowWidget;
import com.workpoint.icpak.client.ui.events.cpd.MemberCPDEvent;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.MemberCPDDto;
import com.workpoint.icpak.shared.model.MemberDto;

public class CPDSummaryTableRow extends RowWidget {

	private static ActivitiesTableRowUiBinder uiBinder = GWT.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends UiBinder<Widget, CPDSummaryTableRow> {
	}

	@UiField
	HTMLPanel row;
	@UiField
	ActionLink aMemberNo;
	@UiField
	HTMLPanel divMemberName;
	@UiField
	HTMLPanel divCategory;
	@UiField
	HTMLPanel divStatus;
	@UiField
	HTMLPanel div2016;
	@UiField
	HTMLPanel div2015;
	@UiField
	HTMLPanel div2014;
	@UiField
	HTMLPanel div2013;
	@UiField
	HTMLPanel div2012;
	@UiField
	HTMLPanel div2011;

	public CPDSummaryTableRow(final MemberCPDDto dto) {
		initWidget(uiBinder.createAndBindUi(this));
		aMemberNo.setText(dto.getMemberNo());
		aMemberNo.setHref("#cpdmgt;p=memberCPD;refId=" + dto.getRefId());
		divMemberName.add(new InlineLabel(dto.getMemberNames()));
		divCategory.add(new InlineLabel(dto.getCustomerType()));
		divStatus.add(setStatus(dto.getStatus()));
		div2016.add(new InlineLabel(dto.getYear2016() + ""));
		div2015.add(new InlineLabel(dto.getYear2015() + ""));
		div2014.add(new InlineLabel(dto.getYear2014() + ""));
		div2013.add(new InlineLabel(dto.getYear2013() + ""));
		div2012.add(new InlineLabel(dto.getYear2012() + ""));
		div2011.add(new InlineLabel(dto.getYear2011() + ""));

		aMemberNo.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MemberDto member = new MemberDto();
				member.setFullName(dto.getMemberNames());
				member.setMemberNo(dto.getMemberNo());
				AppContext.fireEvent(new MemberCPDEvent(member));
			}
		});

	}

	private InlineLabel setStatus(String status) {
		InlineLabel label = new InlineLabel((status.equals("1") ? "Active" : "Inactive"));
		if (status.equals("1")) {
			label.addStyleName("label label-success");
		} else {
			label.addStyleName("label label-danger");
		}
		return label;
	}

}
