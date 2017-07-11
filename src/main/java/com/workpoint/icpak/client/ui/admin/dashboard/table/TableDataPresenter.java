package com.workpoint.icpak.client.ui.admin.dashboard.table;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.workpoint.icpak.shared.model.dashboard.ChartType;

public class TableDataPresenter extends
		PresenterWidget<TableDataPresenter.ITableDataView> {

	public interface ITableDataView extends View {
	}
	
	ChartType type;
	
	boolean loaded = false;
	
	@Inject
	public TableDataPresenter(final EventBus eventBus, final ITableDataView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		
	}

}
