package com.workpoint.icpak.client.ui.grid;

import java.util.List;

public abstract class DataMapper {

	public abstract <T> T getData(DataModel model);

	public abstract List<DataModel> getDataModels(List objs);
	
	public DataModel getModel(Object obj){
		return null;
	}
}
