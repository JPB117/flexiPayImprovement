package com.workpoint.icpak.client.ui.users.save;

import java.util.List;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewImpl;
import com.workpoint.icpak.client.model.UploadContext;
import com.workpoint.icpak.client.model.UploadContext.UPLOADACTION;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.client.ui.component.TextArea;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.ui.component.autocomplete.MultiSelectField;
import com.workpoint.icpak.client.ui.upload.custom.Uploader;
import com.workpoint.icpak.client.ui.users.save.UserSavePresenter.TYPE;
import com.workpoint.icpak.shared.model.RoleDto;
import com.workpoint.icpak.shared.model.UserDto;

public class UserSaveView extends PopupViewImpl implements
		UserSavePresenter.IUserSaveView {

	private final Widget widget;
	@UiField
	HTMLPanel divUserDetails;
	@UiField
	HTMLPanel divGroupDetails;
	@UiField
	IssuesPanel issues;
	@UiField
	Anchor aClose;

	// @UiField
	// TextField txtUserName;
	@UiField
	TextField txtFirstname;
	@UiField
	TextField txtLastname;
	@UiField
	TextField txtEmail;
	@UiField
	TextField txtPhoneNo;
	@UiField
	TextField txtFullNames;
	@UiField
	TextField txtMemberNo;

	@UiField
	TextField txtGroupname;
	@UiField
	TextArea txtDescription;

	@UiField
	ActionLink aResetPassword;

	@UiField
	DivElement divReset;

	@UiField
	CheckBox chkSendEmail;

	@UiField
	PopupPanel AddUserDialog;
	@UiField
	Anchor aSaveGroup;
	@UiField
	Anchor aSaveUser;

	@UiField
	HasClickHandlers aCancelUser;
	@UiField
	HasClickHandlers aCancelGroup;

	@UiField
	SpanElement header;

	@UiField
	DivElement divUserSave;
	@UiField
	Uploader uploader;
	@UiField
	MultiSelectField<RoleDto> lstGroups;

	UserDto user;

	TYPE type;

	public interface Binder extends UiBinder<Widget, UserSaveView> {
	}

	@Inject
	public UserSaveView(final EventBus eventBus, final Binder binder) {
		super(eventBus);
		widget = binder.createAndBindUi(this);
		aClose.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});

		aCancelGroup.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});

		aCancelUser.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});

		AddUserDialog.getElement().getStyle().setDisplay(Display.BLOCK);
		AddUserDialog.getElement().getStyle().setOverflowY(Overflow.AUTO);
		AddUserDialog.setGlassStyleName("modal-backdrop fade in");

		// ----Calculate the Size of Screen; To be Centralized later -----
		int height = Window.getClientHeight();
		int width = Window.getClientWidth();

		/* Percentage to the Height and Width */
		double height1 = (5.0 / 100.0) * height;
		double width1 = (50.0 / 100.0) * width;

		AddUserDialog.setPopupPosition((int) width1, (int) height1);

		txtEmail.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				setContext(event.getValue());
			}
		});

	}

	protected void setContext(String value) {
		UploadContext context = new UploadContext();
		context.setAction(UPLOADACTION.UPLOADUSERIMAGE);
		context.setContext("userId", value + "");
		context.setAccept("png,jpeg,jpg,gif");
		uploader.setContext(context);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public boolean isValid() {
		boolean isValid = true;
		switch (type) {
		case GROUP:
			isValid = isGroupValid();
			break;

		default:
			isValid = isUserValid();
			break;
		}

		if (!isValid) {
			issues.removeStyleName("hide");
		} else {
			issues.addStyleName("hide");
		}

		return isValid;
	}

	public RoleDto getGroup() {
		RoleDto group = new RoleDto();
		group.setFullName(txtDescription.getValue());
		group.setName(txtGroupname.getValue());

		return group;
	}

	public void setGroup(RoleDto group) {
		txtDescription.setValue(group.getFullName());
		txtGroupname.setValue(group.getName());
	}

	public UserDto getUser() {
		UserDto user = new UserDto();
		user.setEmail(txtEmail.getValue());
		user.setName(txtFirstname.getValue());
		user.setSurname(txtLastname.getValue());
		user.setPhoneNumber(txtPhoneNo.getValue());
		user.setGroups(lstGroups.getSelectedItems());
		user.setFullName(txtFullNames.getValue());
		user.setMemberNo(txtMemberNo.getValue());
		return user;
	}

	public void setUser(final UserDto user) {
		this.user = user;
		txtEmail.setValue(user.getEmail());
		txtFirstname.setValue(user.getName());
		txtLastname.setValue(user.getSurname());
		txtFullNames.setValue(user.getFullName());
		txtMemberNo.setValue(user.getMemberNo());
		txtPhoneNo.setValue(user.getPhoneNumber());
		lstGroups.select(user.getGroups());

		aResetPassword.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.open("#activateacc;uid=" + user.getRefId(),
						"Reset User Password", null);
			}
		});

		setContext(user.getRefId());
		if (user.getRefId() != null) {
			divReset.removeClassName("hide");
		}

	}

	private boolean isUserValid() {
		issues.clear();
		boolean valid = true;

		if (isNullOrEmpty(txtFirstname.getValue())) {
			valid = false;
			issues.addError("First Name is mandatory");
		}

		if (isNullOrEmpty(txtLastname.getValue())) {
			valid = false;
			issues.addError("First Name is mandatory");
		}

		if (isNullOrEmpty(txtEmail.getValue())) {
			valid = false;
			issues.addError("Email is mandatory");
		}

		return valid;
	}

	private boolean isGroupValid() {

		issues.clear();
		boolean valid = true;

		if (isNullOrEmpty(txtGroupname.getValue())) {
			valid = false;
			issues.addError("Group Name is mandatory");
		}

		return valid;
	}

	boolean isNullOrEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

	public HasClickHandlers getSaveUser() {
		return aSaveUser;
	}

	public HasClickHandlers getSaveGroup() {
		return aSaveGroup;
	}

	@Override
	public void setType(TYPE type) {
		this.type = type;
		if (type == TYPE.GROUP) {
			divGroupDetails.removeStyleName("hide");
			divUserDetails.addStyleName("hide");
			divUserSave.addClassName("hide");
			header.setInnerText("New Group");
		} else {
			divUserDetails.removeStyleName("hide");
			divGroupDetails.addStyleName("hide");
			divUserSave.removeClassName("hide");
			header.setInnerText("New User");
		}
	}

	@Override
	public void setGroups(List<RoleDto> groups) {
		lstGroups.addItems(groups);
	}

	@Override
	public PopupPanel getPopUpPanel() {
		return AddUserDialog;
	}

	public HasClickHandlers getaResetPassword() {
		return aResetPassword;
	}

	@Override
	public boolean isSendEmail() {
		return chkSendEmail.getValue();
	}
}