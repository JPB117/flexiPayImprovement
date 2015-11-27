package com.workpoint.icpak.client.ui.cpd.table.row;

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
import com.workpoint.icpak.client.ui.OptionControl;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.RowWidget;
import com.workpoint.icpak.client.ui.events.EditModelEvent;
import com.workpoint.icpak.client.ui.events.TableActionEvent;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.client.util.AppContext;
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
	HTMLPanel divStartDate;
	@UiField
	HTMLPanel divEndDate;
	@UiField
	HTMLPanel divAction;
	@UiField
	ActionLink aDownloadCert;
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
	ActionLink aMember;
	@UiField
	HTMLPanel divMember;
	@UiField
	ActionLink aApprove;
	@UiField
	ActionLink aReject;
	private CPDDto dto;

	public CPDTableRow() {
		initWidget(uiBinder.createAndBindUi(this));
		aEdit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AppContext.fireEvent(new EditModelEvent(dto));
			}
		});

		aApprove.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AppManager.showPopUp("Confirm Approval",
						"Are you sure you want to Approve this CPD?",
						new OptionControl() {
							@Override
							public void onSelect(String name) {
								// Window.alert("Called" + name);
								if (name.equals("Confirm")) {
									dto.setStatus(CPDStatus.Approved);
									AppContext.fireEvent(new TableActionEvent(
											dto, TableActionType.APPROVECPD));
									hide();
								} else {
									hide();
								}

							}
						}, "Confirm", "Cancel");

			}
		});

		aReject.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AppManager.showPopUp("Confirm Approval",
						"Are you sure you want to Approve this CPD?",
						new OptionControl() {
							@Override
							public void onSelect(String name) {
								if (name.equals("Confirm")) {
									dto.setStatus(CPDStatus.Rejected);
									AppContext.fireEvent(new TableActionEvent(
											dto, TableActionType.REJECTCPD));
								}
								hide();
							}
						}, "Confirm", "Cancel");
			}
		});

		aView.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AppContext.fireEvent(new TableActionEvent(dto,
						TableActionType.VIEWCPD));
			}
		});

		aDownloadCert.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AppContext.fireEvent(new TableActionEvent(dto,
						TableActionType.DOWNLOADCERT));
			}
		});

		aDelete.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				AppContext.fireEvent(new TableActionEvent(dto,
						TableActionType.DELETECPD));
			}
		});
	}

	public CPDTableRow(CPDDto dto) {
		this();
		this.dto = dto;

		if (AppContext.isCurrentUserAdmin()) {
			divMember.setVisible(true);
			aMember.setText(dto.getFullNames());
		} else {
			divMember.setVisible(false);
		}

		if ((dto.getStartDate() != null)) {
			divStartDate.add(new InlineLabel(DateUtils.DATEFORMAT.format(dto
					.getStartDate())));
		}

		if (dto.getEndDate() != null) {
			divEndDate.add(new InlineLabel(DateUtils.DATEFORMAT.format(dto
					.getEndDate())));
		}

		divCourseName.add(new InlineLabel(dto.getTitle()));
		divOrganiser.add(new InlineLabel(dto.getOrganizer()));

		if (dto.getCategory() != null)
			divCategory
					.add(new InlineLabel(dto.getCategory().getDisplayName()));

		divCPD.add(new InlineLabel(dto.getCpdHours() + " hrs"));

		CPDStatus status = dto.getStatus() == null ? CPDStatus.Unconfirmed
				: dto.getStatus();

		spnStatus.setInnerText(status.name());
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

	}

	private void setActionButtons() {
		clear();

		boolean isViewVisible = AppContext.isCurrentUserAdmin();
		boolean isEditVisible = AppContext.isCurrentUserMember()
				&& !(dto.getOrganizer().equals("ICPAK"))
				&& dto.getStatus() == CPDStatus.Unconfirmed;
		boolean isDeleteVisible = AppContext.isCurrentUserMember()
				&& dto.getStatus() == CPDStatus.Unconfirmed
				&& !(dto.getOrganizer().equals("ICPAK"));
		boolean isApproveRejectVisible = (dto.getStatus() == CPDStatus.Unconfirmed)
				&& AppContext.isCurrentUserAdmin();
		boolean isDownloadVisible = AppContext.isCurrentUserMember()
				&& (dto.getOrganizer().equals("ICPAK"))
				&& dto.getStatus() == CPDStatus.Approved;
		boolean isNoActionVisible = AppContext.isCurrentUserMember()
				&& (!isEditVisible && !isDownloadVisible && !isDeleteVisible);
		aEdit.setVisible(isEditVisible);
		aView.setVisible(isViewVisible);
		aDelete.setVisible(isDeleteVisible);
		aDownloadCert.setVisible(isDownloadVisible);
		aApprove.setVisible(isApproveRejectVisible);
		aReject.setVisible(isApproveRejectVisible);
		if (isNoActionVisible) {
			spnNoAction.removeClassName("hide");
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

	public enum TableActionType {
		DOWNLOADCERT, DELETECPD, VIEWCPD, APPROVECPD, REJECTCPD, RESENDPROFORMA, ERPREFRESH
	}

}
