package com.icpak.rest.dao.helper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.ApplicationFormDao;
import com.icpak.rest.dao.BookingsDao;
import com.icpak.rest.dao.InvoiceDao;
import com.icpak.rest.dao.MemberDao;
import com.icpak.rest.dao.TransactionsDao;
import com.icpak.rest.models.event.Booking;
import com.icpak.rest.models.event.Delegate;
import com.icpak.rest.models.membership.ApplicationFormHeader;
import com.icpak.rest.models.trx.Invoice;
import com.icpak.rest.models.trx.Transaction;
import com.icpak.rest.util.SMSIntegration;
import com.icpak.rest.utils.EmailServiceHelper;
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
	ApplicationFormDao applicationDao;

	@Inject
	SMSIntegration smsIntergration;
	@Inject
	MemberDao memberDao;
	Logger logger = Logger.getLogger(TransactionDaoHelper.class.getName());

	public String charge(String userId, Date chargeDate, String description,
			Date dueDate, Double amount, String documentNo, String invoiceRef) {
		
		Transaction trx = new Transaction();
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

	public void receivePaymentUsingInvoiceNo(String paymentRef,
			String businessNo, String accountNo, String paymentMode,
			String trxNumber, String phoneNumber, String amount) {
		InvoiceDto invoiceDto = invoiceDao.getInvoiceByDocumentNo(paymentRef);
		Transaction trx = dao.findByRefId(invoiceDto.getTrxRefId(),
				Transaction.class);

		// Ensure that payments are equal
		// if (invoiceDto.getInvoiceAmount() != Double.valueOf(amount)) {
		// String smsMessage = " Thank-you for payment to ICPAK."
		// + "However, the amount sent doesn't match with you Invoice amount. "
		// + "Please send the correct amount to process your invoice";
		//
		// String finalPhoneNumber = phoneNumber.replace("254", "0");
		// if (phoneNumber != null) {
		// smsIntergration.send(finalPhoneNumber, smsMessage);
		// }
		// return;
		// }

		// Update of Transaction Details
		trx.setAccountNo(accountNo);
		trx.setPaymentMode((paymentMode == null || paymentMode.equals("") ? "MPESA"
				: paymentMode));
		trx.setTrxNumber(trxNumber);
		trx.setBusinessNo(businessNo);
		trx.setStatus(PaymentStatus.PAID);
		dao.save(trx);

		Invoice inv = new Invoice();
		inv.copyFrom(invoiceDto);

		Booking booking = new Booking();
		booking = dao.findByRefId(inv.getBookingRefId(), Booking.class, false);

		if (booking != null) {
			/*
			 * This is temporary: - You should check this from Transaction
			 * Object to get the payment info
			 */

			if (invoiceDto != null) {
				System.err.println("Looking for a booking record....");
			}

			booking.setPaymentStatus(PaymentStatus.PAID);
			booking.setPaymentDate(new Date());
			booking.setPaymentMode(paymentMode);
			booking.setPaymentRef(paymentRef);
			try {
				sendPaymentConfirmationSMSAndEmail(phoneNumber, trxNumber,
						inv.getContactName(), booking);
			} catch (UnsupportedEncodingException | MessagingException e) {
				e.printStackTrace();
			}
		} else {
			// Get the Application if this payment was for Member
			// Registration
			System.err.println("Looking for application using Invoice Ref::"
					+ invoiceDto.getInvoiceRefId());
			ApplicationFormHeader application = applicationDao
					.getApplicationByInvoiceRef(invoiceDto.getInvoiceRefId());
			// assert (application != null);
			try {
				sendPaymentConfirmationSMSAndEmail(phoneNumber, trxNumber,
						inv.getContactName(), application);
			} catch (UnsupportedEncodingException | MessagingException e) {
				e.printStackTrace();
			}

		}

	}

	public List<TransactionDto> getTransactions(String userId) {
		List<Transaction> transactions = dao.getTransactions(userId);
		List<TransactionDto> trxs = new ArrayList<>();
		for (Transaction t : transactions) {
			trxs.add(t.toDto());
		}

		return trxs;
	}

	private void sendPaymentConfirmationSMSAndEmail(String phoneNumber,
			String transactionNumber, String senderName, Object paymentType)
			throws UnsupportedEncodingException, MessagingException {
		if (paymentType != null && paymentType instanceof Booking) {
			Booking booking = (Booking) paymentType;
			for (Delegate delegate : booking.getDelegates()) {
				String smsMessage = "Dear" + " " + senderName + ","
						+ " Thank-you for booking for the "
						+ booking.getEvent().getName()
						+ ".Your booking status is PAID. Your ERN No. is "
						+ delegate.getErn();

				String finalPhoneNumber = phoneNumber.replace("254", "0");
				if (phoneNumber != null) {
					smsIntergration.send(finalPhoneNumber, smsMessage);
					logger.error("sending sms to :" + finalPhoneNumber);
				}

				if (delegate.getEmail() != null) {
					String subject = "PAYMENT CONFIRMATION FOR "
							+ booking.getEvent().getName().toUpperCase();
					EmailServiceHelper.sendEmail(smsMessage, subject,
							delegate.getEmail());
				}
			}
		} else if (paymentType != null
				&& paymentType instanceof ApplicationFormHeader) {
			ApplicationFormHeader application = (ApplicationFormHeader) paymentType;

			String smsMessage = "Dear" + " " + application.getSurname() + ","
					+ " Thank-you for payment for your member registration. "
					+ "Your account payment status is now PAID.";
			String finalPhoneNumber = phoneNumber.replace("254", "0");

			if (phoneNumber != null) {
				smsIntergration.send(finalPhoneNumber, smsMessage);
				logger.error("sending sms to :" + finalPhoneNumber);
			}

			if (application.getTelephone1() != null) {
				smsIntergration.send(application.getTelephone1(), smsMessage);
				logger.error("sending sms to :" + finalPhoneNumber);
			}

			if (application.getEmail() != null) {
				String subject = "PAYMENT CONFIRMATION FOR "
						+ application.getSurname().toUpperCase()
						+ " MEMBER SUBSCRIPTION ";
				EmailServiceHelper.sendEmail(smsMessage, subject,
						application.getEmail());
			}

		} else {
			logger.error("No notification sent since neither booking nor application was found for this payment");
		}

	}
}
