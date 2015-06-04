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
import com.icpak.rest.dao.helper.ApplicationFormDaoHelper;
import com.icpak.rest.dao.helper.ApplicationsDaoHelper;
import com.icpak.rest.models.membership.ApplicationFormHeader;
import com.icpak.rest.models.membership.ApplicationFormHeader;
import com.sun.jersey.api.core.InjectParam;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;


@Path("applications")
@Api(value="applications", description="Handles CRUD for Applications")
public class ApplicationFormResource extends BaseResource<ApplicationFormHeader>{

	@Inject ApplicationFormDaoHelper helper;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Retrieve all active Applications")
	public Response getAll(@Context UriInfo uriInfo, 
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit) {
		return buildCollectionResponse(helper.getAllApplications(offset, limit,uriInfo));
	}
	
	@GET
	@Path("/{applicationId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Get a application by applicationId", response=ApplicationFormHeader.class, consumes=MediaType.APPLICATION_JSON)
	public Response getById(@Context UriInfo uriInfo, 
			@ApiParam(value="ApplicationFormHeader Id of the application to fetch", required=true) @PathParam("applicationId") String applicationId) {
		
		String uri = uriInfo.getAbsolutePath().toString();
		return buildGetEntityResponse(uri, helper.getApplicationById(applicationId));
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Create a new application", response=ApplicationFormHeader.class, consumes=MediaType.APPLICATION_JSON)
	public Response create(@Context UriInfo uriInfo, ApplicationFormHeader application) {
		
		helper.createApplication(application);
		String uri = uriInfo.getAbsolutePath().toString()+"/"+application.getRefId();
		return buildCreateEntityResponse(uri, application);
	}

	@PUT
	@Path("/{applicationId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Update an existing application", response=ApplicationFormHeader.class, 
	consumes=MediaType.APPLICATION_JSON, produces=MediaType.APPLICATION_JSON)
	public Response update(@Context UriInfo uriInfo, 
			@ApiParam(value="ApplicationFormHeader Id of the application to update", required=true) @PathParam("applicationId") String applicationId, 
			ApplicationFormHeader application) {
		helper.updateApplication(applicationId, application);
		return buildUpdateEntityResponse(uriInfo.getAbsolutePath().toString(),application);
	}

	@DELETE
	@Path("/{applicationId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Delete an existing application")
	public Response delete(
			@ApiParam(value="ApplicationFormHeader Id of the application to delete", required=true) @PathParam("applicationId") String applicationId) {
		
		helper.deleteApplication(applicationId);
		return buildDeleteEntityResponse();
	}
	
	/**
	 * Member CPD
	 * 
	 * @param resource
	 * @return
	 */
	@Path("/{applicationId}/cpd")
	public CPDResource bookings(@InjectParam CPDResource resource){
		return resource;
	}
	
	/**
	 * Member Education
	 * 
	 * @param resource
	 * @return
	 */
	@Path("/{applicationId}/education")
	public EducationResource education(@InjectParam EducationResource resource){
		return resource;
	}
	
	/**
	 * Member Training And Experience
	 * 
	 * @param resource
	 * @return
	 */
	@Path("/{applicationId}/training")
	public TrainingAndExperienceResource bookings(@InjectParam TrainingAndExperienceResource resource){
		return resource;
	}
	
	/**
	 * Member Training And Experience
	 * 
	 * @param resource
	 * @return
	 */
	@Path("/{applicationId}/specialization")
	public SpecializationResource bookings(@InjectParam SpecializationResource resource){
		return resource;
	}
	
	/**
	 * Member Criminal Offenses
	 * 
	 * @param resource
	 * @return
	 */
	@Path("/{applicationId}/offenses")
	public CriminalOffensesResource bookings(@InjectParam CriminalOffensesResource resource){
		return resource;
	}
}
