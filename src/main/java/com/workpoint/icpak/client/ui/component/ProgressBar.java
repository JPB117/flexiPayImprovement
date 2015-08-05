package com.workpoint.icpak.client.ui.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ProgressBar extends Composite {

	private static ProgressBarUiBinder uiBinder = GWT
			.create(ProgressBarUiBinder.class);

	interface ProgressBarUiBinder extends UiBinder<Widget, ProgressBar> {
	}
	
	@UiField DivElement divProgress;
	@UiField SpanElement spnCompletion;

	public ProgressBar() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setProgress(int value){
		divProgress.setAttribute("aria-valuenow", value+"");
		divProgress.addClassName("progress-"+value);
		spnCompletion.setInnerText(value+"% complete");
	}

	public void clear() {
		divProgress.setAttribute("aria-valuenow", 0+"");
		divProgress.addClassName("progress-"+0);
		spnCompletion.setInnerText(0+"% complete");
	}

}
