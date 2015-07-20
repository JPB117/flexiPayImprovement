package com.workpoint.icpak.client.ui.component.autocomplete;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.workpoint.icpak.shared.model.Listable;

public class DataOracle<T extends Listable> extends SimpleOracle<T> {
	
	@Override
	public void requestSuggestions(Request request, Callback callback) {
		Response resp = new Response();
		String query = request.getQuery();
		List<Suggestion> sublist = new ArrayList<Suggestion>();
		for(Suggestion suggest: suggestions){
			if(suggest.getDisplayString().toLowerCase().contains(query.toLowerCase())){
				sublist.add(suggest);
			}
		}
		resp.setSuggestions(sublist);
		callback.onSuggestionsReady(request, resp);
	}
	
	public void setValues(List<T> values){
		suggestions.clear();
		if(values==null)
			return;
		
		for(T value: values){
			DataSuggestion<T> suggestion = new DataSuggestion<T>(value); 
			suggestions.add(suggestion);
		}
	}
	
}
