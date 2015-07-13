package com.workpoint.icpak.shared.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.workpoint.icpak.shared.trx.TransactionDto;

@Path("transactions")
public interface TransactionsResource {

	@GET
	@Path("/payment")
	public String makePayment(@QueryParam("refId") String paymentRef,
			@QueryParam("businessNo") String businessNo,
			@QueryParam("accountNo") String accountNo,
			@QueryParam("paymentMode") String paymentMode,
			@QueryParam("trxNumber") String trxNumber);

	@GET
	public List<TransactionDto> getAllTrxs(@QueryParam("userId") String userId);
}
