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
import com.icpak.rest.dao.EventsDao;
import com.icpak.rest.dao.helper.BookingsDaoHelper;
import com.icpak.rest.dao.helper.EventsDaoHelper;
import com.icpak.rest.factory.ResourceFactory;
import com.icpak.rest.models.event.Event;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.workpoint.icpak.server.payment.CreditCardServiceImpl;
import com.workpoint.icpak.shared.api.EventsResource;
import com.workpoint.icpak.shared.model.EventSummaryDto;
import com.workpoint.icpak.shared.model.events.EventDto;

@Path("events")
@Api(value = "events", description = "Handles CRUD for Events")
public class EventsResourceImpl implements EventsResource {

	@Inject
	EventsDaoHelper helper;
	@Inject
	EventsDao eventsDao;
	@Inject
	ResourceFactory factory;
	@Inject
	CreditCardServiceImpl creditCardService;
	@Inject
	BookingsDaoHelper bookingsDaoHelper;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retrieve all active Events")
	public List<EventDto> getAll(
			@ApiParam(value = "Starting index", required = true) @QueryParam("offset") Integer offset,
			@ApiParam(value = "Number of items to retrieve", required = true) @QueryParam("limit") Integer limit,
			@QueryParam("searchTerm") String searchTerm) {

		List<EventDto> dtos = helper.getAllEvents("", offset, limit, null, searchTerm);
		return dtos;
	}

	@GET
	@Path("/count")
	public Integer getCount() {
		return helper.getCount();
	}

	@GET
	@Path("/searchCount")
	public Integer getSearchCount(@QueryParam("searchTerm") String searchTerm) {
		return helper.getCount(searchTerm);
	}

	@Path("/type/{eventType}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retrieve all active Events by Event Type")
	public List<EventDto> getAllByType(
			@ApiParam(value = "Event Type", required = true) @QueryParam("eventType") String eventType,
			@ApiParam(value = "Starting index", required = true) @QueryParam("offset") Integer offset,
			@ApiParam(value = "Number of items to retrieve", required = true) @QueryParam("limit") Integer limit) {

		List<EventDto> dtos = helper.getAllEvents("", offset, limit, eventType);
		return dtos;
	}

	@Path("/{eventId}/bookings")
	public BookingsResourceImpl bookings(@PathParam("eventId") String eventId) {
		return factory.createBookingResource(eventId);
	}

	@Path("/{eventId}/delegates")
	public DelegatesResourceImpl delegates(@PathParam("eventId") String eventId) {
		return factory.createDelegatesResource(eventId);
	}

	@Path("/{eventId}/accommodations")
	public AccommodationsResourceImpl accommodations(@PathParam("eventId") String eventId) {
		return factory.createAccommodationsResource(eventId);
	}

	@GET
	@Path("/{eventId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get a event by eventId", response = Event.class, consumes = MediaType.APPLICATION_JSON)
	public EventDto getById(
			@ApiParam(value = "Event Id of the event to fetch", required = true) @PathParam("eventId") String eventId) {
		EventDto dto = helper.getEventById(eventId);
		return dto;
	}

	@GET
	@Path("/summary")
	@Produces(MediaType.APPLICATION_JSON)
	public EventSummaryDto getEventsSummary() {
		return eventsDao.getEventsSummary();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create a new event", response = Event.class, consumes = MediaType.APPLICATION_JSON)
	public EventDto create(EventDto event) {
		return helper.createEvent(event);
	}

	@PUT
	@Path("/{eventId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update an existing event", response = Event.class, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public EventDto update(
			@ApiParam(value = "Event Id of the event to update", required = true) @PathParam("eventId") String eventId,
			EventDto event) {
		helper.updateEvent(eventId, event);
		return event;
	}

	@DELETE
	@Path("/{eventId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Delete an existing event")
	public void delete(
			@ApiParam(value = "Event Id of the event to delete", required = true) @PathParam("eventId") String eventId) {
		helper.deleteEvent(eventId);
	}

}
