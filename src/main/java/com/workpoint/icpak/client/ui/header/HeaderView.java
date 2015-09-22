package com.workpoint.icpak.client.ui.header;

import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.shared.model.UserDto;

public class HeaderView extends ViewImpl implements HeaderPresenter.IHeaderView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, HeaderView> {
	}

	boolean isSelected = false;

	@UiField
	Anchor aLogout;

	@UiField
	SpanElement spnLoggedInUser;

	@Inject
	public HeaderView(final Binder binder) {
		widget = binder.createAndBindUi(this);

	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public HasClickHandlers getALogout() {
		return aLogout;
	}

	@Override
	public void setLoggedInUser(UserDto currentUser) {
		spnLoggedInUser.setInnerText(currentUser.getFullName()+
				(currentUser.getMemberNo()==null? "":
					" (Reg "+currentUser.getMemberNo()+")"));
	}

}
