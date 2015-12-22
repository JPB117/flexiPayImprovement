package com.workpoint.icpak.client.ui.cpd.admin;

//import com.workpoint.icpak.shared.requests.CheckPasswordRequest;
//import com.workpoint.icpak.shared.requests.GetUserRequest;
//import com.workpoint.icpak.shared.requests.SaveUserRequest;
//import com.workpoint.icpak.shared.requests.UpdatePasswordRequest;
//import com.workpoint.icpak.shared.responses.CheckPasswordRequestResult;
//import com.workpoint.icpak.shared.responses.GetUserRequestResult;
//import com.workpoint.icpak.shared.responses.SaveUserResponse;
//import com.workpoint.icpak.shared.responses.UpdatePasswordResponse;
import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;

import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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
import com.workpoint.icpak.client.ui.cpd.table.row.CPDTableRow.TableActionType;
import com.workpoint.icpak.client.ui.events.EditModelEvent;
import com.workpoint.icpak.client.ui.events.EditModelEvent.EditModelHandler;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent;
import com.workpoint.icpak.client.ui.events.ProcessingEvent;
import com.workpoint.icpak.client.ui.events.TableActionEvent;
import com.workpoint.icpak.client.ui.events.TableActionEvent.TableActionHandler;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.popup.GenericPopupPresenter;
import com.workpoint.icpak.client.ui.security.AdminGateKeeper;
import com.workpoint.icpak.client.ui.security.LoginGateKeeper;
import com.workpoint.icpak.client.ui.util.DateRange;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.shared.api.MemberResource;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.shared.model.CPDSummaryDto;

