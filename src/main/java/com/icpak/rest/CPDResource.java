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

import org.apache.shiro.authz.annotation.RequiresAuthentication;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.CPDDaoHelper;
import com.icpak.rest.models.cpd.CPD;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;


@RequiresAuthentication
@Path("")
@Api(value = "", description = "Handles CRUD on cpd data")
public class CPDResource extends BaseResource<CPD> {

		@Inject
		CPDDaoHelper helper;

		@GET
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		@ApiOperation(value = "Get a list of all cpds", response = CPD.class, consumes = MediaType.APPLICATION_JSON)
		public Response getAll(@Context UriInfo uriInfo,
				@PathParam("memberId") String memberId,
				@QueryParam("offset") Integer offset,
				@QueryParam("limit") Integer limit) {

			return buildCollectionResponse(helper.getAllCPD(memberId,offset, limit,
					uriInfo));
		}

		@GET
		@Path("/{cpdId}")
		@Produces(MediaType.APPLICATION_JSON)
		@ApiOperation(value = "Get a cpd by cpdId", response = CPD.class, consumes = MediaType.APPLICATION_JSON)
		public Response getById(
				@Context UriInfo uriInfo,
				@PathParam("memberId") String memberId,
				@ApiParam(value = "CPD Id of the cpd to fetch", required = true) @PathParam("cpdId") String cpdId) {

			CPD cpd = helper.getCPD(memberId,cpdId);
			return buildGetEntityResponse(uriInfo.getAbsolutePath().toString(),
					cpd);
		}

		@POST
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		@ApiOperation(value = "Create a new cpd", response = CPD.class, consumes = MediaType.APPLICATION_JSON)
		public Response create(@Context UriInfo uriInfo,
				@PathParam("memberId") String memberId,
				CPD cpd) {
			
			helper.create(memberId,cpd);
			String uri = uriInfo.getAbsolutePath().toString() + "/"
					+ cpd.getRefId();
			return buildCreateEntityResponse(uri, cpd);
		}

		@PUT
		@Path("/{cpdId}")
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		@ApiOperation(value = "Update an existing cpd", response = CPD.class, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
		public Response update(
				@Context UriInfo uriInfo,
				@PathParam("memberId") String memberId,
				@ApiParam(value = "CPD Id of the cpd to fetch", required = true) @PathParam("cpdId") String cpdId,
				CPD cpd) {

			helper.update(memberId,cpdId, cpd);
			String uri = uriInfo.getAbsolutePath().toString();
			return buildUpdateEntityResponse(uri, cpd);
		}

		@DELETE
		@Path("/{cpdId}")
		@Produces(MediaType.APPLICATION_JSON)
		@ApiOperation(value = "Delete an existing cpd")
		public Response delete(
				@PathParam("memberId") String memberId,
				@ApiParam(value = "CPD Id of the cpd to fetch", required = true) @PathParam("cpdId") String cpdId) {

			helper.delete(memberId,cpdId);

			return buildDeleteEntityResponse();
		}

}
