package com.icpak.rest;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.UriInfo;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class ResourceRequestFilter implements ContainerRequestFilter{

	private Provider<UriInfo> uriInfoProvider;

	@Inject
	public ResourceRequestFilter(Provider<UriInfo> uriInfoProvider) {
		this.uriInfoProvider = uriInfoProvider;
	}
	
	public UriInfo getUriInfo(){
		return uriInfoProvider.get();
	}
	
	@Override
	public void filter(ContainerRequestContext context) throws IOException {
	}
}
