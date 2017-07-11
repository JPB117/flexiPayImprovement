package com.icpak.rest.dao;

import java.util.List;

import javax.persistence.Query;

import com.icpak.rest.models.base.PO;
import com.icpak.rest.models.trx.Transaction;

public class TransactionsDao extends BaseDao {

	@Override
	public PO save(PO po) throws RuntimeException {
		getEntityManager().persist(po);
		return po;
	}
	
	public List<Transaction> getTransactions(String userId) {
		return getResultList(getEntityManager().createQuery(
				"FROM Transaction t"));
	}

	public Double getAllPayments(String invoiceNo) {
		String sql = "select sum(amount) from transaction where accountNo=:invoiceNo ";
		Query query = getEntityManager().createNativeQuery(sql).setParameter(
				"invoiceNo", invoiceNo);
		return getSingleResultOrNull(query);
	}
	
}
