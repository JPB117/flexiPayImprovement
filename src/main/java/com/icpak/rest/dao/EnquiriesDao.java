package com.icpak.rest.dao;

import java.util.List;

import com.icpak.rest.models.util.Enquiries;

public class EnquiriesDao extends BaseDao {

	public List<Enquiries> getAllEnquiries(String memberRefId, int offset,
			int limit) {

		if (memberRefId != null) {
			return getResultList(
					getEntityManager().createQuery(
							"from Enquiries where isActive=1 "
									+ "and memberRefId=:memberRefId")
							.setParameter("memberRefId", memberRefId), offset,
					limit);
		}
		
		return getResultList(
				getEntityManager().createQuery(
						"from Enquiries where isActive=1"), offset, limit);

	}

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
