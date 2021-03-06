package com.workpoint.icpak.client.ui;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class MainPageView extends ViewImpl implements MainPagePresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, MainPageView> {
	}

	@UiField
	HTMLPanel pHeader;
	@UiField
	HTMLPanel pContainer;
	@UiField
	HTMLPanel panelContent;

	@Inject
	public MainPageView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if (slot == MainPagePresenter.HEADER_content) {
			pHeader.clear();

			if (content != null) {
				pHeader.add(content);
			}
		} else if (slot == MainPagePresenter.CONTENT_SLOT) {
			pContainer.clear();

			if (content != null) {
				pContainer.add(content);
			}

		} else {
			super.setInSlot(slot, content);
		}
	}

	@Override
	public void showProcessing(boolean processing, String message) {

	}

	@Override
	public void setAlertVisible(String subject, String statement, String url) {
	}

	public void hideAlert() {
		// divAlert.addClassName("hidden");
	}

	@Override
	public void showDisconnectionMessage(String message) {
		if (message == null) {
			message = "Cannot connect to server....";
		}
	}

	@Override
	public void clearDisconnectionMsg() {
		// disconnectionText.addClassName("hide");
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		hidePreLoader();
	}

	private static native void hidePreLoader()/*-{
												alert("Called!");
												//$wnd.jQuery('#preLoader').addClass("hide");
												}-*/;

}
