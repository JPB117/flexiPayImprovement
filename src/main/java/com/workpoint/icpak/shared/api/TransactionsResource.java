package com.workpoint.icpak.shared.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.workpoint.icpak.shared.trx.TransactionDto;

@Path("transactions")
public interface TransactionsResource {

	@GET
	@Path("/payment")
	public String makePayment(@QueryParam("mpesa_acc") String paymentRef,
			@QueryParam("business_number") String businessNo,
			@QueryParam("mpesa_acc") String accountNo,
			@QueryParam("id") String mpesaRef,
			@QueryParam("paymentMode") String paymentMode,
			@QueryParam("trxNumber") String trxNumber,
			@QueryParam("mpesaAmt") String mpesaAmt);

	@GET
	public List<TransactionDto> getAllTrxs(@QueryParam("userId") String userId);
}
