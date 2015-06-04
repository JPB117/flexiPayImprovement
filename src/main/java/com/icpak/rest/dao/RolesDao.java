package com.icpak.rest.dao;

import java.util.List;

import com.icpak.rest.models.auth.Permission;
import com.icpak.rest.models.auth.Role;
import com.icpak.rest.models.auth.User;

public class RolesDao extends BaseDao {
	
	public Role getByRoleId(String refId) {
		return getSingleResultOrNull(getEntityManager().createQuery(
				"from Role u where u.refId=:refId").setParameter("refId",
				refId));
	}

	public void createRole(Role role) {
		save(role);
	}

	public List<Role> getAllRoles(Integer offSet, Integer limit) {
		return getResultList(getEntityManager().createQuery("from Role where isActive=1 order by name"),
				offSet, limit);
	}

	public void updateRole(Role role) {
		createRole(role);
	}

	public int getRoleCount() {
		Number number = getSingleResultOrNull(getEntityManager()
				.createNativeQuery("select count(*) from role where isactive=1"));

		return number.intValue();
	}

	public void deletePermission(String refId, Permission permission) {
		Role role = getByRoleId(refId);
		assert role!=null;
		getEntityManager().createNativeQuery("delete from role_permission where role_Id=:role_Id and permissions=:permission")
		.setParameter("role_Id", role.getId())
		.setParameter("permission", permission.getName())
		.executeUpdate();
		
		
	}

	public boolean checkAssigned(Role role, User user) {
		
		Number count = getSingleResultOrNull(getEntityManager().createNativeQuery("select count(*) from user_role"
				+ " where userid=:userId and refId=:refId")
				.setParameter("userId", user.getId())
				.setParameter("refId", role.getId()));
		
		return count.intValue()==1;
	}

	public int getRoleCount(Permission permission) {
		String permissionName = permission.getName();
		String sql = "select count(*) from role_permission where permissions=:permissionName";
		Number number = getSingleResultOrNull(getEntityManager()
				.createNativeQuery(sql).setParameter("permissions", permissionName));

		return number.intValue();
	}

	public List<Role> getAllRoles(Permission permission, Integer offset,
			Integer limit) {
		
		return getResultList(getEntityManager().createQuery(
				"from Role r where :element in elements(r.permissions)"
				+ "r.isActive=1 order by name").setParameter("permissions",permission.getName()),
				offset, limit);
	}

}
