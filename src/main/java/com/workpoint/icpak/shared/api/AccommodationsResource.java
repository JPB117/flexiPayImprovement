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

import com.workpoint.icpak.shared.model.events.AccommodationDto;

@Produces(MediaType.APPLICATION_JSON)
public interface AccommodationsResource {

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public List<AccommodationDto> getAll(
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);
	
	@GET
	@Path("/{accommodationId}")
	public AccommodationDto getById(@PathParam("accommodationId") String accommodationId);
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public AccommodationDto create(AccommodationDto accommodation);
	

	@PUT
	@Path("/{accommodationId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public AccommodationDto update(@PathParam("accommodationId") String accommodationId,
			AccommodationDto accommodation);
	

	@DELETE
	@Path("/{accommodationId}")
	public void delete(
			@PathParam("accommodationId") String accommodationId);
}
