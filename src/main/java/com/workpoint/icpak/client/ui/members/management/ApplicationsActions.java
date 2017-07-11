package com.workpoint.icpak.client.ui.members.management;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.TextArea;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.auth.ApplicationStatus;

public class ApplicationsActions extends Composite {

	private static ApplicationsActionsUiBinder uiBinder = GWT
			.create(ApplicationsActionsUiBinder.class);

	interface ApplicationsActionsUiBinder extends
			UiBinder<Widget, ApplicationsActions> {
	}

	@UiField
	DropDownList<ApplicationStatus> lstApplicationStatus;
	@UiField
	DropDownList<PaymentStatus> lstPaymentStatus;
	@UiField
	TextArea txtMgmtComment;
	private ApplicationFormHeaderDto application;

	public ApplicationsActions() {
		initWidget(uiBinder.createAndBindUi(this));
		List<ApplicationStatus> allowedActions = new ArrayList<>();
		for (ApplicationStatus stat : ApplicationStatus.values()) {
			if (stat != ApplicationStatus.APPROVED
					|| stat != ApplicationStatus.PROCESSING) {
				allowedActions.add(stat);
			}
		}

		if (AppContext.isCurrentUserFinanceEdit()
				|| AppContext.isCurrentUserFinanceApplications()) {
			lstPaymentStatus.removeStyleName("hide");
		} else {
			lstPaymentStatus.addStyleName("hide");
		}

		lstApplicationStatus.setItems(allowedActions,
				"-Select Application Status-");
		lstPaymentStatus.setItems(Arrays.asList(PaymentStatus.values()),
				"-Select Payment Status-");
	}

	public void bindApplicationActions(ApplicationFormHeaderDto application) {
		this.application = application;
		lstApplicationStatus.setValue(application.getApplicationStatus());
		lstPaymentStatus.setValue(application.getPaymentStatus());
		txtMgmtComment.setValue(application.getManagementComment());
	}

	public ApplicationFormHeaderDto getApplicationAction() {
		if (application == null) {
			Window.alert("No application selected ");
		} else {
			application.setApplicationStatus(lstApplicationStatus.getValue());
			application.setPaymentStatus(lstPaymentStatus.getValue());
			application.setManagementComment((txtMgmtComment.getValue()
					.isEmpty() ? null : txtMgmtComment.getValue()));
		}
		return application;
	}

}
