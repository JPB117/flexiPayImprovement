package com.workpoint.icpak.shared.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("transactions")
public interface TransactionsResource {

	@GET
	@Path("/payment")
	public String makePayment(@QueryParam("mpesa_code") String paymentRef,
			@QueryParam("business_number") String businessNo,
			@QueryParam("mpesa_acc") String accountNo,
			@QueryParam("id") String mpesaRef,
			@QueryParam("paymentMode") String paymentMode,
			@QueryParam("trxNumber") String trxNumber,
			@QueryParam("mpesaAmt") String mpesaAmt,
			@QueryParam("msisdn") String phoneNumber,
			@QueryParam("tstamp") String trxDate,
			@QueryParam("mpesa_sender") String payerNames);
}
