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
import com.workpoint.icpak.client.ui.admin.dashboard.table.TableDataPresenter;
import com.workpoint.icpak.client.ui.admin.dashboard.table.TableDataView;
import com.workpoint.icpak.client.ui.admin.reports.ReportsPresenter;
import com.workpoint.icpak.client.ui.admin.reports.ReportsView;
import com.workpoint.icpak.client.ui.admin.settings.SettingsPresenter;
import com.workpoint.icpak.client.ui.admin.settings.SettingsView;
import com.workpoint.icpak.client.ui.cpd.CPDMemberPresenter;
import com.workpoint.icpak.client.ui.cpd.CPDMemberView;
import com.workpoint.icpak.client.ui.cpd.admin.CPDManagementPresenter;
import com.workpoint.icpak.client.ui.cpd.admin.CPDManagementView;
import com.workpoint.icpak.client.ui.cpd.online.CPDOnlinePresenter;
import com.workpoint.icpak.client.ui.cpd.online.CPDOnlineView;
import com.workpoint.icpak.client.ui.dashboard.DashboardPresenter;
import com.workpoint.icpak.client.ui.dashboard.DashboardView;
import com.workpoint.icpak.client.ui.directory.DirectoryPresenter;
import com.workpoint.icpak.client.ui.directory.DirectoryView;
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
import com.workpoint.icpak.client.ui.events.bookings.BookingsPresenter;
import com.workpoint.icpak.client.ui.events.bookings.BookingsView;
import com.workpoint.icpak.client.ui.events.registration.EventBookingPresenter;
import com.workpoint.icpak.client.ui.events.registration.EventBookingView;
import com.workpoint.icpak.client.ui.eventsandseminars.EventsPresenter;
import com.workpoint.icpak.client.ui.eventsandseminars.EventsView;
import com.workpoint.icpak.client.ui.frontmember.FrontMemberPresenter;
import com.workpoint.icpak.client.ui.frontmember.FrontMemberView;
import com.workpoint.icpak.client.ui.header.HeaderPresenter;
import com.workpoint.icpak.client.ui.header.HeaderView;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.client.ui.home.HomeView;
import com.workpoint.icpak.client.ui.invoices.InvoiceListPresenter;
import com.workpoint.icpak.client.ui.invoices.InvoiceListView;
import com.workpoint.icpak.client.ui.login.LoginPresenter;
import com.workpoint.icpak.client.ui.login.LoginView;
import com.workpoint.icpak.client.ui.login.createpassword.ActivateAccountPresenter;
import com.workpoint.icpak.client.ui.login.createpassword.ActivateAccountView;
import com.workpoint.icpak.client.ui.members.ApplicationsPresenter;
import com.workpoint.icpak.client.ui.members.ApplicationsView;
import com.workpoint.icpak.client.ui.membership.MemberRegistrationPresenter;
import com.workpoint.icpak.client.ui.membership.MemberRegistrationView;
import com.workpoint.icpak.client.ui.notifications.NotificationsPresenter;
import com.workpoint.icpak.client.ui.notifications.NotificationsView;
import com.workpoint.icpak.client.ui.offences.OffencesPresenter;
import com.workpoint.icpak.client.ui.offences.OffencesView;
import com.workpoint.icpak.client.ui.payment.PaymentPresenter;
import com.workpoint.icpak.client.ui.payment.PaymentView;
import com.workpoint.icpak.client.ui.payment.collective.collectivepayments.CollectivePaymentsPresenter;
import com.workpoint.icpak.client.ui.payment.collective.collectivepayments.CollectivePaymentsView;
import com.workpoint.icpak.client.ui.popup.GenericPopupPresenter;
import com.workpoint.icpak.client.ui.popup.GenericPopupView;
import com.workpoint.icpak.client.ui.profile.ProfilePresenter;
import com.workpoint.icpak.client.ui.profile.ProfileView;
import com.workpoint.icpak.client.ui.splashscreen.SplashScreenPresenter;
import com.workpoint.icpak.client.ui.splashscreen.SplashScreenView;
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
import com.workpoint.icpak.client.ui.users.UserPresenter;
import com.workpoint.icpak.client.ui.users.UserView;
import com.workpoint.icpak.client.ui.users.groups.GroupPresenter;
import com.workpoint.icpak.client.ui.users.groups.GroupView;
import com.workpoint.icpak.client.ui.users.item.UserItemPresenter;
import com.workpoint.icpak.client.ui.users.item.UserItemView;
import com.workpoint.icpak.client.ui.users.save.UserSavePresenter;
import com.workpoint.icpak.client.ui.users.save.UserSaveView;
import com.workpoint.icpak.client.util.AppContext;
import com.workpoint.icpak.client.util.Definitions;

