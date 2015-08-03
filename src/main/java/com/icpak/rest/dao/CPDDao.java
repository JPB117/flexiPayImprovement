package com.icpak.rest.dao;

import java.util.List;

import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.cpd.CPD;

/**
 * 
 * @author duggan
 *
 */
public class CPDDao extends BaseDao {

	public void createCPD(CPD cpd) {
		save(cpd);
	}

	public List<CPD> getAllCPDs(Integer offSet, Integer limit) {
		return getAllCPDs(null, offSet, limit);
	}

	public List<CPD> getAllCPDs(String memberId, Integer offSet, Integer limit) {

		if (memberId != null) {
			return getResultList(
					getEntityManager()
							.createQuery(
									"select u from CPD u where u.isActive=1 and u.memberId=:memberId")
							.setParameter("memberId", memberId), offSet, limit);
		} else {
			return getResultList(
					getEntityManager().createQuery(
							"select u from CPD u where u.isActive=1"), offSet,
					limit);
		}
	}

	public void updateCPD(CPD cpd) {
		createCPD(cpd);
	}

	public int getCPDCount(String memberId) {

		Number number = null;
		if (memberId != null) {
			if (memberId.equals("ALL")) {
				number = getSingleResultOrNull(getEntityManager()
						.createNativeQuery(
								"select count(*) from cpd c "
										+ "where c.isactive=1"));
			} else {
				number = getSingleResultOrNull(getEntityManager()
						.createNativeQuery(
								"select count(*) from cpd c inner join Member m on (c.memberId=m.refId) "
										+ "where c.isactive=1 and m.refId=:refId")
						.setParameter("refId", memberId));
			}
		} else {
			throw new ServiceException(ErrorCodes.ILLEGAL_ARGUMENT, "CPD",
					"'MemberId'");
		}

		return number.intValue();
	}

	public CPD findByCPDId(String refId) {
		return findByCPDId(refId, true);
	}

	public CPD findByCPDId(String refId, boolean throwExceptionIfNull) {
		CPD cpd = getSingleResultOrNull(getEntityManager().createQuery(
				"from CPD u where u.refId=:refId").setParameter("refId", refId));

		if (cpd == null && throwExceptionIfNull) {
			throw new ServiceException(ErrorCodes.NOTFOUND, "CPD", "'" + refId
					+ "'");
		}

		return cpd;
	}

}
