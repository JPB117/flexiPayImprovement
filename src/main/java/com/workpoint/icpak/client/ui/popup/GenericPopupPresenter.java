package com.workpoint.icpak.client.ui.popup;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.workpoint.icpak.client.ui.AppManager;

public class GenericPopupPresenter extends
		PresenterWidget<GenericPopupPresenter.MyView> {

	public interface MyView extends PopupView {

		void setHeader(String header);

		PopupPanel getPopUpPanel();

		void addStyleName(String customPopupStyle);

		void removeStyleName(String customPopupStyle);
	}

	@ContentSlot
	public static final Type<RevealContentHandler<?>> BODY_SLOT = new Type<RevealContentHandler<?>>();

	public static final Object BUTTON_SLOT = new Object();

	@Inject
	public GenericPopupPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);

	}

	@Override
	protected void onBind() {
		super.onBind();
	}

	public void center() {
		int[] position = AppManager.calculatePosition(20, 45);
		getView().getPopUpPanel().setPopupPosition(position[1], position[0]);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
	}

	public void setHeader(String header) {
		getView().setHeader(header);
	}
}
