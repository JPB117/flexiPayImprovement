package com.icpak.servlet.config;

import org.apache.shiro.SecurityUtils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.icpak.servlet.modules.BootstrapServletModule;

/**
 * This class goes mapped in web.xml and is used to inject Google Guice's Injector into the Web Application Context.
 * 
 * @author pablo.biagioli
 *
 */
public class GenericGuiceContextListener extends GuiceServletContextListener{

	@Override
	protected Injector getInjector() {
		
		Injector injector = Guice.createInjector(new BootstrapServletModule());
		org.apache.shiro.mgt.SecurityManager securityManager = 
				injector.getInstance(org.apache.shiro.mgt.SecurityManager.class);
		SecurityUtils.setSecurityManager(securityManager);
	    return injector;
	}

	
}
