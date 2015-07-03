package com.workpoint.icpak.client.ui.cpd;

import java.util.Arrays;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.component.tabs.TabContent;
import com.workpoint.icpak.client.ui.component.tabs.TabHeader;
import com.workpoint.icpak.client.ui.component.tabs.TabPanel;
import com.workpoint.icpak.client.ui.component.tabs.TabPanel.TabPosition;
import com.workpoint.icpak.client.ui.cpd.confirmed.ConfirmedCPD;
import com.workpoint.icpak.client.ui.cpd.unconfirmed.UnconfirmedCPD;

public class CPDView extends ViewImpl implements CPDPresenter.ICPDView {

	private final Widget widget;

	@UiField
	TabPanel divTabs;

	@UiField
	HTMLPanel container;

	private ConfirmedCPD confirmedWidget;
	private UnconfirmedCPD unconfirmedWidget;

	public interface Binder extends UiBinder<Widget, CPDView> {
	}

	@Inject
	public CPDView(final Binder binder) {
		widget = binder.createAndBindUi(this);

		unconfirmedWidget = new UnconfirmedCPD();
		confirmedWidget = new ConfirmedCPD();

		 divTabs.setHeaders(Arrays.asList(
		 new TabHeader("Confirmed CPD", true,"confirmed_cpd"),
		 new TabHeader("Un-Confirmed CPD", false,"unconfirmed_cpd")
		 ));

		 divTabs.setContent(Arrays.asList(
		 new TabContent(unconfirmedWidget,"confirmed_cpd", true),
		 new TabContent(confirmedWidget,"unconfirmed_cpd", false)));

		divTabs.setPosition(TabPosition.PILLS);

	}

	@Override
	public Widget asWidget() {
		return widget;
	}

}
