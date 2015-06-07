package com.workpoint.icpak.client.ui.profile;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class ProfileView extends ViewImpl implements ProfilePresenter.IProfileView{

	private final Widget widget;
	
	@UiField HTMLPanel container; 
	public interface Binder extends UiBinder<Widget, ProfileView> {
	}

	@Inject
	public ProfileView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		container.getElement().setAttribute("ui-view", "");
		container.getElement().setAttribute("autoscroll", "false");
		container.getElement().setAttribute("ng-class", "app.viewAnimation");
		
		
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

}
