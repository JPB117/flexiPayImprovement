package com.icpak.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.icpak.rest.dao.InvoiceDaoHelper;
import com.icpak.rest.dao.helper.MemberDaoHelper;
import com.icpak.rest.factory.ResourceFactory;
import com.workpoint.icpak.shared.api.CPDResource;
import com.workpoint.icpak.shared.api.MemberResource;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.MemberDto;
import com.workpoint.icpak.shared.model.UserDto;

@Path("members")
@Produces(MediaType.APPLICATION_JSON)
public class MemberResourceImpl implements MemberResource{

	@Inject ResourceFactory resourceFactory;

	
	@Inject
	private InvoiceDaoHelper helper;
	
	@Inject
	MemberDaoHelper membersHelper;
	
	@GET
	public List<MemberDto> getAll(@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit){
		return membersHelper.getAllMembers(offset, limit, "", null);
	}
	

	@GET
	@Path("/search/{searchTerm}")
	public List<MemberDto> search(@QueryParam("searchTerm") String searchTerm){
	
		return membersHelper.getAllMembers(0, 50, "", searchTerm);
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public List<MemberDto> getAll(@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit, @QueryParam("limit") String searchTerm){
		
		return membersHelper.getAllMembers(offset, limit, "", searchTerm);
	}
	
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
