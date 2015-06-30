package com.workpoint.icpak.client.ui.profile.training;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.TableHeader;
import com.workpoint.icpak.client.ui.component.TableView;

public class TrainingDetails extends Composite {

	private static TrainingDetailsUiBinder uiBinder = GWT
			.create(TrainingDetailsUiBinder.class);

	@UiField
	TableView tblBeforeCPA;

	@UiField
	HTMLPanel panelForm;

	@UiField
	HTMLPanel panelTable;

	@UiField
	ActionLink aAdd;

	@UiField
	ActionLink aSave;

	List<TableHeader> tblBeforeHeaders = new ArrayList<TableHeader>();

	List<TableHeader> tblAfterHeaders = new ArrayList<TableHeader>();

	interface TrainingDetailsUiBinder extends UiBinder<Widget, TrainingDetails> {
	}

	public TrainingDetails() {
		initWidget(uiBinder.createAndBindUi(this));

		createBeforeHeader();

		createRows();

		aAdd.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showForm(true);
			}
		});

		aSave.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showForm(false);
			}
		});
	}

	protected void showForm(boolean show) {
		if (show) {
			aAdd.setVisible(false);
			panelForm.removeStyleName("hide");
			panelTable.addStyleName("hide");
		} else {
			aAdd.setVisible(true);
			panelTable.removeStyleName("hide");
			panelForm.addStyleName("hide");
		}
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

		tblBeforeCPA.setTableHeaders(tblBeforeHeaders);
	}

	private void createRows() {
		for (int i = 0; i < 10; i++) {
			TrainingTableRow row = new TrainingTableRow();
			tblBeforeCPA.addRow(row);
		}
	}

	public void setEditMode(boolean editMode) {
		if (editMode) {
			aAdd.setVisible(true);
		} else {
			aAdd.setVisible(false);
		}
	}
}
