package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.PermissionDao;
import com.icpak.rest.dao.RolesDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.auth.Permission;
import com.icpak.rest.models.auth.Role;
import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.base.RoleUser;
import com.workpoint.icpak.shared.model.RoleDto;

@Transactional
public class RolesDaoHelper {

	@Inject RolesDao dao;
	@Inject UsersDaoHelper userDaoHelper;
	@Inject UsersDao userDao;
	@Inject PermissionDao permissionDao;
	
	public List<RoleDto> getAllRoles(String uriInfo, Integer offset,
			Integer limit) {

		List<Role> roles = dao.getAllRoles(offset, limit);
		List<RoleDto> dtos = new ArrayList<>();
		for(Role role : roles){
			dtos.add(role.toDto());
		}
		return dtos;
	}

	public RoleDto getRoleById(String roleId) {

		Role role = dao.getByRoleId(roleId);
		
		return role.toDto();
	}
	
	public Role getRoleById(String roleId, String...expand) {

		Role role = dao.getByRoleId(roleId);
		if(role==null){
			throw new ServiceException(ErrorCodes.NOTFOUND, "Role", "'"+roleId+"'");
		}
		
		return role.clone(expand);
	}

	
	public RoleDto createRole(RoleDto dto) {
		
		Role role = new Role(dto.getName());
		role.copyFrom(dto);
		dao.save(role);
		
		assert role.getId()!=null;
		return role.toDto();
	}

	public RoleDto updateRole(String roleId, RoleDto dto) {
		
		Role poRole = dao.getByRoleId(roleId);
		poRole.copyFrom(dto);
		dao.save(poRole);
		
		return poRole.toDto();
	}

	public void deleteRole(String roleId) {
		Role role = dao.getByRoleId(roleId);
		dao.delete(role);
	}

	public RoleUser assign(String roleId, String userId) {
		Role role = dao.getByRoleId(roleId);
		User user = userDaoHelper.getUser(userId); 
		user.addRole(role);
		userDao.save(user);
		
		RoleUser roleUser = new RoleUser(user.clone(), role.clone());
		return roleUser;
	}
	
	public RoleUser getRoleUser(String roleId, String userId) {
		Role role = dao.getByRoleId(roleId);
		User user = userDaoHelper.getUser(userId); 
		boolean isAssigned = dao.checkAssigned(role, user);
		
		if(!isAssigned){
			throw new ServiceException(ErrorCodes.ILLEGAL_ARGUMENT,"User "+user.getEmail()
					+" is not assigned role"+role.getName(),":Get Request");
		}
		
		RoleUser roleUser = new RoleUser(user.clone(), role.clone());
		return roleUser;
	}

	public void deleteAssignment(String roleId, String userId) {
		Role role = dao.getByRoleId(roleId);
		User user = userDaoHelper.getUser(userId);
		
		user.removeRole(role);
		userDao.save(user);
	}

	public Role setPermission(String roleId, String permissionName) {
		
		Role role = dao.getByRoleId(roleId);
		Permission permission = permissionDao.getPermissionByName(permissionName);
		role.addPermission(permission);
		
		dao.save(role);
		
		return role;
	}

	public void deletePermission(String roleId, String permissionName) {
		//Role role = dao.getByRoleId(roleId);
		Permission permission = permissionDao.getPermissionByName(permissionName.toUpperCase());
		dao.deletePermission(roleId, permission);
	}	

}
