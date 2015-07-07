package com.icpak.rest;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.google.inject.Inject;
import com.icpak.rest.factory.ResourceFactory;
import com.workpoint.icpak.shared.api.CPDResource;
import com.workpoint.icpak.shared.api.MemberResource;

@Path("members")
@Produces
public class MemberResourceImpl implements MemberResource{

	@Inject ResourceFactory resourceFactory;

	@Path("/{memberId}/cpd")
	public CPDResource cpd(@PathParam("memberId") String memberId){
		return resourceFactory.createCPDResource(memberId);
	}
	
}
