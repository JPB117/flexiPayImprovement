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
import com.icpak.rest.dao.helper.AccountancyDaoHelper;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.workpoint.icpak.shared.api.AccountancyResource;
import com.workpoint.icpak.shared.model.ApplicationFormAccountancyDto;
import com.workpoint.icpak.shared.model.EduType;

@Api(value = "", description = "Handles CRUD for event ApplicationFormAccountancyDto")
public class AccountancyResourceImpl implements AccountancyResource {

	@Inject
	AccountancyDaoHelper helper;

	private final String applicationId;

	@Inject
	public AccountancyResourceImpl(@Assisted String applicationId) {
		this.applicationId = applicationId;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retrieve all active Accountancy entries")
	public List<ApplicationFormAccountancyDto> getAll(
			@ApiParam(value = "Starting point to fetch") @QueryParam("offset") Integer offset,
			@ApiParam(value = "No of Items to fetch") @QueryParam("limit") Integer limit) {

		return helper.getAllAccountancyEntrys("", applicationId, offset, limit);
	}

	@GET
	@Path("/{eduEntryId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get Accountancy by eduEntryId", response = ApplicationFormAccountancyDto.class, consumes = MediaType.APPLICATION_JSON)
	public ApplicationFormAccountancyDto getById(
			@ApiParam(value = "Entry Id of the Accountancy to fetch", required = true) @PathParam("eduEntryId") String eduEntryId) {
		ApplicationFormAccountancyDto Accountancy = helper
				.getAccountancyEntryById(applicationId, eduEntryId);
		return Accountancy;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create a new Accountancy entry", response = ApplicationFormAccountancyDto.class, consumes = MediaType.APPLICATION_JSON)
	public ApplicationFormAccountancyDto create(
			ApplicationFormAccountancyDto Accountancy) {
		Accountancy = helper.createAccountancyEntry(applicationId, Accountancy);
		String uri = "";
		return Accountancy;
	}

	@PUT
	@Path("/{eduEntryId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update an existing Accountancy", response = ApplicationFormAccountancyDto.class, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ApplicationFormAccountancyDto update(
			@ApiParam(value = "Entry Id of the Accountancy to update", required = true) @PathParam("eduEntryId") String eduEntryId,
			ApplicationFormAccountancyDto Accountancy) {

		Accountancy = helper.updateAccountancyEntry(applicationId, eduEntryId,
				Accountancy);
		return Accountancy;
	}

	@DELETE
	@Path("/{eduEntryId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Delete an existing Accountancy")
	public void delete(
			@ApiParam(value = "Entry Id of the Accountancy to delete", required = true) @PathParam("eduEntryId") String eduEntryId) {
		helper.deleteAccountancyEntry(applicationId, eduEntryId);
	}

	@GET
	@Path("academics")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retrieve all active academic Accountancy entries")
	public List<ApplicationFormAccountancyDto> getAllAcademic(
			@ApiParam(value = "Starting point to fetch") @QueryParam("offset") Integer offset,
			@ApiParam(value = "No of Items to fetch") @QueryParam("limit") Integer limit) {

		return helper.getAllAccountancyEntrys("", applicationId,
				EduType.ACADEMIA, offset, limit);
	}

	@GET
	@Path("professional")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retrieve all active professional Accountancy entries")
	public List<ApplicationFormAccountancyDto> getAllProfessional(
			@ApiParam(value = "Starting point to fetch") @QueryParam("offset") Integer offset,
			@ApiParam(value = "No of Items to fetch") @QueryParam("limit") Integer limit) {

		return helper.getAllAccountancyEntrys("", applicationId,
				EduType.PROFESSIONALACCEXAMS, offset, limit);
	}

}
