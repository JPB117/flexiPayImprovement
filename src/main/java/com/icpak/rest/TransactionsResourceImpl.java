package com.icpak.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.TransactionDaoHelper;
import com.workpoint.icpak.shared.api.TransactionsResource;
import com.workpoint.icpak.shared.trx.TransactionDto;

@Path("transactions")
public class TransactionsResourceImpl implements TransactionsResource {

	@Inject
	TransactionDaoHelper trxDaoHelper;

	@GET
	@Path("/payment")
	public String makePayment(@QueryParam("mpesa_code") String paymentRef,
			@QueryParam("business_number") String businessNo,
			@QueryParam("mpesa_acc") String accountNo,
			@QueryParam("id") String mpesaRef,
			@QueryParam("paymentMode") String paymentMode,
			@QueryParam("trxNumber") String trxNumber,
			@QueryParam("mpesa_amt") String mpesaAmt,
			@QueryParam("mpesa_msisdn") String phoneNumber) {

		System.err.println("Mpesa Code>>" + paymentRef + "\n Business No:"
				+ businessNo + "\nAccount No:" + accountNo);
		trxDaoHelper.receivePaymentUsingInvoiceNo(paymentRef, businessNo,
				accountNo, paymentMode, trxNumber, phoneNumber, mpesaAmt);
		return "SUCCESS";
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<TransactionDto> getAllTrxs(@QueryParam("userId") String userId) {
		return trxDaoHelper.getTransactions(userId);
	}
}
