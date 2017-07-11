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
import com.icpak.rest.dao.helper.AccommodationsDaoHelper;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.workpoint.icpak.shared.api.AccommodationsResource;
import com.workpoint.icpak.shared.model.events.AccommodationDto;

@Api(value = "", description = "Handles CRUD on accommodation data")
public class AccommodationsResourceImpl implements AccommodationsResource{

		@Inject
		AccommodationsDaoHelper helper;
		private final String eventId;
		
		@Inject
		public AccommodationsResourceImpl(@Assisted String eventId) {
			this.eventId = eventId;
		}
		
		@GET
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		@ApiOperation(value = "Get a list of all accommodations", response = AccommodationDto.class, consumes = MediaType.APPLICATION_JSON)
		public List<AccommodationDto> getAll(
				@QueryParam("offset") Integer offset,
				@QueryParam("limit") Integer limit) {

			return helper.getAllAccommodations(eventId,offset, limit);
		}

		@GET
		@Path("/{accommodationId}")
		@Produces(MediaType.APPLICATION_JSON)
		@ApiOperation(value = "Get a accommodation by accommodationId", response = AccommodationDto.class, consumes = MediaType.APPLICATION_JSON)
		public AccommodationDto getById(
				@ApiParam(value = "Accommodation Id of the accommodation to fetch", required = true) @PathParam("accommodationId") String accommodationId) {

			AccommodationDto accommodation = helper.getAccommodation(eventId,accommodationId);
			return accommodation;
		}

		@POST
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		@ApiOperation(value = "Create a new accommodation", response = AccommodationDto.class, consumes = MediaType.APPLICATION_JSON)
		public AccommodationDto create(
				AccommodationDto accommodation) {
			return helper.create(eventId,accommodation);
		}

		@PUT
		@Path("/{accommodationId}")
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		@ApiOperation(value = "Update an existing accommodation", response = AccommodationDto.class, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
		public AccommodationDto update(
				@ApiParam(value = "Accommodation Id of the accommodation to fetch", required = true) @PathParam("accommodationId") String accommodationId,
				AccommodationDto accommodation) {

			return helper.update(eventId,accommodationId, accommodation);
			
		}

		@DELETE
		@Path("/{accommodationId}")
		@Produces(MediaType.APPLICATION_JSON)
		@ApiOperation(value = "Delete an existing accommodation")
		public void delete(
				@ApiParam(value = "Accommodation Id of the accommodation to fetch", required = true) @PathParam("accommodationId") String accommodationId) {

			helper.delete(eventId,accommodationId);
		}
}
