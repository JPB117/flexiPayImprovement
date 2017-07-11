package com.workpoint.icpak.client.ui.cpd.admin.table.row;

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
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.RowWidget;
import com.workpoint.icpak.client.ui.events.TableActionEvent;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.shared.model.CPDStatus;
import com.workpoint.icpak.shared.model.TableActionType;

public class CPDAdminTableRow extends RowWidget {

	private static ActivitiesTableRowUiBinder uiBinder = GWT
			.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends
			UiBinder<Widget, CPDAdminTableRow> {
	}

	@UiField
	HTMLPanel row;
	@UiField
	ActionLink aRefId;
	@UiField
	SpanElement spnStatus;
	@UiField
	HTMLPanel divAction;
	@UiField
	HTMLPanel divRecordDate;
	@UiField
	HTMLPanel divCPDActivity;
	@UiField
	HTMLPanel divMember;
	@UiField
	ActionLink aDownloadCert;
	@UiField
	ActionLink aActions;
	@UiField
	ActionLink aView;
	@UiField
	SpanElement spnNoAction;
	@UiField
	ActionLink aDelete;
	private CPDDto dto;

	public CPDAdminTableRow() {
		initWidget(uiBinder.createAndBindUi(this));
		aView.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AppContext.fireEvent(new TableActionEvent(dto,
						TableActionType.VIEWCPD));
			}
		});

		// aRefId.addClickHandler(new ClickHandler() {
		// @Override
		// public void onClick(ClickEvent event) {
		// AppContext.fireEvent(new TableActionEvent(dto,
		// TableActionType.VIEWCPD));
		// }
		// });

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

	public CPDAdminTableRow(CPDDto dto) {
		this();
		this.dto = dto;

		String page = (dto.getStatus() != CPDStatus.Unconfirmed ? "returnArchive"
				: "cpdReturns");
		aRefId.setHref("#cpdmgt;p=" + page + ";refId=" + dto.getRefId());

		if (dto.getCreated() != null) {
			divRecordDate.add(new InlineLabel(DateUtils.READABLETIMESTAMP
					.format(dto.getCreated())));
		}
		divMember.add(new InlineLabel(dto.getFullNames() + " - "
				+ dto.getMemberRegistrationNo()));
		divCPDActivity.add(new InlineLabel(dto.getTitle()));
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
		boolean isDownloadVisible = AppContext.isCurrentUserMember()
				&& (dto.getOrganizer().equals("ICPAK"))
				&& dto.getStatus() == CPDStatus.Approved;
		boolean isNoActionVisible = AppContext.isCurrentUserMember()
				&& (!isEditVisible && !isDownloadVisible && !isDeleteVisible);
		aView.setVisible(isViewVisible);
		aDelete.setVisible(isDeleteVisible);
		aDownloadCert.setVisible(isDownloadVisible);
		if (isNoActionVisible) {
			spnNoAction.removeClassName("hide");
		}
	}

	private void clear() {
		aView.setVisible(false);
		aDelete.setVisible(false);
		aDownloadCert.setVisible(false);
		spnNoAction.addClassName("hide");
	}

}
