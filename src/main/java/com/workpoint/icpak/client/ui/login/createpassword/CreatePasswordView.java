package com.workpoint.icpak.client.ui.login.createpassword;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class CreatePasswordView extends ViewImpl implements
		CreatePasswordPresenter.ILoginView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, CreatePasswordView> {
	}

	@Inject
	public CreatePasswordView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
