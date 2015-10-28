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
import com.icpak.rest.dao.helper.CriminalOffensesDaoHelper;
import com.icpak.rest.models.membership.CriminalOffense;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Api(value="", description="Handles CRUD for member Criminal Offenses")
public class CriminalOffensesResource extends BaseResource<CriminalOffense>{

	@Inject CriminalOffensesDaoHelper helper;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Retrieve all active offense entries")
	public Response getAll(@Context UriInfo uriInfo,
			@ApiParam(value="Member for which offense entries are requested") @PathParam("memberId") String memberId,
			@ApiParam(value="Starting point to fetch") @QueryParam("offset") Integer offset,
			@ApiParam(value="No of Items to fetch") @QueryParam("limit") Integer limit) {
		
		return buildCollectionResponse(helper.getAllCriminalOffenses(uriInfo, memberId, offset, limit));
	}

	@GET
	@Path("/{offenseId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Get an offense by offenseId", response=CriminalOffense.class, consumes=MediaType.APPLICATION_JSON)
	public Response getById(@Context UriInfo uriInfo, 
			@ApiParam(value="Member for which the offense details are requested") @PathParam("memberId") String memberId,
			@ApiParam(value="Entry Id of the offense to fetch", required=true) @PathParam("offenseId") String offenseId) {
		
		CriminalOffense offense = helper.getCriminalOffenseById(memberId,offenseId);
		//offense.getEvent().setUri(uriInfo.getBaseUri()+"members/"+offense.getEvent().getRefId());
		//offense.getUser().setUri(uriInfo.getBaseUri()+"users/"+offense.getUser().getRefId());
		return buildGetEntityResponse(uriInfo.getAbsolutePath().toString(), offense);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Create a new offense", response=CriminalOffense.class, consumes=MediaType.APPLICATION_JSON)
	public Response create(@Context UriInfo uriInfo,
			@ApiParam(value="Member for which offense is being created") @PathParam("memberId") String memberId,
			CriminalOffense offense) {
		
		offense = helper.createCriminalOffense(memberId,offense);
		String uri = uriInfo.getAbsolutePath()+"/"+offense.getRefId();
		//offense.getEvent().setUri(uriInfo.getBaseUri()+"members/"+offense.getEvent().getRefId());
//		offense.getUser().setUri(uriInfo.getBaseUri()+"users/"+offense.getUser().getRefId());
		
		return buildCreateEntityResponse(uri, offense);
	}

	@PUT
	@Path("/{offenseId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Update an existing offense", response=CriminalOffense.class, 
	consumes=MediaType.APPLICATION_JSON, produces=MediaType.APPLICATION_JSON)
	public Response update(@Context UriInfo uriInfo, 
			@ApiParam(value="Member for which offense is being updated") @PathParam("memberId") String memberId,
			@ApiParam(value="Entry Id of the offense to update", required=true) @PathParam("offenseId") String offenseId, 
			CriminalOffense offense) {
		
		offense = helper.updateCriminalOffense(memberId,offenseId, offense);
		//offense.getEvent().setUri(uriInfo.getBaseUri()+"members/"+offense.getEvent().getRefId());
//		offense.getUser().setUri(uriInfo.getBaseUri()+"users/"+offense.getUser().getRefId());
		return buildUpdateEntityResponse(uriInfo.getAbsolutePath().toString(), offense);
	}

	@DELETE
	@Path("/{offenseId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Delete an existing offense")
	public Response delete(
			@ApiParam(value="Member from which offense is being deleted") @PathParam("memberId") String memberId,
			@ApiParam(value="Entry Id of the offense to delete", required=true) @PathParam("offenseId") String offenseId) {
		helper.deleteCriminalOffense(memberId,offenseId);
		return buildDeleteEntityResponse();
	}

}
