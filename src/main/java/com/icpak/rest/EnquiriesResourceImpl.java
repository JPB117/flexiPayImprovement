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
import com.icpak.rest.dao.helper.EnquiriesDaoHelper;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.workpoint.icpak.shared.api.BaseResource;
import com.workpoint.icpak.shared.api.EnquiriesResource;
import com.workpoint.icpak.shared.model.EnquiriesDto;

@Api(value="", description="Handles CRUD for Enquiries")
@Produces(MediaType.APPLICATION_JSON)
public class EnquiriesResourceImpl implements EnquiriesResource,BaseResource{

	@Inject EnquiriesDaoHelper helper;
	
	private String memberId;
	
	@Inject
	public EnquiriesResourceImpl(@Assisted String memberId) {
		this.memberId = memberId;
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get a list of all enquiries", response = EnquiriesDto.class, consumes = MediaType.APPLICATION_JSON)
	public List<EnquiriesDto> getAll(
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit) {
		return helper.getAll(memberId, offset, limit);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create a new enquiry", response = EnquiriesDto.class, consumes = MediaType.APPLICATION_JSON)
	public EnquiriesDto create(
			EnquiriesDto enquiry) {
		return helper.create(memberId,enquiry);
	}

	@PUT
	@Path("/{enquiryId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update an existing enquiry", response = EnquiriesDto.class, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public EnquiriesDto update(
			@ApiParam(value = "Enquiry Id", required = true) @PathParam("enquiryId") String enquiryId,
			EnquiriesDto enquiry) {

		return helper.update(memberId,enquiryId, enquiry);
	}
	
	@GET
	@Path("/count")
	public Integer getCount(){
		return helper.getCount(memberId);
	}
	
	@DELETE
	@Path("/{enquiryId}")
	@ApiOperation(value = "Delete an existing enquiry")
	public void delete(
			@ApiParam(value = "Enquiry Id of the enquiry to fetch", required = true) @PathParam("enquiryId") String enquiryId) {

		helper.delete(memberId,enquiryId);
	}
}
