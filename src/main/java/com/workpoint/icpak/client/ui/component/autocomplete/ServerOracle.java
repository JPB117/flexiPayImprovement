package com.workpoint.icpak.client.ui.component.autocomplete;

import java.util.ArrayList;
import java.util.List;

import com.workpoint.icpak.shared.model.Listable;

public abstract class ServerOracle<T extends Listable> extends SimpleOracle<T> {

	private Request request;
	private Callback callback;
	private List<OnStoreChangedHandler<T>> handlers = new ArrayList<OnStoreChangedHandler<T>>();

	@Override
	public void requestSuggestions(Request request, Callback callback) {
		this.request = request;
		this.callback = callback;
		String query = request.getQuery();
		onLoad(query.trim());

	}

	public abstract void onLoad(final String query);

	public void setValues(List<T> values) {
		suggestions.clear();
		if (values == null)
			return;

		for (T value : values) {
			DataSuggestion<T> suggestion = new DataSuggestion<T>(value);
			suggestions.add(suggestion);
		}

		if (request != null) {
			Response resp = new Response();
			resp.setSuggestions(suggestions); // every server result fits the
												// query
			callback.onSuggestionsReady(request, resp);
			
			//Reset
			request=null;
			resp=null;
			for(OnStoreChangedHandler<T> handler:handlers){
				handler.onStoreChanged(values);
			}
		}
	}
	
	public void addStoreChangedHandler(OnStoreChangedHandler<T> handler){
		handlers.add(handler);
	}
	
	public static abstract class OnStoreChangedHandler<T>{

		public abstract void onStoreChanged(List<T> values);
	}
}
