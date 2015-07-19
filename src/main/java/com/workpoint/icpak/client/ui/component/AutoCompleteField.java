package com.workpoint.icpak.client.ui.component;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.autocomplete.DataOracle;
import com.workpoint.icpak.shared.model.Listable;

public class AutoCompleteField<T extends Listable> extends Composite{

	private static AutoCompleteFieldUiBinder uiBinder = GWT
			.create(AutoCompleteFieldUiBinder.class);

	interface AutoCompleteFieldUiBinder extends
			UiBinder<Widget, AutoCompleteField> {
	}


	@UiField HTMLPanel container;
	
	final TextBox itemBox = new TextBox();
	DataOracle<T> oracle = new DataOracle<T>();
	SuggestBox box=null;
	
	int x=0;
	String id="suggestion_box"+(++x);
	
	private String nullText;
	T value = null;
	
	
	public AutoCompleteField() {
		initWidget(uiBinder.createAndBindUi(this));
		itemBox.getElement().setAttribute("style", "outline-color: -moz-use-text-color; outline-style: none; outline-width: medium;");
		container.getElement().setAttribute("onclick", "document.getElementById('"+id+"').focus()");
		box = new SuggestBox(oracle, itemBox);
		box.getElement().setId(id);
		box.setAnimationEnabled(true);		
		//liPanel.add(box);
		box.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            public void onSelection(SelectionEvent selectionEvent) {
                //deselectItem(itemBox, ulPanel);
            }
        });
		
		container.add(box);
		//box.setFocus(true);
	}
	
	public void setValues(List<T> values){
		if(values==null){
			return;
		}
		
		oracle.setValues(values);	
		
	}

	public T getValue(){
		return value;
	}
	
	public void setValueByKey(String key){
		
	}


	public String getNullText() {
		return nullText;
	}

	public void setNullText(String nullText) {
		this.nullText = nullText;
	}
}
