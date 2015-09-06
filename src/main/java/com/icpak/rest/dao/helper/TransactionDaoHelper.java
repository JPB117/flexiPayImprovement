package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.TransactionsDao;
import com.icpak.rest.models.trx.Transaction;
import com.workpoint.icpak.shared.trx.TransactionDto;

@Transactional
public class TransactionDaoHelper {

	@Inject TransactionsDao dao;
	
	public String charge(String userId, Date chargeDate, String description,
			Date dueDate, Double amount, String documentNo, String invoiceRef) {
		Transaction trx = new Transaction();
		trx.setRefId(invoiceRef);
		trx.setInvoiceRef(invoiceRef);
		trx.setAmount(amount);
		trx.setDate(chargeDate);
		trx.setDescription(description);
		trx.setDueDate(dueDate);
		trx.setUserId(userId);
		trx.setDocumentNo(documentNo);
		dao.save(trx);
		
		return trx.getRefId();
	}
	
	public void receivePayment(String paymentRef,String businessNo,String accountNo,String paymentMode,String trxNumber){
		Transaction trx = dao.findByRefId(paymentRef, Transaction.class);
		trx.setAccountNo(accountNo);
		trx.setPaymentMode(paymentMode);
		trx.setTrxNumber(trxNumber);
		trx.setBusinessNo(businessNo);
		trx.setStatus("PAID");
		dao.save(trx);
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
