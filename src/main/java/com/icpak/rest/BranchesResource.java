package com.icpak.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.PracticeDaoHelper;
import com.icpak.rest.models.membership.Branch;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * Branch Offices management
 * 
 *  Subresource /permissions/permission123/branches
 * 
 * @author duggan
 *
 */
@Api(value="branches", description="Branch Offices management resource")
@Path("branches")
public class BranchesResource extends BaseResource<Branch>{

	@Inject PracticeDaoHelper helper;
	
	
	@GET
	@Path("/{branchId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Retrieve a branch", response=Branch.class, 
	consumes=MediaType.APPLICATION_JSON)
	public Response get(@Context UriInfo uriInfo,
			@ApiParam(value = "Branch Id of branch to retrieve", required = true) @PathParam("branchId") String branchId){
		
		Branch branch = helper.getBranchById(branchId);
		String entityUri= uriInfo.getBaseUri().toString();
		return buildUpdateEntityResponse(entityUri, branch);
	}
	
	@PUT
	@Path("/{branchId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Utility service to update a branch", response=Branch.class, 
	consumes=MediaType.APPLICATION_JSON)
	public Response create(@Context UriInfo uriInfo,
			@ApiParam(value = "Branch Id to update", required = true) @PathParam("branchId") String branchId,
			Branch branch){
		
		Branch rtn = helper.updateBranch(branchId, branch);
		String entityUri= uriInfo.getBaseUri().toString();
		return buildUpdateEntityResponse(entityUri, rtn);
	}
	
	@DELETE
	@Path("/{branchId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Utility api to delete a branch")
	public Response delete(@ApiParam(value = "Branch Id delete", required = true) @PathParam("branchId") String branchId){
		helper.deleteBranch(branchId);
		return buildDeleteEntityResponse();
	}
}