package com.workpoint.icpak.client.ui.cpd;

import java.util.Date;
import java.util.List;

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
import com.workpoint.icpak.client.security.CurrentUser;
import com.workpoint.icpak.client.service.AbstractAsyncCallback;
import com.workpoint.icpak.client.ui.AppManager;
import com.workpoint.icpak.client.ui.OnOptionSelected;
import com.workpoint.icpak.client.ui.OptionControl;
import com.workpoint.icpak.client.ui.admin.TabDataExt;
import com.workpoint.icpak.client.ui.component.PagingConfig;
import com.workpoint.icpak.client.ui.component.PagingLoader;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.cpd.form.RecordCPD;
import com.workpoint.icpak.client.ui.events.EditModelEvent;
import com.workpoint.icpak.client.ui.events.EditModelEvent.EditModelHandler;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent;
import com.workpoint.icpak.client.ui.events.ProcessingEvent;
import com.workpoint.icpak.client.ui.events.TableActionEvent;
import com.workpoint.icpak.client.ui.events.TableActionEvent.TableActionHandler;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.security.MemberGateKeeper;
import com.workpoint.icpak.client.ui.util.DateRange;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.shared.api.MemberResource;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.shared.model.CPDStatus;
import com.workpoint.icpak.shared.model.CPDSummaryDto;
import com.workpoint.icpak.shared.model.TableActionType;

