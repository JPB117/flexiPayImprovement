package com.icpak.rest.dao.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import com.amazonaws.util.json.JSONException;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.ApplicationFormDao;
import com.icpak.rest.dao.BookingsDao;
import com.icpak.rest.dao.InvoiceDao;
import com.icpak.rest.dao.MemberDao;
import com.icpak.rest.dao.TransactionsDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.event.Booking;
import com.icpak.rest.models.event.Delegate;
import com.icpak.rest.models.membership.ApplicationFormHeader;
import com.icpak.rest.models.membership.Member;
import com.icpak.rest.models.trx.Invoice;
import com.icpak.rest.models.trx.Transaction;
import com.icpak.rest.util.SMSIntegration;
import com.icpak.rest.utils.EmailServiceHelper;
import com.workpoint.icpak.server.util.ServerDateUtils;
import com.workpoint.icpak.shared.model.EventType;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.PaymentMode;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.PaymentType;
import com.workpoint.icpak.shared.model.TransactionDto;
import com.workpoint.icpak.shared.model.events.AttendanceStatus;
import com.workpoint.icpak.shared.model.events.DelegateDto;
import com.workpoint.icpak.shared.model.events.EventDto;
import com.workpoint.icpak.shared.trx.OldTransactionDto;

@Transactional
public class TransactionDaoHelper {
	@Inject
	TransactionsDao dao;
	@Inject
	InvoiceDao invoiceDao;
	@Inject
	BookingsDao bookingDao;
	@Inject
	BookingsDaoHelper bookingDaoHelper;
	@Inject
	ApplicationFormDao applicationDao;
	@Inject
	SMSIntegration smsIntergration;
	@Inject
	MemberDao memberDao;

	@Inject
	UsersDao usersDao;
	Logger logger = Logger.getLogger(TransactionDaoHelper.class.getName());
	Locale locale = new Locale("en", "KE");
	NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
	private InvoiceDto invoiceDto;

