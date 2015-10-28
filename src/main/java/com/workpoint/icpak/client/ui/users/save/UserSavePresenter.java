package com.workpoint.icpak.client.ui.users.save;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.workpoint.icpak.client.service.AbstractAsyncCallback;
import com.workpoint.icpak.client.ui.AppManager;
import com.workpoint.icpak.client.ui.events.LoadGroupsEvent;
import com.workpoint.icpak.client.ui.events.LoadUsersEvent;
import com.workpoint.icpak.shared.api.RoleResource;
import com.workpoint.icpak.shared.api.UsersResource;
import com.workpoint.icpak.shared.model.RoleDto;
import com.workpoint.icpak.shared.model.UserDto;

public class UserSavePresenter extends
		PresenterWidget<UserSavePresenter.IUserSaveView> {

	public interface IUserSaveView extends PopupView {

		void setType(TYPE type);

		HasClickHandlers getSaveUser();

		HasClickHandlers getSaveGroup();

		boolean isValid();

		UserDto getUser();

		void setUser(UserDto user);

		RoleDto getGroup();

		void setGroup(RoleDto group);

		void setGroups(List<RoleDto> groups);

		PopupPanel getPopUpPanel();

		HasClickHandlers getaResetPassword();
		
		boolean isSendEmail();

	}

	public enum TYPE {
		GROUP, USER
	}

	TYPE type;

	UserDto user;

	RoleDto group;

	private ResourceDelegate<UsersResource> usersDelegate;

	private ResourceDelegate<RoleResource> rolesDelegate;

	@Inject
	public UserSavePresenter(final EventBus eventBus, final IUserSaveView view,
			final ResourceDelegate<UsersResource> usersDelegate,
			final ResourceDelegate<RoleResource> rolesDelegate) {
		super(eventBus, view);
		this.usersDelegate = usersDelegate;
		this.rolesDelegate = rolesDelegate;
	}

	@Override
	protected void onBind() {
		super.onBind();

		getView().getSaveUser().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (getView().isValid()) {
					UserDto userDto = getView().getUser();
					if (user != null) {

						usersDelegate.withCallback(
								new AbstractAsyncCallback<UserDto>() {
									@Override
									public void onSuccess(UserDto dto) {
										bindUser(dto);
									}
								}).update(user.getRefId(), userDto);
					} else {
						usersDelegate.withCallback(
								new AbstractAsyncCallback<UserDto>() {
									@Override
									public void onSuccess(UserDto dto) {
										bindUser(dto);
									}

								}).create(userDto,getView().isSendEmail());
					}
				}
			}
		});

		getView().getSaveGroup().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (getView().isValid()) {
					RoleDto userGroup = getView().getGroup();

					if (group != null) {

						rolesDelegate.withCallback(
								new AbstractAsyncCallback<RoleDto>() {
									public void onSuccess(RoleDto result) {
										bindGroup(result);
									}
									
								}).update(group.getRefId(), userGroup);
					} else {
						
						rolesDelegate.withCallback(
								new AbstractAsyncCallback<RoleDto>() {
									public void onSuccess(RoleDto result) {
										bindGroup(result);
									};
								}).create(userGroup);
					}
				}
			}
		});
		
		getView().getaResetPassword().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//Send Email to client to Reset password
			}
		});
	}

	private void bindUser(UserDto savedDto) {
		user = savedDto;
		getView().setUser(user);
		getView().hide();
		fireEvent(new LoadUsersEvent());
	}
	
	private void bindGroup(RoleDto savedRole) {
		group = savedRole;
		getView().setGroup(group);
		fireEvent(new LoadGroupsEvent());
		getView().hide();
	};

	@Override
	protected void onReveal() {
		super.onReveal();
		rolesDelegate.withCallback(new AbstractAsyncCallback<List<RoleDto>>() {
			@Override
			public void onSuccess(List<RoleDto> result) {
				getView().setGroups(result);
			}
		}).getAll(0, 100);
	}

	public void center() {
		int[] position = AppManager.calculatePosition(20, 45);
		getView().getPopUpPanel().setPopupPosition(position[1], position[0]);
	}

	public void setType(TYPE type, Object value) {
		this.type = type;
		getView().setType(type);
		if (value != null) {
			if (type == TYPE.USER) {
				user = (UserDto) value;
				getView().setUser(user);
			} else {
				group = (RoleDto) value;
				getView().setGroup(group);
			}
		}

	}
}