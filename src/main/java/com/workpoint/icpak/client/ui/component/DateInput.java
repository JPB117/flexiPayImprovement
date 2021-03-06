package com.workpoint.icpak.client.ui.component;

import static com.workpoint.icpak.client.ui.util.DateUtils.DATEFORMAT;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class DateInput extends Composite {

	private static DateInputUiBinder uiBinder = GWT
			.create(DateInputUiBinder.class);

	interface DateInputUiBinder extends UiBinder<Widget, DateInput> {
	}
	
	@UiField InlineLabel spnCalendar1;
	@UiField DateBox dtInput;
	
	public DateInput() {
		initWidget(uiBinder.createAndBindUi(this));
		dtInput.setFormat(new DateBox.DefaultFormat(DATEFORMAT));
		spnCalendar1.getElement().setInnerHTML("<i class='icon-calendar'/>");
	}
	
	public Date getDate(){
		return dtInput.getValue();
	}

	public void setValue(Date date){
		dtInput.setValue(date);
	}
	
	public void setStyle(String styleName){
		dtInput.setStyleName(styleName);
	}

	public void addValueChangeHandler(
			ValueChangeHandler<Date> valueChangeHandler) {
		dtInput.addValueChangeHandler(valueChangeHandler);
	}
}
