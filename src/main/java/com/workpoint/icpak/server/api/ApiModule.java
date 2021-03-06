package com.workpoint.icpak.server.api;

import com.google.inject.AbstractModule;
import com.icpak.rest.ApplicationFormResourceImpl;
import com.icpak.rest.CountriesResourceImpl;
import com.icpak.rest.DelegatesResourceImpl;
import com.icpak.rest.EventsResourceImpl;
import com.icpak.rest.InvoiceResourceImpl;
import com.icpak.rest.MemberResourceImpl;
import com.icpak.rest.RoleResourceImpl;
import com.icpak.rest.TransactionsResourceImpl;
import com.icpak.rest.UsersResourceImpl;
import com.workpoint.icpak.shared.api.ApplicationFormResource;
import com.workpoint.icpak.shared.api.CountriesResource;
import com.workpoint.icpak.shared.api.DelegatesResource;
import com.workpoint.icpak.shared.api.EventsResource;
import com.workpoint.icpak.shared.api.InvoiceResource;
import com.workpoint.icpak.shared.api.MemberResource;
import com.workpoint.icpak.shared.api.RoleResource;
import com.workpoint.icpak.shared.api.TransactionsResource;
import com.workpoint.icpak.shared.api.UsersResource;

public class ApiModule extends AbstractModule {
    @Override
    protected void configure() {
    	bind(UsersResource.class).to(UsersResourceImpl.class);
    	bind(RoleResource.class).to(RoleResourceImpl.class);
    	bind(ApplicationFormResource.class).to(ApplicationFormResourceImpl.class);
    	bind(CountriesResource.class).to(CountriesResourceImpl.class);
    	bind(EventsResource.class).to(EventsResourceImpl.class);
    	bind(InvoiceResource.class).to(InvoiceResourceImpl.class);
    	bind(TransactionsResource.class).to(TransactionsResourceImpl.class);
    	bind(MemberResource.class).to(MemberResourceImpl.class);
    	bind(DelegatesResource.class).to(DelegatesResourceImpl.class);
    }
}
