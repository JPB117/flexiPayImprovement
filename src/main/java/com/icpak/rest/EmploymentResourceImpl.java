package com.icpak.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.icpak.rest.dao.helper.SpecializationDaoHelper;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiParam;
import com.workpoint.icpak.shared.api.EmploymentResource;
import com.workpoint.icpak.shared.model.ApplicationFormEmploymentDto;

@Api(value = "", description = "Handles CRUD for event Application Form Training")
public class EmploymentResourceImpl implements EmploymentResource {

	@Inject
	SpecializationDaoHelper helper;
	private final String applicationId;

	@Inject
	public EmploymentResourceImpl(@Assisted String applicationId) {
		this.applicationId = applicationId;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ApplicationFormEmploymentDto> getAll(
			@ApiParam(value = "Starting point to fetch") @QueryParam("offset") Integer offset,
			@ApiParam(value = "No of Items to fetch") @QueryParam("limit") Integer limit) {
		return helper.getAllEmploymentEntrys("", applicationId, offset, limit);
	}

	@GET
	@Path("/{employmentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ApplicationFormEmploymentDto getById(
			@ApiParam(value = "Entry Id of the specialization to fetch", required = true) @PathParam("specializationId") String specializationId) {
		ApplicationFormEmploymentDto specialization = helper
				.getEmploymentEntryById(applicationId, specializationId);
		return specialization;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ApplicationFormEmploymentDto create(
			ApplicationFormEmploymentDto specialization) {
		specialization = helper.createEmploymentEntry(applicationId,
				specialization);
		String uri = "";
		return specialization;
	}

	@DELETE
	@Path("/{employmentName}")
	@Produces(MediaType.APPLICATION_JSON)
	public void delete(@PathParam("employmentName") String employmentName) {
		helper.deleteSpecializationEntry(applicationId, employmentName);
	}

}
