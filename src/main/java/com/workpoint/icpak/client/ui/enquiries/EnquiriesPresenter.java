package com.workpoint.icpak.client.ui.enquiries;

import java.util.List;

import org.apache.bcel.generic.ALOAD;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
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
import com.workpoint.icpak.client.ui.component.PagingConfig;
import com.workpoint.icpak.client.ui.component.PagingLoader;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.enquiries.form.CreateEnquiry;
import com.workpoint.icpak.client.ui.events.ProcessingEvent;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.security.LoginGateKeeper;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.api.MemberResource;
import com.workpoint.icpak.shared.model.EnquiriesDto;

public class EnquiriesPresenter
		extends
		Presenter<EnquiriesPresenter.IEnquiriesView, EnquiriesPresenter.IEnquiriesProxy> {

	private ResourceDelegate<MemberResource> membersDelegate;

	public interface IEnquiriesView extends View {
		HasClickHandlers getCreateButton();
		PagingPanel getPagingPanel();
		void bindEnquiries(List<EnquiriesDto> list);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.enquiries)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface IEnquiriesProxy extends
			TabContentProxyPlace<EnquiriesPresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(LoginGateKeeper adminGatekeeper) {
		TabDataExt data = new TabDataExt("Enquiries", "fa fa-bullhorn", 8,
				adminGatekeeper, true);
		return data;
	}

	@Inject
	public EnquiriesPresenter(final EventBus eventBus,
			final IEnquiriesView view, final IEnquiriesProxy proxy,
			ResourceDelegate<MemberResource> membersDelegate) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
		this.membersDelegate = membersDelegate;
	}

	@Override
	protected void onBind() {
		super.onBind();

		getView().getCreateButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showPopUp(false);
			}
		});
		
		getView().getPagingPanel().setLoader(new PagingLoader() {
			
			@Override
			public void load(int offset, int limit) {
				loadEnquiries(offset, limit);
			}
		});
	}
	
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		loadData();
		
	}

	private void loadData() {
		membersDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
			@Override
			public void onSuccess(Integer aCount) {
				PagingPanel panel = getView().getPagingPanel();
				panel.setTotal(aCount);
				PagingConfig config = panel.getConfig();
				loadEnquiries(config.getOffset(),config.getLimit());
			}
			
		}).enquiries(getMemberId())
		.getCount();
	}

	protected void loadEnquiries(int offset, int limit) {
		membersDelegate.withCallback(new AbstractAsyncCallback<List<EnquiriesDto>>() {
			@Override
			public void onSuccess(List<EnquiriesDto> list) {
				getView().bindEnquiries(list);
			}
			
		}).enquiries(getMemberId())
		.getAll(offset, limit);
	}

	protected void showPopUp(boolean edit) {
		if (edit) {
			// newEnquiry.setAccomodationDetails(newEnquiry);
		}

		final CreateEnquiry newEnquiry = new CreateEnquiry();
		AppManager.showPopUp("New Enquiry", newEnquiry.asWidget(),
				new OptionControl() {
					@Override
					public void onSelect(String name) {
						if (name.equals("Save")) {
							if (newEnquiry.isValid()) {
								hide();
								save(newEnquiry.getEnquiry());
							}
						}
					}
				}, "Save");
		
	}

	protected void save(EnquiriesDto dto) {
		fireEvent(new ProcessingEvent());
		
		
		membersDelegate.withCallback(new AbstractAsyncCallback<EnquiriesDto>() {
			@Override
			public void onSuccess(EnquiriesDto result) {
				PagingPanel panel = getView().getPagingPanel();
				PagingConfig config = panel.getConfig();
				loadEnquiries(config.getOffset(), config.getLimit());
			}
			
		}).enquiries(getMemberId())
		.create(dto);
		
	}
	
	private String getMemberId(){
		String memberId = AppContext.isCurrentUserAdmin()? "ALL":
			AppContext.getCurrentUser().getUser().getMemberRefId();
		
		return memberId;
	}

	@Override
	protected void onReset() {
		super.onReset();
	}

}
