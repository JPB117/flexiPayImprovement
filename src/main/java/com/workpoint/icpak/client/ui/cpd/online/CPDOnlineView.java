package com.workpoint.icpak.client.ui.cpd.online;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.util.AppContext;

public class CPDOnlineView extends ViewImpl implements
		CPDOnlinePresenter.ICPDView {

	private final Widget widget;

	@UiField
	ActionLink aCpdOnline;

	public interface Binder extends UiBinder<Widget, CPDOnlineView> {
	}

	@Inject
	public CPDOnlineView(final Binder binder) {
		widget = binder.createAndBindUi(this);

		if (AppContext.getLoggedInCookie() != null) {
			aCpdOnline.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					String url = "http://www.icpak.com:8081/FrmLoginnew.aspx?ID="
							+ AppContext.getLoggedInCookie();
					Window.open(url, "", null);
				}
			});
		}

	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
