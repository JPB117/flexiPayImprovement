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
import com.google.inject.assistedinject.Assisted;
import com.icpak.rest.dao.helper.EducationDaoHelper;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.workpoint.icpak.shared.api.EducationResource;
import com.workpoint.icpak.shared.model.ApplicationFormEducationalDto;
import com.workpoint.icpak.shared.model.EduType;

@Api(value="", description="Handles CRUD for event ApplicationFormEducationalDto")
public class EducationResourceImpl 	implements EducationResource{

	@Inject EducationDaoHelper helper;
	
	private final String applicationId;
	
	@Inject
	public EducationResourceImpl(@Assisted String applicationId) {
		this.applicationId = applicationId;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Retrieve all active education entries")
	public List<ApplicationFormEducationalDto> getAll(
			@ApiParam(value="Starting point to fetch") @QueryParam("offset") Integer offset,
			@ApiParam(value="No of Items to fetch") @QueryParam("limit") Integer limit) {
		
		return helper.getAllEducationEntrys("", applicationId, offset, limit);
	}
	
	@GET
	@Path("/{eduEntryId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Get education by eduEntryId", response=ApplicationFormEducationalDto.class, consumes=MediaType.APPLICATION_JSON)
	public ApplicationFormEducationalDto getById( 
			@ApiParam(value="Entry Id of the education to fetch", required=true) @PathParam("eduEntryId") String eduEntryId) {
		
		ApplicationFormEducationalDto education = helper.getEducationEntryById(applicationId,eduEntryId);
		//education.getEvent().setUri(uriInfo.getBaseUri()+"events/"+education.getEvent().getRefId());
		//education.getUser().setUri(uriInfo.getBaseUri()+"users/"+education.getUser().getRefId());
		return education;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Create a new education entry", response=ApplicationFormEducationalDto.class, consumes=MediaType.APPLICATION_JSON)
	public ApplicationFormEducationalDto create(
			ApplicationFormEducationalDto education) {
		education = helper.createEducationEntry(applicationId,education);
		String uri = "";
		return education;
	}

	@PUT
	@Path("/{eduEntryId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Update an existing education", response=ApplicationFormEducationalDto.class, 
	consumes=MediaType.APPLICATION_JSON, produces=MediaType.APPLICATION_JSON)
	public ApplicationFormEducationalDto update( 
			@ApiParam(value="Entry Id of the education to update", required=true) @PathParam("eduEntryId") String eduEntryId, 
			ApplicationFormEducationalDto education) {
		
		education = helper.updateEducationEntry(applicationId,eduEntryId, education);
		return education;
	}

	@DELETE
	@Path("/{eduEntryId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Delete an existing education")
	public void delete(
			@ApiParam(value="Entry Id of the education to delete", required=true) @PathParam("eduEntryId") String eduEntryId) {
		helper.deleteEducationEntry(applicationId,eduEntryId);
	}
	
	@GET
	@Path("academics")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Retrieve all active academic education entries")
	public List<ApplicationFormEducationalDto> getAllAcademic(
			@ApiParam(value="Starting point to fetch") @QueryParam("offset") Integer offset,
			@ApiParam(value="No of Items to fetch") @QueryParam("limit") Integer limit) {
		
		return helper.getAllEducationEntrys("", applicationId,
				EduType.ACADEMIA, offset, limit);
	}
	
	@GET
	@Path("professional")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Retrieve all active professional education entries")
	public List<ApplicationFormEducationalDto> getAllProfessional(
			@ApiParam(value="Starting point to fetch") @QueryParam("offset") Integer offset,
			@ApiParam(value="No of Items to fetch") @QueryParam("limit") Integer limit) {
		
		return helper.getAllEducationEntrys("", applicationId,
				EduType.PROFESSIONALACCEXAMS, offset, limit);
	}

}
