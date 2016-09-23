package com.icpak.rest.dao;

import java.util.List;

import org.apache.log4j.Logger;

import com.icpak.rest.models.membership.ApplicationCategory;
import com.icpak.rest.models.settings.Setting;

public class SettingDao extends BaseDao {

	Logger logger = Logger.getLogger(CPDDao.class);

	public Setting getBySettingName(String settingName) {
		return getSingleResultOrNull(getEntityManager()
				.createQuery("select u from Setting u" + " where u.isActive=1 and u.settingName=:settingName")
				.setParameter("settingName", settingName));
	}

	public List<Setting> getAllSettings() {
		return getResultList(getEntityManager().createQuery("FROM Setting c where c.isActive=1"));
	}

	public Setting getSettingById(String settingId) {
		return getSingleResultOrNull(
				getEntityManager().createQuery("FROM Setting c where c.refId=:refId").setParameter("refId", settingId));
	}

}
