package com.workpoint.icpak.client.ui.splashscreen;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class SplashScreenView extends ViewImpl implements
		SplashScreenPresenter.ILoginView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, SplashScreenView> {
	}

	@Inject
	public SplashScreenView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

}
