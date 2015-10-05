package com.workpoint.icpak.client.ui.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.autocomplete.DataOracle;
import com.workpoint.icpak.client.ui.component.autocomplete.ServerOracle;
import com.workpoint.icpak.client.ui.component.autocomplete.SimpleOracle;
import com.workpoint.icpak.client.ui.grid.AggregationGridRow;
import com.workpoint.icpak.shared.model.Listable;

public class AutoCompleteField<T extends Listable> extends Composite implements
		HasValueChangeHandlers<T>, HasValue<T>,
		ServerOracle.StoreChangedHandler<T> {

	private static AutoCompleteFieldUiBinder uiBinder = GWT
			.create(AutoCompleteFieldUiBinder.class);

	interface AutoCompleteFieldUiBinder extends
			UiBinder<Widget, AutoCompleteField> {
	}

	@UiField
	HTMLPanel container;

	Map<String, T> valuesMap = new HashMap<String, T>();
	final TextBox itemBox = new TextBox();
	SimpleOracle<T> oracle = new DataOracle<T>();
	SuggestBox box = null;

	static int x = 0;
	String id = "suggestion_box" + (++x);

	private String nullText;
	T value = null;

	private AggregationGridRow aggregationGridRow;

	public AutoCompleteField() {
		this(null);
	}

	public AutoCompleteField(Loader<T> loader) {
		initWidget(uiBinder.createAndBindUi(this));
		itemBox.getElement()
				.setAttribute(
						"style",
						"outline-color: -moz-use-text-color; outline-style: none; outline-width: medium;");
		container.getElement().setAttribute("onclick",
				"document.getElementById('" + id + "').focus()");

		if (loader != null) {
			this.oracle = new ServerOracle<T>(loader);
		}

		box = new SuggestBox(oracle, itemBox);
		box.getElement().setId(id);
		box.setAnimationEnabled(true);
		// liPanel.add(box);
		box.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			public void onSelection(SelectionEvent selectionEvent) {
				String value = box.getValue();

				if (value != null) {
					T val = valuesMap.get(value);
					setValue(val, true);

				} else {
					setValue(null, true);
				}
			}
		});

		if (oracle instanceof ServerOracle) {
			((ServerOracle<T>) oracle)
					.addStoreChangedHandler(AutoCompleteField.this);
		}

		container.add(box);
		// box.setFocus(true);
	}

	public void setValues(List<T> values) {
		setValues(values, true);
	}

	public void setValues(List<T> values, boolean updateOracle) {
		clear();
		if (values == null) {
			return;
		}

		for (T t : values) {
			valuesMap.put(t.getDisplayName(), t);
		}

		if (updateOracle)
			oracle.setValues(values);

	}

	public T getValue() {
		return value;
	}

	public void setValueByKey(String key) {
	}

	public void setEnabled(boolean isEnabled) {
		if (isEnabled) {
			itemBox.getElement().removeAttribute("disabled");
			itemBox.getElement().setAttribute("placeholder",
					"Start typing to autocomplete");
		} else {
			itemBox.getElement().setAttribute("disabled", "disabled");
		}
	}

	public void addItems(List<T> items) {
		// itemsSelected.clear();
		// others
		setValues(items);
	}

	@Override
	public void setValue(T item, boolean fireEvents) {
		this.value = item;
		if (item != null) {
			itemBox.setValue(item.getDisplayName());
		} else {
			itemBox.setValue(null);
		}

		if (fireEvents) {
			fireValueChange(item);
		}
	}

	private void fireValueChange(T item) {
		ValueChangeEvent.fire(this, item);
	}

	public void setValue(T item) {
		// others
		setValue(item, false);
	}

	private void clear() {
		valuesMap.clear();
		value = null;
	}

	public String getNullText() {
		return nullText;
	}

	public void setNullText(String nullText) {
		this.nullText = nullText;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<T> handler) {
		return this.addHandler(handler, ValueChangeEvent.getType());
	}

	public AggregationGridRow getParentRow() {
		return aggregationGridRow;
	}

	public void setParentRow(AggregationGridRow aggregationGridRow) {
		this.aggregationGridRow = aggregationGridRow;

	}

	@Override
	public void onStoreChanged(List<T> values) {
		setValues(values, false);
	}

	public static interface Loader<T> {
		public void onLoad(ServerOracle source, String query);
	}
}
