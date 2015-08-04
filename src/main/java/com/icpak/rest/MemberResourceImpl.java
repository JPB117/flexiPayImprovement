package com.icpak.rest;

import java.util.List;

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
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.workpoint.icpak.shared.api.EnquiriesResource;
import com.workpoint.icpak.shared.api.MemberResource;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.MemberDto;

@Path("members")
@Produces(MediaType.APPLICATION_JSON)
@Api(value="members", description="Handles CRUD for Members")
public class MemberResourceImpl implements MemberResource{

	@Inject ResourceFactory resourceFactory;
	
	@Inject
	private InvoiceDaoHelper helper;
	
	@Inject
	MemberDaoHelper membersHelper;
	
	@GET
	@ApiOperation(value="Retrieve all active members")
	public List<MemberDto> getAll(
			@ApiParam(value="Starting index", required=true) @QueryParam("offset") Integer offset,
			@ApiParam(value="Number of items to retrieve", required=true) @QueryParam("limit") Integer limit){
		return membersHelper.getAllMembers(offset, limit, "", null);
	}
	
	@GET
	@Path("/count")
	public Integer getCount() {
		return membersHelper.getCount();
	}
	
	@Path("/{memberId}")
	@GET
	@ApiOperation(value="Retrieve a member")
	public MemberDto getMember(@ApiParam(value="memberId", required=true) 
			@PathParam("memberId") String memberId){
		return membersHelper.getMemberById(memberId);
	}

	@GET
	@Path("/search/{searchTerm}")
	@ApiOperation(value="Search for members based on a search term (MemberId/ Names)")
	public List<MemberDto> search(
			@ApiParam(value="Search term to use", required=true) @QueryParam("searchTerm") String searchTerm){
	
		return membersHelper.getAllMembers(0, 50, "", searchTerm);
	}
	
//	@GET
//	@Consumes(MediaType.APPLICATION_JSON)
//	@ApiParam(value="Number of items to retrieve", required=true)
//	public List<MemberDto> getAll(@QueryParam("offset") Integer offset,
//			@QueryParam("limit") Integer limit, @QueryParam("limit") String searchTerm){
//		
//		return membersHelper.getAllMembers(offset, limit, "", searchTerm);
//	}
//	

	@Path("/{memberId}/invoice")
	@GET
	@ApiOperation(value="Search for member invoices")
	public List<InvoiceDto> getAllInvoicesForMember(
			@ApiParam(value="Member Id", required=true) @PathParam("memberId") String memberId) {
		
		return helper.getAllInvoicesForMember(memberId);
	}
	
	@Path("/{memberId}/cpd")
	public CPDResourceImpl cpd(@PathParam("memberId") String memberId){
		return resourceFactory.createCPDResource(memberId);
	}
	
	@Path("/{memberId}/enquiries")
	public EnquiriesResourceImpl enquiries(@PathParam("memberId") String memberId){
		return resourceFactory.createEnquiriesResource(memberId);
	}
	
}
