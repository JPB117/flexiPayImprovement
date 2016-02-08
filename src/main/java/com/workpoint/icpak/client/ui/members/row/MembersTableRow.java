package com.workpoint.icpak.client.ui.members.row;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.RowWidget;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.auth.ApplicationStatus;

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
	HTMLPanel divMemberName;
	@UiField
	Element spnApplicationStatus;
	@UiField
	Element spnPaymentStatus;
	@UiField
	HTMLPanel divAction;
	@UiField
	HTMLPanel divEmail;
	@UiField
	ActionLink aMemberName;

	public MembersTableRow() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public MembersTableRow(final ApplicationFormHeaderDto application,
			int counter) {
		this();
		bind(application);
		aMemberName.setHref("#members;counter=" + counter);
	}

	private void bind(ApplicationFormHeaderDto application) {
		Date regDate = application.getApplicationDate() != null ? application
				.getApplicationDate()
				: application.getDate() != null ? application.getDate()
						: application.getCreated();

		divDate.add(new InlineLabel(DateUtils.READABLETIMESTAMP.format(regDate)));
		aMemberName.setText(application.fullNames());
		divEmail.add(new InlineLabel(application.getEmail()));

		if (application.getApplicationStatus() != null
				&& !application.getApplicationStatus().getDisplayName()
						.isEmpty()) {
			
			spnApplicationStatus.setInnerText(application
					.getApplicationStatus() + "");
			if (application.getApplicationStatus() == ApplicationStatus.APPROVED) {
				spnApplicationStatus.addClassName("label label-success");
			} else if (application.getApplicationStatus() == ApplicationStatus.SUBMITTED) {
				spnApplicationStatus.addClassName("label label-warning");
			} else if (application.getApplicationStatus() == ApplicationStatus.PROCESSING) {
				spnApplicationStatus.addClassName("label label-info");
			} else {
				spnApplicationStatus.addClassName("label label-default");
			}
		} else {
			spnApplicationStatus.setInnerText("NO STATUS FOUND!");
			spnApplicationStatus.addClassName("label label-danger");
		}

		if (application.getPaymentStatus() != null
				&& !application.getPaymentStatus().getDisplayName().isEmpty()) {
			spnPaymentStatus.setInnerText(application.getPaymentStatus() + "");

			if (application.getPaymentStatus() == PaymentStatus.PAID) {
				spnPaymentStatus.addClassName("label label-success");
			} else {
				spnPaymentStatus.addClassName("label label-default");
			}
		} else {
			spnApplicationStatus.setInnerText("NO STATUS FOUND!");
			spnApplicationStatus.addClassName("label label-danger");
		}

	}
}
