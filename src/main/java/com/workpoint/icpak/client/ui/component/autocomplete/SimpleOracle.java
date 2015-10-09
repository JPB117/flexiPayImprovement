package com.workpoint.icpak.client.ui.component.autocomplete;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.SuggestOracle;
import com.workpoint.icpak.shared.model.Listable;

public abstract class SimpleOracle<T extends Listable> extends SuggestOracle {

	protected List<Suggestion> suggestions = new ArrayList<Suggestion>();
	
	public void setValues(List<T> values){}
}
