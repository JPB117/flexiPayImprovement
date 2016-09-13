package com.workpoint.icpak.client.ui.cpd.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
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
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.workpoint.icpak.client.model.UploadContext;
import com.workpoint.icpak.client.model.UploadContext.UPLOADACTION;
import com.workpoint.icpak.client.place.NameTokens;
import com.workpoint.icpak.client.security.CurrentUser;
import com.workpoint.icpak.client.service.AbstractAsyncCallback;
import com.workpoint.icpak.client.ui.AppManager;
import com.workpoint.icpak.client.ui.OptionControl;
import com.workpoint.icpak.client.ui.admin.TabDataExt;
import com.workpoint.icpak.client.ui.component.PagingConfig;
import com.workpoint.icpak.client.ui.component.PagingLoader;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.cpd.form.RecordCPD;
import com.workpoint.icpak.client.ui.cpd.member.table.CPDMemberTable;
import com.workpoint.icpak.client.ui.events.AfterSaveEvent;
import com.workpoint.icpak.client.ui.events.EditModelEvent;
import com.workpoint.icpak.client.ui.events.EditModelEvent.EditModelHandler;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent;
import com.workpoint.icpak.client.ui.events.ProcessingEvent;
import com.workpoint.icpak.client.ui.events.TableActionEvent;
import com.workpoint.icpak.client.ui.events.TableActionEvent.TableActionHandler;
import com.workpoint.icpak.client.ui.events.cpd.MemberCPDEvent;
import com.workpoint.icpak.client.ui.events.cpd.MemberCPDEvent.MemberCPDHandler;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.popup.GenericPopupPresenter;
import com.workpoint.icpak.client.ui.security.CPDManagementGateKeeper;
import com.workpoint.icpak.client.ui.util.DateRange;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.shared.api.MemberResource;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.shared.model.CPDFooterDto;
import com.workpoint.icpak.shared.model.CPDSummaryDto;
import com.workpoint.icpak.shared.model.MemberCPDDto;
import com.workpoint.icpak.shared.model.MemberDto;
import com.workpoint.icpak.shared.model.TableActionType;

