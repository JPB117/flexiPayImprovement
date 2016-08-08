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
import com.workpoint.icpak.shared.model.events.BookingDto;
import com.workpoint.icpak.shared.model.events.BookingSummaryDto;
import com.workpoint.icpak.shared.model.events.DelegateDto;

public interface BookingsResource extends BaseResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<BookingDto> getAll(@QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit);

	@GET
	@Path("/{bookingId}")
	@Produces(MediaType.APPLICATION_JSON)
	public BookingDto getById(@PathParam("bookingId") String bookingId);

	@GET
	@Path("/summary/{eventRefId}/{isRefresh}")
	@Produces(MediaType.APPLICATION_JSON)
	public BookingSummaryDto getBookingSummary(@PathParam("eventRefId") String eventRefId,
			@PathParam("isRefresh") String isRefresh);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BookingDto create(BookingDto booking);

	@POST
	@Path("/instantBooking")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create Booking by member No", consumes = MediaType.APPLICATION_JSON)
	public BookingDto createBookingByMemberNo(DelegateDto delegate);

	@POST
	@Path("/{bookingId}/payment")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BookingDto makePayment(@PathParam("bookingId") String bookingId,
			@QueryParam("paymentMode") String paymentMode, @QueryParam("paymentRef") String paymentRef);

	@PUT
	@Path("/{bookingId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BookingDto update(@PathParam("bookingId") String bookingId, BookingDto booking);

	@GET
	@Path("/filteredcount")
	public Integer getBookingCount(@QueryParam("searchTerm") String searchTerm,
			@QueryParam("accomodationRefId") String accomodationRefId,
			@QueryParam("bookingStatus") String bookingStatus);

	@POST
	@Path("/cancel/{bookingId}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean cancelBooking(@PathParam("bookingId") String bookingId);

	@POST
	@Path("/{bookingId}")
	@Produces(MediaType.APPLICATION_JSON)
	public void sendAlert(@PathParam("bookingId") String bookingId);

	@POST
	@Path("/resendProforma/{emails}/{bookingRefId}")
	@Produces(MediaType.APPLICATION_JSON)
	public void resendProforma(@PathParam("emails") String emailAddress,
			@PathParam("bookingRefId") String bookingRefId);

	@DELETE
	@Path("/{bookingId}")
	@Produces(MediaType.APPLICATION_JSON)
	public void delete(@PathParam("bookingId") String bookingId);

	@PUT
	@Path("/{bookingId}/delegates/{delegateId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public DelegateDto updateDelegate(@PathParam("bookingId") String bookingId,
			@PathParam("delegateId") String delegateId, DelegateDto delegate);

}