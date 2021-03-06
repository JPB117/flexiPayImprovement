package com.workpoint.icpak.client.ui.cpd.form;

import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.model.UploadContext;
import com.workpoint.icpak.client.model.UploadContext.UPLOADACTION;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.DateField;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.ui.events.TableActionEvent;
import com.workpoint.icpak.client.ui.upload.custom.Uploader;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.AttachmentDto;
import com.workpoint.icpak.shared.model.CPDAction;
import com.workpoint.icpak.shared.model.CPDCategory;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.shared.model.CPDStatus;
import com.workpoint.icpak.shared.model.TableActionType;

public class RecordCPD extends Composite {

	private static RecordCPDUiBinder uiBinder = GWT
			.create(RecordCPDUiBinder.class);

	interface RecordCPDUiBinder extends UiBinder<Widget, RecordCPD> {
	}

	@UiField
	HTMLPanel panelForm;
	@UiField
	HTMLPanel panelCategories;
	@UiField
	ActionLink aPreviousForm;
	@UiField
	IssuesPanel issues;

	@UiField
	TextField txtTitle;

	@UiField
	TextField txtVenue;

	@UiField
	TextField txtOrganizer;

	@UiField
	DivElement divLocation;

	@UiField
	DateField dtStartDate;

	@UiField
	DateField dtEndDate;

	@UiField
	Uploader uploader;

	@UiField
	DivElement divCpdHours;

	@UiField
	DivElement divMgtComment;

	@UiField
	TextField txtCPDHours;

	@UiField
	ActionLink aStartUpload;

	@UiField
	SpanElement spnLoading;

	@UiField
	HTMLPanel panelUploader;

	@UiField
	HTMLPanel panelUpload;

	@UiField
	HTMLPanel panelPreviousAttachments;

	@UiField
	TextArea txtMgmtComment;

	@UiField
	SpanElement spnMgmntActionBy;

	@UiField
	ActionLink aBack;

	@UiField
	ActionLink aSave;

	@UiField
	DivElement panelInlineActions;
	@UiField
	DivElement panelBreadcrumb;
	@UiField
	DivElement divUpdatedBy;

	@UiField
	SpanElement spnMemberName;

	@UiField
	DropDownList<CPDCategory> lstCategory;

	@UiField
	DropDownList<CPDAction> lstMgmtAction;

	@UiField
	TextField txtMemberNo;
	@UiField
	ActionLink aSearch;
	@UiField
	DivElement panelMemberNo;
	@UiField
	DivElement panelRecordingPanel;

	private CPDDto dto;
	private boolean isViewMode;

	@UiField
	SpanElement spnFullNames;
	@UiField
	SpanElement spnSpinner;

