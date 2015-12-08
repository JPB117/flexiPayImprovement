package com.workpoint.icpak.client.ui.users.item;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.icpak.rest.models.auth.User;
import com.workpoint.icpak.client.model.UploadContext;
import com.workpoint.icpak.client.model.UploadContext.UPLOADACTION;
import com.workpoint.icpak.client.service.AbstractAsyncCallback;
import com.workpoint.icpak.client.ui.AppManager;
import com.workpoint.icpak.client.ui.OnOptionSelected;
import com.workpoint.icpak.client.ui.OptionControl;
import com.workpoint.icpak.client.ui.cpd.table.row.CPDTableRow.TableActionType;
import com.workpoint.icpak.client.ui.events.EditModelEvent;
import com.workpoint.icpak.client.ui.events.EditUserEvent;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent;
import com.workpoint.icpak.client.ui.events.ProcessingEvent;
import com.workpoint.icpak.client.ui.events.TableActionEvent;
import com.workpoint.icpak.client.ui.events.TableActionEvent.TableActionHandler;
import com.workpoint.icpak.client.ui.users.item.statement.MemberStatementWidget;
import com.workpoint.icpak.shared.api.MemberResource;
import com.workpoint.icpak.shared.api.UsersResource;
import com.workpoint.icpak.shared.model.UserDto;

public class UserItemPresenter extends PresenterWidget<UserItemPresenter.MyView> implements TableActionHandler {

	public interface MyView extends View {
		void setValues(UserDto user);

		HasClickHandlers getEdit();

		HasClickHandlers getDelete();

		HasClickHandlers getStatementButton();

		HasClickHandlers getAlmsReview();

		String getMemberRefId();

		void forceRefresh();
	}

	UserDto user;
	private ResourceDelegate<UsersResource> usersDelegate;
	private ResourceDelegate<MemberResource> memberDelegate;

	@Inject
	public UserItemPresenter(final EventBus eventBus, final MyView view,
			final ResourceDelegate<UsersResource> usersDelegate, ResourceDelegate<MemberResource> memberDelegate) {
		super(eventBus, view);
		this.usersDelegate = usersDelegate;
		this.memberDelegate = memberDelegate;
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(TableActionEvent.TYPE, this);
		getView().getEdit().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new EditUserEvent(user));
			}
		});

		getView().getDelete().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AppManager.showPopUp("Confirm Delete",
						new HTMLPanel("Do you want to delete user \"" + user.getName() + "\""), new OnOptionSelected() {

					@Override
					public void onSelect(String name) {
						if (name.equals("Ok")) {
							delete(user);
						}
					}
				}, "Cancel", "Ok");

			}
		});

		getView().getAlmsReview().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				AppManager.showPopUp("LMS Review",
						new HTMLPanel((user.getLmsPayload() == null ? "No Payload Available." : user.getLmsPayload())),
						new OnOptionSelected() {

					@Override
					public void onSelect(String name) {
						if (name.equals("Resend")) {
							repost(user.getRefId());
						}
					}

				}, "Cancel", "Resend");
			}
		});

	}

	private void repost(String refId) {
		usersDelegate.withCallback(new AbstractAsyncCallback<UserDto>() {

			@Override
			public void onSuccess(UserDto result) {
				user = result;
				Window.alert("Success");
			}
		}).repostToLms(refId);
	}

	private void delete(UserDto user) {
		usersDelegate.withoutCallback().delete(user.getRefId());
		getView().asWidget().removeFromParent();
	}

	public void setUser(UserDto user) {
		this.user = user;
		getView().setValues(user);
	}

	@Override
	public void onTableAction(TableActionEvent event) {
		if (event.getAction() == TableActionType.ERPREFRESH) {
			if (!(event.getModel() instanceof UserDto)) {
				return;
			}
			UserDto dto = (UserDto) event.getModel();
			if (!(dto.getRefId().equals(user.getRefId()))) {
				return;
			}

			fireEvent(new ProcessingEvent());
			memberDelegate.withCallback(new AbstractAsyncCallback<Boolean>() {
				@Override
				public void onSuccess(Boolean hasLoaded) {
					fireEvent(new ProcessingCompletedEvent());
					if (!hasLoaded) {
						Window.alert("There was a problem loading ERP Data");
					} else {
						getView().forceRefresh();
					}
				}
			}).getDataFromErp(((UserDto) event.getModel()).getMemberRefId(), true);
		}
	}
}
