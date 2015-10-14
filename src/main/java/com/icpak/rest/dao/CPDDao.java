package com.icpak.rest.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.cpd.CPD;
import com.icpak.rest.models.membership.Member;
import com.icpak.rest.models.trx.Statement;
import com.workpoint.icpak.shared.model.CPDStatus;
import com.workpoint.icpak.shared.model.CPDSummaryDto;

/**
 * 
 * @author duggan
 *
 */
public class CPDDao extends BaseDao {
	Logger logger = Logger.getLogger(CPDDao.class);

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

	public void deleteCPDByMemberAndEvent(String memberId, String eventId) {

		delete(getCPDByMemberAndEvent(memberId, eventId));
	}

	public CPD getCPDByMemberAndEvent(String memberId, String eventId) {
		CPD cpd = getSingleResultOrNull(getEntityManager()
				.createQuery(
						"from CPD u where u.memberId=:memberId and u.eventId=:eventId")
				.setParameter("memberId", memberId)
				.setParameter("eventId", eventId));

		return cpd;
	}

	public CPDSummaryDto getCPDSummary(String memberId) {
		String sql = "select sum(cpdHours), status from cpd where memberId=:memberId group by status";

		List<Object[]> rows = getResultList(getEntityManager()
				.createNativeQuery(sql).setParameter("memberId", memberId));

		CPDSummaryDto summary = new CPDSummaryDto();

		for (Object[] row : rows) {
			int i = 0;
			Object value = null;
			Integer count = (value = row[i++]) == null ? null
					: ((Number) value).intValue();
			String status = (value = row[i++]) == null ? null : value
					.toString();

			if (status != null && status.equals(CPDStatus.Unconfirmed.name())) {
				summary.setUnconfirmedCPD(count);
			} else {
				summary.setConfirmedCPD(count);
			}
		}

		return summary;
	}

	public CPDSummaryDto getCPDSummary() {
		String sql = "select count(*), status from cpd group by status";
		List<Object[]> rows = getResultList(getEntityManager()
				.createNativeQuery(sql));

		CPDSummaryDto summary = new CPDSummaryDto();

		for (Object[] row : rows) {
			int i = 0;
			Object value = null;
			Integer count = (value = row[i++]) == null ? null
					: ((Number) value).intValue();
			String status = (value = row[i++]) == null ? null : value
					.toString();

			if (status != null && status.equals(CPDStatus.Unconfirmed.name())) {
				summary.setPendingCount(count);
			} else {
				summary.setProcessedCount(count);
			}
		}

		return summary;
	}

	public Double getCPDHours(String memberRefId) {
		String sql = "select memberId, sum(cpdhours) cpdhours from cpd "
				+ "where memberId= :memberId and status='Approved' ";

		Double value = getSingleResultOrNull(getEntityManager()
				.createNativeQuery(sql).setParameter("memberId", memberRefId));

		return value == null ? 0.0 : value;

	}

	public List<CPD> getAllCPDS(String memberRefId, Date startDate,
			Date endDate, Integer offset, Integer limit) {

		logger.debug("Member reg no ===<<>>==" + memberRefId);
		logger.debug("startDate ===<<>>==" + startDate);
		logger.debug("endDate ===<<>>==" + endDate);

		StringBuffer sql = new StringBuffer("FROM CPD");

		boolean isFirstParam = true;
		Map<String, Object> params = new HashMap<>();

		if (startDate != null) {
			if (isFirstParam) {
				isFirstParam = false;
				sql.append(" where");
			}
			params.put("startDate", startDate);
			sql.append(" startDate>:startDate");
		}

		if (endDate != null) {
			if (isFirstParam) {
				isFirstParam = false;
				sql.append(" where");
			} else {
				sql.append(" and");
			}
			params.put("endDate", endDate);
			sql.append(" endDate<:endDate");
		}

		if (memberRefId != null && !memberRefId.equals("ALL")) {
			if (isFirstParam) {
				isFirstParam = false;
				sql.append(" where");
			} else {
				sql.append(" and ");
			}
			params.put("memberRefId", memberRefId);
			sql.append(" memberId=:memberRefId");
		}

		sql.append(" order by startDate desc");
		logger.debug("jpql= " + sql);
		Query query = getEntityManager().createQuery(sql.toString());
		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}

		return getResultList(query, offset, limit);

	}

}
