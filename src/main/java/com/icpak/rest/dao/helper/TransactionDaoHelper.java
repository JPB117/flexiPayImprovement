package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.TransactionsDao;
import com.icpak.rest.models.trx.Transaction;
import com.workpoint.icpak.shared.trx.TransactionDto;
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
	
	public void receivePayment(String referenceId, String paymentMode, String status){
		
		Transaction trx = dao.findByRefId(referenceId, Transaction.class, false);
		
		Transaction payment = new Transaction();
		payment.setType(TrxType.DR);
		payment.setPaymentMode(paymentMode);
		payment.setStatus(status);
		
		if(trx!=null){
			payment.setAmount(trx.getAmount());
			payment.setDate(new Date());
			payment.setDescription(trx.getDescription());
			payment.setDueDate(trx.getDueDate());
			payment.setDocumentNo(trx.getDocumentNo());
			payment.setUserId(trx.getUserId());
		}
		
		dao.save(payment);
	}

	public List<TransactionDto> getTransactions(String userId) {

		List<Transaction> transactions = dao.getTransactions(userId);
		
		List<TransactionDto> trxs = new ArrayList<>();
		for(Transaction t : transactions){
			trxs.add(t.toDto());
		}
				
		return trxs;
	}

}
