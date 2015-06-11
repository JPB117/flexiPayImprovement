package com.icpak.rest.dao;

import java.util.List;

import com.icpak.rest.models.trx.Transaction;
import com.workpoint.icpak.shared.trx.TransactionDto;

public class TransactionsDao extends BaseDao {

	public List<Transaction> getTransactions(String userId) {
		
		return getResultList(getEntityManager().createQuery("FROM Transaction t"));
	}

}
