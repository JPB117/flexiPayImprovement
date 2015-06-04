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

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.RolesDaoHelper;
import com.icpak.rest.dao.helper.UsersDaoHelper;
import com.icpak.rest.models.auth.Role;
import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.base.RoleUser;
import com.sun.jersey.api.core.InjectParam;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Path("roles")
@Api(value="roles", description="Handles CRUD for Roles")
public class RoleResource extends BaseResource<Role> {

	@Inject RolesDaoHelper helper;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Retrieve all active Roles")
	public Response getAll(@Context UriInfo uriInfo, 
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit) {
		
		return buildCollectionResponse(helper.getAllRoles(uriInfo, offset, limit));
	}
	
	@Path("/{roleId}/users")
	public RoleAssigneesResource getAssigneesResource(@InjectParam RoleAssigneesResource resource){
		return resource;
	}
	
	@Path("/{roleId}/permissions")
	public RolePermissionsResource getPermissions(@InjectParam RolePermissionsResource resource){
		return resource;
	}

	@GET
	@Path("/{roleId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Get a role by roleId", response=Role.class, consumes=MediaType.APPLICATION_JSON)
	public Response getById(@Context UriInfo uriInfo, 
			@ApiParam(value="Role Id of the role to fetch", required=true) @PathParam("roleId") String roleId) {
		
		return buildGetEntityResponse(uriInfo.getAbsolutePath().toString(), helper.getRoleById(roleId));
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Create a new role", response=Role.class, consumes=MediaType.APPLICATION_JSON)
	public Response create(@Context UriInfo uriInfo, Role role) {
		helper.createRole(role);
		String uri = uriInfo.getAbsolutePath().toString()+"/"+role.getRefId();
		return buildCreateEntityResponse(uri,role);
	}

	@PUT
	@Path("/{roleId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Update an existing role", response=Role.class, 
	consumes=MediaType.APPLICATION_JSON, produces=MediaType.APPLICATION_JSON)
	public Response update(@Context UriInfo uriInfo, 
			@ApiParam(value="Role Id of the role to update", required=true) @PathParam("roleId") String roleId, 
			Role role) {
		
		role = helper.updateRole(roleId, role);
		return buildUpdateEntityResponse(uriInfo.getAbsolutePath().toString(), role);
	}

	@DELETE
	@Path("/{roleId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Delete an existing role")
	public Response delete(
			@ApiParam(value="Role Id of the role to delete", required=true) @PathParam("roleId") String roleId) {
		
		helper.deleteRole(roleId);
		return buildDeleteEntityResponse();
	}
	
	
	
	
	


	/**
	 * Permissions assigned to a role
	 * 
	 *  Subresource /roles/role123/permissions
	 * 
	 * @author duggan
	 *
	 */
	@Api(value="", description="Roles permissions resource")
	public static class RolePermissionsResource extends BaseResource<Role>{

		@Inject RolesDaoHelper helper;
		
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@ApiOperation(value="Utility service to assign a new permission to role", response=Role.class, 
		consumes=MediaType.APPLICATION_JSON)
		public Response get(@Context UriInfo uriInfo,
				@ApiParam(value = "Role Id to assign permission", required = true) @PathParam("roleId") String roleId){
			
			Role role = helper.getRoleById(roleId,"permissions");
			
			String entityUri= uriInfo.getBaseUri().toString()+"roles/"+role.getRefId();
			return buildUpdateEntityResponse(entityUri, role);
		}
		
		@POST
		@Path("/{permissionName}")
		@Produces(MediaType.APPLICATION_JSON)
		@ApiOperation(value="Utility service to assign a new permission to role", response=Role.class, 
		consumes=MediaType.APPLICATION_JSON)
		public Response create(@Context UriInfo uriInfo,
				@ApiParam(value = "Role Id to assign permission", required = true) @PathParam("roleId") String roleId,
				@ApiParam(value = "Name of Permission to assign", required = true) @PathParam("permissionName") String permissionName){
			
			Role role = helper.setPermission(roleId, permissionName);
			
			String entityUri= uriInfo.getBaseUri().toString()+"/roles/"+role.getRefId();
			return buildUpdateEntityResponse(entityUri, role);
		}
		
		@DELETE
		@Path("/{permissionName}")
		@Produces(MediaType.APPLICATION_JSON)
		@ApiOperation(value="Utility api to remove a permission from role")
		public Response delete(@ApiParam(value = "Role Id delete", required = true) @PathParam("roleId") String roleId,
				@ApiParam(value = "Name of Permission to delete", required = true) @PathParam("permissionName") String permissionName){
			
			helper.deletePermission(roleId, permissionName);
			
			return buildDeleteEntityResponse();
		}
	}

	
	
	
	
	/**
	 * Roles-Users Sub-resource to be accessed as /roles/role3432/users
	 * 
	 * @author duggan
	 *
	 */
	@Api(value="", description="Roles assignees resource")
	public static class RoleAssigneesResource extends BaseResource<RoleUser> {

		@Inject
		UsersDaoHelper userHelper;
		@Inject
		RolesDaoHelper roleHelper;
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@ApiOperation(value = "Get a list of all users assigned a role", 
		response = User.class, consumes = MediaType.APPLICATION_JSON)
		public Response getAll(@Context UriInfo uriInfo,
				@QueryParam("offset") Integer offset,
				@QueryParam("limit") Integer limit,
				@PathParam("roleId") String roleId) {

			assert roleId!=null;
			return buildCollectionResponse(userHelper.getAllUsers(offset, limit,
					uriInfo, roleId));
		}

		@GET
		@Path("/{userId}")
		@Produces(MediaType.APPLICATION_JSON)
		@ApiOperation(value = "Assign a role to a user")
		public Response get(
				@Context UriInfo uriInfo,
				@ApiParam(value = "Role Id assign", required = true) @PathParam("roleId") String roleId,
				@ApiParam(value = "User Id assign", required = true) @PathParam("userId") String userId) {

			RoleUser roleUser = roleHelper.getRoleUser(roleId, userId);
			
			roleUser.getRole().setUri(uriInfo.getBaseUri().toString()+"roles/"+roleId);
			roleUser.getUser().setUri(uriInfo.getBaseUri().toString()+"users/"+userId);
			String uri = uriInfo.getAbsolutePath().toString();

			return buildCreateEntityResponse(uri, roleUser);
		}
		
		@POST
		@Path("/{userId}")
		@Produces(MediaType.APPLICATION_JSON)
		@ApiOperation(value = "Assign a role to a user")
		public Response create(
				@Context UriInfo uriInfo,
				@ApiParam(value = "Role Id assign", required = true) @PathParam("roleId") String roleId,
				@ApiParam(value = "User Id assign", required = true) @PathParam("userId") String userId) {

			RoleUser roleUser = roleHelper.assign(roleId, userId);
			
			roleUser.getRole().setUri(uriInfo.getBaseUri().toString()+"roles/"+roleId);
			roleUser.getUser().setUri(uriInfo.getBaseUri().toString()+"users/"+userId);
			String uri = uriInfo.getAbsolutePath().toString();

			return buildCreateEntityResponse(uri, roleUser);
		}

		@DELETE
		@Path("/{userId}")
		@Produces(MediaType.APPLICATION_JSON)
		@ApiOperation(value = "Delete role-user assignment")
		public Response delete(
				@ApiParam(value = "Role Id delete", required = true) @PathParam("roleId") String roleId,
				@ApiParam(value = "User Id delete", required = true) @PathParam("userId") String userId) {

			roleHelper.deleteAssignment(roleId, userId);
			return buildDeleteEntityResponse();
		}

	}

}
