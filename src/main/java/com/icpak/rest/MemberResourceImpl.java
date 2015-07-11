package com.icpak.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.icpak.rest.dao.InvoiceDaoHelper;
import com.icpak.rest.factory.ResourceFactory;
import com.workpoint.icpak.shared.api.CPDResource;
import com.workpoint.icpak.shared.api.MemberResource;
import com.workpoint.icpak.shared.model.InvoiceDto;

@Path("members")
@Produces(MediaType.APPLICATION_JSON)
public class MemberResourceImpl implements MemberResource{

	@Inject ResourceFactory resourceFactory;

	@Inject
	private InvoiceDaoHelper helper;
	
	@Path("/{memberId}/cpd")
	public CPDResource cpd(@PathParam("memberId") String memberId){
		return resourceFactory.createCPDResource(memberId);
	}

	@Path("/{memberId}/invoice")
	@GET
	public List<InvoiceDto> getAllInvoicesForMember(@PathParam("memberId") String memberId) {
		
		return helper.getAllInvoicesForMember(memberId);
	}
	
}
