package com.icpak.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.PracticeDaoHelper;
import com.icpak.rest.models.membership.Branch;
import com.icpak.rest.models.membership.Practice;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Path("practices")
@Api(value="practices", description="Handles CRUD for Practices")
public class PracticesResource extends BaseResource<Practice>{

	@Inject PracticeDaoHelper helper;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Retrieve all active Practices")
	public Response getAll(@Context UriInfo uriInfo, 
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit) {
		return buildCollectionResponse(helper.getAllPractices(offset, limit,uriInfo));
	}

	@GET
	@Path("/{practiceId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Get a practice by practiceId", response=Practice.class, consumes=MediaType.APPLICATION_JSON)
	public Response getById(@Context UriInfo uriInfo, 
			@ApiParam(value="Practice Id of the practice to fetch", required=true) @PathParam("practiceId") String practiceId) {
		
		String uri = uriInfo.getAbsolutePath().toString();
		return buildGetEntityResponse(uri, helper.getPracticeById(practiceId));
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Create a new practice", response=Practice.class, consumes=MediaType.APPLICATION_JSON)
	public Response create(@Context UriInfo uriInfo, Practice practice) {
		
		helper.createPractice(practice);
		String uri = uriInfo.getAbsolutePath().toString()+"/"+practice.getRefId();
		return buildCreateEntityResponse(uri, practice);
	}

	@PUT
	@Path("/{practiceId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Update an existing practice", response=Practice.class, 
	consumes=MediaType.APPLICATION_JSON, produces=MediaType.APPLICATION_JSON)
	public Response update(@Context UriInfo uriInfo, 
			@ApiParam(value="Practice Id of the practice to update", required=true) @PathParam("practiceId") String practiceId, 
			Practice practice) {
		helper.updatePractice(practiceId, practice);
		return buildUpdateEntityResponse(uriInfo.getAbsolutePath().toString(),practice);
	}

	@DELETE
	@Path("/{practiceId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Delete an existing practice")
	public Response delete(
			@ApiParam(value="Practice Id of the practice to delete", required=true) @PathParam("practiceId") String practiceId) {
		
		helper.deletePractice(practiceId);
		return buildDeleteEntityResponse();
	}
	
	@GET
	@Path("/{practiceId}/branches")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Retrieve all branches for practiceId", response=Branch.class)
	public Response getAll(@Context UriInfo uriInfo, 
			@ApiParam(value = "Practice Id of the practice", required = true) @PathParam("practiceId") String practiceId,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit) {
		return buildCollectionResponse(helper.getAllBranches(practiceId,offset, limit,uriInfo));
	}
	
}
