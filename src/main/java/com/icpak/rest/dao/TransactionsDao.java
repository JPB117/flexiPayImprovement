package com.icpak.rest.dao;

import java.util.List;

import com.icpak.rest.models.trx.Transaction;

public class TransactionsDao extends BaseDao {

	public List<Transaction> getTransactions(String userId) {
		return getResultList(getEntityManager().createQuery("FROM Transaction t"));
	}

}
