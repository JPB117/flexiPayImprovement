package com.icpak.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.TransactionDaoHelper;
import com.workpoint.icpak.shared.api.TransactionsResource;

@Path("transactions")
public class TransactionsResourceImpl implements TransactionsResource {

	@Inject
	TransactionDaoHelper trxDaoHelper;
	Logger logger = Logger.getLogger(TransactionsResourceImpl.class.getName());

	@GET
	@Path("/payment")
	public String makePayment(@QueryParam("mpesa_code") String paymentRef,
			@QueryParam("business_number") String businessNo, @QueryParam("mpesa_acc") String accountNo,
			@QueryParam("id") String mpesaRef, @QueryParam("paymentMode") String paymentMode,
			@QueryParam("trxNumber") String trxNumber, @QueryParam("mpesa_amt") String mpesaAmt,
			@QueryParam("mpesa_msisdn") String phoneNumber, @QueryParam("tstamp") String trxDate,
			@QueryParam("mpesa_sender") String payerNames) {
		trxDaoHelper.receivePaymentAndApplyToStatement(paymentRef, businessNo, accountNo, "MPESA", trxNumber,
				phoneNumber, mpesaAmt, trxDate, payerNames);
		return "SUCCESS";
	}


}
