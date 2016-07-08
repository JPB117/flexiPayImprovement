package com.workpoint.icpak.client.ui.events.bookings;

import java.util.List;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.workpoint.icpak.client.place.NameTokens;
import com.workpoint.icpak.client.service.AbstractAsyncCallback;
import com.workpoint.icpak.client.ui.AppManager;
import com.workpoint.icpak.client.ui.OptionControl;
import com.workpoint.icpak.client.ui.admin.TabDataExt;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent;
import com.workpoint.icpak.client.ui.events.ProcessingEvent;
import com.workpoint.icpak.client.ui.events.ToggleSideBarEvent;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.security.MemberGateKeeper;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.api.MemberResource;
import com.workpoint.icpak.shared.model.events.MemberBookingDto;

public class BookingsPresenter
		extends
		Presenter<BookingsPresenter.IBookingsView, BookingsPresenter.IBookingsProxy> {

	private ResourceDelegate<MemberResource> membersDelegate;

	public interface IBookingsView extends View {
		void bindBookings(List<MemberBookingDto> result);
		// PagingPanel getBookingsPagingPanel();
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.bookings)
	@UseGatekeeper(MemberGateKeeper.class)
	public interface IBookingsProxy extends
			TabContentProxyPlace<BookingsPresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(MemberGateKeeper gateKeeper) {
		String tabName = "My Bookings";
		TabDataExt data = new TabDataExt(tabName, "fa fa-tags", 2, gateKeeper,
				true);
		return data;
	}

	@Inject
	public BookingsPresenter(final EventBus eventBus, final IBookingsView view,
			final IBookingsProxy proxy,
			ResourceDelegate<MemberResource> membersDelegate) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
		this.membersDelegate = membersDelegate;
	}

	@Override
	protected void onBind() {
		super.onBind();
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		loadData();
		fireEvent(new ToggleSideBarEvent(false));
	}

	private void loadData() {
		String memberId = AppContext.getCurrentUser().getUser()
				.getMemberRefId();
		fireEvent(new ProcessingEvent());
		membersDelegate.withCallback(
				new AbstractAsyncCallback<List<MemberBookingDto>>() {
					@Override
					public void onSuccess(List<MemberBookingDto> result) {
						getView().bindBookings(result);
						fireEvent(new ProcessingCompletedEvent());
					}

					@Override
					public void onFailure(Throwable caught) {
						callPopOver();
						super.onFailure(caught);
					}

				}).getMemberBookings(memberId, 0, 100);
	}

	public void callPopOver() {
		AppManager.showPopUp("Sorry ....", "", new OptionControl() {

			@Override
			public void onSelect(String name) {
				super.onSelect(name);
				hide();
			}
		}, "Click here to proceed");
	}

}
