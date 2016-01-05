package com.workpoint.icpak.client.ui.profile.education;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.model.UploadContext;
import com.workpoint.icpak.client.model.UploadContext.UPLOADACTION;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.TableHeader;
import com.workpoint.icpak.client.ui.component.TableView;
import com.workpoint.icpak.client.ui.component.tabs.TabHeader;
import com.workpoint.icpak.client.ui.events.EditModelEvent;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.ApplicationFormEducationalDto;
import com.workpoint.icpak.shared.model.AttachmentDto;

public class EducationDetails extends Composite {

	private static EducationDetailsUiBinder uiBinder = GWT
			.create(EducationDetailsUiBinder.class);

	@UiField
	TableView tblEducationalDetail;

	@UiField
	ActionLink aAdd;

	@UiField
	ActionLink aSave;

	@UiField
	HTMLPanel panelForm;

	@UiField
	HTMLPanel panelTable;

	List<TableHeader> tblHeaders = new ArrayList<TableHeader>();
	List<String> allIssues = new ArrayList<>();

	interface EducationDetailsUiBinder extends
			UiBinder<Widget, EducationDetails> {
	}

	public EducationDetails() {
		initWidget(uiBinder.createAndBindUi(this));
		createTableHeader();
	}

	private void createTableHeader() {
		tblHeaders.add(new TableHeader("Institution"));
		tblHeaders.add(new TableHeader("From"));
		tblHeaders.add(new TableHeader("To"));
		tblHeaders.add(new TableHeader("Exam Body"));
		tblHeaders.add(new TableHeader("Class/Division"));
		tblHeaders.add(new TableHeader("Awarded"));
		tblHeaders.add(new TableHeader("Attachments"));
		tblHeaders.add(new TableHeader("Action"));
		tblEducationalDetail.setTableHeaders(tblHeaders);
	}

	protected void showForm(boolean show) {
		aAdd.setVisible(!show);

		if (show) {
			panelForm.removeStyleName("hide");
			panelTable.addStyleName("hide");
		} else {
			panelTable.removeStyleName("hide");
			panelForm.addStyleName("hide");
		}
	}

	public void setEditMode(boolean editMode) {
		if (editMode) {
			aAdd.setVisible(true);
		} else {
			aAdd.setVisible(false);
		}
	}

	public void bindDetails(List<ApplicationFormEducationalDto> result) {
		tblEducationalDetail.clearRows();
		tblEducationalDetail.setNoRecords(result.size());

		for (ApplicationFormEducationalDto edu : result) {

			final ActionLink edit = new ActionLink(edu);
			edit.setStyleName("fa fa-pencil btn btn-primary");
			edit.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					AppContext.fireEvent(new EditModelEvent(edit.getModel()));
				}
			});

			final ActionLink delete = new ActionLink(edu);
			delete.setStyleName("fa fa-trash-o btn btn-danger");
			delete.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					AppContext.fireEvent(new EditModelEvent(edit.getModel(),
							true));
				}
			});

			HTMLPanel panel = new HTMLPanel("");
			panel.add(edit);
			panel.add(delete);

			HTMLPanel panelAttachment = new HTMLPanel("");
			panelAttachment.clear();
			if (edu.getAttachments() != null) {
				for (final AttachmentDto attachment : edu.getAttachments()) {
					final UploadContext ctx = new UploadContext("getreport");
					ctx.setAction(UPLOADACTION.GETATTACHMENT);
					ctx.setContext("refId", attachment.getRefId());

					ActionLink link = new ActionLink(
							attachment.getAttachmentName());
					link.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							Window.open(ctx.toUrl(),
									attachment.getAttachmentName(), "");
						}
					});
					panelAttachment.add(link);
					panelAttachment.add(new HTML("<br/>"));
				}
			} else {
				allIssues.add("Education attachments are required!");
			}

			tblEducationalDetail.addRow(
					new InlineLabel(edu.getWhereObtained()),
					new InlineLabel(DateUtils.DATEFORMAT.format(edu
							.getFromDate())),
					new InlineLabel(
							DateUtils.DATEFORMAT.format(edu.getToDate())),
					new InlineLabel(edu.getExaminingBody()), new InlineLabel(
							edu.getClassDivisionAttained()), new InlineLabel(
							edu.getCertificateAwarded() + ""), panelAttachment,
					panel);
		}
	}

	public HasClickHandlers getSaveButton() {
		return aSave;
	}

	public HasClickHandlers getAddButton() {
		return aAdd;
	}

	public void clear() {
		tblEducationalDetail.clearRows();
	}

	public List<String> getEducationDetailIssues() {
		return allIssues;
	}
}
