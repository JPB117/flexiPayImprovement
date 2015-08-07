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
import com.icpak.rest.dao.helper.TrainingDaoHelper;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.workpoint.icpak.shared.api.TrainingsResource;
import com.workpoint.icpak.shared.model.ApplicationFormTrainingDto;

@Api(value="", description="Handles CRUD for event Application Form Training")
public class TrainingsResourceImpl implements TrainingsResource{

	@Inject TrainingDaoHelper helper;
	
	private final String applicationId;
	
	@Inject
	public TrainingsResourceImpl(@Assisted String applicationId) {
		this.applicationId = applicationId;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Retrieve all active training entries")
	public List<ApplicationFormTrainingDto> getAll(
			@ApiParam(value="Starting point to fetch") @QueryParam("offset") Integer offset,
			@ApiParam(value="No of Items to fetch") @QueryParam("limit") Integer limit) {
		
		return helper.getAllTrainingEntrys("", applicationId, offset, limit);
	}
	
	@GET
	@Path("/{eduEntryId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Get training by eduEntryId", response=ApplicationFormTrainingDto.class, consumes=MediaType.APPLICATION_JSON)
	public ApplicationFormTrainingDto getById( 
			@ApiParam(value="Entry Id of the training to fetch", required=true) @PathParam("eduEntryId") String eduEntryId) {
		
		ApplicationFormTrainingDto training = helper.getTrainingEntryById(applicationId,eduEntryId);
		return training;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Create a new training entry", response=ApplicationFormTrainingDto.class, consumes=MediaType.APPLICATION_JSON)
	public ApplicationFormTrainingDto create(
			ApplicationFormTrainingDto training) {
		training = helper.createTrainingEntry(applicationId,training);
		String uri = "";
		return training;
	}

	@PUT
	@Path("/{eduEntryId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Update an existing training", response=ApplicationFormTrainingDto.class, 
	consumes=MediaType.APPLICATION_JSON, produces=MediaType.APPLICATION_JSON)
	public ApplicationFormTrainingDto update( 
			@ApiParam(value="Entry Id of the training to update", required=true) @PathParam("eduEntryId") String eduEntryId, 
			ApplicationFormTrainingDto training) {
		
		training = helper.updateTrainingEntry(applicationId,eduEntryId, training);
		return training;
	}

	@DELETE
	@Path("/{eduEntryId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Delete an existing training")
	public void delete(
			@ApiParam(value="Entry Id of the training to delete", required=true) @PathParam("eduEntryId") String eduEntryId) {
		helper.deleteTrainingEntry(applicationId,eduEntryId);
	}

}
