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
import javax.ws.rs.core.MediaType;

import com.workpoint.icpak.shared.model.ApplicationCategoryDto;

@Path("categories")
public interface CategoriesResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ApplicationCategoryDto> getAll();
	
	@GET
	@Path("/{categoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ApplicationCategoryDto getById(@PathParam("categoryId") String categoryId);
	

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ApplicationCategoryDto create(ApplicationCategoryDto category);

	@PUT
	@Path("/{categoryId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ApplicationCategoryDto update(
			@PathParam("categoryId") String categoryId, 
			ApplicationCategoryDto category);
	
	@DELETE
	@Path("/{categoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	public void delete(@PathParam("categoryId") String categoryId);

}
