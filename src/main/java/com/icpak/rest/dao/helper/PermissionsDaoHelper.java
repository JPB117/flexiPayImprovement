package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.IDUtils;
import com.icpak.rest.dao.PermissionDao;
import com.icpak.rest.dao.RolesDao;
import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.auth.Permission;
import com.icpak.rest.models.auth.Role;
import com.icpak.rest.models.base.ResourceCollectionModel;

@Transactional
public class PermissionsDaoHelper {

	@Inject PermissionDao dao;
	@Inject RolesDao roleDao;
	
	public ResourceCollectionModel<Permission> getAllPermissions(UriInfo uriInfo, Integer offset,
			Integer limit) {

		int count = dao.getPermissionCount();
		ResourceCollectionModel<Permission> permissions = new ResourceCollectionModel<>(offset,limit,count ,uriInfo);
		
		List<Permission> list = new ArrayList<>();
		for(Permission p: dao.getAllPermissions(offset, limit)){
			Permission permission = new Permission();
			permission.setName(p.getName()+"Test Name");
			permission.setDescription(p.getDescription()+"Test Desc");
			permission.setUri(uriInfo.getAbsolutePath().toString()+"/"+p.getRefId());
			list.add(permission);
		}
		permissions.setItems(list);
		
		return permissions;
	}

	public Permission getPermissionById(String permissionId) {

		Permission permission = dao.getByPermissionId(permissionId);
		if(permission==null){
			throw new ServiceException(ErrorCodes.NOTFOUND, "Permission", "'"+permissionId+"'");
		}
		return permission;
	}
	
	public Permission getPermissionById(String permissionId, String...expand) {

		Permission permission = getPermissionById(permissionId);
		
		return permission;
	}

	
	public void createPermission(Permission permission) {
		
		Permission prev = dao.getPermissionByName(permission.getName());
		if(prev!=null && !prev.getRefId().equals(permission.getRefId())){
			throw new ServiceException(ErrorCodes.DUPLICATEVALUE, "Permission Name");
		}
		
		if(prev!=null){
			//update
			prev.setDescription(permission.getDescription());
			prev.setName(permission.getName());
			dao.save(prev);
		}else{
			permission.setRefId(IDUtils.generateId());
			dao.save(permission);
		}
		
		assert permission.getId()!=null;
	}

	public Permission updatePermission(String permissionId, Permission permission) {
		assert permission.getRefId()!=null;
		permission.setRefId(permissionId);
		createPermission(permission);
		return permission;
	}

	public void deletePermission(String permissionId) {
		Permission permission = dao.getByPermissionId(permissionId, true);
		
		dao.delete(permission);
	}

	public ResourceCollectionModel<Role> getAllRolesWithPermission(String permissionId, UriInfo uriInfo,
			Integer offset, Integer limit) {
		
		int count = roleDao.getRoleCount(dao.getByPermissionId(permissionId, true));
		ResourceCollectionModel<Role> roleCollection = new ResourceCollectionModel<>(offset,limit,count ,uriInfo);
		
		List<Role> roles = roleDao.getAllRoles(dao.getByPermissionId(permissionId, true),offset, limit);
		for(Role role: roles){
			role.clone();
		}
		roleCollection.setItems(roles);
		
		return roleCollection;
	}

	public Permission setPermission(String permissionId, Collection<String> roleIds) {
		
		Permission permission = dao.getByPermissionId(permissionId);
		
		for(String roleId: roleIds){
			Role role = roleDao.getByRoleId(roleId);
			role.addPermission(permission);
			roleDao.save(role);
		}
		return permission;
	}	

}
