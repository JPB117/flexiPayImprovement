package com.workpoint.icpak.client.ui.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class PopOver extends Composite {

	private static PopOverUiBinder uiBinder = GWT.create(PopOverUiBinder.class);

	interface PopOverUiBinder extends UiBinder<Widget, PopOver> {
	}

	public PopOver() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		initWidget();
	}

	public static native void initWidget()/*-{
											$wnd.jQuery(".popover-icon").popover({html:'true'});
											}-*/;
}
