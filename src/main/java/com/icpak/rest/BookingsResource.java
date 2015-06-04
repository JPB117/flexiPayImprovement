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
import com.icpak.rest.dao.helper.BookingsDaoHelper;
import com.icpak.rest.models.event.Booking;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Api(value="", description="Handles CRUD for event Bookings")
public class BookingsResource extends BaseResource<Booking>{

	@Inject BookingsDaoHelper helper;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Retrieve all active bookings")
	public Response getAll(@Context UriInfo uriInfo,
			@ApiParam(value="Event for which bookings are requested") @PathParam("eventId") String eventId,
			@ApiParam(value="Starting point to fetch") @QueryParam("offset") Integer offset,
			@ApiParam(value="No of Items to fetch") @QueryParam("limit") Integer limit) {
		
		return buildCollectionResponse(helper.getAllBookings(uriInfo, eventId, offset, limit));
	}

	@GET
	@Path("/{bookingId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Get a booking by bookingId", response=Booking.class, consumes=MediaType.APPLICATION_JSON)
	public Response getById(@Context UriInfo uriInfo, 
			@ApiParam(value="Event for which the booking are requested") @PathParam("eventId") String eventId,
			@ApiParam(value="Booking Id of the booking to fetch", required=true) @PathParam("bookingId") String bookingId) {
		
		Booking booking = helper.getBookingById(eventId,bookingId);
		booking.getEvent().setUri(uriInfo.getBaseUri()+"events/"+booking.getEvent().getRefId());
		//booking.getUser().setUri(uriInfo.getBaseUri()+"users/"+booking.getUser().getRefId());
		return buildGetEntityResponse(uriInfo.getAbsolutePath().toString(), booking);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Create a new booking", response=Booking.class, consumes=MediaType.APPLICATION_JSON)
	public Response create(@Context UriInfo uriInfo,
			@ApiParam(value="Event for which booking is being created") @PathParam("eventId") String eventId,
			Booking booking) {
		
		booking = helper.createBooking(eventId,booking);
		String uri = uriInfo.getAbsolutePath()+"/"+booking.getRefId();
		booking.getEvent().setUri(uriInfo.getBaseUri()+"events/"+booking.getEvent().getRefId());
//		booking.getUser().setUri(uriInfo.getBaseUri()+"users/"+booking.getUser().getRefId());
		
		return buildCreateEntityResponse(uri, booking);
	}
	
	@POST
	@Path("/{bookingId}/payment")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Make Payment for a booking", consumes=MediaType.APPLICATION_JSON)
	public Response makePayment(@Context UriInfo uriInfo,
			@ApiParam(value="Event for which payment is being made") @PathParam("eventId") String eventId,
			@ApiParam(value="Booking for which payment is being made") @PathParam("bookingId") String bookingId,
			@ApiParam(value="Payment Mode") @QueryParam("paymentMode") String paymentMode,
			@ApiParam(value="Payment referenceNo") @QueryParam("paymentRef") String paymentRef) {
		//TODO
		helper.processPayment(eventId, bookingId, paymentMode, paymentRef);
		return buildEmptySuccessResponse();
	}

	@PUT
	@Path("/{bookingId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Update an existing booking", response=Booking.class, 
	consumes=MediaType.APPLICATION_JSON, produces=MediaType.APPLICATION_JSON)
	public Response update(@Context UriInfo uriInfo, 
			@ApiParam(value="Event for which booking is being updated") @PathParam("eventId") String eventId,
			@ApiParam(value="Booking Id of the booking to update", required=true) @PathParam("bookingId") String bookingId, 
			Booking booking) {
		
		booking = helper.updateBooking(eventId,bookingId, booking);
		booking.getEvent().setUri(uriInfo.getBaseUri()+"events/"+booking.getEvent().getRefId());
//		booking.getUser().setUri(uriInfo.getBaseUri()+"users/"+booking.getUser().getRefId());
		return buildUpdateEntityResponse(uriInfo.getAbsolutePath().toString(), booking);
	}

	@DELETE
	@Path("/{bookingId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Delete an existing booking")
	public Response delete(
			@ApiParam(value="Event from which booking is being deleted") @PathParam("eventId") String eventId,
			@ApiParam(value="Booking Id of the booking to delete", required=true) @PathParam("bookingId") String bookingId) {
		helper.deleteBooking(eventId,bookingId);
		return buildDeleteEntityResponse();
	}

}
