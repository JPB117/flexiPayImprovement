package com.icpak.rest.dao;

import com.google.gwt.dev.util.collect.HashMap;
import com.icpak.rest.dao.BaseDao;
import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.trx.Statement;
import com.workpoint.icpak.shared.model.statement.SearchDto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

public class StatementDao extends BaseDao {

	public Statement getByStatementId(String refId) {
		return getByStatementId(refId, true);
	}

	public Statement getByStatementId(String refId, boolean throwExceptionIfNull) {

		Statement statement = getSingleResultOrNull(getEntityManager()
				.createQuery("from Statement u where u.refId=:refId")
				.setParameter("refId", refId));

		if (throwExceptionIfNull && statement == null) {
			throw new ServiceException(ErrorCodes.NOTFOUND, "Statement", "'"
					+ refId + "'");
		}

		return statement;
	}

	public void createStatement(Statement statement) {
		save(statement);
	}

	public void updateStatement(Statement statement) {
		createStatement(statement);
	}

	public int getStatementCount() {
		Number number = getSingleResultOrNull(getEntityManager()
				.createNativeQuery("select count(*) from statement"));

		return number.intValue();
	}

	/**
	 * 
	 * @param searchCriteria
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<Statement> getAllStatements(Date startDate, Date endDate,
			Integer offset, Integer limit) {
		return getAllStatements(null, startDate, endDate, offset, limit);

	}

	public List<Statement> getAllStatements(String memberRegistrationNo,
			Date startDate, Date endDate, Integer offset, Integer limit) {

		StringBuffer sql = new StringBuffer("FROM Statement");

		boolean isFirstParam = true;
		Map<String, Object> params = new HashMap<>();
		
		if (startDate != null) {
			if (isFirstParam) {
				isFirstParam = false;
				sql.append(" where");
			}
			params.put("startDate", startDate);
			sql.append(" postingDate>:startDate");
		}

		if (endDate != null) {
			if (isFirstParam) {
				isFirstParam = false;
				sql.append(" where");
			} else {
				sql.append(" and");
			}
			params.put("endDate", endDate);
			sql.append(" postingDate<:endDate");
		}
		
		if (memberRegistrationNo != null && !memberRegistrationNo.equals("ALL")) {
			if (isFirstParam) {
				isFirstParam = false;
				sql.append(" where");
			} else {
				sql.append(" and ");
			}
			params.put("regNo", memberRegistrationNo);
			sql.append(" customerNo=:regNo");
		}
		
		sql.append(" order by postingdate desc");

		Query query = getEntityManager().createQuery(sql.toString());
		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}

		return getResultList(query, offset, limit);

		// return getResultList(getEntityManager()
		// .createQuery("FROM Statement where customerNo=:regNo order by postingdate desc")
		// .setParameter("regNo", memberRegistrationNo), offset, limit);
	}

	public Integer getStatementCount(String memberNo,Date startDate, Date endDate) {
		Number count = null;

		StringBuffer sql = new StringBuffer("select count(*) from statement");

		boolean isFirstParam = true;
		Map<String, Object> params = new HashMap<>();
		
		if (startDate != null) {
			if (isFirstParam) {
				isFirstParam = false;
				sql.append(" where");
			}
			params.put("startDate", startDate);
			sql.append(" postingDate>:startDate");
		}

		if (endDate != null) {
			if (isFirstParam) {
				isFirstParam = false;
				sql.append(" where");
			} else {
				sql.append(" and");
			}
			params.put("endDate", endDate);
			sql.append(" postingDate<:endDate");
		}
		
		if (memberNo != null && !memberNo.equals("ALL")) {
			if (isFirstParam) {
				isFirstParam = false;
				sql.append(" where");
			} else {
				sql.append(" and ");
			}
			params.put("regNo", memberNo);
			sql.append(" customerNo=:regNo");
		}
		Query query = getEntityManager().createNativeQuery(sql.toString());
		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}
		
		count = getSingleResultOrNull(query);

		return count.intValue();
	}
}