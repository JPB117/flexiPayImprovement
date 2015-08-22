package com.workpoint.icpak.client.ui.users;

import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
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
import com.workpoint.icpak.client.service.ServiceCallback;
import com.workpoint.icpak.client.ui.admin.TabDataExt;
import com.workpoint.icpak.client.ui.component.PagingConfig;
import com.workpoint.icpak.client.ui.component.PagingLoader;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.events.EditGroupEvent;
import com.workpoint.icpak.client.ui.events.EditGroupEvent.EditGroupHandler;
import com.workpoint.icpak.client.ui.events.EditUserEvent;
import com.workpoint.icpak.client.ui.events.EditUserEvent.EditUserHandler;
import com.workpoint.icpak.client.ui.events.LoadGroupsEvent;
import com.workpoint.icpak.client.ui.events.LoadGroupsEvent.LoadGroupsHandler;
import com.workpoint.icpak.client.ui.events.LoadUsersEvent;
import com.workpoint.icpak.client.ui.events.LoadUsersEvent.LoadUsersHandler;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent;
import com.workpoint.icpak.client.ui.events.ProcessingEvent;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.security.AdminGateKeeper;
import com.workpoint.icpak.client.ui.users.groups.GroupPresenter;
import com.workpoint.icpak.client.ui.users.item.UserItemPresenter;
import com.workpoint.icpak.client.ui.users.save.UserSavePresenter;
import com.workpoint.icpak.client.ui.users.save.UserSavePresenter.TYPE;
import com.workpoint.icpak.shared.api.RoleResource;
import com.workpoint.icpak.shared.api.UsersResource;
import com.workpoint.icpak.shared.model.RoleDto;
import com.workpoint.icpak.shared.model.UserDto;

public class UserPresenter extends
		Presenter<UserPresenter.MyView, UserPresenter.MyProxy> implements
		EditUserHandler, LoadUsersHandler, LoadGroupsHandler, EditGroupHandler {

	public interface MyView extends View {

		HasClickHandlers getaNewUser();

		HasClickHandlers getaNewGroup();

		void setType(TYPE type);

		PagingPanel getPagingPanel();

		HasKeyDownHandlers getTxtSearch();

		String getSearchText();
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.usermgt)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface MyProxy extends TabContentProxyPlace<UserPresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(AdminGateKeeper adminGatekeeper) {
		return new TabDataExt("Users and Groups", "fa fa-globe", 3,
				adminGatekeeper, true);
	}

	public static final Object ITEMSLOT = new Object();
	public static final Object GROUPSLOT = new Object();

	IndirectProvider<UserSavePresenter> userFactory;
	IndirectProvider<UserItemPresenter> userItemFactory;
	IndirectProvider<GroupPresenter> groupFactory;

	TYPE type = TYPE.USER;
	private ResourceDelegate<UsersResource> usersDelegate;
	private ResourceDelegate<RoleResource> roleResource;

	@Inject
	public UserPresenter(final EventBus eventBus, final MyView view,
			MyProxy proxy, Provider<UserSavePresenter> addUserProvider,
			Provider<UserItemPresenter> itemProvider,
			Provider<GroupPresenter> groupProvider,
			ResourceDelegate<UsersResource> usersDelegate,
			ResourceDelegate<RoleResource> roleResource) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
		this.usersDelegate = usersDelegate;
		this.roleResource = roleResource;
		userFactory = new StandardProvider<UserSavePresenter>(addUserProvider);
		userItemFactory = new StandardProvider<UserItemPresenter>(itemProvider);
		groupFactory = new StandardProvider<GroupPresenter>(groupProvider);
	}

	KeyDownHandler keyHandler = new KeyDownHandler() {
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				if (!isNullOrEmpty(getView().getSearchText())) {
					loadUsers(getView().getSearchText());
				}
			}
		}
	};

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(EditUserEvent.TYPE, this);
		addRegisteredHandler(LoadUsersEvent.TYPE, this);
		addRegisteredHandler(LoadGroupsEvent.TYPE, this);
		addRegisteredHandler(EditGroupEvent.TYPE, this);

		getView().getTxtSearch().addKeyDownHandler(keyHandler);

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

		getView().getPagingPanel().setLoader(new PagingLoader() {

			@Override
			public void load(int offset, int limit) {
				loadUsers(offset, limit, "");
			}
		});
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);

		String page = request.getParameter("p", "USER").toUpperCase();
		try {
			type = TYPE.valueOf(page);
		} catch (Exception e) {
		}

		if (type == null) {
			type = TYPE.USER;
		}

		setType(type);
		loadData();
	}

	private void showPopup(final UserSavePresenter.TYPE type) {
		showPopup(type, null);
	}

	private void showPopup(final UserSavePresenter.TYPE type, final Object obj) {
		userFactory.get(new ServiceCallback<UserSavePresenter>() {
			@Override
			public void processResult(UserSavePresenter result) {
				result.center();
				result.setType(type, obj);
				addToPopupSlot(result, false);
			}
		});

	}

	void loadData() {
		if (type == TYPE.USER) {
			loadUsers("");
		} else {
			loadGroups();
		}
	}

	private void loadGroups() {

		fireEvent(new ProcessingEvent());
		roleResource.withCallback(new AbstractAsyncCallback<List<RoleDto>>() {
			@Override
			public void onSuccess(List<RoleDto> result) {
				loadGroups(result);
				fireEvent(new ProcessingCompletedEvent());
			}
		}).getAll(0, 100);
	}

	protected void loadGroups(List<RoleDto> groups) {
		setInSlot(GROUPSLOT, null);
		for (final RoleDto group : groups) {
			groupFactory.get(new ServiceCallback<GroupPresenter>() {
				@Override
				public void processResult(GroupPresenter result) {
					result.setGroup(group);
					addToSlot(GROUPSLOT, result);
				}
			});
		}

	}

	private void loadUsers(final String searchTerm) {
		fireEvent(new ProcessingEvent());
		usersDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
			@Override
			public void onSuccess(Integer aCount) {
				fireEvent(new ProcessingCompletedEvent());
				PagingPanel panel = getView().getPagingPanel();
				panel.setTotal(aCount);
				PagingConfig config = panel.getConfig();

				loadUsers(config.getOffset(), config.getLimit(), searchTerm);
				fireEvent(new ProcessingCompletedEvent());
			}
		}).getCount();
	}

	protected void loadUsers(int offset, int limit, String searchTerm) {
		fireEvent(new ProcessingEvent());
		usersDelegate.withCallback(new AbstractAsyncCallback<List<UserDto>>() {
			@Override
			public void onSuccess(List<UserDto> result) {
				loadUsers(result);
				fireEvent(new ProcessingCompletedEvent());
			}
		}).getAll(offset, limit, searchTerm);
	}

	protected void loadUsers(List<UserDto> users) {
		setInSlot(ITEMSLOT, null);
		if (users != null)
			for (final UserDto user : users) {
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
		this.type = type;
		getView().setType(type);
	}

	@Override
	public void onEditGroup(EditGroupEvent event) {
		showPopup(type, event.getGroup());
	}

}