public class ClientModule extends AbstractPresenterModule {

	@Override
	protected void configure() {
		RestDispatchAsyncModule.Builder dispatchBuilder = new RestDispatchAsyncModule.Builder();
		install(dispatchBuilder.build());

		// Bind RestApplicationPath To Server endpoint
		bindConstant().annotatedWith(RestApplicationPath.class).to("api");

		install(new DefaultModule.Builder().placeManager(
				ClientPlaceManager.class).build());

		// SECURITY
		bind(CurrentUser.class).asEagerSingleton();

		requestStaticInjection(AppContext.class);
		requestStaticInjection(AppManager.class);

		bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.splash);
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

		bindPresenter(InvoiceListPresenter.class,
				InvoiceListPresenter.IInvoiceView.class, InvoiceListView.class,
				InvoiceListPresenter.InvoiceListProxy.class);

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

		bindPresenter(BookingsPresenter.class,
				BookingsPresenter.IBookingsView.class, BookingsView.class,
				BookingsPresenter.IBookingsProxy.class);

		bindPresenter(EventBookingPresenter.class,
				EventBookingPresenter.MyView.class, EventBookingView.class,
				EventBookingPresenter.MyProxy.class);
		
		bindPresenter(CollectivePaymentsPresenter.class,
				CollectivePaymentsPresenter.MyView.class, CollectivePaymentsView.class,
				CollectivePaymentsPresenter.MyProxy.class);

		bindPresenter(DashboardPresenter.class,
				DashboardPresenter.IDashboardView.class, DashboardView.class,
				DashboardPresenter.IDashboardProxy.class);

		bindPresenter(CPDMemberPresenter.class,
				CPDMemberPresenter.ICPDView.class, CPDMemberView.class,
				CPDMemberPresenter.ICPDProxy.class);

		bindPresenter(CPDManagementPresenter.class,
				CPDManagementPresenter.ICPDManagementView.class,
				CPDManagementView.class,
				CPDManagementPresenter.ICPDManagementProxy.class);

		bindPresenter(CPDOnlinePresenter.class,
				CPDOnlinePresenter.ICPDView.class, CPDOnlineView.class,
				CPDOnlinePresenter.ICPDProxy.class);

		bindPresenter(ApplicationsPresenter.class,
				ApplicationsPresenter.IApplicationsView.class,
				ApplicationsView.class,
				ApplicationsPresenter.IApplicationsProxy.class);

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

		bindPresenter(SplashScreenPresenter.class,
				SplashScreenPresenter.ILoginView.class, SplashScreenView.class,
				SplashScreenPresenter.ILoginProxy.class);

		bindPresenter(ActivateAccountPresenter.class,
				ActivateAccountPresenter.IActivateAccountView.class,
				ActivateAccountView.class,
				ActivateAccountPresenter.IActivateAccountProxy.class);

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

		bindPresenter(SettingsPresenter.class,
				SettingsPresenter.ISettingsView.class, SettingsView.class,
				SettingsPresenter.MyProxy.class);

		bindPresenterWidget(TableDataPresenter.class,
				TableDataPresenter.ITableDataView.class, TableDataView.class);

		bindPresenterWidget(PaymentPresenter.class,
				PaymentPresenter.MyView.class, PaymentView.class);

		bindPresenter(DirectoryPresenter.class,
				DirectoryPresenter.MyDirectoryView.class, DirectoryView.class,
				DirectoryPresenter.MyDirectoryProxy.class);

		bindPresenter(FrontMemberPresenter.class,
				FrontMemberPresenter.MyFrontMemberView.class,
				FrontMemberView.class,
				FrontMemberPresenter.MyFrontMemberProxy.class);

		bind(TabPanel.class);

	}
}
