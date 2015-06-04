package com.workpoint.icpak.client.ui.admin.dashboard.linegraph;

import java.util.List;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.workpoint.icpak.shared.model.dashboard.ChartType;
import com.workpoint.icpak.shared.model.dashboard.Data;

public class LineGraphPresenter extends
		PresenterWidget<LineGraphPresenter.ILineGraphView> {

	public interface ILineGraphView extends View {
		void setData(List<Data> data);
	}

	ChartType type;
	boolean loaded=false;
	
	@Inject
	public LineGraphPresenter(final EventBus eventBus, final ILineGraphView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		loadData();
	}
	
	
	public void loadData(){
		if(loaded){
			return;
		}
		loaded=true;
//		requestHelper.execute(new GetTaskCompletionRequest(), new TaskServiceCallback<GetTaskCompletionResponse>() {
//			@Override
//			public void processResult(GetTaskCompletionResponse aResponse) {
//				
//				getView().setData(aResponse.getData());
//			}
//		});
	}

	public void setChart(ChartType type) {
		this.type =type; 		
	}
}
