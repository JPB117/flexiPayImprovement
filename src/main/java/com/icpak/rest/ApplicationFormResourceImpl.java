package com.icpak.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.ApplicationFormDaoHelper;
import com.icpak.rest.models.membership.ApplicationFormHeader;
import com.sun.jersey.api.core.InjectParam;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.workpoint.icpak.shared.api.ApplicationFormResource;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;


@Path("applications")
@Api(value="applications", description="Handles CRUD for Applications")
public class ApplicationFormResourceImpl implements ApplicationFormResource{

	@Inject ApplicationFormDaoHelper helper;

	private String getUri() {
		return "";
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Retrieve all active Applications")
	public List<ApplicationFormHeaderDto> getAll(
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit) {
		
		List<ApplicationFormHeaderDto> dtos = helper.getAllApplications(offset, limit,"");
		return dtos;
	}
	
	@GET
	@Path("/{applicationId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Get a application by applicationId", response=ApplicationFormHeader.class, consumes=MediaType.APPLICATION_JSON)
	public ApplicationFormHeaderDto getById(	
			@ApiParam(value="ApplicationFormHeader Id of the application to fetch", required=true) 
			@PathParam("applicationId") String applicationId) {
		
		String uri = getUri();
		ApplicationFormHeaderDto dto = helper.getApplicationById(applicationId).toDto();
		dto.setUri(uri);
		return dto;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Create a new application", response=ApplicationFormHeader.class, consumes=MediaType.APPLICATION_JSON)
	public ApplicationFormHeaderDto create(ApplicationFormHeaderDto application) {
		
		helper.createApplication(application);
		String uri = getUri()+"/"+application.getRefId();
		application.setUri(uri);
		
		return application;
	}

	@PUT
	@Path("/{applicationId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Update an existing application", response=ApplicationFormHeader.class, 
	consumes=MediaType.APPLICATION_JSON, produces=MediaType.APPLICATION_JSON)
	public ApplicationFormHeaderDto update(
			@ApiParam(value="ApplicationFormHeader Id of the application to update", required=true)
			@PathParam("applicationId") String applicationId, 
			ApplicationFormHeaderDto application) {
		helper.updateApplication(applicationId, application);
		application.setUri(getUri());
		
		return application;
	}

	@DELETE
	@Path("/{applicationId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Delete an existing application")
	public void delete(
			@ApiParam(value="ApplicationFormHeader Id of the application to delete", required=true) 
			@PathParam("applicationId") String applicationId) {
		
		helper.deleteApplication(applicationId);
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
