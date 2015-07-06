package com.workpoint.icpak.client.ui.members.row;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.RowWidget;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;

public class MembersTableRow extends RowWidget {

	private static ActivitiesTableRowUiBinder uiBinder = GWT
			.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends
			UiBinder<Widget, MembersTableRow> {
	}

	@UiField
	HTMLPanel row;
	@UiField
	HTMLPanel divDate;
	@UiField
	HTMLPanel divMemberNo;
	@UiField
	HTMLPanel divMemberName;
	@UiField
	Element spnStatus;
	@UiField
	HTMLPanel divAction;
	@UiField
	HTMLPanel divEmail;
	
	@UiField Element divCompletion;

	public MembersTableRow() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public MembersTableRow(ApplicationFormHeaderDto application) {
		this();
		bind(application);
	}

	private void bind(ApplicationFormHeaderDto application) {
		Date regDate = application.getApplicationDate()!=null? application.getApplicationDate():
			application.getDate()!=null? application.getDate(): application.getCreated();
		
		divDate.add(new InlineLabel(DateUtils.DATEFORMAT.format(regDate)));
		divMemberNo.add(new InlineLabel(application.getMemberNo()));
		divMemberName.add(new InlineLabel(application.getFullNames()));
		divEmail.add(new InlineLabel(application.getEmail()));
		divCompletion.setAttribute("data-label", application.getPercCompletion()+"%");
		divCompletion.addClassName("radial-bar-"+application.getPercCompletion());
		spnStatus.setInnerText("Pending");
	}
	
	

}
