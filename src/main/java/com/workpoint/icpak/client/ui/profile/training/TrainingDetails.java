package com.workpoint.icpak.client.ui.profile.training;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.TableHeader;
import com.workpoint.icpak.client.ui.component.TableView;

public class TrainingDetails extends Composite {

	private static TillDetailsUiBinder uiBinder = GWT
			.create(TillDetailsUiBinder.class);
	
	@UiField TableView tblBeforeCPA;
	
	List<TableHeader> th = new ArrayList<TableHeader>();


	interface TillDetailsUiBinder extends UiBinder<Widget, TrainingDetails> {
	}

	public TrainingDetails() {
		initWidget(uiBinder.createAndBindUi(this));
		
		createHeader();
	}

	private void createHeader() {
		th.add(new TableHeader("Member No"));
		th.add(new TableHeader("Title"));
		th.add(new TableHeader("SurName"));
		th.add(new TableHeader("Other Names"));
		th.add(new TableHeader("Email"));

		tblBeforeCPA.setTableHeaders(th);
	}

}
