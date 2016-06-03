package com.icpak.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.TransactionDaoHelper;
import com.wordnik.swagger.annotations.ApiOperation;
import com.workpoint.icpak.shared.api.TransactionsResource;
import com.workpoint.icpak.shared.model.events.MpesaDTO;
import com.workpoint.icpak.shared.model.events.MpesaKYCDTO;
import com.workpoint.icpak.shared.trx.OldTransactionDto;

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
			@QueryParam("mpesa_msisdn") String phoneNumber,
			@QueryParam("tstamp") String trxDate,
			@QueryParam("mpesa_sender") String payerNames) {
		trxDaoHelper.receivePaymentUsingInvoiceNo(paymentRef, businessNo,
				accountNo, "MPESA", trxNumber, phoneNumber, mpesaAmt, trxDate,
				payerNames);
		return "SUCCESS";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Receives Mpesa Info via the new api", consumes = MediaType.APPLICATION_JSON)
	public String receiveMpesaG2(MpesaDTO mpesaTrx) {
		String tstamp = mpesaTrx.getTransTime().substring(0, 4) + "-"
				+ mpesaTrx.getTransTime().substring(4, 6) + "-"
				+ mpesaTrx.getTransTime().substring(6, 8) + " "
				+ mpesaTrx.getTransTime().substring(8, 10) + ":"
				+ mpesaTrx.getTransTime().substring(10, 12) + ":"
				+ mpesaTrx.getTransTime().substring(12, 14);
		String payerFullName = "";
		for (MpesaKYCDTO m : mpesaTrx.getKYCInfoList()) {
			payerFullName = payerFullName + m.getKycValue() + " ";
		}
		System.err.println("Full Names>>>>" + payerFullName);
		trxDaoHelper.receivePaymentUsingInvoiceNo(mpesaTrx.getTransID(),
				mpesaTrx.getBusinessShortCode(), mpesaTrx.getBillRefNumber(),
				"MPESA", mpesaTrx.getTransID(), mpesaTrx.getMSISDN(),
				mpesaTrx.getTransAmount(), tstamp, payerFullName);
		return "SUCCESS";
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<OldTransactionDto> getAllTrxs(
			@QueryParam("userId") String userId) {
		return trxDaoHelper.getTransactions(userId);
	}
}