public class CPDManagementPresenter
		extends
		Presenter<CPDManagementPresenter.ICPDManagementView, CPDManagementPresenter.ICPDManagementProxy>
		implements EditModelHandler, TableActionHandler {

	public interface ICPDManagementView extends View {
		HasClickHandlers getRecordButton();

		void bindResults(List<CPDDto> result);

		void showDetailedView();

		PagingPanel getPagingPanel();

		void bindSummary(CPDSummaryDto summary);

		void setInitialDates(DateRange thisyear, Date date);

		HasClickHandlers getFilterButton();

		Date getStartDate();

		Date getEndDate();

		HasValueChangeHandlers<String> getSearchValueChangeHander();

		HasKeyDownHandlers getTxtSearch();

		String getSearchValue();

	}

	@ProxyCodeSplit
	@NameToken(NameTokens.cpdmgt)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface ICPDManagementProxy extends
			TabContentProxyPlace<CPDManagementPresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(AdminGateKeeper adminGatekeeper) {
		TabDataExt data = new TabDataExt("C.P.D Management",
				"fa fa-graduation-cap", 5, adminGatekeeper, true);
		return data;
	}

	protected final ResourceDelegate<MemberResource> memberDelegate;
	protected final CurrentUser currentUser;

	ValueChangeHandler<String> cpdValueChangeHandler = new ValueChangeHandler<String>() {
		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
			searchCPD(getView().getSearchValue().trim());
		}
	};

	KeyDownHandler keyHandler = new KeyDownHandler() {
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				if (!isNullOrEmpty(getView().getSearchValue().trim())) {
					searchCPD(getView().getSearchValue().trim());
				}
			}
		}
	};

	@Inject
	public CPDManagementPresenter(final EventBus eventBus,
			final ICPDManagementView view, final ICPDManagementProxy proxy,
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

		getView().getPagingPanel().setLoader(new PagingLoader() {
			@Override
			public void onLoad(int offset, int limit) {
				onLoad(offset, limit);
			}
		});

		getView().getFilterButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				startDate = getView().getStartDate();
				endDate = getView().getEndDate();
				loadCPD(getView().getPagingPanel().getConfig().getOffset(),
						getView().getPagingPanel().getConfig().getLimit());
			}
		});

		getView().getSearchValueChangeHander().addValueChangeHandler(
				cpdValueChangeHandler);

		getView().getTxtSearch().addKeyDownHandler(keyHandler);
	}

	@Inject
	GenericPopupPresenter popup;
	private Date startDate;
	private Date endDate;

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
						String memberId = currentUser.getUser().getRefId();
						memberDelegate
								.withCallback(
										new AbstractAsyncCallback<CPDDto>() {
											@Override
											public void onSuccess(CPDDto result) {
												cpdRecord.setCPD(result);
												cpdRecord.showUploadPanel(true);
											}
										}).cpd(memberId)
								.create(cpdRecord.getCPD());
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
						} else {
							hide();
						}
					}
				}, (isViewMode == true ? "Cancel" : "Previous"),
				(isViewMode == true ? "Save" : "Save"));
	}

	protected void saveRecord(CPDDto dto) {
		fireEvent(new ProcessingEvent());
		if (dto.getRefId() != null) {
			memberDelegate.withCallback(new AbstractAsyncCallback<CPDDto>() {
				@Override
				public void onSuccess(CPDDto result) {
					loadData(startDate, endDate);
				}
			}).cpd(dto.getMemberRefId()).update(dto.getRefId(), dto);
		}
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

	private void searchCPD(final String searchTerm) {
		fireEvent(new ProcessingEvent());
		memberDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
			@Override
			public void onSuccess(Integer count) {
				// TODO Auto-generated method stub
				fireEvent(new ProcessingCompletedEvent());
				PagingPanel pagingPanel = getView().getPagingPanel();
				pagingPanel.setTotal(count);
				PagingConfig pagingConfig = pagingPanel.getConfig();
				pagingConfig.setPAGE_LIMIT(100);
				getCPDSearchResults(pagingConfig.getOffset(),
						pagingConfig.getLimit(), searchTerm);
			}
		}).cpd("ALL").getCPDsearchCount(searchTerm);
	}

	private void getCPDSearchResults(int offset, int limit, String searchTerm) {
		fireEvent(new ProcessingEvent());
		memberDelegate
				.withCallback(new AbstractAsyncCallback<List<CPDDto>>() {
					@Override
					public void onSuccess(List<CPDDto> result) {
						getView().bindResults(result);
						fireEvent(new ProcessingCompletedEvent());
					}
				})
				.cpd("ALL")
				.searchCPd(offset, limit, searchTerm,
						getView().getStartDate().getTime(),
						getView().getEndDate().getTime());
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		getView().setInitialDates(DateRange.THISYEAR, new Date());
		this.startDate = DateUtils.getDateByRange(DateRange.THISYEAR, false);
		this.endDate = new Date();
		loadData(startDate, endDate);
	}

	protected void loadData(Date startDate, Date endDate) {
		fireEvent(new ProcessingEvent());

		memberDelegate.withCallback(new AbstractAsyncCallback<CPDSummaryDto>() {
			@Override
			public void onSuccess(CPDSummaryDto summary) {
				getView().bindSummary(summary);
			}
		}).cpd("ALL").getCPDSummary(startDate.getTime(), endDate.getTime());

		memberDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
			@Override
			public void onSuccess(Integer aCount) {
				fireEvent(new ProcessingCompletedEvent());
				PagingPanel panel = getView().getPagingPanel();
				panel.setTotal(aCount);
				PagingConfig config = panel.getConfig();
				loadCPD(config.getOffset(), PagingConfig.PAGE_LIMIT);
			}
		}).cpd("ALL").getCount(startDate.getTime(), startDate.getTime());

	}

	protected void loadCPD(int offset, int limit) {
		fireEvent(new ProcessingEvent());
		memberDelegate.withCallback(new AbstractAsyncCallback<List<CPDDto>>() {
			@Override
			public void onSuccess(List<CPDDto> result) {
				fireEvent(new ProcessingCompletedEvent());
				getView().bindResults(result);

			}
		}).cpd("ALL")
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
	}

	@Override
	public void onTableAction(TableActionEvent event) {
		if (event.getAction() == TableActionType.APPROVECPD) {
			final CPDDto dto = (CPDDto) event.getModel();
			saveRecord(dto);
		} else if (event.getAction() == TableActionType.REJECTCPD) {
			final CPDDto dto = (CPDDto) event.getModel();
			saveRecord(dto);
		} else if (event.getAction() == TableActionType.VIEWCPD) {
			final CPDDto dto = (CPDDto) event.getModel();
			showForm(dto, true);
		}
	}

}
