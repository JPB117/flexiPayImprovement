package com.workpoint.icpak.client.ui.frontmember.table.row;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.RowWidget;
import com.workpoint.icpak.shared.model.ApplicationType;
import com.workpoint.icpak.shared.model.DirectoryDto;
import com.workpoint.icpak.shared.model.MemberDto;

public class FrontMemberTableRow extends RowWidget {

	private static ActivitiesTableRowUiBinder uiBinder = GWT
			.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends
			UiBinder<Widget, FrontMemberTableRow> {
	}

	private MemberDto memberDto;

	@UiField
	HTMLPanel row;
	@UiField
	HTMLPanel divName;
	@UiField
	HTMLPanel divCategory;
	@UiField
	HTMLPanel divMemberShip;
	@UiField
	HTMLPanel divPracStatus;
	@UiField
	Label lMemberNo;
	@UiField
	Label lName;
	@UiField
	Label lCategory;
	@UiField
	SpanElement lMemberShip;
	@UiField
	Label lPracStatus;

	public FrontMemberTableRow() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public FrontMemberTableRow(MemberDto memberDto) {
		this();
		this.memberDto = memberDto;
		if (memberDto.getMemberNo() != null)
			lMemberNo.setText(Double.valueOf(memberDto.getMemberNo()) + "");
		lName.setText(memberDto.getFullName());
		if (memberDto.getCustomerPostingGroup() != null)
			lCategory.setText(memberDto.getCustomerPostingGroup());
		if (memberDto.getMembershipStatus() != null)
			lMemberShip
					.setInnerText(memberDto.getMembershipStatus().toString());

		if (memberDto.getCustomerType() != null) {
			if (memberDto.getCustomerType() == ApplicationType.PRACTISING) {
				lPracStatus.setText("VALID CERTIFICATE("
						+ memberDto.getPractisingNo() + ")");
			} else {
				lPracStatus.setText(memberDto.getCustomerType()
						.getDisplayName());
			}
		}
	}
}
