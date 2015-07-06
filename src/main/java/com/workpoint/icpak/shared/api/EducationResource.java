package com.workpoint.icpak.shared.api;

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

import com.workpoint.icpak.shared.model.ApplicationFormEducationalDto;

public interface EducationResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ApplicationFormEducationalDto> getAll(@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);
		
	@GET
	@Path("/{eduEntryId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ApplicationFormEducationalDto getById( 
			@PathParam("eduEntryId") String eduEntryId);
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ApplicationFormEducationalDto create(
			ApplicationFormEducationalDto education);
	
	@PUT
	@Path("/{eduEntryId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ApplicationFormEducationalDto update( 
			@PathParam("eduEntryId") String eduEntryId, 
			ApplicationFormEducationalDto education);
	
	@DELETE
	@Path("/{eduEntryId}")
	@Produces(MediaType.APPLICATION_JSON)
	public void delete(
			@PathParam("eduEntryId") String eduEntryId);
	
	
	@GET
	@Path("academics")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ApplicationFormEducationalDto> getAllAcademic(
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);
	
	@GET
	@Path("professional")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ApplicationFormEducationalDto> getAllProfessional(
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);
}
