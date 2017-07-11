package com.workpoint.icpak.client.ui.users.save;

import java.util.Arrays;
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
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.client.ui.component.TextArea;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.client.ui.component.autocomplete.MultiSelectField;
import com.workpoint.icpak.client.ui.upload.custom.Uploader;
import com.workpoint.icpak.client.ui.users.save.UserSavePresenter.TYPE;
import com.workpoint.icpak.shared.model.Listable;
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
	TextField txtMemberQrCode;
	@UiField
	DivElement divQrCode;

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
	DivElement divMemberNo;
	@UiField
	DivElement divGroups;
	@UiField
	Uploader uploader;
	@UiField
	MultiSelectField<RoleDto> lstGroups;

	@UiField
	DropDownList<UserCategory> lstUserType;
	UserDto user;
	TYPE type;
	private List<RoleDto> groups;

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

		lstUserType.setItems(Arrays.asList(UserCategory.values()));

		lstUserType
				.addValueChangeHandler(new ValueChangeHandler<UserSaveView.UserCategory>() {
					@Override
					public void onValueChange(
							ValueChangeEvent<UserCategory> event) {
						if (event.getValue() == UserCategory.MEMBER) {
							divMemberNo.removeClassName("hide");
							divGroups.addClassName("hide");
							divQrCode.removeClassName("hide");
						} else if (event.getValue() == UserCategory.STAFF) {
							divMemberNo.addClassName("hide");
							divGroups.removeClassName("hide");
							divQrCode.addClassName("hide");
						}
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
		if (txtFirstname.getValue() != null) {
			user.setName(txtFirstname.getValue());
		}

		if (txtLastname != null) {
			user.setSurname(txtLastname.getValue());
		}
		user.setPhoneNumber(txtPhoneNo.getValue());

		user.setFullName(txtFullNames.getValue());
		if (txtMemberNo.getValue() != null) {
			user.setMemberNo(txtMemberNo.getValue());
		}

		if (lstUserType.getValue() == UserCategory.STAFF) {
			user.setMemberNo(null);
		}

		if (lstUserType.getValue() == UserCategory.MEMBER) {
			lstGroups.clearSelection();
			RoleDto memberRole = getGroupByName("MEMBER");
			if (memberRole != null) {
				lstGroups.select(Arrays.asList(memberRole));
				user.setGroups(lstGroups.getSelectedItems());
			}
		} else {
			user.setGroups(lstGroups.getSelectedItems());
		}

		if (txtMemberQrCode != null) {
			user.setMemberQrCode(txtMemberQrCode.getValue());
		}
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
		txtMemberQrCode.setValue(user.getMemberQrCode());

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

		if (user.getMemberNo() != null) {
			lstUserType.setValue(UserCategory.MEMBER);
			divMemberNo.removeClassName("hide");
			divGroups.addClassName("hide");
			divQrCode.removeClassName("hide");
		} else {
			lstUserType.setValue(UserCategory.STAFF);
			divMemberNo.addClassName("hide");
			divGroups.removeClassName("hide");
			divQrCode.addClassName("hide");
		}

	}

	private boolean isUserValid() {
		issues.clear();
		boolean valid = true;

		// if (isNullOrEmpty(txtFirstname.getValue())) {
		// valid = false;
		// issues.addError("First Name is mandatory");
		// }
		//
		// if (isNullOrEmpty(txtLastname.getValue())) {
		// valid = false;
		// issues.addError("Last Name is mandatory");
		// }

		// if (isNullOrEmpty(txtMemberNo.getValue())) {
		// valid = false;
		// issues.addError("Member No is mandatory");
		// }

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
		this.groups = groups;
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

	public enum UserCategory implements Listable {
		MEMBER("MEMBER"), STAFF("STAFF/NEW APPLICANT");
		private String displayName;

		private UserCategory(String displayName) {
			this.displayName = displayName;
		}

		@Override
		public String getName() {
			return name();
		}

		@Override
		public String getDisplayName() {
			return displayName;
		}
	}

	public RoleDto getGroupByName(String groupName) {
		for (RoleDto group : groups) {
			if (groupName.equals(group.getName())) {
				return group;
			}
		}
		return null;
	}
}