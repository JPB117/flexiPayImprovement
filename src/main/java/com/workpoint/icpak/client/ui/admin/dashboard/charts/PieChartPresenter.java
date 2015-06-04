package com.workpoint.icpak.client.ui.admin.dashboard.charts;

import java.util.List;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.workpoint.icpak.shared.model.dashboard.ChartType;
import com.workpoint.icpak.shared.model.dashboard.Data;

public class PieChartPresenter extends
		PresenterWidget<PieChartPresenter.IPieChartView> {

	public interface IPieChartView extends View {

		void setData(List<Data> data);
	}
	ChartType type;
	
	@Inject
	public PieChartPresenter(final EventBus eventBus, final IPieChartView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	public void setChart(ChartType type) {
		this.type =type; 		
	}

	public void setValues(List<Data> data) {
		getView().setData(data);
	}
}
