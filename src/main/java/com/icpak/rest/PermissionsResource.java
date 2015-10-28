package com.icpak.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.Collection;

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
import com.icpak.rest.dao.helper.PermissionsDaoHelper;
import com.icpak.rest.dao.helper.RolesDaoHelper;
import com.icpak.rest.models.auth.Permission;
import com.icpak.rest.models.auth.Role;
import com.icpak.rest.models.base.ResourceCollectionModel;
import com.sun.jersey.api.core.InjectParam;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@RequiresAuthentication
@Path("permissions")
@Api(value = "permissions", description = "Handles CRUD on Permission data")
public class PermissionsResource extends BaseResource<Permission>{

	@Inject
	PermissionsDaoHelper helper;

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get a list of all permissions", response = Permission.class, produces = MediaType.APPLICATION_JSON)
	public Response getAll(@Context UriInfo uriInfo,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit) {
		
		ResourceCollectionModel<Permission> collection = helper.getAllPermissions(uriInfo,
				getOffset(offset), getLimit(limit));
		return buildCollectionResponse(collection);
	}
	
//	@Path("/{permissionId}/users")
//	public UserPermissionsResource getAssigneesResource(@InjectParam UserPermissionsResource resource){
//		return resource;
//	}
	
	@Path("/{permissionId}/roles")
	public RolePermissionsResource getPermissions(@InjectParam RolePermissionsResource resource){
		return resource;
	}

	@GET
	@Path("/{permissionId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get a permission by permissionId", response = Permission.class, consumes = MediaType.APPLICATION_JSON)
	public Response getById(
			@Context UriInfo uriInfo,
			@ApiParam(value = "Permission Id of the permission to fetch", required = true) @PathParam("permissionId") String permissionId) {

		Permission permission = helper.getPermissionById(permissionId);
		return buildGetEntityResponse(uriInfo.getAbsolutePath().toString(),
				permission);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create a new permission", response = Permission.class, consumes = MediaType.APPLICATION_JSON)
	public Response create(@Context UriInfo uriInfo, Permission permission) {

		helper.createPermission(permission);
		String uri = uriInfo.getAbsolutePath().toString() + "/"
				+ permission.getRefId();
		return buildCreateEntityResponse(uri, permission);
	}

	@PUT
	@Path("/{permissionId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update an existing permission", response = Permission.class, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public Response update(
			@Context UriInfo uriInfo,
			@ApiParam(value = "Permission Id of the permission to fetch", required = true) @PathParam("permissionId") String permissionId,
			Permission permission) {

		helper.updatePermission(permissionId, permission);
		String uri = uriInfo.getAbsolutePath().toString();
		return buildUpdateEntityResponse(uri, permission);
	}

	@DELETE
	@Path("/{permissionId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Delete an existing permission")
	public Response delete(
			@ApiParam(value = "Permission Id of the permission to fetch", required = true) @PathParam("permissionId") String permissionId) {

		helper.deletePermission(permissionId);

		return buildDeleteEntityResponse();
	}
	
	
	
	

	/**
	 * Subresource class represents Roles assigned a specific permission<b>
	 * It also allows bulk assignment of a permission to a role
	 * <p>
	 *  Subresource /permissions/{permission123}/roles
	 * <p>
	 * @author duggan
	 *
	 */
	@Api(value="", description="Permission->Roles Resource")
	public static class RolePermissionsResource extends BaseResource<Role>{

		@Inject RolesDaoHelper rolesHelper;

		@Inject	PermissionsDaoHelper helper;
		/**
		 * permissions/permission123/roles
		 * 
		 * @param uriInfo
		 * @param permissionId
		 * @param offset
		 * @param limit
		 * 
		 * @return List of all roles with the permission permissionId
		 */
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@ApiOperation(value="Utility api to get all roles with a given permission", 
		consumes=MediaType.APPLICATION_JSON)
		public Response get(@Context UriInfo uriInfo,
				@ApiParam(value = "Retrieves all roles with a selected permission", required = true)
				@PathParam("permissionId") String permissionId,
				@QueryParam("offset") Integer offset,
				@QueryParam("limit") Integer limit){
			
			ResourceCollectionModel<Role> roles = helper.getAllRolesWithPermission(permissionId, uriInfo, offset, limit);
			
			return buildCollectionResponse(roles);
		}
		
		/**
		 * 
		 * @param uriInfo
		 * @param roleId
		 * @param permissionName
		 * @return Updated Permission
		 */
		@POST
		@Consumes(APPLICATION_JSON)
		@Produces(APPLICATION_JSON)
		@ApiOperation(value="Utility api to bulk assign a permission to roles", response=Role.class, 
		consumes=APPLICATION_JSON)
		public Response create(@Context UriInfo uriInfo,
				@ApiParam(value = "Permission Id to bulk assign to roles", required = true) 
				@PathParam("permissionId") String permissionId,
				Collection<String> roleIds){
			
			Permission permission = helper.setPermission(permissionId,roleIds);
			return buildUpdateEntityResponse(uriInfo.getAbsolutePath().toString(), permission);
		}
		
		@DELETE
		@Path("/{roleId}")
		@Produces(MediaType.APPLICATION_JSON)
		@ApiOperation(value="Utility api to delete a permission")
		public Response delete(@ApiParam(value = "Permission Id delete", required = true) @PathParam("permissionId") String permissionId){
			
			helper.deletePermission(permissionId);
			return buildDeleteEntityResponse();
		}
	}

}
