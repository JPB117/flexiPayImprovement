package com.workpoint.icpak.client.gin;

import com.gwtplatform.dispatch.rest.client.RestApplicationPath;
import com.gwtplatform.dispatch.rest.client.gin.RestDispatchAsyncModule;
import com.gwtplatform.dispatch.shared.SecurityCookie;
import com.gwtplatform.mvp.client.annotations.ErrorPlace;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import com.workpoint.icpak.client.place.ClientPlaceManager;
import com.workpoint.icpak.client.place.DefaultPlace;
import com.workpoint.icpak.client.place.NameTokens;
import com.workpoint.icpak.client.registration.WebsiteClientPresenter;
import com.workpoint.icpak.client.registration.WebsiteClientView;
import com.workpoint.icpak.client.ui.AppManager;
import com.workpoint.icpak.client.ui.MainPagePresenter;
import com.workpoint.icpak.client.ui.MainPageView;
import com.workpoint.icpak.client.ui.admin.AdminHomePresenter;
import com.workpoint.icpak.client.ui.admin.AdminHomeView;
import com.workpoint.icpak.client.ui.admin.TabPanel;
import com.workpoint.icpak.client.ui.admin.dashboard.DashboardPresenter;
import com.workpoint.icpak.client.ui.admin.dashboard.DashboardView;
import com.workpoint.icpak.client.ui.admin.dashboard.charts.PieChartPresenter;
import com.workpoint.icpak.client.ui.admin.dashboard.charts.PieChartView;
import com.workpoint.icpak.client.ui.admin.dashboard.linegraph.LineGraphPresenter;
import com.workpoint.icpak.client.ui.admin.dashboard.linegraph.LineGraphView;
import com.workpoint.icpak.client.ui.admin.dashboard.table.TableDataPresenter;
import com.workpoint.icpak.client.ui.admin.dashboard.table.TableDataView;
import com.workpoint.icpak.client.ui.admin.reports.ReportsPresenter;
import com.workpoint.icpak.client.ui.admin.reports.ReportsView;
import com.workpoint.icpak.client.ui.admin.settings.SettingsPresenter;
import com.workpoint.icpak.client.ui.admin.settings.SettingsView;
import com.workpoint.icpak.client.ui.admin.users.UserPresenter;
import com.workpoint.icpak.client.ui.admin.users.UserView;
import com.workpoint.icpak.client.ui.admin.users.groups.GroupPresenter;
import com.workpoint.icpak.client.ui.admin.users.groups.GroupView;
import com.workpoint.icpak.client.ui.admin.users.item.UserItemPresenter;
import com.workpoint.icpak.client.ui.admin.users.item.UserItemView;
import com.workpoint.icpak.client.ui.admin.users.save.UserSavePresenter;
import com.workpoint.icpak.client.ui.admin.users.save.UserSaveView;
import com.workpoint.icpak.client.ui.error.ErrorPagePresenter;
import com.workpoint.icpak.client.ui.error.ErrorPageView;
import com.workpoint.icpak.client.ui.error.ErrorPresenter;
import com.workpoint.icpak.client.ui.error.ErrorView;
import com.workpoint.icpak.client.ui.error.NotfoundPresenter;
import com.workpoint.icpak.client.ui.error.NotfoundView;
import com.workpoint.icpak.client.ui.header.HeaderPresenter;
import com.workpoint.icpak.client.ui.header.HeaderView;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.home.HomeView;
import com.workpoint.icpak.client.ui.login.LoginPresenter;
import com.workpoint.icpak.client.ui.login.LoginView;
import com.workpoint.icpak.client.ui.notifications.NotificationsPresenter;
import com.workpoint.icpak.client.ui.notifications.NotificationsView;
import com.workpoint.icpak.client.ui.popup.GenericPopupPresenter;
import com.workpoint.icpak.client.ui.popup.GenericPopupView;
import com.workpoint.icpak.client.ui.profile.ProfilePresenter;
import com.workpoint.icpak.client.ui.profile.ProfileView;
import com.workpoint.icpak.client.ui.registration.MemberRegistrationPresenter;
import com.workpoint.icpak.client.ui.registration.MemberRegistrationView;
import com.workpoint.icpak.client.ui.upload.UploadDocumentPresenter;
import com.workpoint.icpak.client.ui.upload.UploadDocumentView;
import com.workpoint.icpak.client.ui.upload.attachment.AttachmentPresenter;
import com.workpoint.icpak.client.ui.upload.attachment.AttachmentView;
import com.workpoint.icpak.client.ui.upload.href.IFrameDataPresenter;
import com.workpoint.icpak.client.ui.upload.href.IFrameDataView;
import com.workpoint.icpak.client.ui.user.UserSelectionPresenter;
import com.workpoint.icpak.client.ui.user.UserSelectionView;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.client.util.Definitions;

