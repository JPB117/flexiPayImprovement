package com.workpoint.icpak.client.ui.profile.accountancy;

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
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.model.UploadContext;
import com.workpoint.icpak.client.model.UploadContext.UPLOADACTION;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.TableHeader;
import com.workpoint.icpak.client.ui.component.TableView;
import com.workpoint.icpak.client.ui.events.EditModelEvent;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.ApplicationFormAccountancyDto;
import com.workpoint.icpak.shared.model.AttachmentDto;

public class AccountancyDetails extends Composite {

	private static TrainingDetailsUiBinder uiBinder = GWT
			.create(TrainingDetailsUiBinder.class);

	@UiField
	TableView tblTrainingDetails;

	@UiField
	HTMLPanel panelTable;

	@UiField
	ActionLink aAdd;

	List<TableHeader> tblBeforeHeaders = new ArrayList<TableHeader>();

	List<TableHeader> tblAfterHeaders = new ArrayList<TableHeader>();

	List<String> allIssues = new ArrayList<>();

	interface TrainingDetailsUiBinder extends
			UiBinder<Widget, AccountancyDetails> {
	}

	public AccountancyDetails() {
		initWidget(uiBinder.createAndBindUi(this));
		createBeforeHeader();
	}

	private void createBeforeHeader() {
		tblBeforeHeaders.add(new TableHeader("Name of Examining Body"));
		tblBeforeHeaders.add(new TableHeader("Registration No."));
		tblBeforeHeaders
				.add(new TableHeader("Sections, Stages, Parts passed "));
		tblBeforeHeaders.add(new TableHeader("Date Passed"));
		tblBeforeHeaders.add(new TableHeader("Attachments"));
		tblBeforeHeaders.add(new TableHeader("Action"));
		tblTrainingDetails.setTableHeaders(tblBeforeHeaders);
	}

	public void setEditMode(boolean editMode) {
		if (editMode) {
			aAdd.setVisible(true);
		} else {
			aAdd.setVisible(false);
		}
	}

	public HasClickHandlers getAddButton() {
		return aAdd;
	}

	public void bindDetails(List<ApplicationFormAccountancyDto> result) {
		tblTrainingDetails.clearRows();
		tblTrainingDetails.setNoRecords(result.size());
		for (ApplicationFormAccountancyDto accountancy : result) {
			final ActionLink edit = new ActionLink(accountancy);
			edit.setStyleName("fa fa-pencil btn btn-primary");
			edit.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					AppContext.fireEvent(new EditModelEvent(edit.getModel()));
				}
			});

			final ActionLink delete = new ActionLink(accountancy);
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
			if (accountancy.getAttachments() != null) {
				for (final AttachmentDto attachment : accountancy
						.getAttachments()) {
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
				}
			} else {
				allIssues
						.add("Accountancy Examination attachments are required!");
			}

			tblTrainingDetails.addRow(
					new InlineLabel(accountancy.getExaminingBody()),
					new InlineLabel(accountancy.getRegistrationNo()),
					new InlineLabel(accountancy.getSectionPassed()),
					new InlineLabel(DateUtils.DATEFORMAT.format(accountancy
							.getDatePassed())), panelAttachment, panel);
		}
	}

	public void clear() {
		tblTrainingDetails.clearRows();
	}

	public List<String> getExaminationDetailIssues() {
		return allIssues;
	}
}
