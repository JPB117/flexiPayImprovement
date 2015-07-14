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

import com.workpoint.icpak.shared.model.RoleDto;

@Path("roles")
@Produces(MediaType.APPLICATION_JSON)
public interface RoleResource {

	@GET
	public List<RoleDto> getAll(@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);
	
	@GET
	@Path("/{roleId}")
	public RoleDto getById( 
			@PathParam("roleId") String roleId);
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public RoleDto create(RoleDto role);
	

	@PUT
	@Path("/{roleId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public RoleDto update( 
			@PathParam("roleId") String roleId, 
			RoleDto role);
	

	@DELETE
	@Path("/{roleId}")
	public void delete(
			@PathParam("roleId") String roleId);
	
	
	
	
}
