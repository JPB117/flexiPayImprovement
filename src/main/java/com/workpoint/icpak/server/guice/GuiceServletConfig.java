package com.workpoint.icpak.server.guice;

import org.apache.shiro.SecurityUtils;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.icpak.rest.dao.helper.StatementDaoHelper;
import com.icpak.servlet.modules.BootstrapServletModule;

public class GuiceServletConfig extends GuiceServletContextListener {
	@Override
	protected Injector getInjector() {
		Injector injector = Guice.createInjector(new BootstrapServletModule(), new ServerModule(),
				new DispatchServletModule(), new AbstractModule() {
					@Override
					protected void configure() {
						requestInjection(StatementDaoHelper.class);
					}
				});
		org.apache.shiro.mgt.SecurityManager securityManager = injector
				.getInstance(org.apache.shiro.mgt.SecurityManager.class);
		SecurityUtils.setSecurityManager(securityManager);
		return injector;
	}

}
