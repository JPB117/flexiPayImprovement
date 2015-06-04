package com.workpoint.icpak.client.ui.admin.users.groups;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.workpoint.icpak.client.ui.AppManager;
import com.workpoint.icpak.client.ui.OnOptionSelected;
import com.workpoint.icpak.client.ui.events.EditGroupEvent;
import com.workpoint.icpak.shared.model.UserGroup;

public class GroupPresenter extends PresenterWidget<GroupPresenter.MyView>{

	public interface MyView extends View {
		void setValues(String code, String name);
		
		HasClickHandlers getEdit();
		
		HasClickHandlers getDelete();
	}

	UserGroup group;
	
	@Inject
	public GroupPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		
		getView().getEdit().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new EditGroupEvent(group));
			}
		});
		
		getView().getDelete().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				AppManager.showPopUp("Confirm Delete",new HTMLPanel("Do you want to delete group \""
						+group.getName()+"\""), new OnOptionSelected() {
							
							@Override
							public void onSelect(String name) {
								if(name.equals("Ok")){
									delete(group);
								}
							}

						},"Cancel","Ok");
				
				
			}
		});
	}
	
	protected void delete(UserGroup group) {
//		SaveGroupRequest request = new SaveGroupRequest(group);
//		request.setDelete(true);
//		requestHelper.execute(request, new TaskServiceCallback<SaveGroupResponse>() {
//			@Override
//			public void processResult(SaveGroupResponse result) {
//				getView().asWidget().removeFromParent();
//			}
//		});
	}

	public void setGroup(UserGroup group){
		this.group = group;
		getView().setValues(group.getName(), group.getFullName());
	}

}
