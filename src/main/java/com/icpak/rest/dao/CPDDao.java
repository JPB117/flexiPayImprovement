package com.icpak.rest.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.cpd.CPD;
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

	public List<CPD> getAllCPDs(Integer offSet, Integer limit, Date startDate,
			Date endDate) {
		return getAllCPDs(null, offSet, limit, startDate, endDate);
	}

	public List<CPD> getAllCPDs(String memberRefId, Integer offSet, Integer limit,
			Date startDate, Date endDate) {
		StringBuffer sql = new StringBuffer("FROM CPD");

		boolean isFirstParam = true;
		Map<String, Object> params = new HashMap<>();

		if (startDate != null) {
			if (isFirstParam) {
				isFirstParam = false;
				sql.append(" where");
			}
			params.put("startDate", startDate);
			sql.append(" startDate>=:startDate");
		}

		if (endDate != null) {
			if (isFirstParam) {
				isFirstParam = false;
				sql.append(" where");
			} else {
				sql.append(" and");
			}
			params.put("endDate", endDate);
			sql.append(" endDate<=:endDate");
		}

		if (memberRefId != null && !memberRefId.equals("ALL")) {
			if (isFirstParam) {
				isFirstParam = false;
				sql.append(" where");
			} else {
				sql.append(" and ");
			}
			params.put("memberRefId", memberRefId);
			sql.append(" memberRefId=:memberRefId");
		}
		sql.append(" and isActive=1");
		sql.append(" order by startDate asc");

		Query query = getEntityManager().createQuery(sql.toString());
		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}

		return getResultList(query, offSet, limit);
	}

	public void updateCPD(CPD cpd) {
		createCPD(cpd);
	}

	public int getCPDCount(String memberRefId, Date startDate, Date endDate) {

		Number number = null;
		if (memberRefId != null) {
			if (memberRefId.equals("ALL")) {
				number = getSingleResultOrNull(getEntityManager()
						.createNativeQuery(
								"select count(*) from cpd c "
										+ "where c.isactive=1 and startDate>=:startDate and endDate<=:endDate")
						.setParameter("startDate", startDate)
						.setParameter("endDate", endDate));
			} else {
				number = getSingleResultOrNull(getEntityManager()
						.createNativeQuery(
								"select count(*) from cpd c inner join Member m on (c.memberRefId=m.refId) "
										+ "where c.isactive=1 and m.refId=:refId and startDate>=:startDate and endDate<:endDate")
						.setParameter("startDate", startDate)
						.setParameter("endDate", endDate)
						.setParameter("refId", memberRefId));
			}
		} else {
			throw new ServiceException(ErrorCodes.ILLEGAL_ARGUMENT, "CPD",
					"'MemberRefId'");
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

	public void deleteCPDByMemberAndEvent(String memberRefId, String eventId) {

		delete(getCPDByMemberAndEvent(memberRefId, eventId));
	}

	public CPD getCPDByMemberAndEvent(String memberRefId, String eventId) {
		CPD cpd = getSingleResultOrNull(getEntityManager()
				.createQuery(
						"from CPD u where u.memberRefId=:memberRefId and u.eventId=:eventId")
				.setParameter("memberRefId", memberRefId)
				.setParameter("eventId", eventId));

		return cpd;
	}

	public CPDSummaryDto getCPDSummary(String memberRefId, Date startDate,
			Date endDate) {
		String sql = "select sum(cpdHours), status from cpd where "
				+ "memberRefId=:memberRefId and startDate>=:startDate "
				+ "and endDate<=:endDate group by status";

		List<Object[]> rows = getResultList(getEntityManager()
				.createNativeQuery(sql).setParameter("memberRefId", memberRefId)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate));

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

	public CPDSummaryDto getCPDSummary(Date startDate, Date endDate) {
		String sql = "select count(*), status from cpd where"
				+ " startDate>=:startDate and endDate<=:endDate group by status";
		List<Object[]> rows = getResultList(getEntityManager()
				.createNativeQuery(sql).setParameter("startDate", startDate)
				.setParameter("endDate", endDate));

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
		String sql = "select sum(cpdhours) cpdhours from cpd "
				+ "where memberRefId= :memberRefId and status='Approved' limit 1 ";

		Number value = (Number) getSingleResultOrNull(getEntityManager()
				.createNativeQuery(sql).setParameter("memberRefId", memberRefId));

		return value == null ? 0.0 : value.doubleValue();

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
			sql.append(" startDate>=:startDate");
		}

		if (endDate != null) {
			if (isFirstParam) {
				isFirstParam = false;
				sql.append(" where");
			} else {
				sql.append(" and");
			}
			params.put("endDate", endDate);
			sql.append(" endDate<=:endDate");
		}

		if (memberRefId != null && !memberRefId.equals("ALL")) {
			if (isFirstParam) {
				isFirstParam = false;
				sql.append(" where");
			} else {
				sql.append(" and ");
			}
			params.put("memberRefId", memberRefId);
			sql.append(" memberRefId=:memberRefId");
		}

		sql.append(" order by startDate desc");
		logger.debug("jpql= " + sql);
		Query query = getEntityManager().createQuery(sql.toString());
		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}

		return getResultList(query, offset, limit);

	}
	
	public List<CPD> searchCPD(String searchTerm ,Integer offset, Integer limit) {
		
		String sql = "select c.startdate,c.enddate, concat(u.firstName,' ',u.lastName),"
				+ "c.title,c.organizer,c.category,c.cpdhours,c.status "
				+ "from cpd c "
				+ "inner join member m on (c.memberRefId=m.refId) "
				+ "inner join user u on (u.id=m.userId) "
				+ "where "
				+ "u.firstName like :searchTerm or "
				+ "u.lastName like :searchTerm or "
				+ "concat(u.firstName,' ',u.lastName) like :searchTerm"
				+ "c.title like :searchTerm or "
				+ "c.organizer like :searchTerm";
		
		Query query = getEntityManager().createNativeQuery(sql).setParameter("searchTerm", searchTerm);

		return getResultList(query, offset, limit);

	}
	
public BigInteger cpdSearchCount(String searchTerm ,Integer offset, Integer limit) {
		
		String sql = "select count(*) "
				+ "from cpd c "
				+ "inner join member m on (c.memberRefId=m.refId) "
				+ "inner join user u on (u.id=m.userId) "
				+ "where "
				+ "u.firstName like :searchTerm or "
				+ "u.lastName like :searchTerm or "
				+ "concat(u.firstName,' ',u.lastName) like :searchTerm"
				+ "c.title like :searchTerm or "
				+ "c.organizer like :searchTerm";
		
		Query query = getEntityManager().createNativeQuery(sql).setParameter("searchTerm", searchTerm);

		return getSingleResultOrNull(query);
	}
}
