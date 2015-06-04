package com.icpak.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.EventsDaoHelper;
import com.icpak.rest.models.event.Event;
import com.sun.jersey.api.core.InjectParam;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Path("events")
@Api(value="events", description="Handles CRUD for Events")
public class EventsResource extends BaseResource<Event>{

	@Inject EventsDaoHelper helper;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Retrieve all active Events")
	public Response getAll(@Context UriInfo uriInfo, 
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit) {
		return buildCollectionResponse(helper.getAllEvents(uriInfo, offset, limit));
	}
	
	@Path("/{eventId}/bookings")
	public BookingsResource bookings(@InjectParam BookingsResource resource){
		return resource;
	}

	@GET
	@Path("/{eventId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Get a event by eventId", response=Event.class, consumes=MediaType.APPLICATION_JSON)
	public Response getById(@Context UriInfo uriInfo, 
			@ApiParam(value="Event Id of the event to fetch", required=true) @PathParam("eventId") String eventId) {
		
		String uri = uriInfo.getAbsolutePath().toString();
		return buildGetEntityResponse(uri, helper.getEventById(eventId));
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Create a new event", response=Event.class, consumes=MediaType.APPLICATION_JSON)
	public Response create(@Context UriInfo uriInfo, Event event) {
		
		helper.createEvent(event);
		String uri = uriInfo.getAbsolutePath().toString()+"/"+event.getRefId();
		return buildCreateEntityResponse(uri, event);
	}

	@PUT
	@Path("/{eventId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Update an existing event", response=Event.class, 
	consumes=MediaType.APPLICATION_JSON, produces=MediaType.APPLICATION_JSON)
	public Response update(@Context UriInfo uriInfo, 
			@ApiParam(value="Event Id of the event to update", required=true) @PathParam("eventId") String eventId, 
			Event event) {
		helper.updateEvent(eventId, event);
		return buildUpdateEntityResponse(uriInfo.getAbsolutePath().toString(),event);
	}

	@DELETE
	@Path("/{eventId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Delete an existing event")
	public Response delete(
			@ApiParam(value="Event Id of the event to delete", required=true) @PathParam("eventId") String eventId) {
		
		helper.deleteEvent(eventId);
		return buildDeleteEntityResponse();
	}
}
