package com.icpak.rest.dao.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.ApplicationFormDao;
import com.icpak.rest.dao.BookingsDao;
import com.icpak.rest.dao.InvoiceDao;
import com.icpak.rest.dao.MemberDao;
import com.icpak.rest.dao.StatementDao;
import com.icpak.rest.dao.TransactionsDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.event.Booking;
import com.icpak.rest.models.trx.Invoice;
import com.icpak.rest.models.trx.Statement;
import com.icpak.rest.models.trx.Transaction;
import com.icpak.rest.util.SMSIntegration;
import com.workpoint.icpak.server.util.ServerDateUtils;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.PaymentMode;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.PaymentType;
import com.workpoint.icpak.shared.model.TransactionDto;

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
	StatementDaoHelper statementDaoHelper;
	@Inject
	StatementDao statementDao;
	@Inject
	MemberDao memberDao;
	@Inject
	UsersDao usersDao;
	Logger logger = Logger.getLogger(TransactionDaoHelper.class.getName());
	Locale locale = new Locale("en", "KE");
	NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
	private InvoiceDto invoiceDto;
	// 2.Post to Nav
	// StartPoint start = new StartPoint();

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

	public void receivePaymentAndApplyToStatement(String paymentRef, String businessNo, String accountNo,
			String paymentMode, String trxNumber, String phoneNumber, String amount, String trxDate,
			String payerNames) {

		// Prepare the transaction for saving
		Date parsedDate = null;
		Double amountPaid = Double.parseDouble(amount);
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
			}
		}
		trx.setTrxNumber(paymentRef);
		trx.setPayerNames(payerNames);
		trx.setPayerPhoneNumber(phoneNumber);
		trx.setBusinessNo(businessNo);
		trx.setAccountNo(accountNo);
		trx.setAmount(amountPaid);
		trx.setStatus(PaymentStatus.PAID);
		trx.setDescription("Loan Repayment:" + payerNames + "  - " + phoneNumber);
		trx.setPaymentType(PaymentType.SUBSCRIPTION);

		// Pull Users Statements by checking the payee's PhoneNumber
		User user = usersDao.getByUserPhoneNo(phoneNumber, false);
		if (user != null) {
			logger.debug("Creating statement record");
			createStatementRecord(user, trx);
		} else {
			logger.debug("No user found OR User has No memberNo from the phoneNumber sent by MPESA::" + phoneNumber);
			User u = usersDao.getByUserPhoneNo(accountNo, false);
			if (u != null) {
				logger.debug("Creating statement record");
				createStatementRecord(user, trx);
			} else {
				logger.debug("No user found even with account number. Giving up NOW!!!!");
			}
		}

	}

	private void createStatementRecord(User user, Transaction trx) {
		Statement statement = new Statement();
		statement.setPostingDate(trx.getDate());
		statement.setDocumentType(trx.getPaymentMode() + " PAYMENT");
		statement.setDocumentNo(trx.getTrxNumber());
		statement.setEntryNo("CLEARED");
		statement.setAmount(trx.getAmount());
		statement.setCustomerNo(user.getMember().getMemberNo());
		statement.setDescription("Payment by " + trx.getPayerNames() + "-" + trx.getPayerPhoneNumber());
		statement.setTransactionNumber(trx.getTrxNumber());
		statement = (Statement) statementDao.save(statement);

		// Save the transaction
		trx.setStatementRef(statement.getRefId());
		saveTransaction(trx); // For Future Reference

		// Last Thing confirm to the user that you've received the money
		sendConfirmationMessage(trx, user);
	}

	/**
	 * 
	 * @param trx
	 * @param user
	 */
	private void sendConfirmationMessage(Transaction trx, User user) {
		Double balance = (statementDao.getOpeningBalance(user.getMemberNo(), null)).getAmount();
		if (trx.getAmount() != null && balance != null) {
			user = usersDao.getByUserPhoneNo(user.getPhoneNumber(), false);
			String message = "Hi %s, We have received your payment of Kes %s transaction ref: %s. "
					+ "Your balance is Kes %s.";

			message = String.format(message, user.getFullName(), numberFormat.format(trx.getAmount()),
					trx.getTrxNumber(), numberFormat.format(Math.abs(balance)));

			smsIntergration.send(user.getPhoneNumber(), message);

			System.err.println(">>>Message:::" + message);
		}

	}

	private Transaction saveTransaction(Transaction trx) {
		return (Transaction) dao.save(trx);
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
