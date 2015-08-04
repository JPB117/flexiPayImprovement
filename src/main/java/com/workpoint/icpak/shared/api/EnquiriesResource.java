package com.workpoint.icpak.shared.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.workpoint.icpak.shared.model.EnquiriesDto;


public interface EnquiriesResource {

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public List<EnquiriesDto> getAll(
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public EnquiriesDto create(
			EnquiriesDto enquiry);

	@PUT
	@Path("/{enquiryId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public EnquiriesDto update(@PathParam("enquiryId") String enquiryId,
			EnquiriesDto enquiry);	
	@GET
	@Path("/count")
	public Integer getCount();
	
	@DELETE
	@Path("/{enquiryId}")
	public void delete(
			@PathParam("enquiryId") String enquiryId);	
}
