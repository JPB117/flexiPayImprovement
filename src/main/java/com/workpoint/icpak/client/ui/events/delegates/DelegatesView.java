package com.workpoint.icpak.client.ui.events.delegates;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.TableHeader;
import com.workpoint.icpak.client.ui.component.TableView;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.ui.events.delegates.row.DelegateTableRow;

public class DelegatesView extends ViewImpl implements
		DelegatesPresenter.MyView {

	private final Widget widget;

	@UiField
	TableView tblDelegates;

	@UiField
	ActionLink aAddRow;

	List<TableHeader> th = new ArrayList<TableHeader>();

	protected int counter;

	protected int rowId;

	public interface Binder extends UiBinder<Widget, DelegatesView> {
	}

	@Inject
	public DelegatesView(final Binder binder) {
		widget = binder.createAndBindUi(this);

		createHeader();

		aAddRow.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				createRow();
				rowId++;
			}
		});
	}

	protected void createRow() {
		DelegateTableRow row = new DelegateTableRow();

		while (counter < th.size()) {
			counter++;
			row.InsertParameters(new TextField(), new TextField(),
					new TextField(), new TextField(), new TextField(), rowId);
		}
		
		tblDelegates.addRow(row);

	}

	public void createHeader() {
		th.add(new TableHeader("Member No"));
		th.add(new TableHeader("Title"));
		th.add(new TableHeader("SurName"));
		th.add(new TableHeader("Other Names"));
		th.add(new TableHeader("Email"));

		tblDelegates.setTableHeaders(th);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

}
