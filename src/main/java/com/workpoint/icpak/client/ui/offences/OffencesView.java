package com.workpoint.icpak.client.ui.offences;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class OffencesView extends ViewImpl implements OffencesPresenter.IOffencesView{

	private final Widget widget;
	
	@UiField HTMLPanel container; 
	public interface Binder extends UiBinder<Widget, OffencesView> {
	}

	@Inject
	public OffencesView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

}
