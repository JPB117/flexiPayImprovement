package com.workpoint.icpak.server.api;

import com.google.inject.AbstractModule;
import com.icpak.rest.UsersResourceImpl;
import com.workpoint.icpak.shared.api.UsersResource;

public class ApiModule extends AbstractModule {
    @Override
    protected void configure() {
    	bind(UsersResource.class).to(UsersResourceImpl.class);
    }
}
