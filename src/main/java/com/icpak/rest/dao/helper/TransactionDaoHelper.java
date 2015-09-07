package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.TransactionsDao;
import com.icpak.rest.models.event.Booking;
import com.icpak.rest.models.trx.Invoice;
import com.icpak.rest.models.trx.Transaction;
import com.workpoint.icpak.shared.model.PaymentStatus;
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
		trx.setMemberId(userId);
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
		trx.setStatus(PaymentStatus.PAID);
		
		//Remove this duplication #07/10/2015
		if(trx.getInvoiceRef()!=null){
			Invoice invoice = dao.findByRefId(trx.getInvoiceRef(), Invoice.class, false);
			if(invoice!=null && invoice.getBookingRefId()!=null){
				Booking booking = dao.findByRefId(invoice.getBookingRefId(), Booking.class, false);
				booking.setPaymentStatus(PaymentStatus.PAID);
				booking.setPaymentDate(new Date());
				booking.setPaymentMode(paymentMode);
				booking.setPaymentRef(paymentRef);
			}
		}
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
