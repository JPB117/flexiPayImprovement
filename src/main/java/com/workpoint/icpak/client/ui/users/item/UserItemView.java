package com.workpoint.icpak.client.ui.users.item;

import java.util.Date;

import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.model.UploadContext;
import com.workpoint.icpak.client.model.UploadContext.UPLOADACTION;
import com.workpoint.icpak.client.ui.AppManager;
import com.workpoint.icpak.client.ui.OptionControl;
import com.workpoint.icpak.client.ui.cpd.table.row.CPDTableRow.TableActionType;
import com.workpoint.icpak.client.ui.events.TableActionEvent;
import com.workpoint.icpak.client.ui.users.item.statement.MemberStatementWidget;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.model.UserDto;

public class UserItemView extends ViewImpl implements UserItemPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, UserItemView> {
	}

	@UiField
	HTMLPanel panelFirstName;
	@UiField
	HTMLPanel panelLastName;
	@UiField
	HTMLPanel panelEmail;
	@UiField
	HTMLPanel panelGroups;
	@UiField
	HTMLPanel panelMemberNo;
	@UiField
	SpanElement spnLMSStatus;

	@UiField
	Anchor aEdit;
	@UiField
	Anchor aDelete;
	@UiField
	Anchor almsReview;

	@UiField
	Anchor aStatementDownload;

	@UiField
	Anchor aGoodStanding;
	private UserDto user;

	final MemberStatementWidget statementWidget = new MemberStatementWidget();

	@Inject
	public UserItemView(final Binder binder) {
		widget = binder.createAndBindUi(this);

	}

	@Override
	public void setValues(final UserDto user) {
		this.user = user;
		if (user.getMemberNo() != null) {
			panelMemberNo.getElement().setInnerText(user.getMemberNo());
		}

		if (user.getSurname() != null) {
			panelFirstName.getElement().setInnerText(user.getSurname());
		}

		if (user.getName() != null) {
			panelLastName.getElement().setInnerText(user.getName());
		}

		if (user.getEmail() != null) {
			panelEmail.getElement().setInnerText(user.getEmail());
		}

		if (user.getGroups() != null) {
			panelGroups.getElement().setInnerText(user.getGroupsAsString());
		}

		if (user.getLmsStatus() != null) {
			spnLMSStatus.setInnerText(user.getLmsStatus());
			if (user.getLmsStatus().equals("Success")) {
				spnLMSStatus.setClassName("label label-success popover-icon");
			} else {
				spnLMSStatus.setClassName("label label-danger popover-icon");
			}
			if (user.getLmsResponse() != null) {
				spnLMSStatus.setAttribute("data-content",
						"LMS Response::" + user.getLmsResponse()
								+ "<br/>LMS Payload::" + user.getLmsPayload());
			}
		}

		if (user.getMemberRefId() != null) {
			aStatementDownload.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					statementWidget.setLastUpdated(DateUtils.CREATEDFORMAT
							.format(user.getLastDateUpdateFromErp()));

					statementWidget.getRefreshButton().addClickHandler(
							new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									statementWidget.showLoading(true);
									AppContext.fireEvent(new TableActionEvent(
											user, TableActionType.ERPREFRESH));
								}
							});

					AppManager.showPopUp(
							"Generate Statements for " + user.getDisplayName()
									+ "-" + user.getMemberNo(),
							statementWidget, new OptionControl() {
								@Override
								public void onSelect(String name) {
									if (name.equals("Cancel")) {
										hide();
									} else {
										UploadContext ctx = new UploadContext(
												"getreport");
										if (statementWidget.getStartDate()
												.getValueDate() != null)
											ctx.setContext("startdate",
													statementWidget
															.getStartDate()
															.getValueDate()
															.getTime()
															+ "");
										if (statementWidget.getEndDate()
												.getValueDate() != null)
											ctx.setContext("enddate",
													statementWidget
															.getEndDate()
															.getValueDate()
															.getTime()
															+ "");
										ctx.setContext("memberRefId",
												user.getMemberRefId());
										ctx.setAction(UPLOADACTION.GETSTATEMENT);
										Window.open(ctx.toUrl(), "", null);
									}
								};
							}, "Generate", "Cancel");

				}
			});

			aGoodStanding.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					UploadContext ctx = new UploadContext("getreport");
					ctx.setAction(UPLOADACTION.DownloadCertGoodStanding);
					ctx.setContext("memberRefId", user.getMemberRefId());
					Window.open(ctx.toUrl(), "Certificate Of Good Standing", "");
				}
			});
		} else {
			aGoodStanding.setVisible(false);
			aStatementDownload.setVisible(false);
		}

	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public HasClickHandlers getEdit() {
		return aEdit;
	}

	public HasClickHandlers getDelete() {
		return aDelete;
	}
	
	public HasClickHandlers getAlmsReview() {
		return almsReview;
	}

	public HasClickHandlers getStatementButton() {
		return aStatementDownload;
	}

	public String getMemberRefId() {
		return user.getMemberRefId();
	}

	@Override
	public void forceRefresh() {
		statementWidget.setLastUpdated(DateUtils.CREATEDFORMAT
				.format(new Date()));
		statementWidget.showLoading(false);
	}
}