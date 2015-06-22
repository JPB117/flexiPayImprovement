package com.workpoint.icpak.shared.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.workpoint.icpak.shared.trx.TransactionDto;


@Path("transactions")
public interface TransactionsResource {

	@POST
	public void makePayment(@QueryParam("refId") String paymentRef, 
			@QueryParam("status") String refId, @QueryParam("paymentMode") String paymentMode);
	
	
	@GET
	public List<TransactionDto> getAllTrxs(@QueryParam("userId") String userId);
}
