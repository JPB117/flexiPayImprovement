package com.icpak.rest.dao.helper;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
import com.icpak.rest.models.membership.Member;
import com.icpak.rest.models.trx.Invoice;
import com.icpak.rest.models.trx.Transaction;
import com.icpak.rest.util.SMSIntegration;
import com.icpak.rest.utils.EmailServiceHelper;
import com.workpoint.icpak.server.util.DateUtils;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.PaymentMode;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.events.EventDto;
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

	public String charge(String accountNo, Date chargeDate, String description,
			Date dueDate, Double amount, String documentNo, String invoiceRef) {
		Transaction trx = new Transaction();
		trx.setInvoiceRef(invoiceRef);
		trx.setAmount(amount);
		trx.setDate(chargeDate);
		trx.setDescription(description);
		trx.setAccountNo(accountNo);
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
		trx.setPaymentMode(PaymentMode.valueOf(paymentMode));
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

	Locale locale = new Locale("en", "KE");
	NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
	private InvoiceDto invoiceDto;

	public void receivePaymentUsingInvoiceNo(String paymentRef,
			String businessNo, String accountNo, String paymentMode,
			String trxNumber, String phoneNumber, String amount, String trxDate) {

		accountNo = accountNo.trim();
		accountNo = accountNo.replaceAll("[-+.^:,]", "");
		accountNo = accountNo.replaceAll("[oO]", "");
		logger.info("Trimmed Account No::::" + accountNo);

		if (accountNo == null || accountNo.isEmpty()) {
			logger.info("Account No is NULL, send message to tell the customer to input the correct account No"
					+ accountNo);
			String smsMessage = " Thank-you for your payment of "
					+ numberFormat.format(amount)
					+ ".Please send details of this payment to memberservices@icpak.com. ";

			String finalPhoneNumber = phoneNumber.replace("254", "0");
			if (phoneNumber != null) {
				smsIntergration.send(finalPhoneNumber, smsMessage);
				logger.error("sending sms to :" + finalPhoneNumber);
			}
			return;
		}

		invoiceDto = invoiceDao.getInvoiceByDocumentNo(accountNo);
		if (invoiceDto == null) {
			logger.info("No Invoice found for this transaction..");

			String smsMessage = " Thank-you for your payment of "
					+ numberFormat.format(amount)
					+ ".Please send details of this payment to memberservices@icpak.com. ";

			String finalPhoneNumber = phoneNumber.replace("254", "0");
			if (phoneNumber != null) {
				smsIntergration.send(finalPhoneNumber, smsMessage);
				logger.error("sending sms to :" + finalPhoneNumber);
			}
			return;
		}

		// Store this transaction
		logger.info("Storing this transaction >>>" + accountNo);

		Date parsedDate = null;
		try {
			parsedDate = DateUtils.FULLTIMESTAMP.parse(trxDate);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Transaction trx = new Transaction();
		trx.setAccountNo(accountNo);
		trx.setDate(parsedDate);
		trx.setPaymentMode((paymentMode == null || paymentMode.equals("") ? PaymentMode.MPESA
				: PaymentMode.MPESA));
		trx.setTrxNumber(trxNumber);
		trx.setBusinessNo(businessNo);
		trx.setAmount(Double.parseDouble(amount));
		trx.setStatus(PaymentStatus.PAID);
		saveTransactionFirst(trx);

		// Convert DTO to Invoice Object
		Invoice inv = new Invoice();
		inv.copyFrom(invoiceDto);

		// Find if this payment was for a booking or not
		Booking booking = new Booking();
		booking = dao.findByRefId(inv.getBookingRefId(), Booking.class, false);
		if (booking != null) {
			logger.info("Payment was for booking refId::" + booking.getRefId());
			try {
				sendPaymentConfirmationSMSAndEmail(phoneNumber, trxNumber,
						inv.getContactName(), booking, trxDate, amount, trx);
			} catch (UnsupportedEncodingException | MessagingException e) {
				e.printStackTrace();
			}
		} else {
			// Get the Application if this payment was for Member
			// Registration
			logger.info("Looking for application using Invoice Ref::"
					+ invoiceDto.getInvoiceRefId());
			ApplicationFormHeader application = applicationDao
					.getApplicationByInvoiceRef(invoiceDto.getInvoiceRefId());
			// assert (application != null);
			try {
				sendPaymentConfirmationSMSAndEmail(phoneNumber, trxNumber,
						inv.getContactName(), application, trxDate, amount, trx);
			} catch (UnsupportedEncodingException | MessagingException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * Returns what the person should be charged
	 */

	private void saveTransactionFirst(Transaction trx) {
		dao.save(trx);
		return;
	}

	private Double applyDiscountsAndPenalties(EventDto event, String trxDate)
			throws ParseException {
		// Parse the mpesa trx Date 2016-01-21 18:42:08
		Date parsedDate = DateUtils.FULLTIMESTAMP.parse(trxDate);

		// Apply Discount && Penalties based on todays Date
		Date penaltyDate = null;
		Date discountDate = null;
		Double chargableAmount = invoiceDto.getInvoiceAmount();

		if (event.getPenaltyDate() != null) {
			penaltyDate = DateUtils.DATEFORMAT_SYS
					.parse(event.getPenaltyDate());
			if (parsedDate.getTime() >= penaltyDate.getTime()) {
				chargableAmount = invoiceDto.getInvoiceAmount()
						+ invoiceDto.getTotalPenalty();
				logger.info("Penalty of " + invoiceDto.getTotalPenalty()
						+ " applied for late payment >>>>" + parsedDate);
			}
		}

		if (event.getDiscountDate() != null) {
			discountDate = DateUtils.DATEFORMAT_SYS.parse(event
					.getDiscountDate());
			if (parsedDate.getTime() <= discountDate.getTime()) {
				chargableAmount = invoiceDto.getInvoiceAmount()
						- invoiceDto.getTotalDiscount();
				logger.info("Discount of " + invoiceDto.getTotalDiscount()
						+ " applied for Early Bird Payment >>>>" + parsedDate);
			}
		}
		return chargableAmount;
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
			String transactionNumber, String senderName, Object paymentType,
			String trxDate, String amount, Transaction trx)
			throws UnsupportedEncodingException, MessagingException {

		if (paymentType != null && paymentType instanceof Booking) {
			Booking booking = (Booking) paymentType;
			String smsMessage = "";

			Double chargeAbleAmt = 0.0;
			try {
				chargeAbleAmt = applyDiscountsAndPenalties(booking.getEvent()
						.toDto(), trxDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			// Get the balance for this Booking - What is remaining from this
			// invoice?
			Double paidAmt = Double.parseDouble(amount);
			Double previousPayments = dao.getAllPayments(trx.getAccountNo()) == null ? 0.0
					: dao.getAllPayments(trx.getAccountNo());
			Double balance = chargeAbleAmt - previousPayments;

			logger.info("Chargable Amount>>>>" + balance);
			logger.info("Invoice Amount>>>>" + invoiceDto.getInvoiceAmount());
			logger.info("Balance Calculated>>>>" + balance);
			logger.info("Previous Payments>>>>" + previousPayments);

			if (balance > 0) {
				logger.info("This invoice still has some balance:::"
						+ trx.getAccountNo());
				smsMessage = " Thank-you for your payment of "
						+ numberFormat.format(paidAmt)
						+ " for "
						+ booking.getEvent().getName()
						+ ". To fully pay for this booking, kindly pay balance of "
						+ numberFormat.format(balance) + ".";

				String finalPhoneNumber = phoneNumber.replace("254", "0");
				if (phoneNumber != null) {
					smsIntergration.send(finalPhoneNumber, smsMessage);
					logger.error("sending sms to :" + finalPhoneNumber);
				}

			} else {
				booking.setPaymentStatus(PaymentStatus.PAID);
				invoiceDto.setStatus(PaymentStatus.PAID);
				System.err.println("Invoice RefId>>"+invoiceDto.getRefId());
				Invoice invoiceSave = invoiceDao.findByRefId(
						invoiceDto.getRefId(), Invoice.class);
				invoiceSave.copyFrom(invoiceDto);
				invoiceDao.save(invoiceSave);

				for (Delegate delegate : booking.getDelegates()) {
					smsMessage = "Dear " + delegate.getFullName() + ","
							+ " Thank-you for booking the "
							+ booking.getEvent().getName()
							+ ".Your booking status is PAID. Your ERN No. is "
							+ delegate.getErn();

					if (delegate.getMemberRefId() != null) {
						Member member = memberDao.findByRefId(
								delegate.getMemberRefId(), Member.class);
						logger.info("Sending SMS to "
								+ member.getUser().getPhoneNumber());

						if (member.getUser().getPhoneNumber() != null) {
							try {
								smsIntergration.send(member.getUser()
										.getPhoneNumber(), smsMessage);
							} catch (RuntimeException e) {
								logger.error("Invalid Phone Number");
								e.printStackTrace();
							}
						}
					} else {
						logger.info("Non-member cannot be send sms..");
					}
				}

				logger.info("This invoice is fully paid:::"
						+ trx.getAccountNo());
				smsMessage = " Thank-you for your payment of "
						+ numberFormat.format(paidAmt) + " for "
						+ booking.getEvent().getName()
						+ ". The booking status is PAID.";

				String finalPhoneNumber = phoneNumber.replace("254", "0");
				if (phoneNumber != null) {
					smsIntergration.send(finalPhoneNumber, smsMessage);
					logger.error("sending sms to :" + finalPhoneNumber);
				}

				if (booking.getContact().getEmail() != null) {
					String subject = "PAYMENT CONFIRMATION FOR "
							+ booking.getEvent().getName().toUpperCase();
					EmailServiceHelper.sendEmail(smsMessage, "RE: ICPAK '"
							+ subject, Arrays.asList(booking.getContact()
							.getEmail()), Arrays.asList(booking.getContact()
							.getContactName()));
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
				EmailServiceHelper.sendEmail(
						smsMessage,
						"RE: ICPAK '" + subject,
						Arrays.asList(application.getEmail()),
						Arrays.asList(application.getSurname() + " "
								+ application.getOtherNames()));
			}

		} else {
			logger.error("No notification sent since neither booking nor application was found for this payment");
		}

	}
}
