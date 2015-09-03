package com.workpoint.icpak.client.ui.grid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.AutoCompleteField;
import com.workpoint.icpak.client.ui.component.AutoCompleteField.Loader;
import com.workpoint.icpak.client.ui.component.DoubleField;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.IntegerField;
import com.workpoint.icpak.client.ui.component.TextArea;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.ui.component.autocomplete.SimpleOracle;
import com.workpoint.icpak.shared.model.BooleanValue;
import com.workpoint.icpak.shared.model.DataType;
import com.workpoint.icpak.shared.model.DateValue;
import com.workpoint.icpak.shared.model.DoubleValue;
import com.workpoint.icpak.shared.model.Listable;
import com.workpoint.icpak.shared.model.LongValue;
import com.workpoint.icpak.shared.model.StringValue;
import com.workpoint.icpak.shared.model.Value;

public class ColumnConfig {

	private String key;
	private String displayName;
	private String placeHolder;
	private String styleName;
	private boolean isAggregationColumn;
	private boolean isMandatory;
	private DataType type;
	private Loader loader;
	private List<Listable> dropDownItems = new ArrayList<Listable>();
	private List<ValueChangeHandler> valueChangeHandlers = new ArrayList<ValueChangeHandler>();
	private boolean isEnabled;

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public ColumnConfig(String key, String displayName, DataType type) {
		this.key = key;
		this.displayName = displayName;
		this.type = type;
	}

	public ColumnConfig(String key, String displayName, DataType type,
			String placeHolder) {
		this(key, displayName, type);
		this.placeHolder = placeHolder;
	}

	public ColumnConfig(String key, String displayName, DataType type,
			String placeHolder, String styleName) {
		this(key, displayName, type);
		this.placeHolder = placeHolder;
		this.styleName = styleName;
	}

	public ColumnConfig(String key, String displayName, DataType type,
			String placeHolder, String styleName, boolean isEnabled) {
		this(key, displayName, type, placeHolder, styleName);
		this.isEnabled = isEnabled;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Widget createWidget(Object value) {
		HasValue widget = null;
		if (type == DataType.INTEGER) {
			IntegerField field = new IntegerField();
			field.setPlaceholder(placeHolder == null ? "" : placeHolder);
			// field.setStyleName("input-medium");
			widget = field;
		} else if (type == DataType.DOUBLE) {
			DoubleField field = new DoubleField();
			field.setPlaceholder(placeHolder == null ? "" : placeHolder);
			// field.setStyleName("input-medium");
			widget = field;
		} else if (type == DataType.SELECTBASIC) {
			DropDownList dropDown = new DropDownList();
			dropDown.setItems(dropDownItems);
			widget = dropDown;
		} else if (type == DataType.SELECTAUTOCOMPLETE) {
			AutoCompleteField auto = new AutoCompleteField(loader);
			auto.setEnabled(isEnabled);
			// ((Widget) widget).getElement().getFirstChildElement()
			// .addClassName(styleName);

			// auto.setValues(dropDownItems);
			widget = auto;
		} else if (type == DataType.STRINGLONG) {
			TextArea field = new TextArea();
			field.setPlaceholder(placeHolder == null ? "" : placeHolder);
			widget = field;
		} else if (type == DataType.BOOLEAN) {
			CheckBox field = new CheckBox();
			widget = field;
		} else {
			TextField field = new TextField();
			field.setPlaceholder(placeHolder == null ? "" : placeHolder);
			widget = field;
		}
		if (styleName != null) {
			((Widget) widget).addStyleName(styleName);
		}

		for (ValueChangeHandler handler : valueChangeHandlers) {
			widget.addValueChangeHandler(handler);
		}

		widget.setValue(value);
		return (Widget) widget;
	}

	public void addValueChangeHandler(ValueChangeHandler<Listable> handler) {
		valueChangeHandlers.add(handler);
	}

	public static Value getValue(Long id, String key, Object obj, DataType type) {
		if (obj == null) {
			return null;
		}

		Value value = null;
		switch (type) {
		case BOOLEAN:
			value = new BooleanValue(id, key, (Boolean) obj);
			break;

		case DATE:
			value = new DateValue(id, key, (Date) obj);
			break;

		case DOUBLE:
			value = new DoubleValue(id, key, ((Number) obj).doubleValue());
			break;

		case INTEGER:
			value = new LongValue(id, key, ((Number) obj).longValue());
			break;

		case STRING:
			value = new StringValue(id, key, obj.toString());
			break;

		case CHECKBOX:
			value = new BooleanValue(id, key, (Boolean) obj);
			break;

		case MULTIBUTTON:
			value = new StringValue(id, key, obj.toString());
			break;

		case SELECTBASIC:
			value = new StringValue(id, key, obj.toString());
			break;

		case SELECTMULTIPLE:
			value = new StringValue(id, key, obj.toString());
			break;

		case STRINGLONG:
			value = new StringValue(id, key, obj.toString());
			break;
		}

		return value;
	}

	public void setDropDownItems(List<Listable> items) {
		this.dropDownItems.clear();
		this.dropDownItems.addAll(items);
	}

	public Widget createHeaderWidget(String styleName) {
		InlineLabel label = new InlineLabel(displayName);
		if (styleName.isEmpty()) {
			label.addStyleName("center");
		} else {
			label.removeStyleName("left");
		}
		return label;
	}

	public boolean isAggregationColumn() {
		return isAggregationColumn;
	}

	public void setAggregationColumn(boolean isAggregationColumn) {
		this.isAggregationColumn = isAggregationColumn;
	}

	public DataType getType() {
		return type;
	}

	public void setType(DataType type) {
		this.type = type;
	}

	public String getPlaceHolder() {
		return placeHolder;
	}

	public void setPlaceHolder(String placeHolder) {
		this.placeHolder = placeHolder;
	}

	public boolean isMandatory() {
		return isMandatory;
	}

	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public String getStyleName() {
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

	public Widget createHeaderWidget() {
		return createHeaderWidget("");
	}

	public Loader getLoader() {
		return loader;
	}

	public void setLoader(Loader loader) {
		this.loader = loader;
	}

}
