package com.workpoint.icpak.client.ui.user;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;

public class UserSelectionPresenter extends
		PresenterWidget<UserSelectionPresenter.MyView> {

	public interface MyView extends PopupView {
		// TODO Put your view methods here
	}

	@Inject
	public UserSelectionPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
}
