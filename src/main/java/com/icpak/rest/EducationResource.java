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
import com.icpak.rest.dao.helper.EducationDaoHelper;
import com.icpak.rest.models.membership.ApplicationFormEducational;
import com.icpak.rest.models.membership.EduType;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Api(value="", description="Handles CRUD for event ApplicationFormEducational")
public class EducationResource extends BaseResource<ApplicationFormEducational>{

	@Inject EducationDaoHelper helper;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Retrieve all active education entries")
	public Response getAll(@Context UriInfo uriInfo,
			@ApiParam(value="Application for which education entries are requested") @PathParam("applicationId") String applicationId,
			@ApiParam(value="Starting point to fetch") @QueryParam("offset") Integer offset,
			@ApiParam(value="No of Items to fetch") @QueryParam("limit") Integer limit) {
		
		return buildCollectionResponse(helper.getAllEducationEntrys(uriInfo, applicationId, offset, limit));
	}
	
	@GET
	@Path("academics")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Retrieve all active academic education entries")
	public Response getAllAcademic(@Context UriInfo uriInfo,
			@ApiParam(value="Application for which education entries are requested") @PathParam("applicationId") String applicationId,
			@ApiParam(value="Starting point to fetch") @QueryParam("offset") Integer offset,
			@ApiParam(value="No of Items to fetch") @QueryParam("limit") Integer limit) {
		
		return buildCollectionResponse(helper.getAllEducationEntrys(uriInfo, applicationId,EduType.ACADEMIA, offset, limit));
	}
	
	@GET
	@Path("professional")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Retrieve all active professional education entries")
	public Response getAllProfessional(@Context UriInfo uriInfo,
			@ApiParam(value="Application for which education entries are requested") @PathParam("applicationId") String applicationId,
			@ApiParam(value="Starting point to fetch") @QueryParam("offset") Integer offset,
			@ApiParam(value="No of Items to fetch") @QueryParam("limit") Integer limit) {
		
		return buildCollectionResponse(helper.getAllEducationEntrys(uriInfo, applicationId,EduType.PROFESSIONALACCEXAMS, offset, limit));
	}

	@GET
	@Path("/{eduEntryId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Get education by eduEntryId", response=ApplicationFormEducational.class, consumes=MediaType.APPLICATION_JSON)
	public Response getById(@Context UriInfo uriInfo, 
			@ApiParam(value="Application for which the education details are requested") @PathParam("applicationId") String applicationId,
			@ApiParam(value="Entry Id of the education to fetch", required=true) @PathParam("eduEntryId") String eduEntryId) {
		
		ApplicationFormEducational education = helper.getEducationEntryById(applicationId,eduEntryId);
		//education.getEvent().setUri(uriInfo.getBaseUri()+"events/"+education.getEvent().getRefId());
		//education.getUser().setUri(uriInfo.getBaseUri()+"users/"+education.getUser().getRefId());
		return buildGetEntityResponse(uriInfo.getAbsolutePath().toString(), education);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Create a new education entry", response=ApplicationFormEducational.class, consumes=MediaType.APPLICATION_JSON)
	public Response create(@Context UriInfo uriInfo,
			@ApiParam(value="Application for which education is being created") @PathParam("applicationId") String applicationId,
			ApplicationFormEducational education) {
		
		education = helper.createEducationEntry(applicationId,education);
		String uri = uriInfo.getAbsolutePath()+"/"+education.getRefId();
		//education.getEvent().setUri(uriInfo.getBaseUri()+"events/"+education.getEvent().getRefId());
//		education.getUser().setUri(uriInfo.getBaseUri()+"users/"+education.getUser().getRefId());
		
		return buildCreateEntityResponse(uri, education);
	}

	@PUT
	@Path("/{eduEntryId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Update an existing education", response=ApplicationFormEducational.class, 
	consumes=MediaType.APPLICATION_JSON, produces=MediaType.APPLICATION_JSON)
	public Response update(@Context UriInfo uriInfo, 
			@ApiParam(value="Application for which education is being updated") @PathParam("applicationId") String applicationId,
			@ApiParam(value="Entry Id of the education to update", required=true) @PathParam("eduEntryId") String eduEntryId, 
			ApplicationFormEducational education) {
		
		education = helper.updateEducationEntry(applicationId,eduEntryId, education);
		//education.getEvent().setUri(uriInfo.getBaseUri()+"events/"+education.getEvent().getRefId());
//		education.getUser().setUri(uriInfo.getBaseUri()+"users/"+education.getUser().getRefId());
		return buildUpdateEntityResponse(uriInfo.getAbsolutePath().toString(), education);
	}

	@DELETE
	@Path("/{eduEntryId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Delete an existing education")
	public Response delete(
			@ApiParam(value="Application from which education is being deleted") @PathParam("applicationId") String applicationId,
			@ApiParam(value="Entry Id of the education to delete", required=true) @PathParam("eduEntryId") String eduEntryId) {
		helper.deleteEducationEntry(applicationId,eduEntryId);
		return buildDeleteEntityResponse();
	}

}
