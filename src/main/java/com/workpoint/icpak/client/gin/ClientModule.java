package com.workpoint.icpak.client.gin;

import com.gwtplatform.dispatch.rest.client.RestApplicationPath;
import com.gwtplatform.dispatch.rest.client.gin.RestDispatchAsyncModule;
import com.gwtplatform.dispatch.shared.SecurityCookie;
import com.gwtplatform.mvp.client.annotations.ErrorPlace;
import com.gwtplatform.mvp.client.annotations.UnauthorizedPlace;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import com.workpoint.icpak.client.place.ClientPlaceManager;
import com.workpoint.icpak.client.place.DefaultPlace;
import com.workpoint.icpak.client.place.NameTokens;
import com.workpoint.icpak.client.security.CurrentUser;
import com.workpoint.icpak.client.ui.AppManager;
import com.workpoint.icpak.client.ui.MainPagePresenter;
import com.workpoint.icpak.client.ui.MainPageView;
import com.workpoint.icpak.client.ui.accomodation.AccomodationPresenter;
import com.workpoint.icpak.client.ui.accomodation.AccomodationView;
import com.workpoint.icpak.client.ui.admin.AdminHomePresenter;
import com.workpoint.icpak.client.ui.admin.AdminHomeView;
import com.workpoint.icpak.client.ui.admin.TabPanel;
import com.workpoint.icpak.client.ui.admin.dashboard.AdminDashboardPresenter;
import com.workpoint.icpak.client.ui.admin.dashboard.AdminDashboardView;
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
import com.workpoint.icpak.client.ui.cpd.CPDPresenter;
import com.workpoint.icpak.client.ui.cpd.CPDView;
import com.workpoint.icpak.client.ui.dashboard.DashboardPresenter;
import com.workpoint.icpak.client.ui.dashboard.DashboardView;
import com.workpoint.icpak.client.ui.enquiries.EnquiriesPresenter;
import com.workpoint.icpak.client.ui.enquiries.EnquiriesView;
import com.workpoint.icpak.client.ui.error.ErrorPagePresenter;
import com.workpoint.icpak.client.ui.error.ErrorPageView;
import com.workpoint.icpak.client.ui.error.ErrorPresenter;
import com.workpoint.icpak.client.ui.error.ErrorView;
import com.workpoint.icpak.client.ui.error.NotfoundPresenter;
import com.workpoint.icpak.client.ui.error.NotfoundView;
import com.workpoint.icpak.client.ui.error.UnauthorizedPagePresenter;
import com.workpoint.icpak.client.ui.error.UnauthorizedPageView;
import com.workpoint.icpak.client.ui.events.registration.EventBookingPresenter;
import com.workpoint.icpak.client.ui.events.registration.EventBookingView;
import com.workpoint.icpak.client.ui.eventsandseminars.EventsPresenter;
import com.workpoint.icpak.client.ui.eventsandseminars.EventsView;
import com.workpoint.icpak.client.ui.header.HeaderPresenter;
import com.workpoint.icpak.client.ui.header.HeaderView;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.home.HomeView;
import com.workpoint.icpak.client.ui.login.LoginPresenter;
import com.workpoint.icpak.client.ui.login.LoginView;
import com.workpoint.icpak.client.ui.login.createpassword.CreatePasswordPresenter;
import com.workpoint.icpak.client.ui.login.createpassword.CreatePasswordView;
import com.workpoint.icpak.client.ui.members.MembersPresenter;
import com.workpoint.icpak.client.ui.members.MembersView;
import com.workpoint.icpak.client.ui.notifications.NotificationsPresenter;
import com.workpoint.icpak.client.ui.notifications.NotificationsView;
import com.workpoint.icpak.client.ui.offences.OffencesPresenter;
import com.workpoint.icpak.client.ui.offences.OffencesView;
import com.workpoint.icpak.client.ui.popup.GenericPopupPresenter;
import com.workpoint.icpak.client.ui.popup.GenericPopupView;
import com.workpoint.icpak.client.ui.profile.ProfilePresenter;
import com.workpoint.icpak.client.ui.profile.ProfileView;
import com.workpoint.icpak.client.ui.registration.MemberRegistrationPresenter;
import com.workpoint.icpak.client.ui.registration.MemberRegistrationView;
import com.workpoint.icpak.client.ui.statements.StatementsPresenter;
import com.workpoint.icpak.client.ui.statements.StatementsView;
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

		// SECURITY
		bind(CurrentUser.class).asEagerSingleton();

		// bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.home);
		bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.login);
		bindConstant().annotatedWith(ErrorPlace.class).to(NameTokens.error);
		bindConstant().annotatedWith(UnauthorizedPlace.class).to(
				NameTokens.unauthorized);
		bindConstant().annotatedWith(SecurityCookie.class).to(
				Definitions.AUTHENTICATIONCOOKIE);

		bindPresenter(MainPagePresenter.class, MainPagePresenter.MyView.class,
				MainPageView.class, MainPagePresenter.MyProxy.class);

		bindPresenter(HomePresenter.class, HomePresenter.IHomeView.class,
				HomeView.class, HomePresenter.MyProxy.class);

		bindPresenterWidget(HeaderPresenter.class,
				HeaderPresenter.IHeaderView.class, HeaderView.class);

		bindPresenter(MemberRegistrationPresenter.class,
				MemberRegistrationPresenter.MyView.class,
				MemberRegistrationView.class,
				MemberRegistrationPresenter.MyProxy.class);

		bindPresenter(StatementsPresenter.class,
				StatementsPresenter.IStatementsView.class,
				StatementsView.class,
				StatementsPresenter.IStatementsProxy.class);

		bindPresenter(OffencesPresenter.class,
				OffencesPresenter.IOffencesView.class, OffencesView.class,
				OffencesPresenter.IOffencesProxy.class);

		bindPresenter(AccomodationPresenter.class,
				AccomodationPresenter.IAccomodationView.class,
				AccomodationView.class,
				AccomodationPresenter.IAccomodationProxy.class);

		bindPresenter(EnquiriesPresenter.class,
				EnquiriesPresenter.IEnquiriesView.class, EnquiriesView.class,
				EnquiriesPresenter.IEnquiriesProxy.class);

		bindPresenter(EventsPresenter.class, EventsPresenter.IEventsView.class,
				EventsView.class, EventsPresenter.IEventsProxy.class);

		bindPresenter(EventBookingPresenter.class,
				EventBookingPresenter.MyView.class, EventBookingView.class,
				EventBookingPresenter.MyProxy.class);

		bindPresenter(DashboardPresenter.class,
				DashboardPresenter.IDashboardView.class, DashboardView.class,
				DashboardPresenter.IDashboardProxy.class);

		bindPresenter(CPDPresenter.class, CPDPresenter.ICPDView.class,
				CPDView.class, CPDPresenter.ICPDProxy.class);

		bindPresenter(MembersPresenter.class,
				MembersPresenter.IMembersView.class, MembersView.class,
				MembersPresenter.IMembersProxy.class);

		bindPresenter(ErrorPagePresenter.class,
				ErrorPagePresenter.MyView.class, ErrorPageView.class,
				ErrorPagePresenter.MyProxy.class);

		bindPresenter(UnauthorizedPagePresenter.class,
				UnauthorizedPagePresenter.MyView.class,
				UnauthorizedPageView.class,
				UnauthorizedPagePresenter.MyProxy.class);

		bindPresenterWidget(ErrorPresenter.class, ErrorPresenter.MyView.class,
				ErrorView.class);

		bindPresenter(LoginPresenter.class, LoginPresenter.ILoginView.class,
				LoginView.class, LoginPresenter.ILoginProxy.class);

		bindPresenter(CreatePasswordPresenter.class,
				CreatePasswordPresenter.ILoginView.class,
				CreatePasswordView.class,
				CreatePasswordPresenter.ILoginProxy.class);

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

		bindPresenter(AdminDashboardPresenter.class,
				AdminDashboardPresenter.IAdminDashboardView.class,
				AdminDashboardView.class,
				AdminDashboardPresenter.IAdminDashboardProxy.class);

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
