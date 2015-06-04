package com.workpoint.icpak.client.ui.admin.users;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
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
import com.workpoint.icpak.client.service.ServiceCallback;
import com.workpoint.icpak.client.ui.admin.AdminHomePresenter;
import com.workpoint.icpak.client.ui.admin.TabDataExt;
import com.workpoint.icpak.client.ui.admin.users.groups.GroupPresenter;
import com.workpoint.icpak.client.ui.admin.users.item.UserItemPresenter;
import com.workpoint.icpak.client.ui.admin.users.save.UserSavePresenter;
import com.workpoint.icpak.client.ui.admin.users.save.UserSavePresenter.TYPE;
import com.workpoint.icpak.client.ui.events.EditGroupEvent;
import com.workpoint.icpak.client.ui.events.EditGroupEvent.EditGroupHandler;
import com.workpoint.icpak.client.ui.events.EditUserEvent;
import com.workpoint.icpak.client.ui.events.EditUserEvent.EditUserHandler;
import com.workpoint.icpak.client.ui.events.LoadGroupsEvent;
import com.workpoint.icpak.client.ui.events.LoadGroupsEvent.LoadGroupsHandler;
import com.workpoint.icpak.client.ui.events.LoadUsersEvent;
import com.workpoint.icpak.client.ui.events.LoadUsersEvent.LoadUsersHandler;
import com.workpoint.icpak.client.ui.security.AdminGateKeeper;
import com.workpoint.icpak.shared.model.UserDto;
import com.workpoint.icpak.shared.model.UserGroup;

public class UserPresenter extends Presenter<UserPresenter.MyView, UserPresenter.MyProxy> 
implements EditUserHandler, LoadUsersHandler, LoadGroupsHandler, EditGroupHandler{

		
	public interface MyView extends View {

		HasClickHandlers getaNewUser();
		HasClickHandlers getaNewGroup();
		void setType(TYPE type);
	}
	
	@ProxyCodeSplit
	@NameToken(NameTokens.usermgt)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface MyProxy extends TabContentProxyPlace<UserPresenter> {
	}
	
	@TabInfo(container = AdminHomePresenter.class)
    static TabData getTabLabel(AdminGateKeeper adminGatekeeper) {
        return new TabDataExt("Users and Groups","icon-group",3, adminGatekeeper);
    }
	
	public static final Object ITEMSLOT = new Object();
	public static final Object GROUPSLOT = new Object();
	
	IndirectProvider<UserSavePresenter> userFactory;
	IndirectProvider<UserItemPresenter> userItemFactory;
	IndirectProvider<GroupPresenter> groupFactory;

	TYPE type = TYPE.USER;

	@Inject
	public UserPresenter(final EventBus eventBus, final MyView view,MyProxy proxy,
			Provider<UserSavePresenter> addUserProvider,
			Provider<UserItemPresenter> itemProvider,
			Provider<GroupPresenter> groupProvider) {
		super(eventBus, view, proxy,AdminHomePresenter.SLOT_SetTabContent);
		userFactory = new StandardProvider<UserSavePresenter>(addUserProvider);
		userItemFactory = new StandardProvider<UserItemPresenter>(itemProvider);
		groupFactory = new StandardProvider<GroupPresenter>(groupProvider); 
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(EditUserEvent.TYPE, this);
		addRegisteredHandler(LoadUsersEvent.TYPE, this);
		addRegisteredHandler(LoadGroupsEvent.TYPE, this);
		addRegisteredHandler(EditGroupEvent.TYPE, this);
		
		getView().getaNewUser().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showPopup(TYPE.USER);
			}
		});
		
		getView().getaNewGroup().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showPopup(UserSavePresenter.TYPE.GROUP);
			}
		});
	}
	
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		
		String page = request.getParameter("p", "USER").toUpperCase();
		try{
			type=TYPE.valueOf(page);
		}catch(Exception e){}
		
		if(type==null){
			type = TYPE.USER;
		}
		
		setType(type);
		loadData();
	}
	
	private void showPopup(final UserSavePresenter.TYPE type){
		showPopup(type, null);
	}
	
	private void showPopup(final UserSavePresenter.TYPE type, final Object obj) {
		userFactory.get(new ServiceCallback<UserSavePresenter>() {
			@Override
			public void processResult(UserSavePresenter result) {
				result.setType(type, obj);
				addToPopupSlot(result,false);
			}
		});
			
	}
	
	void loadData(){
		if(type==TYPE.USER){
			loadUsers();
		}else{
			loadGroups();
		}
	}
	
	private void loadGroups() {
//		GetGroupsRequest request = new GetGroupsRequest();
//		fireEvent(new ProcessingEvent());
//		requestHelper.execute(request, new TaskServiceCallback<GetGroupsResponse>() {
//			@Override
//			public void processResult(GetGroupsResponse result) {
//				List<UserGroup> groups = result.getGroups();
//				loadGroups(groups);
//				fireEvent(new ProcessingCompletedEvent());
//			}
//		});
	}

	protected void loadGroups(List<UserGroup> groups) {
		setInSlot(GROUPSLOT, null);
		for(final UserGroup group: groups){
			groupFactory.get(new ServiceCallback<GroupPresenter>() {
				@Override
				public void processResult(GroupPresenter result) {
					result.setGroup(group);
					addToSlot(GROUPSLOT, result);
				}
			});
		}
		
	}

	private void loadUsers() {
//		GetUsersRequest request = new GetUsersRequest();
//		fireEvent(new ProcessingEvent());
//		requestHelper.execute(request, new TaskServiceCallback<GetUsersResponse>() {
//			@Override
//			public void processResult(GetUsersResponse result) {
//				List<User> users = result.getUsers();
//				loadUsers(users);
//				fireEvent(new ProcessingCompletedEvent());
//			}
//		});
	}

	protected void loadUsers(List<UserDto> users) {
		setInSlot(ITEMSLOT, null);
		if(users!=null)
			for(final UserDto user: users){
				userItemFactory.get(new ServiceCallback<UserItemPresenter>() {
					@Override
					public void processResult(UserItemPresenter result) {
						result.setUser(user);
						addToSlot(ITEMSLOT, result);
					}
				});
			}
	}

	@Override
	public void onEditUser(EditUserEvent event) {
		showPopup(TYPE.USER, event.getUser());
	}

	@Override
	public void onLoadUsers(LoadUsersEvent event) {
		loadData();
	}
	
	@Override
	public void onLoadGroups(LoadGroupsEvent event) {
		loadData();
	}

	public void setType(TYPE type) {
		this.type=type;
		getView().setType(type);
	}

	@Override
	public void onEditGroup(EditGroupEvent event) {
		showPopup(type, event.getGroup());
	}

}
