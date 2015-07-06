package com.workpoint.icpak.shared.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.workpoint.icpak.shared.model.ApplicationFormEducationalDto;


public interface EducationResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ApplicationFormEducationalDto> getAll(@PathParam("applicationId") String applicationId,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);
}
