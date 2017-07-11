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

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.CoursesDaoHelper;
import com.icpak.rest.factory.ResourceFactory;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.workpoint.icpak.shared.model.EventType;
import com.workpoint.icpak.shared.model.events.CourseDto;

@Path("courses")
@Api(value = "courses", description = "Handles CRUD for Courses")
public class CoursesResourceImpl {
	Logger logger = Logger.getLogger(CoursesResourceImpl.class);

	@Inject
	CoursesDaoHelper helper;
	@Inject
	ResourceFactory factory;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retrieve all active Courses")
	public List<CourseDto> getAll(
			@ApiParam(value = "Starting index", required = true) @QueryParam("offset") Integer offset,
			@ApiParam(value = "Number of items to retrieve", required = true) @QueryParam("limit") Integer limit) {
		logger.info("+++++ GET ALL COURSES ++++ ");
		List<CourseDto> dtos = helper.getAllEvents("", offset, limit, EventType.COURSE.name());
		return dtos;
	}

	@Path("/{courseId}/enrollments")
	public EnrollmentsResourceImpl bookings(@PathParam("courseId") String courseId) {
		return factory.createEnrollmentsResource(courseId);
	}

	// @Path("/{courseId}/accommodations")
	// public AccommodationsResource accommodations(@PathParam("courseId")
	// String courseId){
	// return factory.createAccommodationsResource(courseId);
	// }

	@GET
	@Path("/{courseId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get a course by courseId", response = CourseDto.class, consumes = MediaType.APPLICATION_JSON)
	public CourseDto getById(
			@ApiParam(value = "Course Id of the course to fetch", required = true) @PathParam("courseId") String courseId) {

		CourseDto dto = helper.getEventById(courseId);

		return dto;
	}

	@GET
	@Path("/{lmsCourseId}/findbyid")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get a course by courseId", response = CourseDto.class, consumes = MediaType.APPLICATION_JSON)
	public CourseDto getByLongId(
			@ApiParam(value = "Course long Id of the course to fetch", required = true) @PathParam("lmsCourseId") Integer id) {

		CourseDto dto = helper.getCourseByLongId(id);

		return dto;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create a new course", response = CourseDto.class, consumes = MediaType.APPLICATION_JSON)
	public CourseDto create(CourseDto course) {
		return helper.createEvent(course);
	}

	@PUT
	@Path("/{courseRefId}/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update an existing course", response = CourseDto.class, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public CourseDto update(
			@ApiParam(value = "Course Id of the course to update", required = true) @PathParam("courseRefId") String courseRefId,
			CourseDto course) {
		logger.info(" +++ UPDATE EXISTING COURSE API ++++ ");
		CourseDto dto = helper.updateEvent(courseRefId, course);
		return dto;
	}

	@DELETE
	@Path("/{courseRefId}/remove")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Delete an existing course")
	public void delete(
			@ApiParam(value = "Course Id of the course to delete", required = true) @PathParam("courseRefId") String courseRefId) {
		helper.deleteEvent(courseRefId);
	}

	@PUT
	@Path("/updateScore")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update member from lms course attendance", response = String.class, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public String updateCPD(
			@ApiParam(value = "Course Done", required = true) @QueryParam("lmsCourseId") Integer lmsCourseId,
			@ApiParam(value = "Member no done course", required = true) @QueryParam("memberNo") String memberNo) {
		return helper.updateCPDCOurse(lmsCourseId, memberNo);
	}

}
