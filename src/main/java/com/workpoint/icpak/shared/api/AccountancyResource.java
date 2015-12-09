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

import com.workpoint.icpak.shared.model.ApplicationFormAccountancyDto;

public interface AccountancyResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ApplicationFormAccountancyDto> getAll(
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);

	@GET
	@Path("/{eduEntryId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ApplicationFormAccountancyDto getById(
			@PathParam("eduEntryId") String eduEntryId);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ApplicationFormAccountancyDto create(
			ApplicationFormAccountancyDto accountancy);

	@PUT
	@Path("/{eduEntryId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ApplicationFormAccountancyDto update(
			@PathParam("eduEntryId") String eduEntryId,
			ApplicationFormAccountancyDto accountancy);

	@DELETE
	@Path("/{eduEntryId}")
	@Produces(MediaType.APPLICATION_JSON)
	public void delete(@PathParam("eduEntryId") String eduEntryId);

	@GET
	@Path("academics")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ApplicationFormAccountancyDto> getAllAcademic(
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);

	@GET
	@Path("professional")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ApplicationFormAccountancyDto> getAllProfessional(
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);
}