public class ClientModule extends AbstractPresenterModule {

	@Override
	protected void configure() {
		RestDispatchAsyncModule.Builder dispatchBuilder = new RestDispatchAsyncModule.Builder();
		install(dispatchBuilder.build());

		// Bind RestApplicationPath To Server endpoint
		bindConstant().annotatedWith(RestApplicationPath.class).to("/api");

		install(new DefaultModule.Builder().placeManager(
				ClientPlaceManager.class).build());

		// bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.home);
		bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.signup);
		bindConstant().annotatedWith(ErrorPlace.class).to(NameTokens.error);
		bindConstant().annotatedWith(SecurityCookie.class).to(
				Definitions.AUTHENTICATIONCOOKIE);

		bindPresenter(MainPagePresenter.class, MainPagePresenter.MyView.class,
				MainPageView.class, MainPagePresenter.MyProxy.class);

		bindPresenter(WebsiteClientPresenter.class,
				WebsiteClientPresenter.MyView.class, WebsiteClientView.class,
				WebsiteClientPresenter.MyProxy.class);

		bindPresenter(HomePresenter.class, HomePresenter.IHomeView.class,
				HomeView.class, HomePresenter.MyProxy.class);

		bindPresenterWidget(HeaderPresenter.class,
				HeaderPresenter.IHeaderView.class, HeaderView.class);

		bindPresenter(MemberRegistrationPresenter.class,
				MemberRegistrationPresenter.MyView.class,
				MemberRegistrationView.class,
				MemberRegistrationPresenter.MyProxy.class);

		bindPresenter(ErrorPagePresenter.class,
				ErrorPagePresenter.MyView.class, ErrorPageView.class,
				ErrorPagePresenter.MyProxy.class);

		bindPresenterWidget(ErrorPresenter.class, ErrorPresenter.MyView.class,
				ErrorView.class);

		bindPresenter(LoginPresenter.class, LoginPresenter.ILoginView.class,
				LoginView.class, LoginPresenter.MyProxy.class);

		requestStaticInjection(AppContext.class);
		requestStaticInjection(AppManager.class);

		bindPresenter(NotfoundPresenter.class, NotfoundPresenter.MyView.class,
				NotfoundView.class, NotfoundPresenter.MyProxy.class);

		bindPresenterWidget(NotificationsPresenter.class,
				NotificationsPresenter.MyView.class, NotificationsView.class);

		bindPresenterWidget(AttachmentPresenter.class,
				AttachmentPresenter.IAttachmentView.class, AttachmentView.class);

		bindPresenterWidget(UserSelectionPresenter.class,
				UserSelectionPresenter.MyView.class, UserSelectionView.class);

		bindPresenterWidget(UploadDocumentPresenter.class,
				UploadDocumentPresenter.MyView.class, UploadDocumentView.class);

		bindPresenter(AdminHomePresenter.class,
				AdminHomePresenter.MyView.class, AdminHomeView.class,
				AdminHomePresenter.MyProxy.class);

		bindPresenter(UserPresenter.class, UserPresenter.MyView.class,
				UserView.class, UserPresenter.MyProxy.class);

		bindPresenter(DashboardPresenter.class,
				DashboardPresenter.IDashboardView.class, DashboardView.class,
				DashboardPresenter.MyProxy.class);

		bindPresenterWidget(ReportsPresenter.class,
				ReportsPresenter.MyView.class, ReportsView.class);

		bindPresenterWidget(UserSavePresenter.class,
				UserSavePresenter.IUserSaveView.class, UserSaveView.class);

		bindPresenterWidget(UserItemPresenter.class,
				UserItemPresenter.MyView.class, UserItemView.class);

		bindPresenterWidget(GroupPresenter.class, GroupPresenter.MyView.class,
				GroupView.class);

		bindPresenterWidget(GenericPopupPresenter.class,
				GenericPopupPresenter.MyView.class, GenericPopupView.class);

		bindPresenterWidget(IFrameDataPresenter.class,
				IFrameDataPresenter.IFrameView.class, IFrameDataView.class);

		bindPresenter(ProfilePresenter.class,
				ProfilePresenter.IProfileView.class, ProfileView.class,
				ProfilePresenter.IProfileProxy.class);

		bindPresenterWidget(PieChartPresenter.class,
				PieChartPresenter.IPieChartView.class, PieChartView.class);

		bindPresenterWidget(LineGraphPresenter.class,
				LineGraphPresenter.ILineGraphView.class, LineGraphView.class);

		bindPresenter(SettingsPresenter.class,
				SettingsPresenter.ISettingsView.class, SettingsView.class,
				SettingsPresenter.MyProxy.class);

		bindPresenterWidget(TableDataPresenter.class,
				TableDataPresenter.ITableDataView.class, TableDataView.class);

		bind(TabPanel.class);

	}
}
