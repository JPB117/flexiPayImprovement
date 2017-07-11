package com.workpoint.icpak.client.ui.component.autocomplete;

import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.workpoint.icpak.shared.model.Listable;

public class DataSuggestion<T extends Listable> implements Suggestion{
	
	private T data = null;
	
	public DataSuggestion(T data) {
		this.data = data;
	}

	@Override
	public String getDisplayString() {
		return data.getDisplayName();
	}

	@Override
	public String getReplacementString() {
		return data.getDisplayName();
	}
	
}