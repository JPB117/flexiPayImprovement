package com.workpoint.icpak.client.ui.cpd.member.table.row;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.AppManager;
import com.workpoint.icpak.client.ui.OnOptionSelected;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.RowWidget;
import com.workpoint.icpak.client.ui.events.EditModelEvent;
import com.workpoint.icpak.client.ui.events.TableActionEvent;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.CPDCategory;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.shared.model.CPDStatus;
import com.workpoint.icpak.shared.model.TableActionType;

public class CPDMemberTableRow extends RowWidget {

	private static ActivitiesTableRowUiBinder uiBinder = GWT
			.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends
			UiBinder<Widget, CPDMemberTableRow> {
	}

	@UiField
	HTMLPanel row;
	@UiField
	HTMLPanel divCourseName;
	@UiField
	HTMLPanel divCPD;
	@UiField
	SpanElement spnStatus;
	@UiField
	HTMLPanel divStartDate;
	@UiField
	HTMLPanel divEndDate;
	@UiField
	HTMLPanel divAction;
	@UiField
	ActionLink aDownloadCert;
	@UiField
	ActionLink aUndoApproval;
	@UiField
	ActionLink aActions;
	@UiField
	ActionLink aEdit;
	@UiField
	ActionLink aView;
	@UiField
	SpanElement spnNoAction;
	@UiField
	ActionLink aDelete;
	@UiField
	ActionLink aApprove;
	@UiField
	ActionLink aReject;
	@UiField
	HTMLPanel divCount;
	private CPDDto dto;

	public CPDMemberTableRow() {
		initWidget(uiBinder.createAndBindUi(this));

		aEdit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AppContext.fireEvent(new EditModelEvent(dto));
			}
		});

		aDelete.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				AppContext.fireEvent(new TableActionEvent(dto,
						TableActionType.DELETECPD));
			}
		});

		aUndoApproval.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				confirmPayments();
			}
		});
	}

	protected void confirmPayments() {
		AppManager.showPopUp("Confirm Undo", "Undo this Approval/Rejection",
				new OnOptionSelected() {
					@Override
					public void onSelect(String name) {
						if (name.equals("Confirm")) {
							dto.setStatus(CPDStatus.Unconfirmed);
							AppContext.fireEvent(new TableActionEvent(dto,
									TableActionType.UPDATECPD));
						}
					}
				}, "Confirm", "Cancel");
	}

	public CPDMemberTableRow(final CPDDto dto) {
		this();
		this.dto = dto;

		if ((dto.getStartDate() != null)) {
			divStartDate.add(new InlineLabel(DateUtils.DATEFORMAT_SYS
					.format(dto.getStartDate())));
		}
		if (dto.getEndDate() != null) {
			divEndDate.add(new InlineLabel(DateUtils.DATEFORMAT_SYS.format(dto
					.getEndDate())));
		}

		divCourseName.add(new InlineLabel(dto.getTitle().toUpperCase()));
		divCPD.add(new InlineLabel(dto.getCpdHours() + ""));

		divCount.add(new InlineLabel(dto.getId() + ""));
		CPDStatus status = dto.getStatus() == null ? CPDStatus.Unconfirmed
				: dto.getStatus();
		spnStatus.setInnerText(status.name());

		if (dto.getCategory() != null) {
			row.getElement().setTitle(dto.getCategory() + "");
		}

		if (status.equals(CPDStatus.Approved)) {
			spnStatus.removeClassName("label-danger");
			spnStatus.addClassName("label-success");
		} else {
			spnStatus.removeClassName("label-success");
			spnStatus.addClassName("label-danger");
		}

		setActionButtons();

		final String url = "getreport?action=downloadcpdcert&cpdRefId="
				+ dto.getRefId();
		final String wintitle = "CPD Certificate for event " + dto.getTitle();

		aDownloadCert.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.open(url, wintitle, "");
			}
		});

		aView.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AppContext.fireEvent(new TableActionEvent(dto,
						TableActionType.VIEWCPD));
			}
		});
	}

	private void setActionButtons() {
		clear();
		boolean isViewVisible = AppContext.isCurrentUserAdmin()
				&& (dto.getStatus() == CPDStatus.Unconfirmed);
		boolean isUndoApprovalVisible = AppContext.isCurrentUserAdmin()
				&& (dto.getStatus() == CPDStatus.Approved || dto.getStatus() == CPDStatus.Rejected);
		boolean isEditVisible = AppContext.isCurrentUserMember()
				&& !(dto.getCategory() == CPDCategory.CATEGORY_A)
				&& dto.getStatus() == CPDStatus.Unconfirmed;
		boolean isDeleteVisible = AppContext.isCurrentUserMember()
				&& dto.getStatus() == CPDStatus.Unconfirmed
				&& !(dto.getOrganizer().equals("ICPAK"));
		boolean isApproveRejectVisible = false;
		boolean isDownloadVisible = (dto.getOrganizer().equals("ICPAK"))
				&& (dto.getStatus() == CPDStatus.Approved)
				&& (dto.getCategory() == CPDCategory.CATEGORY_A);
		boolean isNoActionVisible = (!isEditVisible && !isDownloadVisible
				&& !isDeleteVisible && isViewVisible && isUndoApprovalVisible);
		aEdit.setVisible(isEditVisible);
		aView.setVisible(isViewVisible);
		aDelete.setVisible(isDeleteVisible);
		aDownloadCert.setVisible(isDownloadVisible);
		aApprove.setVisible(isApproveRejectVisible);
		aReject.setVisible(isApproveRejectVisible);
		aUndoApproval.setVisible(isUndoApprovalVisible);
		if (isNoActionVisible) {
			spnNoAction.removeClassName("hide");
		}
		if (dto.getStatus() == CPDStatus.Approved) {
			aUndoApproval.setText("Undo Approval");
		} else if (dto.getStatus() == CPDStatus.Rejected) {
			aUndoApproval.setText("Undo Rejection");
		}
	}

	private void clear() {
		aEdit.setVisible(false);
		aView.setVisible(false);
		aDelete.setVisible(false);
		aDownloadCert.setVisible(false);
		aApprove.setVisible(false);
		aReject.setVisible(false);
		spnNoAction.addClassName("hide");
	}

}
