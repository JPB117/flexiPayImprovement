package com.icpak.rest.dao;

import com.icpak.rest.dao.BaseDao;
import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.trx.Statement;

import java.util.List;

public class StatementDao extends BaseDao {

	public Statement getByStatementId(String refId) {
		return getByStatementId(refId, true);
	}
	
	public Statement getByStatementId(String refId, boolean throwExceptionIfNull) {
		
		Statement statement = getSingleResultOrNull(getEntityManager().createQuery(
				"from Statement u where u.refId=:refId").setParameter("refId",
				refId));
		
		if(throwExceptionIfNull && statement==null){
			throw new ServiceException(ErrorCodes.NOTFOUND, "Statement", "'"+refId+"'");
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


	public List<Statement> getAllStatements(
			Integer offset, Integer limit) {
		return getResultList(getEntityManager()
				.createQuery("FROM Statement"), offset, limit);
	}

	public List<Statement> getAllStatements(String memberRegistrationNo,
			Integer offset, Integer limit) {
		return getResultList(getEntityManager()
				.createQuery("FROM Statement where customerNo=:regNo order by postingdate desc")
				.setParameter("regNo", memberRegistrationNo), offset, limit);
	}

	public Integer getStatementCount(String memberNo) {
		Number count = null;
		
		if(memberNo==null){
			count = getSingleResultOrNull(getEntityManager().createNativeQuery(
					"select count(*) from statement"));
		}else{
			count = getSingleResultOrNull(getEntityManager().createNativeQuery(
					"select count(*) from statement where customerNo=:regNo")
					.setParameter("regNo", memberNo));
		}
			
		return count.intValue();
	}
}