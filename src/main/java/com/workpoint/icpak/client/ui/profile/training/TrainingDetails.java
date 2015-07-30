package com.workpoint.icpak.client.ui.profile.training;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.TableHeader;
import com.workpoint.icpak.client.ui.component.TableView;
import com.workpoint.icpak.client.ui.events.EditModelEvent;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.ApplicationFormTrainingDto;

public class TrainingDetails extends Composite {

	private static TrainingDetailsUiBinder uiBinder = GWT
			.create(TrainingDetailsUiBinder.class);

	@UiField
	TableView tblTrainingDetails;

	@UiField
	HTMLPanel panelForm;

	@UiField
	HTMLPanel panelTable;
	
	@UiField
	ActionLink aAdd;
	

	List<TableHeader> tblBeforeHeaders = new ArrayList<TableHeader>();

	List<TableHeader> tblAfterHeaders = new ArrayList<TableHeader>();

	interface TrainingDetailsUiBinder extends UiBinder<Widget, TrainingDetails> {
	}

	public TrainingDetails() {
		initWidget(uiBinder.createAndBindUi(this));
		createBeforeHeader();
	}

	private void createBeforeHeader() {
		tblBeforeHeaders.add(new TableHeader("Organization"));
		tblBeforeHeaders.add(new TableHeader("Start Date"));
		tblBeforeHeaders.add(new TableHeader("End Date"));
		tblBeforeHeaders.add(new TableHeader("Position"));
		tblBeforeHeaders.add(new TableHeader("Task Nature"));
		tblBeforeHeaders.add(new TableHeader("Responsibilities"));
		tblBeforeHeaders.add(new TableHeader("Clients"));
		tblBeforeHeaders.add(new TableHeader("Date Passed"));
		tblBeforeHeaders.add(new TableHeader("Type"));
		tblBeforeHeaders.add(new TableHeader("Action"));

		tblTrainingDetails.setTableHeaders(tblBeforeHeaders);
	}

	private void createRows() {
		for (int i = 0; i < 10; i++) {
			TrainingTableRow row = new TrainingTableRow();
			tblTrainingDetails.addRow(row);
		}
	}

	public void setEditMode(boolean editMode) {
		if (editMode) {
			// aAdd.setVisible(true);
		} else {
			// aAdd.setVisible(false);
		}
	}

	public HasClickHandlers getAddButton() {
		return aAdd;
	}

	public void bindDetails(List<ApplicationFormTrainingDto> result) {
		tblTrainingDetails.clearRows();
		for (ApplicationFormTrainingDto training : result) {

			final ActionLink edit = new ActionLink(training);
			edit.setStyleName("fa fa-pencil btn btn-primary");
			edit.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					AppContext.fireEvent(new EditModelEvent(edit.getModel()));
				}
			});

			final ActionLink delete = new ActionLink(training);
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
			tblTrainingDetails.addRow(
					new InlineLabel(training.getOrganisationName()),
					new InlineLabel(DateUtils.DATEFORMAT.format(training
							.getFromDate())),
					new InlineLabel(DateUtils.DATEFORMAT.format(training
							.getToDate())),
					new InlineLabel(training.getPosition()),
					new InlineLabel(training.getTaskNature()),
					new InlineLabel(training.getResponsibility()),
					new InlineLabel(training.getClients()),
					new InlineLabel(DateUtils.DATEFORMAT.format(training
							.getDatePassed())), panel);
		}
	}
}