public class CPDManagementPresenter
		extends Presenter<CPDManagementPresenter.ICPDManagementView, CPDManagementPresenter.ICPDManagementProxy>
		implements EditModelHandler, TableActionHandler, MemberCPDHandler {

	public interface ICPDManagementView extends View {
		void bindResults(List<CPDDto> result, String loadType);

		void showDetailedView();

		PagingPanel getReturnsPagingPanel();

		void bindSummary(CPDSummaryDto summary);

		void setInitialDates(DateRange thisyear, Date date);

		HasClickHandlers getFilterButton();

		Date getStartDate();

		Date getEndDate();

		HasValueChangeHandlers<String> getReturnsSearchValueChangeHander();

		HasValueChangeHandlers<String> getSummarySearchValueChangeHander();

		HasKeyDownHandlers getTxtSearch();

		String getReturnsSearchValue();

		void setTab(String page, String refId);

		void bindSingleResults(CPDDto result, String loadType);

		void bindMemberSummary(List<MemberCPDDto> result);

		PagingPanel getArchivePagingPanel();

		PagingPanel getCPDSummaryPagingPanel();

		void bindIndividualCPDFooter(List<CPDFooterDto> result);

		void bindIndividualResults(List<CPDDto> result);

		PagingPanel getIndividualMemberPagingPanel();

		void setIndividualMemberInitialDates(Date startDate, Date endDate);

		HasClickHandlers getRecordButton();

		HasKeyDownHandlers getMemberSummaryKeyDownHandler();

		String getMemberSummaryTxtSearch();

		HasKeyDownHandlers getArchiveSummaryKeyDownHandler();

		String getArchiveSummaryTxtSearch();

		CPDMemberTable getMemberSummaryTable();

		void setMemberFullNames(String fullNames);

		HasClickHandlers getBackButton();

		void bindMemberDetails(MemberDto member);

	}

	@ProxyCodeSplit
	@NameToken(NameTokens.cpdmgt)
	@UseGatekeeper(CPDManagementGateKeeper.class)
	public interface ICPDManagementProxy extends TabContentProxyPlace<CPDManagementPresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(CPDManagementGateKeeper adminGatekeeper) {
		TabDataExt data = new TabDataExt("C.P.D Management", "fa fa-graduation-cap", 5, adminGatekeeper, true);
		return data;
	}

	protected final ResourceDelegate<MemberResource> memberDelegate;
	protected final CurrentUser currentUser;
	private String recordMemberRefId;
	private String recordMemberNo;
	protected String searchTerm = "";
	@Inject
	GenericPopupPresenter popup;
	private Date startDate;
	private Date endDate;
	private String page;
	protected int pageLimit = 20;
	private String memberRefId = "";

	ValueChangeHandler<String> cpdValueChangeHandler = new ValueChangeHandler<String>() {
		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
			searchCPD(getView().getReturnsSearchValue().trim());
		}
	};

	KeyDownHandler keyHandler = new KeyDownHandler() {
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				// searchCPD(getView().getSearchValue().trim());
				Map<String, String> params = new HashMap<String, String>();
				params.put("p", "cpdReturns");
				params.put("searchTerm", getView().getReturnsSearchValue().trim());
				PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(NameTokens.cpdmgt).with(params)
						.build();
				placeManager.revealPlace(placeRequest);
			}
		}
	};

	KeyDownHandler memberSummaryKeyDownHandler = new KeyDownHandler() {
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				// loadCPDSummaryCount(getView().getMemberSummaryTxtSearch());
				Map<String, String> params = new HashMap<String, String>();
				params.put("p", "memberCPD");
				params.put("searchTerm", getView().getMemberSummaryTxtSearch());
				PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(NameTokens.cpdmgt).with(params)
						.build();
				placeManager.revealPlace(placeRequest);
			}
		}
	};

	KeyDownHandler archiveKeyDownHandler = new KeyDownHandler() {
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				// searchCPD(getView().getArchiveSummaryTxtSearch().trim());
				Map<String, String> params = new HashMap<String, String>();
				params.put("p", "returnArchive");
				params.put("searchTerm", getView().getArchiveSummaryTxtSearch().trim());
				PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(NameTokens.cpdmgt).with(params)
						.build();
				placeManager.revealPlace(placeRequest);
			}
		}
	};
	private PlaceManager placeManager;

	@Inject
	public CPDManagementPresenter(final EventBus eventBus, final ICPDManagementView view,
			final ICPDManagementProxy proxy, final ResourceDelegate<MemberResource> memberDelegate,
			final CurrentUser currentUser, final PlaceManager placeManager) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
		this.memberDelegate = memberDelegate;
		this.currentUser = currentUser;
		this.placeManager = placeManager;
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(EditModelEvent.TYPE, this);
		addRegisteredHandler(TableActionEvent.TYPE, this);
		addRegisteredHandler(MemberCPDEvent.getType(), this);

		getView().getRecordButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showCreatePopup();
			}
		});

		getView().getReturnsPagingPanel().setLoader(new PagingLoader() {
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
				loadCPD(getView().getReturnsPagingPanel().getConfig().getOffset(),
						getView().getReturnsPagingPanel().getConfig().getLimit(), "CPDRETURNS");
			}
		});

		getView().getTxtSearch().addKeyDownHandler(keyHandler);

		getView().getMemberSummaryKeyDownHandler().addKeyDownHandler(memberSummaryKeyDownHandler);

		getView().getArchiveSummaryKeyDownHandler().addKeyDownHandler(archiveKeyDownHandler);

		getView().getReturnsPagingPanel().setLoader(new PagingLoader() {
			@Override
			public void onLoad(int offset, int limit) {
				loadCPD(offset, pageLimit, page);
			}
		});

		getView().getArchivePagingPanel().setLoader(new PagingLoader() {
			@Override
			public void onLoad(int offset, int limit) {
				loadCPD(offset, pageLimit, page);
			}
		});

		getView().getCPDSummaryPagingPanel().setLoader(new PagingLoader() {
			@Override
			public void onLoad(int offset, int limit) {
				loadCPDSummaryData(searchTerm, offset, pageLimit);
			}
		});

		getView().getIndividualMemberPagingPanel().setLoader(new PagingLoader() {
			@Override
			public void onLoad(int offset, int limit) {
				loadIndividualMemberCPD(offset, pageLimit);
			}
		});

		getView().getMemberSummaryTable().getFilterButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				startDate = getView().getMemberSummaryTable().getStartDate();

				endDate = getView().getMemberSummaryTable().getEndDate();
				loadIndividualData(memberRefId, startDate, endDate);
			}
		});

		getView().getMemberSummaryTable().getDownloadButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (memberRefId != null) {
					UploadContext ctx = new UploadContext("getreport");
					if (getView().getMemberSummaryTable().getStartDate() != null)
						ctx.setContext("startdate", getView().getMemberSummaryTable().getStartDate().getTime() + "");
					if (getView().getMemberSummaryTable().getEndDate() != null)
						ctx.setContext("enddate", getView().getMemberSummaryTable().getEndDate().getTime() + "");
					ctx.setContext("memberRefId", memberRefId);
					ctx.setAction(UPLOADACTION.GETCPDSTATEMENT);
					Window.open(ctx.toUrl(), "", null);
				} else {
					Window.alert("Please enter a valid memberNo in the search box...");
				}
			}
		});

		getView().getBackButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.back();
			}
		});

	}

	protected void showCreatePopup() {
		final RecordCPD cpdRecord = new RecordCPD();
		// Upload Mechanism
		cpdRecord.getStartUploadButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (cpdRecord.getCPD().getRefId() == null) {
					if (cpdRecord.isValid()) {
						CPDDto cpd = cpdRecord.getCPD();
						String memberRefId = recordMemberRefId;
						String memberRegistrationNo = recordMemberNo;
						cpd.setMemberRegistrationNo(memberRegistrationNo);
						memberDelegate.withCallback(new AbstractAsyncCallback<CPDDto>() {
							@Override
							public void onSuccess(CPDDto result) {
								cpdRecord.setCPD(result);
								cpdRecord.showUploadPanel(true);
							}
						}).cpd(memberRefId).create(cpd);
					}
				} else {
					cpdRecord.showUploadPanel(true);
				}
			}
		});
		cpdRecord.showForm(true);
		cpdRecord.setAdminMode(false);
		cpdRecord.showMemberNoPanel(true);

		cpdRecord.getMemberNoKeyDownHandler().addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					cpdRecord.showMemberLoading(true);
					if (!cpdRecord.getMemberNoValue().equals("")) {
						memberDelegate.withCallback(new AbstractAsyncCallback<MemberDto>() {
							@Override
							public void onSuccess(MemberDto member) {
								cpdRecord.showMemberLoading(false);
								if (member.getRefId() != null) {
									cpdRecord.setFullNames(member.getFullName());
									recordMemberRefId = member.getRefId();
									recordMemberNo = member.getMemberNo();
									cpdRecord.showRecordingPanel(true);

								} else {
									cpdRecord.setFullNames("Member not found!");
								}
							}

							public void onFailure(Throwable caught) {
								cpdRecord.showMemberLoading(false);
								cpdRecord.setFullNames("Member not found!");
							};

						}).searchIndividualByMemberNo(cpdRecord.getMemberNoValue());
					}
				}
			}
		});

		AppManager.showPopUp("Record CPD Wizard", cpdRecord.asWidget(), new OptionControl() {
			@Override
			public void onSelect(String name) {
				if (name.equals("Save")) {
					if (cpdRecord.isValid()) {
						CPDDto cpdSave = cpdRecord.getCPD();
						if (cpdSave.getMemberRefId() == null) {
							cpdSave.setMemberRefId(recordMemberRefId);
							cpdSave.setMemberRegistrationNo(recordMemberNo);
						}
						saveRecord(cpdSave);
					}
				}
				hide();
			}
		}, "Save", "Cancel");

	}

	protected void saveRecord(CPDDto dto) {
		fireEvent(new ProcessingEvent());
		if (dto.getMemberRefId() != null) {
			if (dto.getRefId() != null) {
				memberDelegate.withCallback(new AbstractAsyncCallback<CPDDto>() {
					@Override
					public void onSuccess(CPDDto result) {
						fireEvent(new ProcessingCompletedEvent());
						fireEvent(new AfterSaveEvent("CPD Record successfully updated."));
					}
				}).cpd(dto.getMemberRefId()).update(dto.getRefId(), dto);
			} else {
				memberDelegate.withCallback(new AbstractAsyncCallback<CPDDto>() {
					@Override
					public void onSuccess(CPDDto result) {
						fireEvent(new ProcessingCompletedEvent());
						fireEvent(new AfterSaveEvent("CPD Record successfully created."));
					}
				}).cpd(dto.getMemberRefId()).create(dto);
			}

		} else {
			Window.alert("Member records not found!");
			return;
		}

	}

	private void searchCPD(final String searchTerm) {
		fireEvent(new ProcessingEvent());
		memberDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
			@Override
			public void onSuccess(Integer count) {
				fireEvent(new ProcessingCompletedEvent());
				PagingPanel pagingPanel = getView().getReturnsPagingPanel();
				pagingPanel.setTotal(count);
				PagingConfig pagingConfig = pagingPanel.getConfig();
				getCPDSearchResults(pagingConfig.getOffset(), pageLimit, searchTerm);
			}
		}).cpd(page).getCPDsearchCount(searchTerm);
	}

	private void getCPDSearchResults(int offset, int limit, String searchTerm) {
		fireEvent(new ProcessingEvent());
		memberDelegate.withCallback(new AbstractAsyncCallback<List<CPDDto>>() {
			@Override
			public void onSuccess(List<CPDDto> result) {
				getView().bindResults(result, page);
				fireEvent(new ProcessingCompletedEvent());
			}
		}).cpd(page).searchCPd(offset, limit, searchTerm, startDate.getTime(), endDate.getTime());
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		loadSummary();
	}

	protected void loadSummary() {
		memberDelegate.withCallback(new AbstractAsyncCallback<CPDSummaryDto>() {
			@Override
			public void onSuccess(CPDSummaryDto summary) {
				getView().bindSummary(summary);
			}
		}).cpd("ALL").getCPDSummary(startDate.getTime(), endDate.getTime());
	}

	protected void loadData(Date startDate, Date endDate, final String loadType, final Integer offset) {
		fireEvent(new ProcessingEvent());
		memberDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
			@Override
			public void onSuccess(Integer aCount) {
				fireEvent(new ProcessingCompletedEvent());
				PagingPanel panel = null;
				if (loadType.equals("ALLRETURNS") || loadType.equals("cpdReturns")) {
					panel = getView().getReturnsPagingPanel();
					panel.setTotal(aCount);
				} else if (loadType.equals("ALLARCHIVE") || loadType.equals("returnArchive")) {
					panel = getView().getArchivePagingPanel();
					panel.setTotal(aCount);
				}
				PagingConfig config = panel.getConfig();

				int passedOffset = 0;
				if (offset != null) {
					passedOffset = offset;
				} else {
					passedOffset = config.getOffset();
				}
				loadCPD(passedOffset, pageLimit, loadType);
			}
		}).cpd(loadType).getCount(startDate.getTime(), endDate.getTime());

	}

	protected void loadCPD(int offset, int limit, final String loadType) {
		fireEvent(new ProcessingEvent());

		memberDelegate.withCallback(new AbstractAsyncCallback<List<CPDDto>>() {
			@Override
			public void onSuccess(List<CPDDto> result) {
				fireEvent(new ProcessingCompletedEvent());
				getView().bindResults(result, loadType);
			}
		}).cpd(loadType).getAll(offset, limit, startDate.getTime(), endDate.getTime());
	}

	protected void loadIndividualData(String memberId, Date startDate, Date endDate) {
		fireEvent(new ProcessingEvent());

		memberDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
			@Override
			public void onSuccess(Integer aCount) {
				fireEvent(new ProcessingCompletedEvent());
				PagingPanel panel = getView().getIndividualMemberPagingPanel();
				panel.setTotal(aCount);
				PagingConfig config = panel.getConfig();
				loadIndividualMemberCPD(config.getOffset(), pageLimit);
			}
		}).cpd(memberId).getCount(startDate.getTime(), endDate.getTime());

	}

	protected void loadIndividualMemberCPD(int offset, int limit) {
		fireEvent(new ProcessingEvent());
		memberDelegate.withCallback(new AbstractAsyncCallback<List<CPDDto>>() {
			@Override
			public void onSuccess(List<CPDDto> result) {
				fireEvent(new ProcessingCompletedEvent());
				getView().bindIndividualResults(result);
				loadYearSummaries();
			}
		}).cpd(memberRefId).getAll(offset, limit, startDate.getTime(), endDate.getTime());

	}

	protected void loadYearSummaries() {
		fireEvent(new ProcessingEvent());
		memberDelegate.withCallback(new AbstractAsyncCallback<List<CPDFooterDto>>() {
			@Override
			public void onSuccess(List<CPDFooterDto> result) {
				fireEvent(new ProcessingCompletedEvent());
				getView().bindIndividualCPDFooter(result);
			}
		}).cpd(memberRefId).getYearSummaries(startDate.getTime(), endDate.getTime());
	}

	protected void loadCPD(String refId, final String loadType) {
		fireEvent(new ProcessingEvent());

		memberDelegate.withCallback(new AbstractAsyncCallback<CPDDto>() {
			@Override
			public void onSuccess(CPDDto result) {
				fireEvent(new ProcessingCompletedEvent());
				getView().bindSingleResults(result, loadType);
			}
		}).cpd("ALL").getById(refId);
	}

	String getApplicationRefId() {
		String applicationRefId = currentUser.getUser() == null ? null : currentUser.getUser().getApplicationRefId();
		return applicationRefId;
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		page = request.getParameter("p", "cpdReturns");
		String refId = request.getParameter("refId", "");
		searchTerm = request.getParameter("searchTerm", "");
		this.startDate = DateUtils.DATEFORMAT_SYS.parse("2011-01-01");
		this.endDate = new Date();

		// Window.alert("r>>" + refId);

		// Set Tab to Front End
		getView().setTab(page, refId);

		// If there is no refId - Load Table Data else load individual CPD
		// Record
		if (refId.isEmpty()) {
			if (page.equals("cpdReturns")) {
				if (searchTerm.equals("")) {
					loadData(startDate, endDate, "ALLRETURNS", null);
				} else {
					searchCPD(getView().getReturnsSearchValue());
				}
			} else if (page.equals("returnArchive")) {
				if (searchTerm.equals("")) {
					loadData(startDate, endDate, "ALLARCHIVE", null);
				} else {
					searchCPD(getView().getArchiveSummaryTxtSearch());
				}
			} else if (page.equals("memberCPD")) {
				if (searchTerm.equals("")) {
					loadCPDSummaryCount("");
				} else {
					loadCPDSummaryCount(searchTerm);
				}
			}
		} else {
			if (page.equals("memberCPD")) {
				this.memberRefId = refId;
				loadIndividualData(memberRefId, startDate, endDate);
				getView().setIndividualMemberInitialDates(startDate, endDate);
			} else {
				// Load Individual CPD - Return OR Archive
				getView().setIndividualMemberInitialDates(startDate, endDate);
				loadCPD(refId, page);
			}

		}
	}

	private void loadCPDSummaryCount(final String searchTerm) {
		fireEvent(new ProcessingEvent());
		memberDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
			@Override
			public void onSuccess(Integer aCount) {
				fireEvent(new ProcessingCompletedEvent());
				PagingPanel panel = getView().getCPDSummaryPagingPanel();
				panel.setTotal(aCount);
				PagingConfig config = panel.getConfig();
				loadCPDSummaryData(searchTerm, config.getOffset(), pageLimit);
			}
		}).cpd("ALL").getMemberSummaryCount(searchTerm);
	}

	private void loadCPDSummaryData(String searchTerm, Integer offset, Integer limit) {
		fireEvent(new ProcessingEvent());
		memberDelegate.withCallback(new AbstractAsyncCallback<List<MemberCPDDto>>() {
			@Override
			public void onSuccess(List<MemberCPDDto> result) {
				getView().bindMemberSummary(result);
				fireEvent(new ProcessingCompletedEvent());
			}
		}).cpd("ALL").getAllMemberSummary(searchTerm, offset, limit);
	}

	@Override
	public void onEditModel(EditModelEvent event) {
	}

	public void refreshIndividualMembersPage() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("refId", memberRefId);
		params.put("p", "memberCPD");
		PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(NameTokens.cpdmgt).with(params).build();
		placeManager.revealPlace(placeRequest);
	}

	@Override
	public void onTableAction(TableActionEvent event) {
		if (event.getAction() == TableActionType.APPROVECPD) {
			final CPDDto dto = (CPDDto) event.getModel();
			saveRecord(dto);
		} else if (event.getAction() == TableActionType.UPDATECPD) {
			saveRecord((CPDDto) event.getModel());
			refreshIndividualMembersPage();
		} else if (event.getAction() == TableActionType.VIEWCPD) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("refId", ((CPDDto) event.getModel()).getRefId());
			params.put("p", "cpdReturns");
			PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(NameTokens.cpdmgt).with(params).build();
			placeManager.revealPlace(placeRequest);
		}
	}

	@Override
	public void onMemberCPD(MemberCPDEvent event) {
		// loadIndividualData(event.getMember().getRefId(), startDate, endDate);
		// getView().setIndividualMemberInitialDates(startDate, endDate);
		getView().bindMemberDetails(event.getMember());
	}
}
