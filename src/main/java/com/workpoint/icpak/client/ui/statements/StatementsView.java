package com.workpoint.icpak.client.ui.statements;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class StatementsView extends ViewImpl implements StatementsPresenter.IStatementsView{

	private final Widget widget;
	
	@UiField HTMLPanel container;
	
	public interface Binder extends UiBinder<Widget, StatementsView> {
	}

	@Inject
	public StatementsView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

}
