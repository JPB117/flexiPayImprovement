package com.workpoint.icpak.client.util;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.workpoint.icpak.client.place.NameTokens;
import com.workpoint.icpak.client.security.CurrentUser;
import com.workpoint.icpak.shared.model.UserDto;
import com.workpoint.icpak.shared.model.Version;
import com.workpoint.icpak.shared.model.auth.CurrentUserDto;


/**
 * 
 * @author duggan
 *
 */
public class AppContext {
	
	
	@Inject static EventBus eventBus;
	@Inject static PlaceManager placeManager;
	@Inject static CurrentUser user;
	static Version version;

	static String organizationName;
	
	static Timer timer = new Timer() {
		
		@Override
		public void run() {
			reloadContext();
		}
	};

	public static void setSessionValues(UserDto User, String authCookie){
		setUserValues(User);
		CookieManager.setCookies(authCookie, new Date().getTime());
		
	}
	
	public static void setSessionValue(String name, String value){
		CookieManager.setSessionValue(name, value);
	}
	
	public static String getSessionValue(String name){
		return CookieManager.getSessionValue(name);
	}
	
	public static boolean isShowWelcomeWiget(){
		String val = CookieManager.getSessionValue(Definitions.SHOWWELCOMEWIDGET, "true");
		Boolean show = Boolean.valueOf(val);
		
		return show;
	}
	 
	public static boolean isValid(){
		//System.err.println("Session Cookie Asked:: "+CookieManager.getAuthCookie());
		
		boolean valid = user.isLoggedIn();
		
		if(valid){
			checkNeedReloadState();
		}else{
			//store targetUrl
			PlaceRequest req = placeManager.getCurrentPlaceRequest();
			
			if(req!=null && !req.matchesNameToken(NameTokens.login)){
				if(req.getNameToken()!=null){
					String token = placeManager.buildHistoryToken(req);
					Cookies.setCookie(Definitions.PENDINGREQUESTURL, token);
				}
			}
		}
		return true;
	}
	
	private static void checkNeedReloadState() {
		if(user.getUser()==null || user.getUser().getUserId()==null){
			reloadContext();
		}
	}

	static boolean reloading =false;//Controlled reload
	public static void reloadContext() {
		if(!reloading){
			reloading=true;
//			dispatcher.execute(new GetContextRequest(), new TaskServiceCallback<GetContextRequestResult>() {
//				@Override
//				public void processResult(GetContextRequestResult result) {
//					//System.err.println("Reloading Context!!!!!!!!!!!!!!!!!!!!");
//					organizationName= result.getOrganizationName();
//					setUserValues(result.getUser());
//					version = result.getVersion();
//					reportViewImpl = result.getReportViewImpl();
//					
//					ContextLoadedEvent event = new ContextLoadedEvent(result.getUser(), version);
//					event.setOrganizationName(organizationName);
//					eventBus.fireEvent(event);
//					reloading=false;
//				}
//				
//				@Override
//				public void onFailure(Throwable caught) {
//					reloading=false;
//					super.onFailure(caught);
//				}
//			});
		}
	}
	
	protected static void setUserValues(UserDto User) {
		CookieManager.setSessionValue(Definitions.ISADMINSESSION, User.isAdmin()? "Y":"N");
	}

	
	public static PlaceManager getPlaceManager(){
		return placeManager;
	}

	public static void destroy(){
		setSessionValues(new UserDto(), null);
		CookieManager.clear();		
	}
	
	public static String getUserId(){
		return user.getUser().getRefId();
	}
	
	public static String getUserNames(){
		return user.getUser().getFullName();
	}
	
	public static String getUserGroups(){
		return user.getUser().getGroupsAsString();
	}
	
	public static EventBus getEventBus(){
		return eventBus;
	}
	
	public static String getLastRequestUrl(){
		return Cookies.getCookie(Definitions.PENDINGREQUESTURL);
	}

	public static void fireEvent(GwtEvent event) {
		eventBus.fireEvent(event);
	}
	
	public static UserDto getContextUser(){
		return user.getUser();
	}

	public static boolean isCurrentUserAdmin() {
		boolean isAdmin = CookieManager.isCurrentUserAdmin();
		if(!isAdmin){
			//System.err.println("2) IsCurrentUserAdmin? NOOO :: on Refresh -- USER IS NOT AN ADMIN");
		}
		
		return isAdmin;
	}


	public static boolean isCurrentUser(String userId) {
		
		if(getContextUser()==null){	
			return false;
		}
		
		return getContextUser().getUserId().equals(userId);
	}

	public static String getOrganizationName() {
		return organizationName;
	}
	
	public static String getBaseUrl(){
		
		String moduleUrl = GWT.getModuleBaseURL().replace("/gwtht", "");
		if (moduleUrl.endsWith("/")) {
			moduleUrl = moduleUrl.substring(0, moduleUrl.length() - 1);
		}
		
		return moduleUrl;
	}
	
	public static String getUserImageUrl(){
		String moduleUrl = getBaseUrl();				
		String url = moduleUrl
				+ "/getreport?ACTION=GetUser&userId="
				+ user.getUser().getUserId();
		return url;
	}
	
	public static String getUserImageUrl(double width){
		return getUserImageUrl()+"&width="+width;
	}
	
	public static String getUserImageUrl(double width, double height){
		return getUserImageUrl()+"&width="+width+"&height="+height;
	}
	
	public static void clear() {
		user.fromCurrentUserDto(new CurrentUserDto());
	}
}
