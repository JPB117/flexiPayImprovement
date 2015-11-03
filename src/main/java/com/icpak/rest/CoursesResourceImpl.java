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

		List<CourseDto> dtos = helper.getAllEvents("", offset, limit,
				EventType.COURSE.name());
		return dtos;
	}

	@Path("/{courseId}/enrollments")
	public EnrollmentsResourceImpl bookings(
			@PathParam("courseId") String courseId) {
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
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create a new course", response = CourseDto.class, consumes = MediaType.APPLICATION_JSON)
	public CourseDto create(CourseDto course) {
		return helper.createEvent(course);
	}
	@PUT
	@Path("/{courseId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update an existing course", response = CourseDto.class, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public CourseDto update(
			@ApiParam(value = "Course Id of the course to update", required = true) @PathParam("courseId") String courseId,
			CourseDto course) {
		helper.updateEvent(courseId, course);
		return course;
	}

	@DELETE
	@Path("/{courseId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Delete an existing course")
	public void delete(
			@ApiParam(value = "Course Id of the course to delete", required = true) @PathParam("courseId") String courseId) {
		helper.deleteEvent(courseId);
	}
}
