package com.workpoint.icpak.client.ui.component;

import static com.workpoint.icpak.client.ui.util.DateUtils.DATEFORMAT_SYS;
import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.workpoint.icpak.client.ui.util.DateUtils;

public class DateField extends Composite {

	@UiField
	TextField txtDate;

	@UiField
	HTMLPanel panelContainer;

	private static DateFieldUiBinder uiBinder = GWT
			.create(DateFieldUiBinder.class);

	interface DateFieldUiBinder extends UiBinder<Widget, DateField> {
	}

	public DateField() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public Date getValueDate() {
		String dateStr = txtDate.getValue();
		// yyyy-MM-dd
		if (isNullOrEmpty(dateStr)) {
			return null;
		}
		return DATEFORMAT_SYS.parse(dateStr);
	}

	public void setValue(Date date) {
		if (date == null) {
			txtDate.setValue("");
		} else {
			txtDate.setValue(DateUtils.DATEFORMAT_SYS.format(date));
		}
	}

	public HasValueChangeHandlers<String> getDateInput() {
		return txtDate;
	}

	public void setPlaceholder(String placeHolderValue) {
		txtDate.getElement().setAttribute("placeHolder", placeHolderValue);
	}

	public void setClass(String className) {
		setStyleName(className);
	}

	public void setType(String type) {
		txtDate.getElement().setAttribute("type", type);
	}

	public void setDataToggle(String dataToggle) {
		txtDate.getElement().setAttribute("data-toggle", dataToggle);
	}

	public void setAriaHaspopup(String ariaHasPopup) {
		txtDate.getElement().setAttribute("aria-haspopup", ariaHasPopup);
	}

	public void setAriaExpanded(String ariaExpanded) {
		txtDate.getElement().setAttribute("aria-expanded", ariaExpanded);
	}

	public void setDisabled(Boolean isDisabled) {
		if (isDisabled) {
			txtDate.getElement().setAttribute("disabled", "disabled");
		}
	}

	public void setRequired(Boolean isRequired) {
		if (isRequired) {
			txtDate.getElement().setAttribute("required", "required");
		}
	}

	public void setMaxLength(String maxLength) {
		txtDate.getElement().setAttribute("max-length", maxLength);
	}

	public void setSize(String size) {
		txtDate.getElement().setAttribute("size", size);
	}

	public void clear() {
		txtDate.setValue("");
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		Date today = new Date();
		CalendarUtil.addDaysToDate(today, 1);

		initCollapsable(DateUtils.DATEFORMAT_SYS.format(today));
	}

	public static native void initCollapsable(String maxDate)/*-{
																var ToEndDate = new Date();
																$wnd.jQuery(".datepicker").datetimepicker({
																icons: {
																time: "fa fa-clock-o",
																date: "fa fa-calendar",
																up: "fa fa-arrow-up",
																down: "fa fa-arrow-down",
																previous: 'fa fa-chevron-left',
																next: 'fa fa-chevron-right',
																},
																format: 'YYYY-MM-DD',
																maxDate:maxDate,
																useCurrent:false
																});
																}-*/;
}
