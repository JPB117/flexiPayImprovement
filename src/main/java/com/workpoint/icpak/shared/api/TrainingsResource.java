package com.workpoint.icpak.shared.api;

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

import com.workpoint.icpak.shared.model.ApplicationFormTrainingDto;

public interface TrainingsResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ApplicationFormTrainingDto> getAll(@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);
		
	@GET
	@Path("/{trainingId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ApplicationFormTrainingDto getById( 
			@PathParam("trainingId") String trainingId);
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ApplicationFormTrainingDto create(
			ApplicationFormTrainingDto education);
	
	@PUT
	@Path("/{trainingId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ApplicationFormTrainingDto update( 
			@PathParam("trainingId") String trainingId, 
			ApplicationFormTrainingDto education);
	
	@DELETE
	@Path("/{trainingId}")
	@Produces(MediaType.APPLICATION_JSON)
	public void delete(
			@PathParam("trainingId") String trainingId);
	
	
}
