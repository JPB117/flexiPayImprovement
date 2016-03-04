package com.workpoint.icpak.client.ui.component.autocomplete;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Timer;
import com.workpoint.icpak.client.ui.component.AutoCompleteField;
import com.workpoint.icpak.shared.model.Listable;

public class ServerOracle<T extends Listable> extends SimpleOracle<T> {

	String searchTerm = "";
	private Request request;
	private Callback callback;
	private List<StoreChangedHandler<T>> handlers = new ArrayList<StoreChangedHandler<T>>();
	private AutoCompleteField.Loader<T> loader;

	Timer timer = new Timer() {

		@Override
		public void run() {
			loader.onLoad(ServerOracle.this, searchTerm);
		}
		
	};

	public ServerOracle(AutoCompleteField.Loader<T> loader) {
		this.loader = loader;
	}

	@Override
	public void requestSuggestions(Request request, Callback callback) {
		this.request = request;
		this.callback = callback;
		searchTerm = request.getQuery();
		timer.cancel();
		timer.schedule(400);
	}

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
			callback.onSuggestionsReady(request, resp);
			// Reset
			request = null;
			resp = null;
			for (StoreChangedHandler<T> handler : handlers) {
				handler.onStoreChanged(values);
			}
		}
	}

	public void addStoreChangedHandler(StoreChangedHandler<T> handler) {
		handlers.add(handler);
	}

	public static interface StoreChangedHandler<T> {
		public void onStoreChanged(List<T> values);
	}
}
