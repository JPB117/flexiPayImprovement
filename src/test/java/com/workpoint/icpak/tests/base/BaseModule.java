package com.workpoint.icpak.tests.base;

import com.google.inject.AbstractModule;
import com.icpak.servlet.modules.BootstrapPropertiesModule;

public class BaseModule extends AbstractModule{

	@Override
	protected void configure() {
		//get the bootstrapping Properties file
		install(new BootstrapPropertiesModule());
		install(new DatabaseModule());
	}
}
