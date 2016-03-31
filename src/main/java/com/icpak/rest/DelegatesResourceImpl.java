package com.icpak.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.icpak.rest.dao.helper.BookingsDaoHelper;
import com.sun.jersey.api.core.HttpContext;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.workpoint.icpak.shared.api.DelegatesResource;
import com.workpoint.icpak.shared.model.events.BookingDto;
import com.workpoint.icpak.shared.model.events.DelegateDto;

@Api(value = "", description = "Handles CRUD for delegates in an event")
public class DelegatesResourceImpl implements DelegatesResource {

	@Inject
	BookingsDaoHelper helper;

	private final String eventId;
	@Context
	HttpContext httpContext;

	@Inject
	public DelegatesResourceImpl(@Assisted String eventId) {
		this.eventId = eventId;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retrieve all active bookings")
	public List<DelegateDto> getAll(
			@ApiParam(value = "Starting point to fetch") @QueryParam("offset") Integer offset,
			@ApiParam(value = "No of Items to fetch") @QueryParam("limit") Integer limit,
			@QueryParam("searchTerm") String searchTerm,
			@QueryParam("accomodationRefId") String accomodationRefId,
			@QueryParam("bookingStatus") String bookingStatus) {
		String uri = "";
		List<DelegateDto> dtos = helper.getAllDelegates("", eventId, offset,
				limit, searchTerm, accomodationRefId, bookingStatus);
		return dtos;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retrieve booking by qr code")
	@Path("/qrCodeSearch")
	public List<DelegateDto> getByQrCode(
			@QueryParam("searchTerm") String searchTerm) {
		String uri = "";
		List<DelegateDto> dtos = helper.getDelegateByQrCode("", eventId, 0, 10,
				searchTerm);
		return dtos;
	}

	@GET
	@Path("/count")
	public Integer getCount() {
		return getSearchCount("", "", "");
	}

	@GET
	@Path("/searchCount")
	public Integer getSearchCount(@QueryParam("searchTerm") String searchTerm,
			@QueryParam("accomodationRefId") String accomodationRefId,
			@QueryParam("bookingStatus") String bookingStatus) {
		return helper.getDelegatesCount(eventId, searchTerm, accomodationRefId,
				bookingStatus);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/checkExist")
	public BookingDto checkExist(@QueryParam("email") String email) {
		return helper.checkEmailExists(email, eventId);
	}
}
