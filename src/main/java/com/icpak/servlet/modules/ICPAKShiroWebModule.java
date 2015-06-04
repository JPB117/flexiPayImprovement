package com.icpak.servlet.modules;

import javax.servlet.ServletContext;

import org.apache.shiro.guice.web.ShiroWebModule;

import com.icpak.rest.security.ICPAKAuthenticatingRealm;

public class ICPAKShiroWebModule extends ShiroWebModule{

	public ICPAKShiroWebModule(ServletContext context) {
		super(context);
	}
	
	@SuppressWarnings("unchecked")
	protected void configureShiroWeb() {
       	bindRealm().to(ICPAKAuthenticatingRealm.class);

        addFilterChain("/logout", LOGOUT);
		//addFilterChain("/api/**", AUTHC_BASIC);
        addFilterChain("/api/**", ANON);
		addFilterChain("/**", ANON);
    }

}
