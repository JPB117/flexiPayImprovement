package com.workpoint.icpak.client.ui.error;

import java.util.Date;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class UnauthorizedPageView extends ViewImpl implements
	UnauthorizedPagePresenter.MyView {

	private final Widget widget;
	
	public interface Binder extends UiBinder<Widget, UnauthorizedPageView> {
	}

	@Inject
	public UnauthorizedPageView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setError(Date errorDate, String message, String stack, String userAgent, String address) {
	}
}
