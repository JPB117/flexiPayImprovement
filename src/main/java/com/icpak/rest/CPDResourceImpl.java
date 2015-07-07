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
import com.icpak.rest.dao.helper.CPDDaoHelper;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.workpoint.icpak.shared.api.CPDResource;
import com.workpoint.icpak.shared.model.CPDDto;

@Api(value = "", description = "Handles CRUD on cpd data")
public class CPDResourceImpl implements CPDResource{

		@Inject
		CPDDaoHelper helper;
		private final String memberId;
		
		@Inject
		public CPDResourceImpl(@Assisted String memberId) {
			this.memberId = memberId;
		}
		
		@GET
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		@ApiOperation(value = "Get a list of all cpds", response = CPDDto.class, consumes = MediaType.APPLICATION_JSON)
		public List<CPDDto> getAll(
				@QueryParam("offset") Integer offset,
				@QueryParam("limit") Integer limit) {

			return helper.getAllCPD(memberId,offset, limit);
		}

		@GET
		@Path("/{cpdId}")
		@Produces(MediaType.APPLICATION_JSON)
		@ApiOperation(value = "Get a cpd by cpdId", response = CPDDto.class, consumes = MediaType.APPLICATION_JSON)
		public CPDDto getById(
				@ApiParam(value = "CPD Id of the cpd to fetch", required = true) @PathParam("cpdId") String cpdId) {

			CPDDto cpd = helper.getCPD(memberId,cpdId);
			return cpd;
		}

		@POST
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		@ApiOperation(value = "Create a new cpd", response = CPDDto.class, consumes = MediaType.APPLICATION_JSON)
		public CPDDto create(
				CPDDto cpd) {
			return helper.create(memberId,cpd);
		}

		@PUT
		@Path("/{cpdId}")
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		@ApiOperation(value = "Update an existing cpd", response = CPDDto.class, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
		public CPDDto update(
				@ApiParam(value = "CPD Id of the cpd to fetch", required = true) @PathParam("cpdId") String cpdId,
				CPDDto cpd) {

			return helper.update(memberId,cpdId, cpd);
			
		}

		@DELETE
		@Path("/{cpdId}")
		@Produces(MediaType.APPLICATION_JSON)
		@ApiOperation(value = "Delete an existing cpd")
		public void delete(
				@ApiParam(value = "CPD Id of the cpd to fetch", required = true) @PathParam("cpdId") String cpdId) {

			helper.delete(memberId,cpdId);
		}
}
