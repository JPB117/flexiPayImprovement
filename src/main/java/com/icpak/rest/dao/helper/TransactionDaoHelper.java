package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.BookingsDao;
import com.icpak.rest.dao.InvoiceDao;
import com.icpak.rest.dao.TransactionsDao;
import com.icpak.rest.models.event.Booking;
import com.icpak.rest.models.trx.Invoice;
import com.icpak.rest.models.trx.Transaction;
import com.icpak.rest.util.SMSIntegration;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.trx.TransactionDto;

@Transactional
public class TransactionDaoHelper {

	@Inject
	TransactionsDao dao;

	@Inject
	InvoiceDao invoiceDao;

	@Inject
	BookingsDao bookingDao;

	@Inject
	SMSIntegration smsIntergration;

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

	/*
	 * Old Method for receiving payment
	 */
	public void receivePayment(String paymentRef, String businessNo,
			String accountNo, String paymentMode, String trxNumber) {
		Transaction trx = dao.findByRefId(paymentRef, Transaction.class);
		trx.setAccountNo(accountNo);
		trx.setPaymentMode(paymentMode);
		trx.setTrxNumber(trxNumber);
		trx.setBusinessNo(businessNo);
		trx.setStatus(PaymentStatus.PAID);

		// Remove this duplication #07/10/2015
		if (trx.getInvoiceRef() != null) {
			Invoice invoice = dao.findByRefId(trx.getInvoiceRef(),
					Invoice.class, false);
			if (invoice != null && invoice.getBookingRefId() != null) {
				Booking booking = dao.findByRefId(invoice.getBookingRefId(),
						Booking.class, false);
				booking.setPaymentStatus(PaymentStatus.PAID);
				booking.setPaymentDate(new Date());
				booking.setPaymentMode(paymentMode);
				booking.setPaymentRef(paymentRef);
				bookingDao.save(booking);
			}
		}
		dao.save(trx);
	}

	public void receivePaymentFromInvoiceNo(String paymentRef,
			String businessNo, String accountNo, String paymentMode,
			String trxNumber) {
		InvoiceDto invoiceDto = invoiceDao.getInvoiceByDocumentNo(paymentRef);
		Transaction trx = dao.findByRefId(invoiceDto.getTrxRefId(),
				Transaction.class);
		trx.setAccountNo(accountNo);
		trx.setPaymentMode((paymentMode == null || paymentMode.equals("") ? "MPESA"
				: paymentMode));
		trx.setTrxNumber(trxNumber);
		trx.setBusinessNo(businessNo);
		trx.setStatus(PaymentStatus.PAID);

		Invoice inv = new Invoice();
		inv.copyFrom(invoiceDto);

		Booking booking = new Booking();
		System.err.println("Booking Ref>>>" + inv.getBookingRefId());
		booking = dao.findByRefId(inv.getBookingRefId(), Booking.class, false);
		if (booking != null) {
			booking.setPaymentStatus(PaymentStatus.PAID);
			booking.setPaymentDate(new Date());
			booking.setPaymentMode(paymentMode);
			booking.setPaymentRef(paymentRef);
		}

		dao.save(trx);

	}

	public List<TransactionDto> getTransactions(String userId) {

		List<Transaction> transactions = dao.getTransactions(userId);

		List<TransactionDto> trxs = new ArrayList<>();
		for (Transaction t : transactions) {
			trxs.add(t.toDto());
		}

		return trxs;
	}

}