	public String charge(String accountNo, Date chargeDate, String description, Date dueDate, Double amount,
			String documentNo, String invoiceRef) {
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
	public void receivePayment(String paymentRef, String businessNo, String accountNo, String paymentMode,
			String trxNumber) {
		Transaction trx = dao.findByRefId(paymentRef, Transaction.class);
		trx.setAccountNo(accountNo);
		trx.setPaymentMode(PaymentMode.valueOf(paymentMode));
		trx.setTrxNumber(trxNumber);
		trx.setBusinessNo(businessNo);
		trx.setStatus(PaymentStatus.PAID);

		// Remove this duplication #07/10/2015
		if (trx.getInvoiceRef() != null) {
			Invoice invoice = dao.findByRefId(trx.getInvoiceRef(), Invoice.class, false);
			if (invoice != null && invoice.getBookingRefId() != null) {
				Booking booking = dao.findByRefId(invoice.getBookingRefId(), Booking.class, false);
				booking.setPaymentStatus(PaymentStatus.PAID);
				booking.setPaymentDate(new Date());
				booking.setPaymentMode(paymentMode);
				booking.setPaymentRef(paymentRef);
				bookingDao.save(booking);
			}
		}
		dao.save(trx);
	}

	public TransactionDto saveTransaction(TransactionDto trxDto) {
		Transaction trx = new Transaction();
		trx.setAccountNo(trxDto.getAccountNo());
		trx.setDate(trxDto.getCreatedDate());
		trx.setPaymentMode(trxDto.getPaymentMode());
		trx.setTrxNumber(trxDto.getTrxNumber());
		trx.setDescription(trxDto.getDescription());
		trx.setInvoiceRef(trxDto.getInvoiceRef());
		trx.setInvoiceAmount(trxDto.getInvoiceAmt());
		trx.setChargableAmount(trxDto.getChargableAmnt());
		trx.setAmount(trxDto.getAmountPaid());
		trx.setStatus(PaymentStatus.PAID);
		Transaction savedTrx = (Transaction) dao.save(trx);
		return savedTrx.toDto1();
	}

	public void receivePaymentUsingInvoiceNo(String paymentRef, String businessNo, String accountNo, String paymentMode,
			String trxNumber, String phoneNumber, String amount, String trxDate, String payerNames) {

		// Account No Details
		accountNo = accountNo.trim();
		accountNo = accountNo.replaceAll("[-+.^:,]", "");
		accountNo = accountNo.replaceAll("[oO]", "0");
		logger.info("Trimmed Account No::::" + accountNo);

		// Always store the transaction
		Date parsedDate = null;
		try {
			parsedDate = ServerDateUtils.MPESATIMESTAMP.parse(trxDate);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		Transaction trx = new Transaction();
		trx.setAccountNo(accountNo);
		trx.setDate(parsedDate);
		if (paymentMode != null) {
			if (paymentMode.equals("MPESA")) {
				trx.setPaymentMode(PaymentMode.MPESA);
			} else if (paymentMode.equals("CARDS")) {
				trx.setPaymentMode(PaymentMode.CARDS);
			}
		}
		trx.setTrxNumber(paymentRef);
		trx.setPayerNames(payerNames);
		trx.setBusinessNo(businessNo);
		trx.setAmount(Double.parseDouble(amount));
		trx.setStatus(PaymentStatus.PAID);

		// Check if the account number is null or empty
		if (accountNo == null || accountNo.isEmpty()) {
			logger.info("Account No is NULL, send message to tell the customer to input the correct account No"
					+ accountNo);
			trx.setDescription("Unknown payment with no account number");
			trx.setPaymentType(PaymentType.UNKNOWN);
			saveTransactionFirst(trx);

			Double amt = Double.valueOf(amount);
			String smsMessage = "Thank-you for your payment of " + numberFormat.format(amt)
					+ ".Please send details of this payment to memberservices@icpak.com ";

			String finalPhoneNumber = phoneNumber.replace("254", "0");
			if (phoneNumber != null) {
				smsIntergration.send(finalPhoneNumber, smsMessage);
				logger.error("sending sms to :" + finalPhoneNumber);
			}
			return;
		}

		// If accountno doesn't start with inv, then we check if its
		// subscription renewal
		String firstAccountChars = accountNo.substring(0, 2);
		if (!firstAccountChars.toUpperCase().equals("INV")) {
			// Check if this is a valid membership no
			User user = usersDao.findUserByMemberNo(accountNo);
			if (user != null) {
				logger.info("Subscription renewal for " + accountNo);
				trx.setDescription("Subscription payments for " + user.getFullName());
				trx.setInvoiceRef(user.getRefId());
				trx.setPaymentType(PaymentType.SUBSCRIPTION);
				saveTransactionFirst(trx);

				Double amt = Double.valueOf(amount);
				String smsMessage = "Dear " + user.getFullName() + ", Thank-you for your " + trx.getPaymentMode()
						+ " payment of KES " + numberFormat.format(amt) + " for your member subscription. "
						+ "Your account will be updated in the next 72 hours. ";

				String finalPhoneNumber = phoneNumber.replace("254", "0");
				if (phoneNumber != null) {
					smsIntergration.send(finalPhoneNumber, smsMessage);
					logger.error("sending sms to :" + finalPhoneNumber);
				}
				return;
			}
		}
		
		//Booking or Registration Payment
		invoiceDto = invoiceDao.getInvoiceByDocumentNo(accountNo);
		
		//If No Invoice is found
		if (invoiceDto == null) {
			logger.info("No Invoice found for this transaction..");
			trx.setDescription("Unknown payment with invalid account no");
			trx.setPaymentType(PaymentType.UNKNOWN);
			saveTransactionFirst(trx);

			Double amt = Double.valueOf(amount);
			String smsMessage = " Thank-you for your payment of " + numberFormat.format(amt)
					+ ".Please send details of this payment to memberservices@icpak.com. ";

			String finalPhoneNumber = phoneNumber.replace("254", "0");
			if (phoneNumber != null) {
				smsIntergration.send(finalPhoneNumber, smsMessage);
				logger.error("sending sms to :" + finalPhoneNumber);
			}
			return;
		}

		// Convert DTO to Invoice Object
		Invoice inv = new Invoice();
		inv.copyFrom(invoiceDto);
		// Payment was for Booking
		Booking booking = new Booking();
		booking = dao.findByRefId(inv.getBookingRefId(), Booking.class, false);
		if (booking != null) {
			logger.info("Payment was for booking refId::" + booking.getRefId());
			trx.setDescription("Payment for " + booking.getEvent().getName() + "-" + payerNames);
			trx.setInvoiceRef(booking.getRefId());
			trx.setPaymentType(PaymentType.BOOKING);

			// Save this transaction
			saveTransactionFirst(trx);
			try {
				sendPaymentConfirmationSMSAndEmail(phoneNumber, trxNumber, inv.getContactName(), booking, trxDate,
						amount, trx);
			} catch (UnsupportedEncodingException | MessagingException e) {
				e.printStackTrace();
			}
		} else {
			// Payment was for New Member Registration
			logger.info("Looking for application using Invoice Ref::" + invoiceDto.getInvoiceRefId());
			ApplicationFormHeader application = applicationDao.getApplicationByInvoiceRef(invoiceDto.getInvoiceRefId());
			trx.setDescription("Registration Payment for " + application.getSurname());
			trx.setInvoiceRef(application.getRefId());
			trx.setPaymentType(PaymentType.REGISTRATION);
			// Save this transaction
			saveTransactionFirst(trx);
			try {
				sendPaymentConfirmationSMSAndEmail(phoneNumber, trxNumber, inv.getContactName(), application, trxDate,
						amount, trx);
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
	}

	private Double applyDiscountsAndPenalties(EventDto event, String trxDate) throws ParseException {
		// Parse the mpesa trx Date 2016-01-21 18:42:08
		Date parsedDate = ServerDateUtils.FULLTIMESTAMP.parse(trxDate);

		// Apply Discount && Penalties based on todays Date
		Date penaltyDate = null;
		Date discountDate = null;
		Double chargableAmount = invoiceDto.getInvoiceAmount();

		if (event.getPenaltyDate() != null) {
			penaltyDate = ServerDateUtils.DATEFORMAT_SYS.parse(event.getPenaltyDate());
			if (parsedDate.getTime() >= penaltyDate.getTime()) {
				chargableAmount = invoiceDto.getInvoiceAmount() + invoiceDto.getTotalPenalty();
				logger.info(
						"Penalty of " + invoiceDto.getTotalPenalty() + " applied for late payment >>>>" + parsedDate);
			}
		}

		if (event.getDiscountDate() != null) {
			discountDate = ServerDateUtils.DATEFORMAT_SYS.parse(event.getDiscountDate());
			if (parsedDate.getTime() <= discountDate.getTime()) {
				chargableAmount = invoiceDto.getInvoiceAmount() - invoiceDto.getTotalDiscount();
				logger.info("Discount of " + invoiceDto.getTotalDiscount() + " applied for Early Bird Payment >>>>"
						+ parsedDate);
			}
		}
		return chargableAmount;
	}

	public List<OldTransactionDto> getTransactions(String userId) {
		List<Transaction> transactions = dao.getTransactions(userId);
		List<OldTransactionDto> trxs = new ArrayList<>();
		for (Transaction t : transactions) {
			trxs.add(t.toDto());
		}
		return trxs;
	}

	/*
	 * Calculate if there is any balance and send an sms with this balance
	 */
	private void sendPaymentConfirmationSMSAndEmail(String phoneNumber, String transactionNumber, String senderName,
			Object paymentType, String trxDate, String amount, Transaction trx)
			throws UnsupportedEncodingException, MessagingException {

		// Get the balance for this Booking/Application - What is remaining from
		// this
		// invoice?
		Double paidAmt = Double.parseDouble(amount);
		Double previousPayments = dao.getAllPayments(trx.getAccountNo()) == null ? 0.0
				: dao.getAllPayments(trx.getAccountNo());

		if (paymentType != null && paymentType instanceof Booking) {
			Booking booking = (Booking) paymentType;
			String smsMessage = "";

			Double chargeAbleAmt = 0.0;
			try {
				chargeAbleAmt = applyDiscountsAndPenalties(booking.getEvent().toDto(), trxDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Double balance = chargeAbleAmt - previousPayments;
			// Chargable Amount -> Payments after Discounts and necessary
			// penalties has been applied
			logger.info("Chargable Amount>>>>" + chargeAbleAmt);
			logger.info("Invoice Amount>>>>" + invoiceDto.getInvoiceAmount());
			logger.info("Previous Payments>>>>" + previousPayments);
			logger.info("Balance Calculated>>>>" + balance);
			trx.setChargableAmount(chargeAbleAmt);
			trx.setTotalPreviousPayments(previousPayments);
			trx.setBalance(balance);
			trx.setInvoiceAmount(invoiceDto.getInvoiceAmount());

			// Store this value against the transaction

			if (balance > 0) {
				logger.info("This invoice still has some balance:::" + trx.getAccountNo());
				smsMessage = " Thank-you for your " + trx.getPaymentType() + " payment of "
						+ numberFormat.format(paidAmt) + " for " + booking.getEvent().getName() + ",Invoice No:"
						+ invoiceDto.getDocumentNo() + ".To fully pay for this booking, kindly pay balance of "
						+ numberFormat.format(balance) + ".";

				String finalPhoneNumber = phoneNumber.replace("254", "0");
				if (phoneNumber != null && !phoneNumber.equals("N/A")) {
					smsIntergration.send(finalPhoneNumber, smsMessage);
					logger.error("sending sms to :" + finalPhoneNumber);
				} else {
					// Get the sponsor phone-number;
					String phoneNo = booking.getContact().getTelephoneNumbers();
					String firstNo = booking.getContact().getTelephoneNumbers().substring(0, 1);
					if (firstNo.equals("0")) {
						finalPhoneNumber = phoneNo.replace("254", "0");
					}
					smsIntergration.send(finalPhoneNumber, smsMessage);

					// Get Sponsor Email
					if (booking.getContact().getEmail() != null) {
						String subject = "PAYMENT CONFIRMATION FOR " + booking.getEvent().getName().toUpperCase();
						EmailServiceHelper.sendEmail(smsMessage, "RE: ICPAK '" + subject,
								Arrays.asList(booking.getContact().getEmail()),
								Arrays.asList(booking.getContact().getContactName()));
					}
				}

			}
			// Booking has been fully paid for>>>
			else {
				booking.setPaymentStatus(PaymentStatus.PAID);
				invoiceDto.setStatus(PaymentStatus.PAID);
				Invoice invoiceSave = invoiceDao.findByRefId(invoiceDto.getRefId(), Invoice.class);
				invoiceSave.setStatus(PaymentStatus.PAID);
				// System.err.println("Invoice RefId>>" +
				// invoiceSave.getRefId());
				invoiceDao.save(invoiceSave);

				// Is this Payment for a Course
				if (booking.getEvent().getType() != null && booking.getEvent().getType() == EventType.COURSE) {
					logger.info("Payment for a course" + booking.getEvent().getName());
					/* Payers message */
					smsMessage = " Thank-you for your " + trx.getPaymentType() + " payment of "
							+ numberFormat.format(paidAmt) + " for " + booking.getEvent().getName()
							+ ". The booking status is PAID. ";

					String finalPhoneNumber = phoneNumber.replace("254", "0");
					if (phoneNumber != null) {
						smsIntergration.send(finalPhoneNumber, smsMessage);
						logger.error("sending sms to :" + finalPhoneNumber);
					}

					if (booking.getContact().getEmail() != null) {
						String subject = "PAYMENT CONFIRMATION FOR " + booking.getEvent().getName().toUpperCase();
						EmailServiceHelper.sendEmail(smsMessage, "RE: ICPAK '" + subject,
								Arrays.asList(booking.getContact().getEmail()),
								Arrays.asList(booking.getContact().getContactName()));
					}

					/* Delegates Message */
					List<DelegateDto> allDelegates = new ArrayList<>();
					for (Delegate delegate : booking.getDelegates()) {
						allDelegates.add(delegate.toDto());
					}
					try {
						bookingDaoHelper.enrolDelegateToLMS(allDelegates, booking.getEvent());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				}

				// This is a normal event booking
				// Send message to all Delegates
				for (Delegate delegate : booking.getDelegates()) {
					smsMessage = "Dear " + delegate.getFullName() + "," + " Thank-you for your " + trx.getPaymentType()
							+ " payment for " + booking.getEvent().getName()
							+ ".Your booking status is now PAID. Your ERN No. is " + delegate.getErn();

					if (delegate.getMemberRefId() != null) {
						Member member = memberDao.findByRefId(delegate.getMemberRefId(), Member.class);
						logger.info("Sending SMS to " + member.getUser().getPhoneNumber());

						if (member.getUser().getPhoneNumber() != null) {
							try {
								smsIntergration.send(member.getUser().getPhoneNumber(), smsMessage);
							} catch (RuntimeException e) {
								logger.error("Invalid Phone Number");
								e.printStackTrace();
							}
						}
					} else {
						logger.info("Non-member cannot be send sms..");
					}
				}

				logger.info("This invoice is fully paid:::" + trx.getAccountNo());
				smsMessage = " Thank-you for your " + trx.getPaymentType() + " payment of "
						+ numberFormat.format(paidAmt) + " for " + booking.getEvent().getName()
						+ ". The booking status is PAID.";

				String finalPhoneNumber = phoneNumber.replace("254", "0");
				if (phoneNumber != null) {
					smsIntergration.send(finalPhoneNumber, smsMessage);
					logger.error("sending sms to :" + finalPhoneNumber);
				}

				if (booking.getContact().getEmail() != null) {
					String subject = "PAYMENT CONFIRMATION FOR " + booking.getEvent().getName().toUpperCase();
					EmailServiceHelper.sendEmail(smsMessage, "RE: ICPAK '" + subject,
							Arrays.asList(booking.getContact().getEmail()),
							Arrays.asList(booking.getContact().getContactName()));
				}
			}

		} else if (paymentType != null && paymentType instanceof ApplicationFormHeader) {
			ApplicationFormHeader application = (ApplicationFormHeader) paymentType;

			if (invoiceDto.getInvoiceAmount() == null) {
				logger.info("Invoice amount is null");
				return;
			}

			Double balance = invoiceDto.getInvoiceAmount() - previousPayments;
			String smsMessage = "";

			trx.setChargableAmount(invoiceDto.getInvoiceAmount());
			trx.setTotalPreviousPayments(previousPayments);
			trx.setBalance(balance);
			trx.setInvoiceAmount(invoiceDto.getInvoiceAmount());

			if (balance > 0) {
				logger.info("This invoice still has some balance:::" + trx.getAccountNo());
				smsMessage = " Thank-you for your payment of " + numberFormat.format(paidAmt)
						+ " for your membership registration fee."
						+ ". To fully pay for your application, kindly pay balance of " + numberFormat.format(balance)
						+ ".";

				String finalPhoneNumber = phoneNumber.replace("254", "0");
				if (phoneNumber != null) {
					smsIntergration.send(finalPhoneNumber, smsMessage);
					logger.error("sending sms to :" + finalPhoneNumber);
				}

				if (application.getEmail() != null) {
					String subject = "PAYMENT CONFIRMATION FOR " + application.getSurname().toUpperCase()
							+ " MEMBER SUBSCRIPTION ";
					EmailServiceHelper.sendEmail(smsMessage, "RE: ICPAK '" + subject,
							Arrays.asList(application.getEmail()),
							Arrays.asList(application.getSurname() + " " + application.getOtherNames()));
				}

			} else {
				application.setPaymentStatus(PaymentStatus.PAID);
				invoiceDto.setStatus(PaymentStatus.PAID);
				Invoice invoiceSave = invoiceDao.findByRefId(invoiceDto.getRefId(), Invoice.class);
				invoiceSave.setStatus(PaymentStatus.PAID);
				// System.err.println("Invoice RefId>>" +
				// invoiceSave.getRefId());
				invoiceDao.save(invoiceSave);
				applicationDao.save(application);

				smsMessage = "Dear" + " " + application.getSurname() + ","
						+ " Thank-you for payment for your member registration. " + "Your payment status is now PAID.";
				String finalPhoneNumber = phoneNumber.replace("254", "0");

				if (phoneNumber != null) {
					smsIntergration.send(finalPhoneNumber, smsMessage);
					logger.error("sending sms to :" + finalPhoneNumber);
				}

				if (application.getEmail() != null) {
					String subject = "PAYMENT CONFIRMATION FOR " + application.getSurname().toUpperCase()
							+ " MEMBER SUBSCRIPTION ";
					EmailServiceHelper.sendEmail(smsMessage, "RE: ICPAK '" + subject,
							Arrays.asList(application.getEmail()),
							Arrays.asList(application.getSurname() + " " + application.getOtherNames()));
				}
			}

		} else {
			logger.error("No notification sent since neither booking nor application was found for this payment");
		}

	}

	public void performServerIPN(String payLoad) {
		// Perform the HTTP
		String urlHost = "https://www.icpak.com/mTransport/index.php/paybillv2";
		String urlLocal = "http://localhost/mTransport/index.php/paybillv2";

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(urlLocal);
		String res = "";
		HttpResponse response = null;
		StringBuffer result = null;

		try {
			StringEntity entity = new StringEntity(payLoad);
			post.setEntity(entity);
			response = client.execute(post);

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		assert result != null;
		res = result.toString();
		System.err.println("Repsonse::::" + res);
	}
}
