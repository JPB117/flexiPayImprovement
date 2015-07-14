package com.workpoint.icpak.client.ui.users.item;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.workpoint.icpak.client.ui.AppManager;
import com.workpoint.icpak.client.ui.OnOptionSelected;
import com.workpoint.icpak.client.ui.events.EditUserEvent;
import com.workpoint.icpak.shared.api.UsersResource;
import com.workpoint.icpak.shared.model.UserDto;

public class UserItemPresenter extends PresenterWidget<UserItemPresenter.MyView> {

	public interface MyView extends View {
		void setValues(String firstName, String lastName, String username, String email, String groups,
				 int inbox,int participated, int total);
		
		HasClickHandlers getEdit();
		
		HasClickHandlers getDelete();
	}

	UserDto user;
	private ResourceDelegate<UsersResource> usersDelegate;
	
	@Inject
	public UserItemPresenter(final EventBus eventBus, final MyView view,
			final ResourceDelegate<UsersResource> usersDelegate) {
		super(eventBus, view);
		this.usersDelegate = usersDelegate;
	}

	@Override
	protected void onBind() {
		super.onBind();
		
		getView().getEdit().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new EditUserEvent(user));
			}
		});
		
		getView().getDelete().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				AppManager.showPopUp("Confirm Delete",new HTMLPanel("Do you want to delete user \""
				+user.getName()+"\""), new OnOptionSelected() {
					
					@Override
					public void onSelect(String name) {
						if(name.equals("Ok")){
							delete(user);
						}
					}

				},"Cancel","Ok");
				
			}
		});
	}

	private void delete(UserDto user) {

		usersDelegate.withoutCallback().delete(user.getRefId());
		getView().asWidget().removeFromParent();
		
//		SaveUserRequest request = new SaveUserRequest(user);
//		request.setDelete(true);
//		requestHelper.execute(request, new TaskServiceCallback<SaveUserResponse>() {
//			@Override
//			public void processResult(SaveUserResponse result) {
//				getView().asWidget().removeFromParent();
//			}
//		});
	}
	
	
	public void setUser(UserDto user){
		this.user = user;
		getView().setValues(user.getName(), user.getSurname(), user.getEmail(),
				user.getEmail(), user.getGroupsAsString(), 
				user.getInbox(), user.getParticipated(), user.getTotal());
	}
}
