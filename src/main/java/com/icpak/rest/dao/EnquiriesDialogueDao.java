package com.icpak.rest.dao;

import java.util.List;

import com.icpak.rest.models.util.Enquiries;

public class EnquiriesDialogueDao extends BaseDao {

	public Integer getCount(String memberRefId) {

		Number number = null;

		if (memberRefId != null) {
			number = getSingleResultOrNull(getEntityManager().createQuery(
					"SELECT count(*) from Enquiries where isActive=1 "
							+ "and memberRefId=:memberRefId").setParameter(
					"memberRefId", memberRefId));
		} else {
			number = getSingleResultOrNull(getEntityManager().createQuery(
					"SELECT count(*) from Enquiries where isActive=1"));
		}

		return number.intValue();
	}

}
