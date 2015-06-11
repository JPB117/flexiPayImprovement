package com.icpak.rest.dao.helper;

import java.util.Date;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.TransactionsDao;
import com.icpak.rest.models.trx.Transaction;
import com.workpoint.icpak.shared.trx.TrxType;

@Transactional
public class TransactionDaoHelper {

	@Inject TransactionsDao dao;
	
	public void charge(String userId, Date chargeDate, String description,
			Date dueDate, Double amount, String documentNo) {
		Transaction trx = new Transaction();
		trx.setAmount(amount);
		trx.setDate(chargeDate);
		trx.setDescription(description);
		trx.setDueDate(dueDate);
		trx.setDocumentNo(documentNo);
		trx.setUserId(userId);
		trx.setType(TrxType.CR);
		dao.save(trx);
	}
	
	public void receivePayment(String userId, Date receiptDate, String description,
			String documentNo, String paymentMode, Double amount){
		
	}

}
