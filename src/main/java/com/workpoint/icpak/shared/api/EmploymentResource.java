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

import com.wordnik.swagger.annotations.ApiOperation;
import com.workpoint.icpak.shared.model.ApplicationFormEmploymentDto;
import com.workpoint.icpak.shared.model.ApplicationFormSpecializationDto;

public interface EmploymentResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ApplicationFormEmploymentDto> getAll(
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);

	@GET
	@Path("/{employmentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ApplicationFormEmploymentDto getById(
			@PathParam("specializationId") String specializationId);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ApplicationFormSpecializationDto create(
			ApplicationFormSpecializationDto specialization);

	@PUT
	@Path("/{specializationId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ApplicationFormSpecializationDto update(
			@PathParam("specializationId") String specializationId,
			ApplicationFormSpecializationDto specialization);

	@DELETE
	@Path("/{specializationName}")
	@Produces(MediaType.APPLICATION_JSON)
	public void delete(
			@PathParam("specializationName") String specializationName);

}
