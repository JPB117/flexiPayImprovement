package com.workpoint.icpak.client.ui.enquiries;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class EnquiriesView extends ViewImpl implements EnquiriesPresenter.IEnquiriesView{

	private final Widget widget;
	
	@UiField HTMLPanel container; 
	public interface Binder extends UiBinder<Widget, EnquiriesView> {
	}

	@Inject
	public EnquiriesView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

}
