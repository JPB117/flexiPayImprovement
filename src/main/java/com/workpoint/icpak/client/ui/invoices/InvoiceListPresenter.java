package com.workpoint.icpak.client.ui.invoices;

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
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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
import com.workpoint.icpak.client.ui.OnOptionSelected;
import com.workpoint.icpak.client.ui.admin.TabDataExt;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.PagingConfig;
import com.workpoint.icpak.client.ui.component.PagingLoader;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent;
import com.workpoint.icpak.client.ui.events.ProcessingEvent;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.invoices.advanced.AdvancedFilter;
import com.workpoint.icpak.client.ui.security.FinanceGateKeeper;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.client.ui.util.StringUtils;
import com.workpoint.icpak.shared.api.InvoiceResource;
import com.workpoint.icpak.shared.model.InvoiceSummary;
import com.workpoint.icpak.shared.model.PaymentMode;
import com.workpoint.icpak.shared.model.PaymentType;
import com.workpoint.icpak.shared.model.TransactionDto;

public class InvoiceListPresenter
		extends
		Presenter<InvoiceListPresenter.IInvoiceView, InvoiceListPresenter.InvoiceListProxy> {

	public interface IInvoiceView extends View {
		void bindInvoices(List<TransactionDto> trxs);

		void setCount(Integer aCount);

		PagingPanel getPagingPanel();

		void bindSummary(InvoiceSummary result);

		String getSearchText();

		HasClickHandlers getAdvancedFilterButton();

		HasClickHandlers getSearchButton();

		HasKeyDownHandlers getTxtSearch();

		DropDownList<PaymentMode> getLstPaymentMode();

		DropDownList<PaymentType> getLstPaymentType();

		void setTransactionDateString(String dateString);

		HasClickHandlers getDownloadXlsButton();
	}

	private int pageLimit = 20;

	@ProxyCodeSplit
	@NameToken(NameTokens.invoices)
	@UseGatekeeper(FinanceGateKeeper.class)
	public interface InvoiceListProxy extends
			TabContentProxyPlace<InvoiceListPresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(FinanceGateKeeper adminGatekeeper) {
		TabDataExt data = new TabDataExt("Financial Summary",
				"fa fa-briefcase", 7, adminGatekeeper, true);
		return data;
	}

	@Inject
	CurrentUser user;

	private final ResourceDelegate<InvoiceResource> invoiceDelegate;

	private String fromDate = "";

	private String endDate = "";
	private String startTime = "00:00";
	private String endTime = "00:00";

	private String paymentType = "";

	private String paymentMode = "";

	private String searchTerm = "";

	private PlaceManager placeManager;

	private AdvancedFilter advancedFilter = new AdvancedFilter();

	@Inject
	public InvoiceListPresenter(final EventBus eventBus,
			final IInvoiceView view, final InvoiceListProxy proxy,
			final ResourceDelegate<InvoiceResource> invoiceDelegate,
			final PlaceManager placeManager) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
		this.invoiceDelegate = invoiceDelegate;
		this.placeManager = placeManager;
	}

	KeyDownHandler keyHandler = new KeyDownHandler() {
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				Map<String, String> params = new HashMap<String, String>();
				searchTerm = getView().getSearchText();
				params.put("paymentType", paymentType);
				params.put("paymentMode", paymentMode);
				params.put("searchTerm", searchTerm);
				params.put("fromDate", fromDate);
				params.put("endDate", endDate);
				PlaceRequest placeRequest = new PlaceRequest.Builder()
						.nameToken(NameTokens.invoices).with(params).build();
				placeManager.revealPlace(placeRequest);
			}
		}
	};

	ClickHandler searchClickHandler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("paymentType", getView().getLstPaymentType().getValue()
					.name());
			params.put("paymentMode", getView().getLstPaymentMode().getValue()
					.name());
			params.put("searchTerm", searchTerm);
			params.put("fromDate", fromDate);
			params.put("endDate", endDate);

			PlaceRequest placeRequest = new PlaceRequest.Builder()
					.nameToken(NameTokens.invoices).with(params).build();
			placeManager.revealPlace(placeRequest);
		}
	};

	ClickHandler advancedFilterClickHandler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			AppManager.showPopUp("Date Filter", advancedFilter,
					new OnOptionSelected() {
						@Override
						public void onSelect(String name) {
							Map<String, String> params = new HashMap<String, String>();
							if (advancedFilter.getStartDate() != null) {
								fromDate = DateUtils.SHORTTIMESTAMP
										.format(advancedFilter.getStartDate());
							}

							if (advancedFilter.getEndDate() != null) {
								endDate = DateUtils.SHORTTIMESTAMP
										.format(advancedFilter.getEndDate());
							}

							if (!StringUtils.isNullOrEmpty(advancedFilter
									.getStartTime())) {
								startTime = DateUtils.TIMEFORMAT24HR
										.format(DateUtils.TIMEFORMAT12HR
												.parse(advancedFilter
														.getStartTime()));
							}
							fromDate = fromDate + " " + startTime;

							if (!StringUtils.isNullOrEmpty(advancedFilter
									.getEndTime())) {
								endTime = DateUtils.TIMEFORMAT24HR
										.format(DateUtils.TIMEFORMAT12HR
												.parse(advancedFilter
														.getEndTime()));
							}
							endDate = endDate + " " + endTime;

							params.put("paymentType", paymentType);
							params.put("searchTerm", searchTerm);
							params.put("paymentMode", paymentMode);
							params.put("fromDate", fromDate);
							params.put("endDate", endDate);
							PlaceRequest placeRequest = new PlaceRequest.Builder()
									.nameToken(NameTokens.invoices)
									.with(params).build();
							placeManager.revealPlace(placeRequest);
						}
					}, "Apply");
		}
	};

	ValueChangeHandler<PaymentMode> paymentModeValueChangeHandler = new ValueChangeHandler<PaymentMode>() {
		@Override
		public void onValueChange(ValueChangeEvent<PaymentMode> event) {
			if (event.getValue() != null) {
				paymentMode = event.getValue().getName();
			} else {
				paymentMode = "";
			}
			Map<String, String> params = new HashMap<String, String>();
			params.put("paymentMode", paymentMode);
			params.put("searchTerm", searchTerm);
			params.put("paymentType", paymentType);
			params.put("fromDate", fromDate);
			params.put("endDate", endDate);

			PlaceRequest placeRequest = new PlaceRequest.Builder()
					.nameToken(NameTokens.invoices).with(params).build();
			placeManager.revealPlace(placeRequest);
		}
	};

	ValueChangeHandler<PaymentType> paymentTypeValueChangeHandler = new ValueChangeHandler<PaymentType>() {
		@Override
		public void onValueChange(ValueChangeEvent<PaymentType> event) {
			if (event.getValue() != null) {
				paymentType = event.getValue().name();
			} else {
				paymentType = "";
			}
			Map<String, String> params = new HashMap<String, String>();
			params.put("paymentType", paymentType);
			params.put("searchTerm", searchTerm);
			params.put("paymentMode", paymentMode);
			params.put("fromDate", fromDate);
			params.put("endDate", endDate);
			PlaceRequest placeRequest = new PlaceRequest.Builder()
					.nameToken(NameTokens.invoices).with(params).build();
			placeManager.revealPlace(placeRequest);
		}
	};

	@Override
	protected void onBind() {
		super.onBind();

		getView().getPagingPanel().setLoader(new PagingLoader() {
			@Override
			public void onLoad(int offset, int limit) {
				loadInvoices(offset, pageLimit);
			}
		});

		getView().getDownloadXlsButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				UploadContext ctx = new UploadContext("getreport");
				ctx.setContext("paymentType", paymentType);
				ctx.setContext("paymentMode", paymentMode);
				ctx.setContext("searchTerm", searchTerm);
				ctx.setContext("fromDate", fromDate);
				ctx.setContext("endDate", endDate);
				ctx.setAction(UPLOADACTION.GETTRANSACTIONSREPORT);
				Window.open(ctx.toUrl(), "Download Transaction Report", null);
			}
		});

		getView().getTxtSearch().addKeyDownHandler(keyHandler);
		getView().getSearchButton().addClickHandler(searchClickHandler);
		getView().getAdvancedFilterButton().addClickHandler(
				advancedFilterClickHandler);

		getView().getLstPaymentMode().addValueChangeHandler(
				paymentModeValueChangeHandler);
		getView().getLstPaymentType().addValueChangeHandler(
				paymentTypeValueChangeHandler);

	}

	protected void save() {
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		fromDate = request.getParameter("fromDate", "");
		endDate = request.getParameter("endDate", "");
		paymentType = request.getParameter("paymentType", "");
		paymentMode = request.getParameter("paymentMode", "");
		searchTerm = request.getParameter("searchTerm", "");

		if (!fromDate.equals("") && !endDate.equals("")) {
			getView().setTransactionDateString(
					DateUtils.MONTHTIME.format(DateUtils.FULLHOURMINUTESTAMP
							.parse(fromDate))
							+ " to "
							+ DateUtils.MONTHTIME
									.format(DateUtils.FULLHOURMINUTESTAMP
											.parse(endDate)));
		} else {
			getView().setTransactionDateString("All dates");
		}

		loadData();
	}

	private void loadData() {
		fireEvent(new ProcessingEvent());

		invoiceDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
			@Override
			public void onSuccess(Integer aCount) {
				getView().setCount(aCount);
				PagingConfig config = getView().getPagingPanel().getConfig();
				loadInvoices(config.getOffset(), pageLimit);
			}
		}).getAllTransactionCount("ALL", searchTerm, paymentType, paymentMode,
				fromDate, endDate);

	}

	protected void loadInvoices(int offset, int limit) {
		fireEvent(new ProcessingEvent());
		invoiceDelegate.withCallback(
				new AbstractAsyncCallback<List<TransactionDto>>() {
					public void onSuccess(List<TransactionDto> result) { //
						getView().bindInvoices(result);
						fireEvent(new ProcessingCompletedEvent());
					}
				}).getAllTransactions("ALL", offset, pageLimit, searchTerm,
				paymentType, paymentMode, fromDate, endDate);

	}

}
