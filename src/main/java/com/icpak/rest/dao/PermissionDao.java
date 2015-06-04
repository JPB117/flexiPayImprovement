package com.icpak.rest.dao;

import java.util.List;

import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.auth.Permission;

public class PermissionDao extends BaseDao{

	public Permission getByPermissionId(String permissionId) {
		return getByPermissionId(permissionId, true);
	}
	
	public Permission getByPermissionId(String refId, boolean throwExceptionIfNull) {
		
		Permission permission = getSingleResultOrNull(getEntityManager().createQuery(
				"from Permission u where u.refId=:permissionId").setParameter("permissionId",
						refId));
		
		if(throwExceptionIfNull && permission==null){
			throw new ServiceException(ErrorCodes.NOTFOUND, "Permission", "'"+refId+"'");
		}
		
		return permission;
	}
	
	public Permission getPermissionByName(String name) {
		
		Permission permission = getSingleResultOrNull(getEntityManager().createQuery(
				"from Permission u where u.name=:name").setParameter("name",
						name));
		return permission;
	}

	public void createPermission(Permission Permission) {
		save(Permission);
	}

	public List<Permission> getAllPermissions(Integer offSet, Integer limit) {
		return getResultList(getEntityManager().createQuery("from Permission where isActive=1 order by name"),
				offSet, limit);
	}

	public void updatePermission(Permission Permission) {
		createPermission(Permission);
	}

	public int getPermissionCount() {
		Number number = getSingleResultOrNull(getEntityManager()
				.createQuery("select count(p.id) from Permission p where p.isActive=1"));

		return number.intValue();
	}

}
