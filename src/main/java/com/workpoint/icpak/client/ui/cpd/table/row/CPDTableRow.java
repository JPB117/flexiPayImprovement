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
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.RowWidget;
import com.workpoint.icpak.client.ui.events.EditModelEvent;
import com.workpoint.icpak.client.ui.events.TableActionEvent;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.CPDCategory;
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
	ActionLink aMember;

	@UiField
	HTMLPanel divMember;

	@UiField
	ActionLink aAttended;

	@UiField
	ActionLink aNotAttended;

	private CPDDto dto;

	public CPDTableRow() {
		initWidget(uiBinder.createAndBindUi(this));
		aEdit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AppContext.fireEvent(new EditModelEvent(dto));
			}
		});

		aDownloadCert.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AppContext.fireEvent(new TableActionEvent(dto,
						TableActionType.DOWNLOADCERT));
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
		if (status.equals(CPDStatus.Unconfirmed)) {
			spnStatus.removeClassName("label-success");
			spnStatus.addClassName("label-warning");
		}

		if (!AppContext.isCurrentUserAdmin()) {
			aAttended.addStyleName("hide");
			aNotAttended.addStyleName("hide");
		}

		InlineLabel spnNoAction = new InlineLabel("No Action Possible");
		if (dto.getStatus() == CPDStatus.Approved
				&& dto.getCategory() == CPDCategory.CATEGORY_D) {
			aDownloadCert.setVisible(false);
			aEdit.setVisible(false);
			aActions.setVisible(false);
			divAction.add(spnNoAction);
		} else if (dto.getStatus() == CPDStatus.Approved) {
			aEdit.setVisible(false);
		} else {
			aDownloadCert.setVisible(true);
			aEdit.setVisible(true);
			aActions.setVisible(true);
			spnNoAction.setVisible(false);
		}

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

	public enum TableActionType {
		DOWNLOADCERT
	}

}