	public RecordCPD() {
		initWidget(uiBinder.createAndBindUi(this));
		showForm(false);

		List<CPDCategory> categories = new ArrayList<CPDCategory>();
		for (CPDCategory cat : CPDCategory.values()) {
			if (cat != CPDCategory.NO_CATEGORY) {
				categories.add(cat);
			}
		}
		lstCategory.setItems(categories);

		lstCategory
				.addValueChangeHandler(new ValueChangeHandler<CPDCategory>() {
					@Override
					public void onValueChange(
							ValueChangeEvent<CPDCategory> event) {
						showLocation(event.getValue());
					}
				});

		List<CPDAction> cpdActions = new ArrayList<CPDAction>();
		for (CPDAction cpd : CPDAction.values()) {
			cpdActions.add(cpd);
		}
		lstMgmtAction.setItems(cpdActions);
		// uploader.setAutoSubmit(false);

		aSave.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (isValid()) {
					AppContext.fireEvent(new TableActionEvent(getCPD(),
							TableActionType.APPROVECPD));
				}
			}
		});

		aBack.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.back();
			}
		});

	}

	protected void showLocation(CPDCategory value) {
		if (value == CPDCategory.CATEGORY_A) {
			divLocation.removeClassName("hide");
		} else {
			divLocation.addClassName("hide");
		}
	}

	public void showUploadPanel(boolean showForm) {
		aStartUpload.setVisible(!showForm);
		if (showForm) {
			panelUploader.removeStyleName("hide");
			setUploadContext();
		} else {
			panelUploader.addStyleName("hide");

		}
	}

	public void showAwaitingToAttach(boolean show) {
		if (show) {
			aStartUpload.setVisible(false);
			spnLoading.removeClassName("hide");
		} else {
			spnLoading.addClassName("hide");
		}
	}

	private void setUploadContext() {
		UploadContext context = new UploadContext();
		context.setContext("cpdRefId", getCPD().getRefId());
		context.setAction(UPLOADACTION.UPLOADCPD);
		context.setAccept(Arrays.asList("doc", "pdf", "jpg", "jpeg", "png",
				"docx"));
		uploader.setContext(context);
	}

	public boolean isValid() {
		boolean isValid = true;
		issues.clear();
		if (isNullOrEmpty(txtTitle.getValue())) {
			isValid = false;
			issues.addError("Title is mandatory");
		}
		if (isNullOrEmpty(txtOrganizer.getValue())) {
			isValid = false;
			issues.addError("Organizer is mandatory");
		}
		if (dtStartDate.getValueDate() == null) {
			isValid = false;
			issues.addError("Start date is mandatory");
		}
		if (dtEndDate.getValueDate() == null) {
			isValid = false;
			issues.addError("End date is mandatory");
		}
		if (lstCategory.getValue() == null) {
			isValid = false;
			issues.addError("Category is mandatory");
		}
		if (isViewMode && txtCPDHours.getValue().equals("0")
				&& (lstMgmtAction.getValue() == CPDAction.APPROVED)) {
			isValid = false;
			issues.addError("CPD Hours must be greater than 0!");
		}
		if (!isValid) {
			issues.getElement().scrollIntoView();
			issues.removeStyleName("hide");
		} else {
			issues.addStyleName("hide");
		}
		return isValid;
	}

	public void showForm(boolean show) {
		if (show) {
			panelForm.setVisible(true);
			panelCategories.setVisible(false);
		} else {
			panelForm.setVisible(false);
			panelCategories.setVisible(true);
		}
	}

	public CPDDto getCPD() {
		CPDDto dto = new CPDDto();
		if (this.dto != null) {
			dto = this.dto;
		}
		dto.setCategory(lstCategory.getValue());
		dto.setStartDate(dtStartDate.getValueDate());
		dto.setEndDate(dtEndDate.getValueDate());
		dto.setOrganizer(txtOrganizer.getValue());
		dto.setTitle(txtTitle.getValue());
		if (txtVenue.getValue() != null) {
			dto.setEventLocation(txtVenue.getValue());
		}
		if (lstMgmtAction.getValue() != null) {
			CPDStatus status = (lstMgmtAction.getValue().getName() == "APPROVED" ? CPDStatus.Approved
					: CPDStatus.Rejected);
			dto.setStatus(status);
			dto.setUpdatedBy(AppContext.getCurrentUser().getUser()
					.getFullName());
		}
		if (txtCPDHours.getValue() != null
				&& !(txtCPDHours.getValue().isEmpty())) {
			dto.setCpdHours(Double.valueOf(txtCPDHours.getValue()));
		}
		if (txtMgmtComment.getValue() != null) {
			dto.setManagementComment(txtMgmtComment.getValue());
		}
		return dto;
	}

	public void setCPD(CPDDto dto) {
		this.dto = dto;
		if (dto == null) {
			return;
		}
		lstCategory.setValue(dto.getCategory());

		if (dto.getCategory() == CPDCategory.CATEGORY_A) {
			divLocation.removeClassName("hide");
		} else {
			divLocation.addClassName("hide");
		}
		dtEndDate.setValue(dto.getEndDate());
		dtStartDate.setValue(dto.getStartDate());
		txtTitle.setValue(dto.getTitle());
		txtOrganizer.setValue(dto.getOrganizer());
		txtCPDHours.setValue(Double.toString(dto.getCpdHours()));

		if (dto.getUpdatedBy() != null) {
			spnMgmntActionBy.setInnerText(dto.getUpdatedBy());
		}

		if (dto.getStatus() != null) {
			if (dto.getStatus() == CPDStatus.Approved) {
				lstMgmtAction.setValue(CPDAction.APPROVED);
			} else if (dto.getStatus() == CPDStatus.Rejected) {
				lstMgmtAction.setValue(CPDAction.REJECTED);
			}
		}

		if (dto.getEventLocation() != null) {
			divLocation.removeClassName("hide");
			txtVenue.setValue(dto.getEventLocation());
		} else {
			divLocation.addClassName("hide");
		}

		txtMgmtComment.setValue(dto.getManagementComment());
		panelPreviousAttachments.clear();

		if (dto.getAttachments() != null) {
			for (final AttachmentDto attachment : dto.getAttachments()) {
				final UploadContext ctx = new UploadContext("getreport");
				ctx.setAction(UPLOADACTION.GETATTACHMENT);
				ctx.setContext("refId", attachment.getRefId());

				ActionLink link = new ActionLink(attachment.getAttachmentName());
				link.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						Window.open(ctx.toUrl(),
								attachment.getAttachmentName(), "");
					}
				});
				panelPreviousAttachments.add(new HTML("<br/>"));
				panelPreviousAttachments.add(link);
			}
		} else {
			panelPreviousAttachments.add(new InlineLabel(
					"No attachments available"));
		}
	}

	public HasClickHandlers getStartUploadButton() {
		return aStartUpload;
	}

	public void setAdminMode(boolean isViewMode) {
		this.isViewMode = isViewMode;
		if (isViewMode) {
			// lstCategory.getElement().getFirstChildElement()
			// .setAttribute("disabled", "disabled");
			// dtEndDate.setDisabled(true);
			// dtStartDate.setDisabled(true);
			// txtTitle.getElement().setAttribute("disabled", "disabled");
			// txtOrganizer.getElement().setAttribute("disabled", "disabled");
			aPreviousForm.setVisible(false);
			panelUpload.setVisible(false);
			divCpdHours.removeClassName("hide");
			divMgtComment.removeClassName("hide");
			panelInlineActions.removeClassName("hide");
			panelBreadcrumb.removeClassName("hide");
			divUpdatedBy.removeClassName("hide");

		} else {
			lstCategory.getElement().getFirstChildElement()
					.removeAttribute("disabled");
			dtEndDate.setDisabled(false);
			dtStartDate.setDisabled(false);
			txtTitle.getElement().removeAttribute("disabled");
			txtOrganizer.getElement().removeAttribute("disabled");
			panelUpload.setVisible(true);
			divCpdHours.addClassName("hide");
			panelInlineActions.addClassName("hide");
			panelBreadcrumb.addClassName("hide");
			divUpdatedBy.addClassName("hide");
			panelMemberNo.addClassName("hide");
		}
	}

	public void setBackHref(String href) {
		// aBack.setHref(href);
	}

	public void showMemberNoPanel(boolean show) {
		if (show) {
			panelMemberNo.removeClassName("hide");
			panelRecordingPanel.addClassName("hide");
		} else {
			panelMemberNo.addClassName("hide");
			panelRecordingPanel.removeClassName("hide");
		}
	}

	public void setMemberName(String memberName) {
		spnMemberName.setInnerText(memberName);
	}

	public String getMemberNoValue() {
		return txtMemberNo.getValue();
	}

	public HasKeyDownHandlers getMemberNoKeyDownHandler() {
		return txtMemberNo;
	}

	public void setFullNames(String fullNames) {
		spnFullNames.setInnerText(fullNames);
	}

	public void showMemberLoading(boolean show) {
		if (show) {
			spnSpinner.removeClassName("hide");
		} else {
			spnSpinner.addClassName("hide");
		}
	}

	public void showRecordingPanel(boolean show) {
		if (show) {
			panelRecordingPanel.removeClassName("hide");
			divCpdHours.removeClassName("hide");
			divMgtComment.removeClassName("hide");
		} else {
			panelRecordingPanel.addClassName("hide");
			divCpdHours.addClassName("hide");
			divMgtComment.addClassName("hide");
		}
	}

}
