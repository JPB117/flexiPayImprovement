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
import com.icpak.rest.dao.helper.SpecializationDaoHelper;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.workpoint.icpak.shared.api.SpecializationsResource;
import com.workpoint.icpak.shared.model.ApplicationFormSpecializationDto;

@Api(value="", description="Handles CRUD for event Application Form Training")
public class SpecializationsResourceImpl implements SpecializationsResource{

	@Inject SpecializationDaoHelper helper;
	
	private final String applicationId;
	
	@Inject
	public SpecializationsResourceImpl(@Assisted String applicationId) {
		this.applicationId = applicationId;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Retrieve all active specialization entries")
	public List<ApplicationFormSpecializationDto> getAll(
			@ApiParam(value="Starting point to fetch") @QueryParam("offset") Integer offset,
			@ApiParam(value="No of Items to fetch") @QueryParam("limit") Integer limit) {
		
		return helper.getAllSpecializationEntrys("", applicationId, offset, limit);
	}
	
	@GET
	@Path("/{specializationId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Get specialization by specializationId", response=ApplicationFormSpecializationDto.class, consumes=MediaType.APPLICATION_JSON)
	public ApplicationFormSpecializationDto getById( 
			@ApiParam(value="Entry Id of the specialization to fetch", required=true) @PathParam("specializationId") String specializationId) {
		
		ApplicationFormSpecializationDto specialization = helper.getSpecializationEntryById(applicationId,specializationId);
		return specialization;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Create a new specialization entry", response=ApplicationFormSpecializationDto.class, consumes=MediaType.APPLICATION_JSON)
	public ApplicationFormSpecializationDto create(
			ApplicationFormSpecializationDto specialization) {
		specialization = helper.createSpecializationEntry(applicationId,specialization);
		String uri = "";
		return specialization;
	}

	@PUT
	@Path("/{specializationId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Update an existing specialization", response=ApplicationFormSpecializationDto.class, 
	consumes=MediaType.APPLICATION_JSON, produces=MediaType.APPLICATION_JSON)
	public ApplicationFormSpecializationDto update( 
			@ApiParam(value="Entry Id of the specialization to update", required=true) @PathParam("specializationId") String specializationId, 
			ApplicationFormSpecializationDto specialization) {
		
		specialization = helper.updateSpecializationEntry(applicationId,specializationId, specialization);
		return specialization;
	}

	@DELETE
	@Path("/{specializationName}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Delete an existing specialization")
	public void delete(
			@ApiParam(value="Specialization Name of the specialization to delete", required=true) @PathParam("specializationName") String specializationName) {
		helper.deleteSpecializationEntry(applicationId,specializationName);
	}

}
