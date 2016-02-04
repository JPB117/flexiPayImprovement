package com.workpoint.icpak.shared.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.workpoint.icpak.shared.model.ApplicationFormEmploymentDto;

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
			@PathParam("employmentId") String employmentId);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ApplicationFormEmploymentDto create(
			ApplicationFormEmploymentDto employment);

	@DELETE
	@Path("/{employmentName}")
	@Produces(MediaType.APPLICATION_JSON)
	public void delete(@PathParam("employmentName") String employmentName);

}
