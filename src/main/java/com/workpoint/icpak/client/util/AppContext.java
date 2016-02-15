package com.workpoint.icpak.client.util;

import java.util.Date;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest.Builder;
import com.workpoint.icpak.client.place.NameTokens;
import com.workpoint.icpak.client.place.ParameterTokens;
import com.workpoint.icpak.client.security.CurrentUser;
import com.workpoint.icpak.shared.api.ApiParameters;
import com.workpoint.icpak.shared.api.SessionResource;
import com.workpoint.icpak.shared.api.UsersResource;
import com.workpoint.icpak.shared.model.UserDto;
import com.workpoint.icpak.shared.model.Version;
import com.workpoint.icpak.shared.model.auth.CurrentUserDto;

/**
 * 
 * @author duggan
 *
 */
public class AppContext {

	@Inject
	static EventBus eventBus;
	@Inject
	static PlaceManager placeManager;
	@Inject
	static CurrentUser user;
	@Inject
	static ResourceDelegate<UsersResource> usersDelegate;
	@Inject
	static ResourceDelegate<SessionResource> sessionResource;
	private static final int REMEMBER_ME_DAYS = 14;

	static Version version;

	static String organizationName;

	static Timer timer = new Timer() {

		@Override
		public void run() {
			reloadContext();
		}
	};

	public static void setSessionValues(UserDto User, String authCookie) {
		setUserValues(User);
		CookieManager.setCookies(authCookie, new Date().getTime());

	}

	public static void setSessionValue(String name, String value) {
		CookieManager.setSessionValue(name, value);
	}

	public static String getSessionValue(String name) {
		return CookieManager.getSessionValue(name);
	}

	public static boolean isShowWelcomeWiget() {
		String val = CookieManager.getSessionValue(
				Definitions.SHOWWELCOMEWIDGET, "true");
		Boolean show = Boolean.valueOf(val);

		return show;
	}

	public static boolean isLoggedIn() {
		boolean isValid = user.isLoggedIn();
		if (isValid) {
			return true;
		}

		// if(isValid){
		// Window.alert("####User Logged in!!!!!");
		// return true;
		//
		// }else{
		// Window.alert("User Not Logged in!!!!!");
		// }

		PlaceRequest request = placeManager.getCurrentPlaceRequest();
		PlaceRequest place = new Builder().nameToken(NameTokens.login)
				.with(ParameterTokens.REDIRECT, request.getNameToken()).build();

		placeManager.revealPlace(place);

		return false;
	}

	public static String getLoggedInCookie() {
		return Cookies.getCookie(ApiParameters.LOGIN_COOKIE);
	}

	static boolean isReloading = false;

	public static void reloadContext() {
		if (!isReloading) {
			isReloading = true;

		}
	}

	protected static void setUserValues(UserDto User) {
		CookieManager.setSessionValue(Definitions.ISADMINSESSION,
				User.isAdmin() ? "Y" : "N");
	}

	public static PlaceManager getPlaceManager() {
		return placeManager;
	}

	public static void destroy() {
		setSessionValues(new UserDto(), null);
		CookieManager.clear();
	}

	public static String getUserId() {
		return user.getUser().getRefId();
	}

	public static String getUserNames() {
		return user.getUser().getFullName();
	}

	public static String getUserGroups() {
		return user.getUser().getGroupsAsString();
	}

	public static EventBus getEventBus() {
		return eventBus;
	}

	public static String getLastRequestUrl() {
		return Cookies.getCookie(Definitions.PENDINGREQUESTURL);
	}

	public static void fireEvent(GwtEvent event) {
		eventBus.fireEvent(event);
	}

	public static UserDto getContextUser() {
		return user.getUser();
	}

	public static boolean isCurrentUser(String userId) {

		if (getContextUser() == null) {
			return false;
		}

		return getContextUser().getRefId().equals(userId);
	}

	public static String getOrganizationName() {
		return organizationName;
	}

	public static String getBaseUrl() {

		String moduleUrl = GWT.getModuleBaseURL().replace("/gwtht", "");
		if (moduleUrl.endsWith("/")) {
			moduleUrl = moduleUrl.substring(0, moduleUrl.length() - 1);
		}

		return moduleUrl;
	}

	public static String getUserImageUrl() {
		String moduleUrl = getBaseUrl();
		String url = moduleUrl + "/getreport?ACTION=GetUser&userId="
				+ user.getUser().getRefId();
		return url;
	}

	public static String getUserImageUrl(double width) {
		return getUserImageUrl() + "&width=" + width;
	}

	public static String getUserImageUrl(double width, double height) {
		return getUserImageUrl() + "&width=" + width + "&height=" + height;
	}

	public static void clear() {
		user.fromCurrentUserDto(new CurrentUserDto());
		CookieManager.clear();
	}

	public static boolean hasLoggedInCookie() {
		return !Strings.isNullOrEmpty(getLoggedInCookie());
	}

	public static CurrentUser getCurrentUser() {
		return user;
	}

	public static boolean isCurrentUserMember() {
		return (user.getUser().getMemberNo() != null)
				&& (!user.getUser().getMemberNo().isEmpty());
	}

	public static boolean isCurrentBasicMember() {
		return (user == null ? false
				: (user.getUser().isBasicMember() || hasNoGroup()));
	}

	public static boolean isCurrentUserEventEdit() {
		return (user == null ? false : (user.getUser().isEventEdit())
				|| isCurrentUserAdmin());
	}

	public static boolean isCurrentUserEventRead() {
		return (user == null ? false : user.getUser().isEventRead());
	}

	public static boolean isCurrentUserUsersEdit() {
		return (user == null ? false : (user.getUser().isUsersEdit())
				|| isCurrentUserAdmin());
	}

	public static boolean isCurrentUserUsersRead() {
		return (user == null ? false : user.getUser().isUsersRead());
	}

	public static boolean isCurrentUserApplicationsEdit() {
		return (user == null ? false : (user.getUser().isApplicationsEdit())
				|| isCurrentUserAdmin());
	}

	public static boolean isCurrentUserApplicationsRead() {
		return (user == null ? false : user.getUser().isApplicationsRead());
	}

	public static boolean isCurrentUserCPDEdit() {
		return (user == null ? false : (user.getUser().isCPDEdit())
				|| isCurrentUserAdmin());
	}

	public static boolean isCurrentUserCPDRead() {
		return (user == null ? false : user.getUser().isCPDRead());
	}

	public static boolean isCurrentUserFinanceEdit() {
		return (user == null ? false : (user.getUser().isFinanceEdit())
				|| isCurrentUserAdmin());
	}

	public static boolean isCurrentUserFinanceApplications() {
		return (user == null ? false : (user.getUser()
				.isFinanceEditApplications()) || isCurrentUserAdmin());
	}

	public static boolean isCurrentUserFinanceEvents() {
		return (user == null ? false : (user.getUser().isFinanceEditEvents())
				|| isCurrentUserAdmin());
	}

	public static boolean isCurrentUserFinanceRead() {
		return (user == null ? false : user.getUser().isFinanceRead());
	}

	public static boolean hasNoGroup() {
		return (user == null ? false
				: (user.getUser().getGroups().isEmpty() ? true : false));
	}

	/* Super Administrator */
	public static boolean isCurrentUserAdmin() {
		boolean isAdmin = user == null ? false : user.getUser().isAdmin();
		return isAdmin;
	}

}