public class CPDMemberPresenter extends
		Presenter<CPDMemberPresenter.ICPDView, CPDMemberPresenter.ICPDProxy>
		implements EditModelHandler, TableActionHandler {

	public interface ICPDView extends View {
		HasClickHandlers getRecordButton();

		void bindResults(List<CPDDto> result);

		void showDetailedView();

		PagingPanel getPagingPanel();

		void bindSummary(CPDSummaryDto summary);

		void setInitialDates(DateRange thisquarter, Date date);

		HasClickHandlers getFilterButton();

		Date getStartDate();

		Date getEndDate();

	}

	@ProxyCodeSplit
	@NameToken(NameTokens.cpd)
	@UseGatekeeper(MemberGateKeeper.class)
	public interface ICPDProxy extends TabContentProxyPlace<CPDMemberPresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(MemberGateKeeper adminGatekeeper) {
		TabDataExt data = new TabDataExt("My C.P.D", "fa fa-graduation-cap", 5,
				adminGatekeeper, true);
		return data;
	}

	protected final ResourceDelegate<MemberResource> memberDelegate;
	protected final CurrentUser currentUser;
	private Date startDate;
	private Date endDate;

	@Inject
	public CPDMemberPresenter(final EventBus eventBus, final ICPDView view,
			final ICPDProxy proxy,
			final ResourceDelegate<MemberResource> memberDelegate,
			final CurrentUser currentUser) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
		this.memberDelegate = memberDelegate;
		this.currentUser = currentUser;
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(EditModelEvent.TYPE, this);
		addRegisteredHandler(TableActionEvent.TYPE, this);

		getView().getRecordButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showCreatePopup();
			}
		});

		getView().getPagingPanel().setLoader(new PagingLoader() {
			@Override
			public void onLoad(int offset, int limit) {
				loadCPD(offset, limit);
			}
		});

		getView().getFilterButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				startDate = getView().getStartDate();
				endDate = getView().getEndDate();
				loadData(startDate, endDate);
			}
		});

	}

	protected void showCreatePopup() {
		showInstructions();
	}

	private void showInstructions() {
		showInstructions(null);
	}

	private void showInstructions(final CPDDto model) {
		final RecordCPD cpdRecord = new RecordCPD();
		cpdRecord.setCPD(model);
		cpdRecord.showForm(false);
		AppManager.showPopUp("Record CPD Wizard", cpdRecord.asWidget(),
				new OnOptionSelected() {
					@Override
					public void onSelect(String name) {
						if (name.equals("Next")) {
							showForm(model);
						}
					}
				}, "Next");
	}

	protected void showForm() {
		showForm(null);
	}

	protected void showForm(final CPDDto model) {
		showForm(model, false);
	}

	protected void showForm(final CPDDto model, boolean isViewMode) {
		final RecordCPD cpdRecord = new RecordCPD();
		cpdRecord.setCPD(model);

		cpdRecord.getStartUploadButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (cpdRecord.getCPD().getRefId() == null) {
					// not saved
					if (cpdRecord.isValid()) {
						CPDDto cpd = cpdRecord.getCPD();
						String memberId = currentUser.getUser().getRefId();
						String memberRegistrationNo = currentUser.getUser()
								.getMemberNo();
						cpd.setMemberRegistrationNo(memberRegistrationNo);
						memberDelegate
								.withCallback(
										new AbstractAsyncCallback<CPDDto>() {
											@Override
											public void onSuccess(CPDDto result) {
												cpdRecord.setCPD(result);
												cpdRecord.showUploadPanel(true);
											}
										}).cpd(memberId).create(cpd);
					}
				} else {
					cpdRecord.showUploadPanel(true);
				}
			}
		});
		cpdRecord.showForm(true);
		cpdRecord.setViewMode(isViewMode);

		AppManager.showPopUp("Record CPD Wizard", cpdRecord.asWidget(),
				new OptionControl() {
					@Override
					public void onSelect(String name) {
						if (name.equals("Save")) {
							if (cpdRecord.isValid()) {
								saveRecord(cpdRecord.getCPD());
								hide();
							}
						} else if (name.equals("Previous")) {
							showInstructions(model);
						} else if (name.equals("Approve")) {
							CPDDto dto = cpdRecord.getCPD();
							dto.setStatus(CPDStatus.Approved);
						} else if (name.equals("Reject")) {
							CPDDto dto = cpdRecord.getCPD();
							dto.setStatus(CPDStatus.Rejected);
						}
					}
				}, (isViewMode == true ? "Reject" : "Previous"),
				(isViewMode == true ? "Approve" : "Save"));
	}

	protected void saveRecord(CPDDto dto) {
		String memberId = currentUser.getUser().getRefId();
		dto.setMemberRegistrationNo(currentUser.getUser().getMemberNo());
		if (dto.getRefId() != null) {
			memberDelegate.withCallback(new AbstractAsyncCallback<CPDDto>() {
				@Override
				public void onSuccess(CPDDto result) {
					loadData(startDate, endDate);
				}
			}).cpd(memberId).update(dto.getRefId(), dto);

		} else {
			memberDelegate.withCallback(new AbstractAsyncCallback<CPDDto>() {
				@Override
				public void onSuccess(CPDDto result) {
					loadData(startDate, endDate);
				}
			}).cpd(memberId).create(dto);
		}

	}

	@Override
	protected void onReveal() {
		super.onReveal();
		getView().setInitialDates(DateRange.THISYEAR, new Date());
		this.startDate = DateUtils.getDateByRange(DateRange.THISYEAR, false);
		this.endDate = new Date();
		loadData(startDate, new Date());
	}

	protected void loadData(Date startDate, Date endDate) {
		String memberId = currentUser.getUser().getRefId();
		fireEvent(new ProcessingEvent());

		memberDelegate.withCallback(new AbstractAsyncCallback<CPDSummaryDto>() {
			@Override
			public void onSuccess(CPDSummaryDto summary) {
				getView().bindSummary(summary);
			}
		}).cpd(memberId).getCPDSummary(startDate.getTime(), endDate.getTime());

		memberDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
			@Override
			public void onSuccess(Integer aCount) {
				fireEvent(new ProcessingCompletedEvent());
				PagingPanel panel = getView().getPagingPanel();
				panel.setTotal(aCount);
				PagingConfig config = panel.getConfig();

				loadCPD(config.getOffset(), config.getLimit());
			}
		}).cpd(memberId).getCount(startDate.getTime(), endDate.getTime());
	}

	protected void loadCPD(int offset, int limit) {
		String memberId = currentUser.getUser().getRefId();
		fireEvent(new ProcessingEvent());
		memberDelegate.withCallback(new AbstractAsyncCallback<List<CPDDto>>() {
			@Override
			public void onSuccess(List<CPDDto> result) {
				fireEvent(new ProcessingCompletedEvent());
				getView().bindResults(result);
			}
		}).cpd(AppContext.isCurrentUserAdmin() ? "ALL" : memberId)
				.getAll(offset, limit, startDate.getTime(), endDate.getTime());
	}

	String getApplicationRefId() {
		String applicationRefId = currentUser.getUser() == null ? null
				: currentUser.getUser().getApplicationRefId();

		return applicationRefId;
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		getView().showDetailedView();
	}

	@Override
	public void onEditModel(EditModelEvent event) {
		if (event.getModel() instanceof CPDDto) {
			if (event.isDelete()) {
				delete((CPDDto) event.getModel());
			} else {
				showForm((CPDDto) event.getModel());
			}
		}
	}

	private void delete(CPDDto model) {
		String memberId = currentUser.getUser().getRefId();
		String cpdId = model.getRefId();
		memberDelegate.withCallback(new AbstractAsyncCallback<CPDDto>() {
			@Override
			public void onSuccess(CPDDto arg0) {
				loadData(startDate, endDate);
			}
		}).cpd(memberId).delete(cpdId);
	}

	@Override
	public void onTableAction(TableActionEvent event) {
		if (event.getAction() == TableActionType.DELETECPD) {
			final CPDDto dto = (CPDDto) event.getModel();
			OptionControl control = new OptionControl() {
				@Override
				public void onSelect(String name) {
					if (name.equals("Confirm")) {
						delete(dto);
					} else if (name.equals("Cancel")) {
						hide();
					}
					hide();
				}
			};
			AppManager.showPopUp("Confirm Delete of CPD",
					"Are you sure that you want to delete " + dto.getTitle(),
					control, "Confirm", "Cancel");
		}
	}
}
