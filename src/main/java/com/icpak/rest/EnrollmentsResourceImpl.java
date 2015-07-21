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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.icpak.rest.dao.helper.EnrollmentsDaoHelper;
import com.sun.jersey.api.core.HttpContext;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.workpoint.icpak.shared.model.events.EnrollmentDto;

@Api(value = "", description = "Handles CRUD for event Enrollments")
public class EnrollmentsResourceImpl {

	@Inject
	EnrollmentsDaoHelper helper;

	private final String courseId;
	@Context HttpContext httpContext;
	
	@Inject
	public EnrollmentsResourceImpl(@Assisted String courseId) {
		this.courseId = courseId;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retrieve all active enrollments")
	public List<EnrollmentDto> getAll(
			@ApiParam(value = "Starting point to fetch") @QueryParam("offset") Integer offset,
			@ApiParam(value = "No of Items to fetch") @QueryParam("limit") Integer limit) {
		String uri = "";
		List<EnrollmentDto> dtos = helper.getAllBookings(uri, courseId, offset,limit);
		return dtos;
	}

	@GET
	@Path("/{enrollmentId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get a enrollment by enrollmentId", response = EnrollmentDto.class, consumes = MediaType.APPLICATION_JSON)
	public EnrollmentDto getById(
			@ApiParam(value = "Enrollment Id of the enrollment to fetch", required = true) @PathParam("enrollmentId") String enrollmentId) {

		String uri = "";
		EnrollmentDto dto = helper.getBookingById(courseId, enrollmentId);
		return dto;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create a new enrollment", response = EnrollmentDto.class, consumes = MediaType.APPLICATION_JSON)
	public EnrollmentDto create(
	// @ApiParam(value="Event for which enrollment is being created")
	// @PathParam("courseId") String courseId,
			EnrollmentDto dto) {

		String uri = "";
//		URI requestUri = httpContext.getRequest().getRequestUri();
//		String host = requestUri.getHost();
//		int port = requestUri.getPort();
//		uri = host+":"+port;
		
		helper.createBooking(courseId, dto);
		uri = uri + "/" + dto.getRefId();
		dto.setUri(uri);

		return dto;
	}

	@POST
	@Path("/{enrollmentId}/payment")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Make Payment for a enrollment", consumes = MediaType.APPLICATION_JSON)
	public EnrollmentDto makePayment(
			// @ApiParam(value="Event for which payment is being made")
			// @PathParam("courseId") String courseId,
			@ApiParam(value = "Enrollment for which payment is being made") @PathParam("enrollmentId") String enrollmentId,
			@ApiParam(value = "Payment Mode") @QueryParam("paymentMode") String paymentMode,
			@ApiParam(value = "Payment referenceNo") @QueryParam("paymentRef") String paymentRef) {
		// TODO
		String uri = "";

		EnrollmentDto dto = helper.processPayment(courseId, enrollmentId, paymentMode,
				paymentRef);

		return dto;
	}

	@PUT
	@Path("/{enrollmentId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update an existing enrollment", response = EnrollmentDto.class, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public EnrollmentDto update(
			// @ApiParam(value="Event for which enrollment is being updated")
			// @PathParam("courseId") String courseId,
			@ApiParam(value = "Enrollment Id of the enrollment to update", required = true) @PathParam("enrollmentId") String enrollmentId,
			EnrollmentDto dto) {

		String uri = "";
		dto = helper.updateBooking(courseId, enrollmentId, dto);
		return dto;
	}

	@DELETE
	@Path("/{enrollmentId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Delete an existing enrollment")
	public void delete(
	// @ApiParam(value="Event from which enrollment is being deleted")
	// @PathParam("courseId") String courseId,
			@ApiParam(value = "Enrollment Id of the enrollment to delete", required = true) @PathParam("enrollmentId") String enrollmentId) {
		helper.deleteBooking(courseId, enrollmentId);
	}
	
//	@PUT
//	@Path("/{enrollmentId}/delegates/{delegateId}")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public DelegateDto updateDelegate(@PathParam("enrollmentId") String enrollmentId,
//			@PathParam("delegateId") String delegateId,
//			DelegateDto delegate){
//		
//		return helper.updateDelegate(enrollmentId, delegateId, delegate);
//	}

}
