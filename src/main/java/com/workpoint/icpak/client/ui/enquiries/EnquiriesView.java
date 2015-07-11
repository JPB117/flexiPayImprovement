package com.workpoint.icpak.client.ui.enquiries;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.component.ActionLink;

public class EnquiriesView extends ViewImpl implements
		EnquiriesPresenter.IEnquiriesView {

	private final Widget widget;

	@UiField
	HTMLPanel container;

	@UiField
	HTMLPanel panelListing;

	@UiField
	ActionLink aCreate;

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

	public HasClickHandlers getCreateButton() {
		return aCreate;
	}

}
